package webservice.model;

public class Response {

    private Double[] range;
    private Double[] forecastRange;
    private Double[] seasonComponent;
    private Double[] forecast;
    private Double[] linearTrend;


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

    public Double[] getForecastRange() {
        return forecastRange;
    }

    public void setForecastRange(Double[] forecastRange) {
        this.forecastRange = forecastRange;
    }

    public Double[] getLinearTrend() {
        return linearTrend;
    }

    public void setLinearTrend(Double[] linearTrend) {
        this.linearTrend = linearTrend;
    }
}
