package mo.visualization.webactivity.plugin.view;

import mo.visualization.webactivity.plugin.model.SearchAction;

import javax.swing.*;
import java.util.List;

public class SearchsPanel extends BasePanel{

    public SearchsPanel(){
        super();
        this.tableHeaders = this.getTableHeaders();
        this.addHeaders();
        this.columnWidths = new float[]{0.1f, 0.5f, 0.1f, 0.2f, 0.1f};
        this.resizeColumns();
    }

    @Override
    List<String> getTableHeaders() {
        List<String> headers = this.initCommonsHeaders();
        headers.add(this.i18n.s("searchColumnName"));
        headers.add(this.i18n.s("captureTimestampColumnName"));
        return headers;
    }

    @Override
    void updateData(List<Object> data) {
        SwingUtilities.invokeLater(() -> {
            this.clearTable();
            for (Object datum : data) {
                SearchAction searchAction = (SearchAction) datum;
                Object[] rowData = new Object[]{
                        searchAction.getBrowser(),
                        searchAction.getPageUrl(),
                        searchAction.getPageTitle(),
                        searchAction.getSearch(),
                        searchAction.getCaptureTimestamp()
                };
                this.insertNewRow(rowData);
            }
        });
    }
}
