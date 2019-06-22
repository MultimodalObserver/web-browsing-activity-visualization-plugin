package mo.visualization.webactivity.plugin.views;

import com.google.gson.JsonObject;
import mo.visualization.webactivity.plugin.models.MouseClick;

import java.util.List;
import java.util.stream.Collectors;

public class MouseClicksPanel extends BasePanel {

    public MouseClicksPanel(){
        super();
        this.tableHeaders = this.getTableHeaders();
    }

    @Override
    List<String> getTableHeaders() {
        List<String> headers = this.initCommonsHeaders();
        this.addPositionHeaders(headers);
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
        List<MouseClick> mouseClicks = data.stream()
                .map(jsonObject -> gson.fromJson(jsonObject.getAsString(), MouseClick.class))
                .collect(Collectors.toList());
        int size = mouseClicks.size();
        if(size == 0){
            return null;
        }
        Object[][] mouseClicksData  = new Object[size][];
        for(int i =0; i < size; i++){
            MouseClick mouseClick = mouseClicks.get(i);
            String[] mouseClickData = new String[]{
                    mouseClick.getBrowser(),
                    mouseClick.getPageUrl(),
                    mouseClick.getPageTitle(),
                    String.valueOf(mouseClick.getxPage()),
                    String.valueOf(mouseClick.getyPage()),
                    String.valueOf(mouseClick.getxClient()),
                    String.valueOf(mouseClick.getyClient()),
                    String.valueOf(mouseClick.getxScreen()),
                    String.valueOf(mouseClick.getyScreen()),
                    String.valueOf(mouseClick.getxMovement()),
                    String.valueOf(mouseClick.getyMovement()),
                    String.valueOf(mouseClick.getCaptureTimestamp())
            };
            mouseClicksData[i] = mouseClickData;
        }
        return mouseClicksData;
    }
}
