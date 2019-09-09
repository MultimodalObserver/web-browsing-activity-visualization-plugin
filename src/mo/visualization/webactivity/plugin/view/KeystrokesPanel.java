package mo.visualization.webactivity.plugin.view;


import mo.visualization.webactivity.plugin.model.Keystroke;

import java.util.List;
import java.util.stream.Collectors;

class KeystrokesPanel extends BasePanel {
    KeystrokesPanel() {
        super();
        this.tableHeaders = this.getTableHeaders();
        this.addHeaders();
        this.columnWidths = new float[]{0.1f, 0.5f, 0.1f, 0.1f, 0.2f};
        this.resizeColumns();
    }

    @Override
    List<String> getTableHeaders() {
        List<String> headers = this.initCommonsHeaders();
        headers.add(this.i18n.s("keyValueColumnName"));
        headers.add(this.i18n.s("captureTimestampColumnName"));
        return headers;
    }

    @Override
    void updateData(String data) {
        Keystroke keystroke = gson.fromJson(data, Keystroke.class);
        Object[] rowData = new String[]{
                keystroke.getBrowser(),
                keystroke.getPageUrl(),
                keystroke.getPageTitle(),
                keystroke.getKeyValue(),
                keystroke.getCaptureTimestamp()
        };
        this.tableModel.addRow(rowData);
        /*int rowCount = this.table.getRowCount();
        this.tableModel.fireTableRowsInserted(rowCount, rowCount + 1);*/
    }

}
