package info.novatec.metricscollector.commons;

import org.influxdb.InfluxDBFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import lombok.Setter;


@SpringBootApplication
@ConfigurationProperties(prefix = "influx")
public class ApplicationInitializerCommons {

    @Setter
    private String dbName;

    @Setter
    private String retention;

    @Setter
    private String url;

    public static void main(String[] args) {
        SpringApplication.run(ApplicationInitializerCommons.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplateBuilder().build();
    }

    @Bean
    public InfluxService influxService() {
        InfluxService influxService = new InfluxService();
        influxService.setDbName(dbName);
        influxService.setRetention(retention);
        influxService.setInfluxDB(InfluxDBFactory.connect(url));
        influxService.configureInflux();
        return influxService;
    }
}
