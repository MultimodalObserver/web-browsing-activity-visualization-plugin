package mo.visualization.webactivity.plugin.model;

public class MouseUp implements Visualizable {

    private String browser;
    private String pageUrl;
    private String pageTitle;
    private String selectedText;
    private Long captureMilliseconds;

    public MouseUp(){

    }

    public MouseUp(String csvLine){
        String[] data = csvLine.split(Separator.CSV_SEPARATOR.getValue());
        this.browser = data[0];
        this.pageUrl = data[1];
        this.pageTitle = data[2];
        this.selectedText = data[3];
        this.captureMilliseconds = Long.parseLong(data[4]);
    }

    public String getBrowser() {
        return browser;
    }

    public void setBrowser(String browser) {
        this.browser = browser;
    }

    public String getPageUrl() {
        return pageUrl;
    }

    public void setPageUrl(String pageUrl) {
        this.pageUrl = pageUrl;
    }

    public String getPageTitle() {
        return pageTitle;
    }

    public void setPageTitle(String pageTitle) {
        this.pageTitle = pageTitle;
    }

    public String getSelectedText() {
        return selectedText;
    }

    public void setSelectedText(String selectedText) {
        this.selectedText = selectedText;
    }

    public Long getCaptureMilliseconds() {
        return captureMilliseconds;
    }

    public void setCaptureMilliseconds(Long captureMilliseconds) {
        this.captureMilliseconds = captureMilliseconds;
    }
}
