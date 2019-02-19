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
    }

    @Override
    public long getStart() {
        return 0;
    }

    @Override
    public long getEnd() {
        return 0;
    }

    @Override
    public void play(long l) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void seek(long l) {

    }

    @Override
    public void stop() {

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
                resultMap.get(objectKeyValue).add(object);
            }
        }
        return resultMap;
    }
}
