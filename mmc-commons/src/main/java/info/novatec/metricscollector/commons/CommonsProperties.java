package info.novatec.metricscollector.commons;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;


@Data
@Component
@ConfigurationProperties("influx")
public class CommonsProperties {

    private String url;
    private String dbName;
    private String retention;
}
