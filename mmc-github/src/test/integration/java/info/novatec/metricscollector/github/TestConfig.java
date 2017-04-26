package info.novatec.metricscollector.github;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import info.novatec.metricscollector.commons.RestService;


@TestConfiguration
@Import(ApplicationInitializerGithub.class)
public class TestConfig {

    @Bean
    @Autowired
    public GithubCollector githubCollector(RestService restService){
        GithubCollector collector = new GithubCollector(restService);
        collector.setToken("");
        return collector;
    }

}
