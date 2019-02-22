package mo.visualization.webactivity.plugin.views;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import mo.visualization.webactivity.plugin.Player;

import javax.swing.*;
import java.util.List;
import java.util.Map;

public class KeystrokesView extends BaseView {

    private JLabel displayDataLabel, pageTitleLabel;
    private Gson gson;

    public KeystrokesView(){
        this.dataType = PlayerPanel.KEYSTROKES_DATA_TYPE;
        this.displayDataLabel = new JLabel();
        this.pageTitleLabel = new JLabel();
        this.gson = new Gson();
    }

    /* Cada vez que vamos a crear o actualizar la vista, debemos chequear que venga el tipo de dato que vamos a mostrar
    ya que puede que en ese instante de tiempo no se hayan captado datos de ese tipo
     */
    @Override
    public void createView(Map<String, Map<String, JsonObject>> searchedDataMap){
        if(!this.canPlayData(searchedDataMap)){
            //Mostrar mensaje de que no hay datos en este instante
            return;
        }
        /* Obtenemos el dato */
        Map <String, JsonObject> dataByPage = searchedDataMap.get(this.dataType);
        String pageTitle = (String) dataByPage.keySet().toArray()[0];
        this.pageTitleLabel.setText(pageTitle);
        this.displayDataLabel.setText(gson.toJson(dataByPage.get(pageTitle)));
        this.pageTitleLabel.setVisible(true);
        this.displayDataLabel.setVisible(true);
    }

    @Override
    public void updateView(Map<String, Map<String, JsonObject>> searchedDataMap){
        this.createView(searchedDataMap);
    }

    @Override
    public void removeView(){

    }


}
