package info.novatec.metricscollector.google;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;

import com.google.api.services.analyticsreporting.v4.*;

import info.novatec.metricscollector.commons.ApplicationInitializerCommons;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import java.io.IOException;
import java.security.GeneralSecurityException;

@SpringBootApplication
@Import(ApplicationInitializerCommons.class)
@ConfigurationProperties(prefix = "google")
public class ApplicationInitializerGoogle {
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

    @Value("${analytics.applicationName}")
    private String applicationName;

    @Value("${analytics.keyFileLocation}")
    private String keyFileLocation;

    public static void main(String[] args) {
        SpringApplication.run(ApplicationInitializerGoogle.class, args);
    }

    @Bean
    public HttpTransport httpTransport() throws GeneralSecurityException, IOException {
        return GoogleNetHttpTransport.newTrustedTransport();
    }

    @Bean
    public GoogleCredential googleCredential() throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        return GoogleCredential
                .fromStream(classLoader.getResourceAsStream(keyFileLocation))
                .createScoped(AnalyticsReportingScopes.all());
    }

    @Bean
    public AnalyticsReporting analyticsReporting(HttpTransport httpTransport, GoogleCredential credential) {
        return new AnalyticsReporting
                .Builder(httpTransport, JSON_FACTORY, credential)
                .setApplicationName(applicationName).build();
    }
}
