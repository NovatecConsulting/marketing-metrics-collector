package info.novatec.metricscollector.github;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import info.novatec.metricscollector.commons.ApplicationInitializerCommons;


@SpringBootApplication
@Import(ApplicationInitializerCommons.class)
public class ApplicationInitializerGithub {

    public static void main(String[] args) {
        SpringApplication.run(ApplicationInitializerGithub.class, args);
    }

}
