package mo.visualization.webactivity.plugin.model;

public class MouseMove implements Visualizable {

    private String browser;
    private String pageUrl;
    private String pageTitle;
    private Long xPage;
    private Long yPage;
    private Long xClient;
    private Long yClient;
    private Long xScreen;
    private Long yScreen;
    private Long xMovement;
    private Long yMovement;
    private Long captureMilliseconds;

    public MouseMove(){

    }

    public MouseMove(String csvLine){
        String[] data = csvLine.split(Separator.CSV_SEPARATOR.getValue());
        this.browser = data[0];
        this.pageUrl = data[1];
        this.pageTitle = data[2];
        this.xPage = Long.parseLong(data[3]);
        this.yPage = Long.parseLong(data[4]);
        this.xClient = Long.parseLong(data[5]);
        this.yClient = Long.parseLong(data[6]);
        this.xScreen = Long.parseLong(data[7]);
        this.yScreen = Long.parseLong(data[8]);
        this.xMovement = Long.parseLong(data[9]);
        this.yMovement = Long.parseLong(data[10]);
        this.captureMilliseconds = Long.parseLong(data[11]);
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

    public Long getxPage() {
        return xPage;
    }

    public void setxPage(Long xPage) {
        this.xPage = xPage;
    }

    public Long getyPage() {
        return yPage;
    }

    public void setyPage(Long yPage) {
        this.yPage = yPage;
    }

    public Long getxClient() {
        return xClient;
    }

    public void setxClient(Long xClient) {
        this.xClient = xClient;
    }

    public Long getyClient() {
        return yClient;
    }

    public void setyClient(Long yClient) {
        this.yClient = yClient;
    }

    public Long getxScreen() {
        return xScreen;
    }

    public void setxScreen(Long xScreen) {
        this.xScreen = xScreen;
    }

    public Long getyScreen() {
        return yScreen;
    }

    public void setyScreen(Long yScreen) {
        this.yScreen = yScreen;
    }

    public Long getxMovement() {
        return xMovement;
    }

    public void setxMovement(Long xMovement) {
        this.xMovement = xMovement;
    }

    public Long getyMovement() {
        return yMovement;
    }

    public void setyMovement(Long yMovement) {
        this.yMovement = yMovement;
    }

    @Override
    public Long getCaptureMilliseconds() {
        return captureMilliseconds;
    }

    public void setCaptureMilliseconds(Long captureMilliseconds) {
        this.captureMilliseconds = captureMilliseconds;
    }
}
