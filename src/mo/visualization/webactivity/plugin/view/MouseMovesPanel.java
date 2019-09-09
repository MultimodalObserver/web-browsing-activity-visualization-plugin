package mo.visualization.webactivity.plugin.view;

import mo.visualization.webactivity.plugin.model.MouseMove;

import java.util.List;
import java.util.stream.Collectors;

class MouseMovesPanel extends BasePanel {
    MouseMovesPanel() {
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
        MouseMove mouseMove = gson.fromJson(data, MouseMove.class);
        Object[] rowData = new String[]{
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
        this.tableModel.addRow(rowData);
        /*int rowCount = this.table.getRowCount();
        this.tableModel.fireTableRowsInserted(rowCount, rowCount + 1);*/
    }

}
