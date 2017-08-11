package info.novatec.metricscollector.linkedin;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;


@Data
@Component
@ConfigurationProperties("linkedin")
public class LinkedInProperties {

    private String client_id;
    private String client_secret;
    private String cron;
}
