package mo.visualization.webactivity.plugin.models;

public class Keystroke {
    private String browser;
    private String pageUrl;
    private String pageTitle;
    private String keyValue;
    private String captureTimestamp;


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

    public String getKeyValue() {
        return keyValue;
    }

    public void setKeyValue(String keyValue) {
        this.keyValue = keyValue;
    }

    public String getCaptureTimestamp() {
        return captureTimestamp;
    }

    public void setCaptureTimestamp(String captureTimestamp) {
        this.captureTimestamp = captureTimestamp;
    }
}
