package mo.visualization.webactivity.plugin.view;

import mo.visualization.webactivity.plugin.model.MouseMove;

import java.util.List;

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
        this.addMousePositionHeaders(headers, true);
        headers.add(this.i18n.s("captureTimestampColumnName"));
        return headers;
    }

    @Override
    void updateData(List<Object> data) {
        this.clearTable();
        for (Object datum : data) {
            MouseMove mouseMove = (MouseMove) datum;
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
            this.insertNewRow(rowData);
        }
    }

}
