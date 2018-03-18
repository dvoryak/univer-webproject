package webservice.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import webservice.model.Response;

@RestController
public class MainController {

    @RequestMapping("/go")
    @CrossOrigin
    public Response getForecast(@RequestParam(value = "data") String data) {
        System.out.println(data);
        return new Response();
    }
}
