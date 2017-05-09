package info.novatec.metricscollector.github;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;


@Data
@Component
@ConfigurationProperties("github")
public class GithubProperties {

    private String token;
    private String cron;
    private List<String> urls;
}
