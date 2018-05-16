package univer.webapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import univer.webapp.controller.util.Util;
import univer.webapp.forecast.adaptive.AdaptiveModelForecasting;
import univer.webapp.model.Range;
import univer.webapp.model.Response;


@RestController
public class MainController {

    @Autowired
    private AdaptiveModelForecasting forecast;

    @RequestMapping("/go")
    @CrossOrigin
    public Response getForecast(@RequestParam(value = "data") String data) {
        Range range = new Range();
        Double[] doubles = Util.convertStringToArray(data);
        range.loadFrom(doubles);


        Range correctSeasonComponent = forecast
                .getCorrectSeasonComponent(forecast
                        .getSeasonComponentAssessment(range, forecast
                                .getCentreMovingAverage(forecast.getMovingAverage(range))));

        Double[] solutionOfEquation = forecast.getSolutionOfEquation(forecast
                .getExcludedInfluenceComponent(correctSeasonComponent, range));

        Double[] forecastForAllPeriods = forecast
                .getForecast(correctSeasonComponent, solutionOfEquation, range.size() + 1);


        Response response = new Response();
        response.setForecast(forecastForAllPeriods);
        response.setRange(range.getList().toArray(new Double[]{}));
        response.setSeasonComponent(correctSeasonComponent.getList().toArray(new Double[]{}));
        response.setForecastRange(forecast.getModelRange(solutionOfEquation,correctSeasonComponent,range)
                .getList().toArray(new Double[]{}));
        response.setLinearTrend(solutionOfEquation);
        return response;
    }
}
