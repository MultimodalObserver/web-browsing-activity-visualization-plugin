package mo.visualization.webactivity.plugin.model;

public enum Separator {
    CSV_SEPARATOR("*_coma_*");

    private final String value;

    Separator(String value){
        this.value  = value;
    }

    public String getValue(){
        return this.value;
    }
}
