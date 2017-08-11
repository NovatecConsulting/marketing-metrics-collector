package info.novatec.metricscollector.linkedin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

import info.novatec.metricscollector.commons.CommonsApplicationInitializer;
import info.novatec.metricscollector.commons.rest.RestService;


@EnableScheduling
@SpringBootApplication
@Import(CommonsApplicationInitializer.class)
public class LinkedInApplicationInitializer {

    @Autowired
    private RestTemplate restTemplate;

    public static void main(String[] args) {
        SpringApplication.run(LinkedInApplicationInitializer.class, args);
    }

    @Bean
    public RestService restService(){
        return new RestService(restTemplate).prepareRequest();
    }
}
