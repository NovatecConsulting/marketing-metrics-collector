package info.novatec.metricscollector.github;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

import info.novatec.metricscollector.commons.CommonsApplicationInitializer;


@EnableScheduling
@SpringBootApplication
@Import(CommonsApplicationInitializer.class)
public class GithubApplicationInitializer {

    @Autowired
    private GithubProperties properties;

    public static void main(String[] args) {
        SpringApplication.run(GithubApplicationInitializer.class, args);
    }

    @Bean
    public String token() {
        return properties.getToken();
    }
}
