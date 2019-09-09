package mo.visualization.webactivity.plugin.view;

import mo.visualization.webactivity.plugin.model.MouseUp;

import java.util.List;
import java.util.stream.Collectors;

class MouseUpsPanel extends BasePanel {
    MouseUpsPanel() {
        super();
        this.tableHeaders = this.getTableHeaders();
        this.addHeaders();
        this.columnWidths = new float[]{0.1f, 0.5f, 0.1f, 0.1f, 0.2f};
        this.resizeColumns();
    }

    @Override
    List<String> getTableHeaders() {
        List<String> headers = this.initCommonsHeaders();
        headers.add(this.i18n.s("selectedTextColumnName"));
        headers.add(this.i18n.s("captureTimestampColumnName"));
        return headers;
    }

    @Override
    void updateData(String data) {
        MouseUp mouseUp = gson.fromJson(data, MouseUp.class);
        Object[] rowData = new String[]{
                mouseUp.getBrowser(),
                mouseUp.getPageUrl(),
                mouseUp.getPageTitle(),
                mouseUp.getSelectedText(),
                String.valueOf(mouseUp.getCaptureTimestamp())
        };
        this.tableModel.addRow(rowData);
       /* int rowCount = this.table.getRowCount();
        this.tableModel.fireTableRowsInserted(rowCount, rowCount + 1);*/
    }
}
