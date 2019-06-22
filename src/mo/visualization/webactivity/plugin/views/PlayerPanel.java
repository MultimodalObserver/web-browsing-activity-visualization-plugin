package mo.visualization.webactivity.plugin.views;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import mo.core.I18n;
import mo.visualization.webactivity.plugin.models.*;

import javax.swing.*;
import javax.xml.crypto.Data;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerPanel extends JTabbedPane {

    private static final int KEYSTROKES_TAB_INDEX = 0;
    private static final int MOUSE_MOVES_TAB_INDEX = 1;
    private static final int MOUSE_CLICKS_TAB_INDEX = 2;
    private static final int MOUSE_UPS_TAB_INDEX = 3;
    private JLabel noDataLabel;
    private JTabbedPane tabbedPane;
    private Map<String, BasePanel> panelsMap;


    /* Los tipos de datos que vienen en el archivo de mapeo a los archivos reales,
    tienen el mismo nombre que las rutas definidas en el plugin de captura, sin considerar el / al inicio!!
     */
    private static final String KEYSTROKES_DATA_TYPE = "keystrokes";
    private static final String MOUSE_MOVES_DATA_TYPE = "mouseMoves";
    private static final String MOUSE_CLICKS_DATA_TYPE = "mouseClicks";
    private static final String MOUSE_UPS_DATA_TYPE = "mouseUps";

    private I18n i18n;


    /* Cuando creamos el panel, definimos los tipos de datos que vamos a mostrar, seteando una lista de subvistas
    * donde cada una corresponde a un tipo de dato en específico, y las agregamos al panel principal del player.
    *
    *
    * Mostraremos cada tipo de dato en una tab correspondiente
    * */
    public PlayerPanel(List<String> dataTypes){
        this.i18n = new I18n(PlayerPanel.class);
        if(dataTypes == null || dataTypes.isEmpty()){
            this.noDataLabel = new JLabel(this.i18n.s("noDataLabelText"));
            this.noDataLabel.setLocation(this.getWidth()/2, this.getHeight()/2);
            this.noDataLabel.setVisible(true);
            return;
        }
        this.panelsMap = this.createPanelsMap(dataTypes);
        this.tabbedPane = new JTabbedPane();
        for(String dataType : dataTypes){
            this.tabbedPane.addTab(this.i18n.s(dataType + "PanelName"), this.panelsMap.get(dataType));
        }
        this.tabbedPane.setVisible(true);
        this.add(tabbedPane);
    }

    private Map<String, BasePanel> createPanelsMap(List<String> dataTypes){
        Map<String, BasePanel> panelsMap = new HashMap<>();
        for(String dataType : dataTypes){
            BasePanel panel;
            switch(dataType){
                case KEYSTROKES_DATA_TYPE:
                    panel = new KeystrokesPanel();
                    break;
                case MOUSE_MOVES_DATA_TYPE:
                    panel = new MouseMovesPanel();
                    break;
                case MOUSE_CLICKS_DATA_TYPE:
                    panel = new MouseClicksPanel();
                    break;
                case MOUSE_UPS_DATA_TYPE:
                    panel = new MouseUpsPanel();
                    break;
                default:
                    panel = null;
                    break;
            }
            panelsMap.put(dataType, panel);
        }
        return panelsMap;
    }

    public void updatePanelData(List<JsonObject> data, String dataType){
        if(this.noDataLabel.isVisible()){
            this.noDataLabel.setText("");
            this.noDataLabel.setVisible(false);
        }
        BasePanel panel = this.panelsMap.get(dataType);
        panel.updateData(data);
        panel.showPanel(true);
    }

    public void showPanel(boolean show){
        this.setVisible(show);
    }
}
