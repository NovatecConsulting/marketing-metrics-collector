package info.novatec.metricscollector.google;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.analyticsreporting.v4.AnalyticsReporting;
import com.google.api.services.analyticsreporting.v4.AnalyticsReportingScopes;

import info.novatec.metricscollector.commons.CommonsApplicationInitializer;


@SpringBootApplication
@Import(CommonsApplicationInitializer.class)
public class ApplicationInitializerGoogle {
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

    @Autowired
    GoogleAnalyticsProperties properties;

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
                .fromStream(classLoader.getResourceAsStream(properties.getKeyFileLocation()))
                .createScoped(AnalyticsReportingScopes.all());
    }

    @Bean
    public AnalyticsReporting analyticsReporting(HttpTransport httpTransport, GoogleCredential credential) {
        return new AnalyticsReporting
                .Builder(httpTransport, JSON_FACTORY, credential)
                .setApplicationName(properties.getApplicationName()).build();
    }

    @Bean
    public List<Metrics> metricsList(){
        return new ArrayList<>();
    }
}
