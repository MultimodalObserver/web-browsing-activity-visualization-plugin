package mo.visualization.webactivity.plugin.view;

import java.util.List;

public class TabsPanel extends BasePanel {
    @Override
    List<String> getTableHeaders() {
        return null;
    }

    @Override
    void updateData(List<String> data) {

    }

    @Override
    Object[][] parseData(List<String> data) {
        return new Object[0][];
    }
}
