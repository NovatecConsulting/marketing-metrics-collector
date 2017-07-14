package info.novatec.metricscollector.commons;

import java.util.concurrent.TimeUnit;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;


@SpringBootApplication
public class CommonsApplicationInitializer {

    @Bean
    InfluxDB influxDb(CommonsProperties properties) {
        InfluxDB influxDB = InfluxDBFactory.connect(properties.getUrl());
        return influxDB.enableBatch(2000, 100, TimeUnit.MILLISECONDS);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplateBuilder().build();
    }
}
