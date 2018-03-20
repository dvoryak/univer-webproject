package webservice.model;

public class Response {

    private Double[] range;
    private Double[] seasonComponent;
    private Double[] forecast;


    public Response() {
    }

    public Double[] getSeasonComponent() {
        return seasonComponent;
    }

    public void setSeasonComponent(Double[] seasonComponent) {
        this.seasonComponent = seasonComponent;
    }

    public Double[] getForecast() {
        return forecast;
    }

    public void setForecast(Double[] forecast) {
        this.forecast = forecast;
    }

    public Double[] getRange() {
        return range;
    }

    public void setRange(Double[] range) {
        this.range = range;
    }


}
