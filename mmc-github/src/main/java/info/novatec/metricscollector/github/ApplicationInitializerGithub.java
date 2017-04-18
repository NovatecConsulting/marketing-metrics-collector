package info.novatec.metricscollector.github;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

import lombok.Getter;
import lombok.Setter;

import info.novatec.metricscollector.commons.ApplicationInitializerCommons;
import info.novatec.metricscollector.commons.RestService;


@SpringBootApplication
@EnableScheduling
@Import(ApplicationInitializerCommons.class)
@ConfigurationProperties(prefix="github")
public class ApplicationInitializerGithub {

    @Setter
    private String token;

    @Getter
    private List<String> urls = new ArrayList<>();

    public static void main(String[] args) {
        SpringApplication.run(ApplicationInitializerGithub.class, args);
    }

    @Bean
    @Autowired
    public GithubCollector githubCollector(RestService restService, GithubMetricsResult metrics){
        GithubCollector collector = new GithubCollector(metrics, restService);
        collector.setToken(token);
        return collector;
    }

    @Bean
    public List<String> urls(){
        return urls;
    }

}
