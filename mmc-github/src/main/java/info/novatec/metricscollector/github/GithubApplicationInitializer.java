package info.novatec.metricscollector.github;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

import lombok.Getter;
import lombok.Setter;

import info.novatec.metricscollector.commons.CommonsApplicationInitializer;


@SpringBootApplication
@EnableScheduling
@Import(CommonsApplicationInitializer.class)
@ConfigurationProperties(prefix="github")
@Setter
public class GithubApplicationInitializer {

    private String token;

    @Getter
    private List<String> urls = new ArrayList<>();

    public static void main(String[] args) {
        SpringApplication.run(GithubApplicationInitializer.class, args);
    }

    @Bean
    public String token(){
        return token;
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplateBuilder().build();
    }

}
