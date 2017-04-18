package info.novatec.metricscollector.github;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;


@TestConfiguration
public class TestConfig {

    @Bean
    @Scope("prototype")
    public GithubMetricsResult githubMetricsResult() {
        return new GithubMetricsResult();
    }
}
