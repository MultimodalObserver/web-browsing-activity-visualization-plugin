package mo.plugin.views;

import com.google.gson.JsonObject;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PlayerPanel extends JPanel {

    private JLabel noDataLabel;
    private List<BaseView> dataTypeViews;


    /* Los tipos de datos que vienen en el archivo de mapeo a los archivos reales,
    tienen el mismo nombre que las rutas definidas en el plugin de captura, sin considerar el / al inicio!!
     */
    private static final String KEYSTROKES_DATA_TYPE = "keystrokes";
    private static final String MOUSE_MOVES_DATA_TYPE = "mouseMoves";
    private static final String MOUSE_CLICKS_DATA_TYPE = "mouseClicks";
    private static final String MOUSE_UPS_DATA_TYPE = "mouseUps";


    /* Cuando creamos el panel, definimos los tipos de datos que vamos a mostrar, seteando una lista de subvistas
    * donde cada una corresponde a un tipo de dato en específico, y las agregamos al panel principal del player.
    *
    *
    * FALTA CONFIGURAR EL GRIDBAGLAYPUT PARA QUE SE MUESTREN LAS VISTAS EN UNA CUADRICULA!!*/
    public PlayerPanel(List<String> dataTypes){
        if(dataTypes == null || dataTypes.isEmpty()){
            this.noDataLabel = new JLabel("There's no data to show");
            this.noDataLabel.setVisible(true);
            return;
        }
        this.dataTypeViews = new ArrayList<>();
        for(String dataType : dataTypes){
            BaseView view = null;
            if(dataType.equals(KEYSTROKES_DATA_TYPE)){
                view = new KeystrokesView();
            }
            else if(dataType.equals(MOUSE_MOVES_DATA_TYPE)){
                view = new MouseMovesView();
            }
            else if(dataType.equals(MOUSE_CLICKS_DATA_TYPE)){
                view = new MouseClicksView();
            }
            else if(dataType.equals(MOUSE_UPS_DATA_TYPE)){
                view = new MouseUpsView();
            }
            if(view == null){
                return;
            }
            this.dataTypeViews.add(view);
            this.add(view);
        }
    }

    /* Este metodo se utiliza para cuando se inicia la reproducción, en el primer instante de la visualización,
    se "crean" las vistas.
     */
    public void createViews(Map<String, Map<String, JsonObject>> searchedDataMap){
        for(BaseView view : this.dataTypeViews){
            view.createView(searchedDataMap);
        }
    }

    /* Este metodo se utiliza para cuando se reproduce un sintante que no es el primero, los objetos de la vista ya estan
    creados y solo se actualizan sus valores.
     */
    public void updateViews(Map<String, Map<String, JsonObject>> searchedDataMap){
        for(BaseView view : this.dataTypeViews){
            view.updateView(searchedDataMap);
        }
    }


    /* Este metodo se utiliza cuando se termina la reproduccion, "matando" todas las subvistas, es decir, removiendo
    todo como corresponde, para poder reiniciar la reproducción de forma facil después.
     */
    public void removeViews(){
        for(BaseView view : this.dataTypeViews){
            view.removeView();
            this.remove(view);
        }
    }
}
