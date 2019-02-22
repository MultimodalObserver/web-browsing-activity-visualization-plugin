package mo.visualization.webactivity.plugin.views;

import com.google.gson.JsonObject;

import javax.swing.*;
import java.util.Map;

public class BaseView extends JFrame {

    protected String dataType;

    public void createView(Map<String, Map<String, JsonObject>> searchedDataMap){

    }

    public void updateView(Map<String, Map<String, JsonObject>> searchedDataMap){

    }

    public void removeView(){

    }

    protected boolean canPlayData(Map<String, Map<String, JsonObject>> searchedDataMap){
        return searchedDataMap.containsKey(this.dataType);
    }

}
