package info.novatec.metricscollector.github;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

import lombok.Setter;

import info.novatec.metricscollector.commons.CommonsApplicationInitializer;

@EnableScheduling
@SpringBootApplication
@Import(CommonsApplicationInitializer.class)
public class GithubApplicationInitializer {

    @Setter
    @Autowired
    private GithubProperties githubProperties;

    public static void main(String[] args) {
        SpringApplication.run(GithubApplicationInitializer.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplateBuilder().build();
    }

}
