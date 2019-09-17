package mo.visualization.webactivity.plugin.view;

import mo.visualization.webactivity.plugin.model.MouseClick;

import java.util.List;

class MouseClicksPanel extends BasePanel {
    MouseClicksPanel() {
        super();
        this.tableHeaders = this.getTableHeaders();
        this.addHeaders();
        this.columnWidths = new float[]{0.083f, 0.1245f, 0.083f, 0.083f, 0.083f, 0.083f,
                0.083f, 0.083f,0.083f, 0.14525f, 0.06225f};
        this.resizeColumns();
    }

    @Override
    List<String> getTableHeaders() {
        List<String> headers = this.initCommonsHeaders();
        this.addMousePositionHeaders(headers, false);
        headers.add(this.i18n.s("mouseClickButtonColumnName"));
        headers.add(this.i18n.s("captureTimestampColumnName"));
        return headers;
    }

    @Override
    void updateData(List<Object> data) {
        this.clearTable();
        for (Object datum : data) {
            MouseClick mouseClick = (MouseClick) datum;
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
                    this.getButtonAsString(mouseClick.getButton()),
                    String.valueOf(mouseClick.getCaptureTimestamp())
            };
            this.insertNewRow(rowData);
        }
    }

    private String getButtonAsString(Integer which){
        String value = "";
        if(which == 0){
            value = this.i18n.s("leftMouseClickValue");
        }
        else if(which == 1){
            value = this.i18n.s("centerMouseClickValue");
        }
        else if(which == 2){
            value = this.i18n.s("rightMouseClickValue");
        }
        return value;
    }
}
