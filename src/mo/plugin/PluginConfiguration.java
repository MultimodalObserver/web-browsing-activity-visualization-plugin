package mo.plugin;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import mo.organization.Configuration;
import mo.plugin.models.VisualizationConfiguration;
import mo.visualization.Playable;
import mo.visualization.VisualizableConfiguration;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PluginConfiguration implements VisualizableConfiguration {

    private static final Logger LOGGER = Logger.getLogger(PluginConfiguration.class.getName());
    private static final String[] CREATORS = new String[] {"mo.plugin.WebBrowsingActivityRecorder"};
    private VisualizationConfiguration temporalConfig;
    private List<File> files;
    private Player player;

    public PluginConfiguration(VisualizationConfiguration temporalConfig) {
        this.temporalConfig = temporalConfig;
        this.files = new ArrayList<>();
        this.player = null;
    }

    public PluginConfiguration(File file){
        String fileName = file.getName();
        String configData = fileName.substring(0, fileName.lastIndexOf("."));
        String[] configElements = configData.split("_");
        /* El elemento 0 es string web-activity-visualization*/
        String configurationName = configElements[1];
        this.temporalConfig  = new VisualizationConfiguration(configurationName);
        this.files = new ArrayList<>();
        this.player = null;
    }

    @Override
    public List<String> getCompatibleCreators() {
        return Arrays.asList(CREATORS);
    }

    @Override
    public void addFile(File file) {
        if(this.files.contains(file)){
            return;
        }
        this.files.add(file);
        int fileIndex = files.size() - 1;
        Map<String, String> selectedFilesMap = getFilepaths(this.files.get(fileIndex));
        this.player = new Player(selectedFilesMap, this.temporalConfig.getName());
    }

    @Override
    public void removeFile(File file) {
        if(!this.files.contains(file)){
            return;
        }
        this.files.remove(file);
    }

    @Override
    public Playable getPlayer() {
        if(this.player == null){
            Map<String, String> selectedFilesMap = getFilepaths(this.files.get(0));
            this.player = new Player(selectedFilesMap, this.temporalConfig.getName());
        }
        return this.player;
    }

    @Override
    public String getId() {
        return this.temporalConfig.getName();
    }

    @Override
    public File toFile(File parent) {
        String childFileName = "web-activity-visualization_"+this.temporalConfig.getName()+".xml";
        File file = new File(parent, childFileName);
        try {
            file.createNewFile();
            return file;
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public Configuration fromFile(File file) {
        String fileName = file.getName();
        if(!fileName.contains("_") || !fileName.contains(".")){
            return null;
        }
        String configData = fileName.substring(0, fileName.lastIndexOf("."));
        String[] configElements = configData.split("_");
        /* El elemento 1 es el string web-activity*/
        String configurationName = configElements[1];
        VisualizationConfiguration auxConfig = new VisualizationConfiguration(configurationName);
        return new PluginConfiguration(auxConfig);
    }

    private Map<String, String> getFilepaths(File mapFile){
        if(mapFile == null || !mapFile.isFile()){
            return null;
        }
        try {
            FileReader fileReader = new FileReader(mapFile);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            Gson gson = new Gson();
            line = bufferedReader.readLine();
            if(line == null){
                return null;
            }
            Type type = new TypeToken<Map<String, String>>(){}.getType();
            return gson.fromJson(line, type);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, null, e);
            return null;
        }
    }
}
