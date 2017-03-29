package info.novatec.metricscollector.github;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import info.novatec.metricscollector.commons.ApplicationInitializerCommons;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
@Import(ApplicationInitializerCommons.class)
public class ApplicationInitializerGithub {

    public static void main(String[] args) {
        SpringApplication.run(ApplicationInitializerGithub.class, args);
    }

}
