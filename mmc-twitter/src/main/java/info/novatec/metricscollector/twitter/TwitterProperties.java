package info.novatec.metricscollector.twitter;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;


@Data
@Component
@ConfigurationProperties("twitter")
public class TwitterProperties {
    private String cron;
    private String consumerKey;
    private String consumerSecret;
    private String accessToken;
    private String accessSecret;
    private Map<String, String> users;
}
