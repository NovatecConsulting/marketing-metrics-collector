package info.novatec.metricscollector.twitter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import info.novatec.metricscollector.commons.ApplicationInitializerCommons;


@SpringBootApplication
@Import(ApplicationInitializerCommons.class)
public class ApplicationInitializerTwitter {

    public static void main(String[] args) {
        SpringApplication.run(ApplicationInitializerTwitter.class, args);
    }

}
