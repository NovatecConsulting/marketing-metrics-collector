package info.novatec.metricscollector.commons;

import org.influxdb.InfluxDBFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

import lombok.Setter;


@SpringBootApplication
@ConfigurationProperties(prefix = "influx")
public class CommonsApplicationInitializer {

    @Setter
    String url;

    @Bean
    public InfluxService influxService() {
        return new InfluxService(InfluxDBFactory.connect(url));
    }
}
