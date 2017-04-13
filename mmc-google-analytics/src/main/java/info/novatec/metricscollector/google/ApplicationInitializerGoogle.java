package info.novatec.metricscollector.google;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Import;

import info.novatec.metricscollector.commons.ApplicationInitializerCommons;


@SpringBootApplication
@Import(ApplicationInitializerCommons.class)
@ConfigurationProperties(prefix="google")
public class ApplicationInitializerGoogle {

    public static void main(String[] args) {
        SpringApplication.run(ApplicationInitializerGoogle.class, args);
    }

}
