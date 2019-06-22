package mo.visualization.webactivity.plugin.views;

import com.google.gson.JsonObject;
import mo.visualization.webactivity.plugin.models.MouseMove;

import java.util.List;
import java.util.stream.Collectors;

public class MouseMovesPanel extends BasePanel {

    public MouseMovesPanel() {
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
        List<MouseMove> mouseMoves = data.stream()
                .map(jsonObject -> gson.fromJson(jsonObject.getAsString(), MouseMove.class))
                .collect(Collectors.toList());
        int size = mouseMoves.size();
        if(size == 0){
            return null;
        }
        Object[][] mouseMovesData = new Object[size][];
        for(int i = 0; i < size; i++){
            MouseMove mouseMove = mouseMoves.get(i);
            String[] mouseMoveData = new String[]{
                    mouseMove.getBrowser(),
                    mouseMove.getPageUrl(),
                    mouseMove.getPageTitle(),
                    String.valueOf(mouseMove.getxPage()),
                    String.valueOf(mouseMove.getyPage()),
                    String.valueOf(mouseMove.getxClient()),
                    String.valueOf(mouseMove.getyClient()),
                    String.valueOf(mouseMove.getxScreen()),
                    String.valueOf(mouseMove.getyScreen()),
                    String.valueOf(mouseMove.getxMovement()),
                    String.valueOf(mouseMove.getyMovement()),
                    String.valueOf(mouseMove.getCaptureTimestamp())
            };
            mouseMovesData[i] = mouseMoveData;
        }
        return mouseMovesData;
    }
}
