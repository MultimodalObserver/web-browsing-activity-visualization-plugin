package mo.plugin;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import mo.core.ui.dockables.DockableElement;
import mo.core.ui.dockables.DockablesRegistry;
import mo.plugin.views.PlayerPanel;
import mo.visualization.Playable;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;


/* Aqui en el player hay que decidir que datos se vana mostrar:

   - Si se mostrarán todos en el mismo panel (datos por sitio o url escrita)

   - Si se mostrarán los datos en un panel aparte, uno por cada tipo de dato

   Esto se debe elegir en la configuración del plugin

   Para el plugin de visualización remota lo mismo.
 */
public class Player implements Playable {

    private Map<String, Map<String, List<JsonObject>>> dataMap;
    private static final String CAPTURE_MILLISECONDS_KEY = "captureMilliseconds";
    private long start;
    private long end;
    private PlayerPanel panel;
    private DockablesRegistry dockablesRegistry;
    private DockableElement dockableElement;
    private static final Logger LOGGER = Logger.getLogger(Player.class.getName());

    public Player(Map filesMap, String configurationName){
        this.dataMap = this.readData(filesMap);
        this.panel = new PlayerPanel(this.getDataTypes(this.dataMap));
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
            Map<String, List<JsonObject>> jsonObjectsByPage = this.dataMap.get(dataType);
            for(Object page: jsonObjectsByPage.keySet()){
                String pageTitle = (String) page;
                List<JsonObject> jsonObjects = jsonObjectsByPage.get(pageTitle);
                minCaptureMilliseconds.add(jsonObjects.get(0).get(CAPTURE_MILLISECONDS_KEY).getAsLong());
            }
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
            Map<String, List<JsonObject>> jsonObjectsByPage = this.dataMap.get(dataType);
            for(Object page: jsonObjectsByPage.keySet()){
                String pageTitle = (String) page;
                List<JsonObject> jsonObjects = jsonObjectsByPage.get(pageTitle);
                maxCaptureMilliseconds.add(jsonObjects.get(jsonObjects.size()-1).get(CAPTURE_MILLISECONDS_KEY).getAsLong());
            }
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
        Map<String, Map<String, JsonObject>> searchedDataMap = this.getDataByCaptureMilliseconds(l);
        /* Solo actualizamos el panel cuando se encuentra un registro con ese tiempo */
        if(searchedDataMap == null){
            return;
        }
        else if(l == this.start){
            this.panel.createViews(searchedDataMap);
            return;
        }
        this.panel.updateViews(searchedDataMap);
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
        this.panel.removeViews();
    }

    @Override
    public void sync(boolean b) {

    }


    /* EN este metodo se cargan todos los datos de todos los archivos, en la siguiente estructura:

        map = {
            'keystrokes': {
                'sitio_web_1': [lista de datos de keystroke que pertenencen a sitio web 1],
                'sitio_web_2': [lista de datos de keystroke que pertenencen a sitio web 2],
                ...
            },
            'mouseMoves': {
                'sitio_web_1': [lista de datos de mouseMoves que pertenencen a sitio web 1],
                'sitio_web_2': [lista de datos de mouseMoves que pertenencen a sitio web 2],
                ...
            },
            ...
        }
     */
    private Map<String, Map<String, List<JsonObject>>> readData(Map filesMap){
        Map<String, Map<String, List<JsonObject>>> dataMap = new HashMap<>();
        for(Object key: filesMap.keySet()){
            String filePath = (String) filesMap.get((String) key);
            File file = new File(filePath);
            if(!file.isFile()){
                break;
            }
            List<JsonObject> fileDataList = new ArrayList<>();
            try {
                FileReader fileReader = new FileReader(filePath);
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                String line;
                JsonParser parser = new JsonParser();
                //Obtenemos los datos de los snapshots de procesos y los agregamos a una lista
                while((line = bufferedReader.readLine()) != null){
                    line = line.replace("\n", "");
                    JsonObject dataObject = parser.parse(line).getAsJsonObject();
                    fileDataList.add(dataObject);
                }
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "", e);
                break;
            }
            Map groupedMap = this.groupByPageTitle(fileDataList);
            //AQUI HAY QUE AGRUPAR POR SITIO WEB!!!
            dataMap.put((String) key , groupedMap);
        }
        return dataMap;
    }

    private Map groupByPageTitle(List<JsonObject> fileDataList){
        if(fileDataList == null || fileDataList.isEmpty()){
            return null;
        }
        /* Obtenemos todos los registros unicos de de pageTitle*/
        Object[] aux = fileDataList.stream().map(jsonObject -> {
            return jsonObject.get("pageTitle").getAsString();
        }).distinct().toArray();
        List<String> uniqueKeyValues = Arrays.asList(Arrays.copyOf(aux, aux.length, String[].class));
        /* Creamos un mapa de resultados*/
        Map<String, List<JsonObject>> resultMap = new HashMap<>();
        /* inicializamos el mapa con los pares: pageTitle -> lista de registros que pertenecen a ese sitio*/
        for(String uniqueKeyValue : uniqueKeyValues){
            resultMap.put(uniqueKeyValue, new ArrayList<>());
        }
        /* Recorremos los registros y agregamos a la lista correspondiente el registro, según el pageTitle al que pertenencen
         */
        for(JsonObject object: fileDataList){
            Object objectKeyValue = object.get("pageTitle");
            if(resultMap.containsKey(objectKeyValue)){
                /* Asumimos que los datos están ordenados segun tiempo de captura*/
                resultMap.get(objectKeyValue).add(object);
            }
        }
        return resultMap;
    }


    /* Encontramos los registros de los tipos de datos capturados en el instante de tiempo deseado, agrupados
    por sitio web tambien.

    Asumimos que cada tipo de dato posee un registro único en ese instante de tiempo, es decir, no se puede
    haber movido el mouse dos veces en el mismo instante, ni presionado dos teclas (evaluar esto!!!), etc..

    Si no se encuentran registros, de todos los tipos de datos, para ese instante de tiempo, devolvemos nulo
     */
    private Map<String, Map<String, JsonObject>> getDataByCaptureMilliseconds(long milliseconds){
        if(milliseconds < this.start || milliseconds > this.end){
            return null;
        }
        Map<String, Map<String, JsonObject>> searchedDataMap = new HashMap<>();
        for(Object key : this.dataMap.keySet()){
            String dataType = (String) key;
            Map<String, List<JsonObject>> jsonObjectsByPage = this.dataMap.get(dataType);
            for(Object page : jsonObjectsByPage.keySet()){
                String pageTitle = (String) page;
                List<JsonObject> jsonObjects = jsonObjectsByPage.get(pageTitle);
                for(JsonObject jsonObject : jsonObjects){
                    if(jsonObject.get(CAPTURE_MILLISECONDS_KEY).getAsLong() == milliseconds){
                        Map<String, JsonObject> data = new HashMap<>();
                        data.put(pageTitle, jsonObject);
                        searchedDataMap.put(dataType, data);
                    }
                }
            }

        }
        if(searchedDataMap.isEmpty()){
            return null;
        }
        return searchedDataMap;
    }

    private List<String> getDataTypes(Map<String, Map<String, List<JsonObject>>> dataMap){
        return null;
    }
}
