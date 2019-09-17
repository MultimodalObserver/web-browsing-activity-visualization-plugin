package mo.visualization.webactivity.plugin.model;

public class TabAction implements Visualizable {

    private String browser;
    private String tabUrl;
    private String tabTitle;
    private String actionType;
    private Integer tabIndex;
    private Integer tabId;
    private Integer windowId;
    private Long captureTimestamp;

    public TabAction(){

    }

    public String getBrowser() {
        return browser;
    }

    public void setBrowser(String browser) {
        this.browser = browser;
    }

    public String getTabUrl() {
        return tabUrl;
    }

    public void setTabUrl(String tabUrl) {
        this.tabUrl = tabUrl;
    }

    public String getTabTitle() {
        return tabTitle;
    }

    public void setTabTitle(String tabTitle) {
        this.tabTitle = tabTitle;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public Integer getTabId() {
        return tabId;
    }

    public void setTabId(Integer tabId) {
        this.tabId = tabId;
    }

    public Integer getWindowId() {
        return windowId;
    }

    public void setWindowId(Integer windowId) {
        this.windowId = windowId;
    }

    @Override
    public Long getCaptureTimestamp() {
        return captureTimestamp;
    }

    public void setCaptureTimestamp(Long captureTimestamp) {
        this.captureTimestamp = captureTimestamp;
    }

    public Integer getTabIndex() {
        return tabIndex;
    }

    public void setTabIndex(Integer tabIndex) {
        this.tabIndex = tabIndex;
    }
}
