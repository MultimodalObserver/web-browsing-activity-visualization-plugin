package mo.visualization.webactivity.plugin.model;

public class SearchAction implements Visualizable {
    private String browser;
    private String pageUrl;
    private String pageTitle;
    private String search;
    private Long captureMilliseconds;

    public SearchAction(){

    }

    public SearchAction(String csvLine){
        String[] data = csvLine.split(Separator.CSV_SEPARATOR.getValue());
        this.browser = data[0];
        this.pageUrl = data[1];
        this.pageTitle = data[2];
        this.search = data[3];
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

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    @Override
    public Long getCaptureMilliseconds() {
        return captureMilliseconds;
    }

    public void setCaptureMilliseconds(Long captureMilliseconds) {
        this.captureMilliseconds = captureMilliseconds;
    }
}
