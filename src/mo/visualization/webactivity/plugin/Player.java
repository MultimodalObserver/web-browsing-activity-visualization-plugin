package mo.visualization.webactivity.plugin;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import mo.core.I18n;
import mo.core.ui.dockables.DockableElement;
import mo.core.ui.dockables.DockablesRegistry;
import mo.visualization.webactivity.plugin.model.*;
import mo.visualization.webactivity.plugin.view.PlayerPanel;
import mo.visualization.Playable;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class Player implements Playable {

    private Map<String, List<?>> dataMap;
    private long start;
    private long end;
    private PlayerPanel panel;
    private List<String> dataTypes;
    private static final Logger LOGGER = Logger.getLogger(Player.class.getName());
    private Gson gson;
    private String fileFormat;

    public Player(Map filesMap, String configurationName) throws FileNotFoundException {
        this.gson = new Gson();
        I18n i18n = new I18n(Player.class);
        this.dataTypes = new ArrayList<>();
        this.dataMap = this.readData(filesMap);
        this.panel = new PlayerPanel(this.dataTypes);
        DockableElement dockableElement = new DockableElement();
        dockableElement.setTitleText(i18n.s("playerTitle") + configurationName);
        dockableElement.add(this.panel);
        DockablesRegistry dockablesRegistry = DockablesRegistry.getInstance();
        dockablesRegistry.addAppWideDockable(dockableElement);
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
            List<?> objectsByDataType = this.dataMap.get(dataType);
            Visualizable firstCapturedObject = (Visualizable) objectsByDataType.get(0);
            minCaptureMilliseconds.add(firstCapturedObject.getCaptureMilliseconds());
        }
        long min = minCaptureMilliseconds.get(0);
        for(Long captureMilliseconds : minCaptureMilliseconds){
            if(captureMilliseconds < min){
                min = captureMilliseconds;
            }
        }
        /* -1 para que el reproductor no comience con datos cargados :S bug MO*/
        this.start = min - 1;
        return this.start;
    }

    /* Para encontrar el final o maximo tiempo de captura, aplicamos la misma lógica que para encontrar el inicio o minimo*/
    @Override
    public long getEnd() {
        this.end = 0;
        List<Long> maxCaptureMilliseconds = new ArrayList<>();
        for(Object key : this.dataMap.keySet()){
            String dataType = (String) key;
            List<?> objectsByDataType = this.dataMap.get(dataType);
            Visualizable lastCapturedObject = (Visualizable) objectsByDataType.get(objectsByDataType.size() - 1);
            maxCaptureMilliseconds.add(lastCapturedObject.getCaptureMilliseconds());
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
            List<Object> searchedData = this.getDataByCaptureMilliseconds(l, dataType);
            /* Solo actualizamos el panel cuando se han encontrado  registros con ese tiempo o menor */
            if(searchedData == null){
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
    private Map<String, List<?>> readData(Map filesMap) throws FileNotFoundException {
        Map<String, List<?>> dataMap = new HashMap<>();
        System.out.println("Voy a leer los datos");
        for(Object key: filesMap.keySet()){
            System.out.println(key.toString());
            String auxKey = key.toString();
            this.dataTypes.add(auxKey);
            String filePath = (String) filesMap.get((key));
            List<?> data;
            if(filePath.endsWith(".csv")){
                System.out.println("VOY A LEER ARCHIVO CSV: " + filePath);
                /* Leer datos desde csv por cada tipo de dato*/
                data = this.readCsvFile(filePath, auxKey);
                dataMap.put(auxKey, data);
            }
            else{
                /* ACTUALIZAR LECTURA DE DATOS USANDO GSON!!!*/
                System.out.println("VOY A LEER ARCHIVO JSON: " + filePath);
                JsonReader reader = new JsonReader(new FileReader(filePath));
                Type type;
                switch (auxKey) {
                    case PlayerPanel.KEYSTROKES_DATA_TYPE:
                        type = new TypeToken<List<Keystroke>>() {
                        }.getType();
                        break;
                    case PlayerPanel.MOUSE_MOVES_DATA_TYPE:
                        type = new TypeToken<List<MouseMove>>() {
                        }.getType();
                        break;
                    case PlayerPanel.MOUSE_CLICKS_DATA_TYPE:
                        type = new TypeToken<List<MouseClick>>() {
                        }.getType();
                        break;
                    case PlayerPanel.MOUSE_UPS_DATA_TYPE:
                        type = new TypeToken<List<MouseUp>>() {
                        }.getType();
                        break;
                    case PlayerPanel.TABS_DATA_TYPE:
                        type = new TypeToken<List<TabAction>>() {
                        }.getType();
                        break;
                    case PlayerPanel.SEARCHS_DATA_TYPE:
                        type = new TypeToken<List<SearchAction>>() {
                        }.getType();
                        break;
                    default:
                        type = null;
                        break;
                }
                data = gson.fromJson(reader, type);
                dataMap.put(auxKey, data);
            }
        }
        return dataMap;
    }


    /* La estrategia de visualización es la siguiente:

    Dado un tiempo que queremos reproducir, encontraremos todos los registros iguales o anteriores en tiempo
     y lo agregaremos a las filas de la tabla correspondiente.
     */
    private List<Object> getDataByCaptureMilliseconds(long milliseconds, String dataType){
        if(milliseconds < this.start || milliseconds > this.end){
            return null;
        }
        return this.dataMap.get(dataType).stream()
                .map(object -> (Visualizable) object)
                .filter(object -> object.getCaptureMilliseconds() <= milliseconds)
                .collect(Collectors.toList());
    }

    private List<Object> readCsvFile(String filePath, String dataType){
        List<Object> data = new ArrayList<>();
        try (BufferedReader br = Files.newBufferedReader(Paths.get(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                Object model;
                /* Leer linea a linea y crear lista de objeto segun el tipo*/
                switch (dataType) {
                    case PlayerPanel.KEYSTROKES_DATA_TYPE:
                        model = new Keystroke(line);
                        break;
                    case PlayerPanel.MOUSE_MOVES_DATA_TYPE:
                        model = new MouseMove(line);
                        break;
                    case PlayerPanel.MOUSE_CLICKS_DATA_TYPE:
                        model = new MouseClick(line);
                        break;
                    case PlayerPanel.MOUSE_UPS_DATA_TYPE:
                        model = new MouseUp(line);
                        break;
                    case PlayerPanel.TABS_DATA_TYPE:
                        model = new TabAction(line);
                        break;
                    case PlayerPanel.SEARCHS_DATA_TYPE:
                        model = new SearchAction(line);
                        break;
                    default:
                        model = null;
                        break;
                }
                data.add(model);
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "", e);
        }
        return data;
    }
}
