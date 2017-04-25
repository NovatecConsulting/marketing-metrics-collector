package info.novatec.metricscollector.github;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import info.novatec.metricscollector.commons.RestService;


@TestConfiguration
public class TestConfig {

    @Bean
    @Autowired
    public GithubCollector githubCollector(RestService restService, GithubMetricsResult metrics){
        GithubCollector collector = new GithubCollector(metrics, restService);
        collector.setToken("");
        return collector;
    }

}
