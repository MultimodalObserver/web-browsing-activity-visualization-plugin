package mo.visualization.webactivity.plugin.views;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import mo.core.I18n;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;

public abstract class BasePanel extends JPanel {

    protected JLabel noDataLabel;
    protected JTable table;
    protected JScrollPane scrollPane;
    protected DefaultTableModel tableModel;
    protected I18n i18n;
    protected List<String> tableHeaders;
    protected static final Gson gson = new Gson();


    protected BasePanel(){
        this.i18n = new I18n(BasePanel.class);
        this.initComponents();
        this.addComponents();
    }

    abstract List<String> getTableHeaders();
    abstract void updateData(List<JsonObject> data);
    abstract Object[][] parseData(List<JsonObject> data);



    public void showPanel(boolean show){
        this.setVisible(show);
    }

    public void setScrollPaneVisibility(boolean scrollPaneVisibility){
        this.noDataLabel.setVisible(!scrollPaneVisibility);
        this.scrollPane.setVisible(scrollPaneVisibility);
    }

    private void initComponents(){
        /* Iniciamos la tabla y el modelo*/
        this.tableModel = new DefaultTableModel();
        this.table = new JTable(this.tableModel);
        this.table.setFillsViewportHeight(true);
        this.table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.table.setCellSelectionEnabled(true);
        this.table.setShowHorizontalLines(false);
        /* Iniciamos el scroll pane que tendra la tabla */
        this.scrollPane = new JScrollPane(this.table);
        this.scrollPane.setVisible(false);
        /* Iniciamos el label de no data y se muestra por defecto*/
        this.noDataLabel = new JLabel(this.i18n.s("noDataLabelText"));
        this.noDataLabel.setLocation(this.getWidth() /2,this.getHeight()/2);
        this.setVisible(true);
    }

    private void addComponents() {
        this.add(scrollPane);
        this.add(noDataLabel);
    }

    public List<String> initCommonsHeaders(){
        List<String> headers = new ArrayList<>();
        headers.add(this.i18n.s("browserColumnName"));
        headers.add(this.i18n.s("pageUrlColumnName"));
        headers.add(this.i18n.s("pageTitleColumnName"));
        return headers;
    }

    public void addPositionHeaders(List<String> headers){
        headers.add(this.i18n.s("xPageColumnName"));
        headers.add(this.i18n.s("yPageColumnName"));
        headers.add(this.i18n.s("xClientColumnName"));
        headers.add(this.i18n.s("yClientColumnName"));
        headers.add(this.i18n.s("xScreenColumnName"));
        headers.add(this.i18n.s("yScreenColumnName"));
        headers.add(this.i18n.s("xMovementColumnName"));
        headers.add(this.i18n.s("yMovementColumnName"));
    }
}
