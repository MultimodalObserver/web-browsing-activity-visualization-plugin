package mo.visualization.webactivity.plugin.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class VisualizationConfiguration {
    public VisualizationConfiguration(){

    }

    private String name;

    public VisualizationConfiguration(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
