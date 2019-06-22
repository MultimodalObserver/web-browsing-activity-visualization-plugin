package mo.visualization.webactivity.plugin.views;

import com.google.gson.JsonObject;
import mo.visualization.webactivity.plugin.models.MouseUp;

import java.util.List;
import java.util.stream.Collectors;

public class MouseUpsPanel extends BasePanel {

    public MouseUpsPanel(){
        super();
        this.tableHeaders = this.getTableHeaders();
    }

    @Override
    List<String> getTableHeaders() {
        List<String> headers = this.initCommonsHeaders();
        headers.add(this.i18n.s("selectedTextColumnName"));
        headers.add(this.i18n.s("captureTimestampColumnName"));
        return headers;
    }

    @Override
    void updateData(List<JsonObject> data) {
        Object[][] parsedData = this.parseData(data);
        this.tableModel.setDataVector(parsedData, this.tableHeaders.toArray());
        this.tableModel.fireTableRowsUpdated(1, parsedData.length);
    }

    @Override
    Object[][] parseData(List<JsonObject> data) {
        List<MouseUp> mouseUps = data.stream()
                .map(jsonObject -> gson.fromJson(jsonObject.getAsString(), MouseUp.class))
                .collect(Collectors.toList());
        int size = mouseUps.size();
        if(size == 0){
            return null;
        }
        Object[][] mouseUpsData = new Object[size][];
        for(int i = 0; i < size; i++) {
            MouseUp mouseUp = mouseUps.get(i);
            String[] mouseUpData = new String[]{
              mouseUp.getBrowser(),
              mouseUp.getPageUrl(),
              mouseUp.getPageTitle(),
              mouseUp.getSelectedText(),
                    /* VER EL TEMA DE PASAR EL LONG DE CAPTURA A TIMESTAMO ENTENDIBLE PARA USUARIO*/
              String.valueOf(mouseUp.getCaptureTimestamp())
            };
            mouseUpsData[i] = mouseUpData;
        }
        return mouseUpsData;
    }
}
