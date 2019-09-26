package mo.visualization.webactivity.plugin.view;

import mo.visualization.webactivity.plugin.model.MouseMove;
import mo.visualization.webactivity.plugin.model.MouseUp;

import javax.swing.*;
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
    void updateData(List<Object> data) {
        SwingUtilities.invokeLater(() -> {
            this.clearTable();
            for (Object datum : data) {
                MouseUp mouseUp = (MouseUp) datum;
                Object[] rowData = new String[]{
                        mouseUp.getBrowser(),
                        mouseUp.getPageUrl(),
                        mouseUp.getPageTitle(),
                        mouseUp.getSelectedText(),
                        String.valueOf(mouseUp.getCaptureTimestamp())
                };
                this.insertNewRow(rowData);
            }
        });
    }
}
