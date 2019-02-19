package mo.plugin;

import bibliothek.util.xml.XElement;
import bibliothek.util.xml.XIO;
import mo.core.I18n;
import mo.core.plugin.Extends;
import mo.core.plugin.Extension;
import mo.organization.Configuration;
import mo.organization.ProjectOrganization;
import mo.organization.StagePlugin;
import mo.visualization.VisualizationProvider;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Extension(
        xtends = {
                @Extends(
                        extensionPointId = "mo.visualization.VisualizationProvider"
                )
        }
)

public class WebActivityVisualizationPlugin implements VisualizationProvider {

    private static final Logger LOGGER = Logger.getLogger(WebActivityVisualizationPlugin.class.getName());
    private I18n i18n;
    List<Configuration> configurations;

    public WebActivityVisualizationPlugin() {
        this.i18n = new I18n(WebActivityVisualizationPlugin.class);
        this.configurations = new ArrayList<>();
    }

    @Override
    public String getName() {
        return this.i18n.s("PLUGIN_DISPLAYED_NAME");
    }

    @Override
    public Configuration initNewConfiguration(ProjectOrganization projectOrganization) {
        return null;
    }

    @Override
    public List<Configuration> getConfigurations() {
        return this.configurations;
    }

    @Override
    public StagePlugin fromFile(File file) {
        if(!file.isFile()){
            return null;
        }
        try{
            WebActivityVisualizationPlugin processesVisualizationPlugin = new WebActivityVisualizationPlugin();
            XElement root = XIO.readUTF(new FileInputStream(file));
            XElement[] pathsX = root.getElements("path");
            for (XElement pathX : pathsX) {
                String path = pathX.getString();
                File archive = new File(file.getParentFile(), path);
                Configuration config = new PluginConfiguration(archive);
                processesVisualizationPlugin.configurations.add(config);
            }
            return processesVisualizationPlugin;
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, null, e);
            return null;
        }
    }

    @Override
    public File toFile(File parent) {
        File file = new File(parent, "web-activity-visualization.xml");
        if (!file.isFile()) {
            try {
                file.createNewFile();
            } catch (IOException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }
        }
        XElement root = new XElement("vis");
        for (Configuration config : configurations) {
            File p = new File(parent, "web-activity-visualization");
            p.mkdirs();
            File f = config.toFile(p);

            XElement path = new XElement("path");
            Path parentPath = parent.toPath();
            Path configPath = f.toPath();
            path.setString(parentPath.relativize(configPath).toString());
            root.addElement(path);
        }
        try {
            XIO.writeUTF(root, new FileOutputStream(file));
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        return file;
    }
}
