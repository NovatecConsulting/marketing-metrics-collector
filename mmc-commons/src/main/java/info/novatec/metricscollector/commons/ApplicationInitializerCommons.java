package info.novatec.metricscollector.commons;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;


@SpringBootApplication
public class ApplicationInitializerCommons {

    public static void main(String[] args) {
        SpringApplication.run(ApplicationInitializerCommons.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplateBuilder().build();
    }

    @Bean
    public InfluxService influxService() {
        return new InfluxService();
    }
}
