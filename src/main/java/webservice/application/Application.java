package webservice.application;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import webservice.controller.MainController;

@SpringBootApplication
@Configurable
public class Application {

    @Bean
    public MainController mainController() {
        return new MainController();
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
