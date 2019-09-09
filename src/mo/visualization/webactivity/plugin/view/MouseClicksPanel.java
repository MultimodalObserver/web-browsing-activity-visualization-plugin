package mo.visualization.webactivity.plugin.view;

import mo.visualization.webactivity.plugin.model.MouseClick;

import java.util.List;
import java.util.stream.Collectors;

class MouseClicksPanel extends BasePanel {
    MouseClicksPanel() {
        super();
        this.tableHeaders = this.getTableHeaders();
        this.addHeaders();
        this.columnWidths = new float[]{0.0415f, 0.1245f, 0.083f, 0.083f, 0.083f, 0.083f, 0.083f,
                0.083f, 0.083f,0.083f, 0.0415f, 0.1245f};
        this.resizeColumns();
    }

    @Override
    List<String> getTableHeaders() {
        List<String> headers = this.initCommonsHeaders();
        this.addPositionHeaders(headers);
        headers.add(this.i18n.s("captureTimestampColumnName"));
        return headers;
    }

    @Override
    void updateData(String data) {
        MouseClick mouseClick = gson.fromJson(data, MouseClick.class);
        Object[] rowData = new String[]{
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
        this.tableModel.addRow(rowData);
        /*int rowCount = this.table.getRowCount();
        this.tableModel.fireTableRowsInserted(rowCount, rowCount + 1);*/
    }
}
