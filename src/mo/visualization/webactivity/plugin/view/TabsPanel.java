package mo.visualization.webactivity.plugin.view;

import mo.visualization.webactivity.plugin.model.TabAction;

import java.util.ArrayList;
import java.util.List;

public class TabsPanel extends BasePanel {

    public TabsPanel(){
        super();
        this.tableHeaders = this.getTableHeaders();
        this.addHeaders();
        this.columnWidths = new float[]{0.1f, 0.5f, 0.1f, 0.10f,0.05f, 0.05f, 0.05f, 0.05f};
        this.resizeColumns();
    }

    @Override
    List<String> getTableHeaders() {
        List<String> headers = new ArrayList<>();
        headers.add(this.i18n.s("browserColumnName"));
        headers.add(this.i18n.s("tabUrlColumnName"));
        headers.add(this.i18n.s("tabTitleColumnName"));
        headers.add(this.i18n.s("actionTypeColumnName"));
        headers.add(this.i18n.s("tabIndexColumnName"));
        headers.add(this.i18n.s("tabIdColumnName"));
        headers.add(this.i18n.s("windowIdColumnName"));
        headers.add(this.i18n.s("captureTimestampColumnName"));
        return headers;
    }

    @Override
    void updateData(List<Object> data) {
        this.clearTable();
        for (Object datum : data) {
            TabAction tabAction = (TabAction) datum;
            Object[] rowData = new Object[]{
                    tabAction.getBrowser(),
                    tabAction.getTabUrl(),
                    tabAction.getTabTitle(),
                    tabAction.getActionType(),
                    tabAction.getTabIndex(),
                    tabAction.getTabId(),
                    tabAction.getWindowId(),
                    tabAction.getCaptureMilliseconds()
            };
            this.insertNewRow(rowData);
        }
    }
}
