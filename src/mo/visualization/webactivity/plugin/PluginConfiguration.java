package mo.visualization.webactivity.plugin;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import mo.organization.Configuration;
import mo.visualization.webactivity.plugin.model.VisualizationConfiguration;
import mo.visualization.Playable;
import mo.visualization.VisualizableConfiguration;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.lang.reflect.Type;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PluginConfiguration implements VisualizableConfiguration {

    private static final Logger LOGGER = Logger.getLogger(PluginConfiguration.class.getName());
    private static final String[] CREATORS = new String[] {"mo.capture.webActivity.plugin.WebBrowsingActivityRecorder"};
    private VisualizationConfiguration temporalConfig;
    private List<File> files;
    private Player player;

    public PluginConfiguration(){

    }

    public PluginConfiguration(VisualizationConfiguration temporalConfig) {
        this.temporalConfig = temporalConfig;
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
        File lastFile = this.files.get(fileIndex);
        Map<String, String> selectedFilesMap = getFilepaths(lastFile);
        try {
            this.player = new Player(selectedFilesMap, this.temporalConfig.getName());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
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
            try {
                this.player = new Player(selectedFilesMap, this.temporalConfig.getName());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
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
        try{
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            JAXBContext jaxbContext = JAXBContext.newInstance(VisualizationConfiguration.class);
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(this.temporalConfig, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            return file;
        } catch (IOException | JAXBException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public Configuration fromFile(File file) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(VisualizationConfiguration.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            VisualizationConfiguration auxConfig = (VisualizationConfiguration) unmarshaller.unmarshal(file);
            return new PluginConfiguration(auxConfig);
        } catch (JAXBException e) {
            e.printStackTrace();
            return null;
        }
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
