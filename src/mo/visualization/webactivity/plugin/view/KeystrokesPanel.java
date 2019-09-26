package mo.visualization.webactivity.plugin.view;


import mo.visualization.webactivity.plugin.model.Keystroke;

import javax.swing.*;
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
    void updateData(List<Object> data) {
        SwingUtilities.invokeLater(() -> {
            this.clearTable();
            for (Object datum : data) {
                Keystroke keystroke = (Keystroke) datum;
                Object[] rowData = new String[]{
                        keystroke.getBrowser(),
                        keystroke.getPageUrl(),
                        keystroke.getPageTitle(),
                        keystroke.getKeyValue(),
                        String.valueOf(keystroke.getCaptureTimestamp())
                };
                this.insertNewRow(rowData);
            }
        });
    }

}
