package mo.visualization.webactivity.plugin;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import mo.core.ui.dockables.DockableElement;
import mo.core.ui.dockables.DockablesRegistry;
import mo.visualization.webactivity.plugin.views.PlayerPanel;
import mo.visualization.Playable;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/* Aqui en el player hay que decidir que datos se vana mostrar:

   - Si se mostrarán todos en el mismo panel (datos por sitio o url escrita)

   - Si se mostrarán los datos en un panel aparte, uno por cada tipo de dato

   Esto se debe elegir en la configuración del plugin

   Para el plugin de visualización remota lo mismo.
 */
public class Player implements Playable {

    private Map<String, List<JsonObject>> dataMap;
    private static final String CAPTURE_MILLISECONDS_KEY = "captureMilliseconds";
    private long start;
    private long end;
    private PlayerPanel panel;
    private List<String> dataTypes;
    private DockablesRegistry dockablesRegistry;
    private DockableElement dockableElement;
    private static final Logger LOGGER = Logger.getLogger(Player.class.getName());

    public Player(Map filesMap, String configurationName){
        this.dataTypes = new ArrayList<>();
        this.dataMap = this.readData(filesMap);
        this.panel = new PlayerPanel(this.dataTypes);
        this.dockableElement = new DockableElement();
        this.dockableElement.setTitleText("Visualization: " + configurationName);
        this.dockableElement.add(this.panel);
        this.dockablesRegistry = DockablesRegistry.getInstance();
        this.dockablesRegistry.addAppWideDockable(dockableElement);
    }

    /* Encontramos el tiempo menor de todos los registros de todos los tipos de datos que contiene la estructura
    *
    * Esto implica que al reproducir hay que validar que exista un registro de cada tipo de dato asociado al
    * tiempo de reproduccion actual*/
    @Override
    public long getStart() {
        this.start = 0;
        List<Long> minCaptureMilliseconds = new ArrayList<>();
        for(Object key : this.dataMap.keySet()){
            String dataType = (String) key;
            List<JsonObject> jsonObjectsByDataType = this.dataMap.get(dataType);
            minCaptureMilliseconds.add(jsonObjectsByDataType.get(0).get(CAPTURE_MILLISECONDS_KEY).getAsLong());
        }
        long min = minCaptureMilliseconds.get(0);
        for(Long captureMilliseconds : minCaptureMilliseconds){
            if(captureMilliseconds < min){
                min = captureMilliseconds;
            }
        }
        this.start = min;
        return this.start;
    }

    /* Para encontrar el final o maximo tiempo de captura, aplicamos la misma lógica que para encontrar el inicio o minimo*/
    @Override
    public long getEnd() {
        this.end = 0;
        List<Long> maxCaptureMilliseconds = new ArrayList<>();
        for(Object key : this.dataMap.keySet()){
            String dataType = (String) key;
            List<JsonObject> jsonObjectsByDataType = this.dataMap.get(dataType);
            maxCaptureMilliseconds.add(jsonObjectsByDataType.get(jsonObjectsByDataType.size()-1).get(CAPTURE_MILLISECONDS_KEY).getAsLong());
        }
        long max = maxCaptureMilliseconds.get(0);
        for(Long captureMilliseconds : maxCaptureMilliseconds){
            if(captureMilliseconds > max){
                max = captureMilliseconds;
            }
        }
        this.end = max;
        return this.end;
    }

    @Override
    public void play(long l) {
        /* reproducimos todas las vistas al mismo tiempo!!*/
        for(String dataType : this.dataTypes){
            List<JsonObject> searchedData = this.getDataByCaptureMilliseconds(l, dataType);
            /* Solo actualizamos el panel cuando se han encontrado  registros con ese tiempo o menor */
            if(searchedData == null || searchedData.isEmpty()){
                continue;
            }
            this.panel.updatePanelData(searchedData, dataType);
        }
    }

    @Override
    public void pause() {

    }

    @Override
    public void seek(long l) {
        this.play(l);
    }

    @Override
    public void stop() {
        this.panel.showPanel(false);
    }

    @Override
    public void sync(boolean b) {

    }


    /* EN este metodo se cargan todos los datos de todos los archivos, en la siguiente estructura:

        map = {
            'keystrokes': {lista de keystrokes },
            'mouseMoves': {lista de mouseMouves}
            ...
        }


        /* AQUI CREAR UN MAPA <string, list<modeloTipoDato>>

        ES EL PLAYER EL ENCARGADO DE ALMACENAR TODOS LOS REGISTROS DE CADA TIPO DE DATO, Y ENTREGARLOS A LOS PANELES QUE CORRESPONDAN.
     */
    private Map<String, List<JsonObject>> readData(Map filesMap){
        Map<String, List<JsonObject>> dataMap = new HashMap<>();
        for(Object key: filesMap.keySet()){
            String filePath = (String) filesMap.get((key));
            File file = new File(filePath);
            if(!file.isFile()){
                break;
            }
            this.dataTypes.add((String) key);
            List<JsonObject> fileDataList = new ArrayList<>();
            try {
                FileReader fileReader = new FileReader(filePath);
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                String line;
                JsonParser parser = new JsonParser();
                while((line = bufferedReader.readLine()) != null){
                    line = line.replace("\n", "");
                    JsonObject dataObject = parser.parse(line).getAsJsonObject();
                    fileDataList.add(dataObject);
                }
                dataMap.put((String) key, fileDataList);
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "", e);
                break;
            }
        }
        return dataMap;
    }


    /* La estrategia de visualización es la siguiente:

    Dado un tiempo que queremos reproducir, buscaremos todos los registros que tengan tiempo igual o menor y los motraremos.
     */
    private List<JsonObject> getDataByCaptureMilliseconds(long milliseconds, String dataType){
        if(milliseconds < this.start || milliseconds > this.end){
            return null;
        }
        return this.dataMap.get(dataType).stream()
                .filter(jsonObject -> jsonObject.get(CAPTURE_MILLISECONDS_KEY).getAsLong() <= milliseconds)
                .collect(Collectors.toList());
    }
}
