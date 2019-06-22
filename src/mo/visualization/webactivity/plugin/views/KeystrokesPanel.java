package mo.visualization.webactivity.plugin.views;


import com.google.gson.JsonObject;
import mo.visualization.webactivity.plugin.models.Keystroke;

import java.util.List;
import java.util.stream.Collectors;

public class KeystrokesPanel extends BasePanel {

    public KeystrokesPanel(){
        super();
        this.tableHeaders = this.getTableHeaders();
    }


    @Override
    public List<String> getTableHeaders(){
        List<String> headers = this.initCommonsHeaders();
        headers.add(this.i18n.s("keyValueColumnName"));
        headers.add(this.i18n.s("captureTimestampColumnName"));
        return headers;
    }

    @Override
    public void updateData(List<JsonObject> jsonKeystrokes){
        Object[][] formattedKeystrokes = this.parseData(jsonKeystrokes);
        this.tableModel.setDataVector(formattedKeystrokes, tableHeaders.toArray());
        this.tableModel.fireTableRowsUpdated(1, formattedKeystrokes.length);
    }


    @Override
    public Object[][] parseData(List<JsonObject> data){
        List<Keystroke> keystrokes = data.stream()
                .map(jsonObject -> gson.fromJson(jsonObject.getAsString(), Keystroke.class))
                .collect(Collectors.toList());
        int size = keystrokes.size();
        Object[][] res = new Object[size][];
        for(int i=0 ; i < size; i++){
            Keystroke keystroke = keystrokes.get(i);
            String[] keystrokeData = new String[]{
                    keystroke.getBrowser(),
                    keystroke.getPageUrl(),
                    keystroke.getPageTitle(),
                    keystroke.getKeyValue(),
                    keystroke.getCaptureTimestamp()
            };
            res[i] = keystrokeData;
        }
        return res;
    }

}
