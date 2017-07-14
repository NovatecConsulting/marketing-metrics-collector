package info.novatec.metricscollector.google;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.analyticsreporting.v4.AnalyticsReporting;
import com.google.api.services.analyticsreporting.v4.AnalyticsReportingScopes;

import info.novatec.metricscollector.commons.CommonsApplicationInitializer;


@EnableScheduling
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
        return GoogleCredential.fromStream(createCredentialInputStream()).createScoped(AnalyticsReportingScopes.all());
    }

    @Bean
    public AnalyticsReporting analyticsReporting(HttpTransport httpTransport, GoogleCredential credential) {
        return new AnalyticsReporting.Builder(httpTransport, JSON_FACTORY, credential).setApplicationName(
            properties.getApplicationName()).build();
    }

    @Bean
    public List<Metrics> metricsList() {
        return new ArrayList<>();
    }

    private InputStream createCredentialInputStream() {
        String credentials =
            "{\n" + "  \"type\": \"service_account\",\n" + "  \"project_id\": \"" + properties.getProject_id() + "\",\n"
                + "  \"private_key_id\": \"" + properties.getPrivate_key_id() + "\",\n" + "  \"private_key\": \""
                + properties.getPrivate_key() + "\",\n" + "  \"client_email\": \"" + properties.getClient_email() + "\",\n"
                + "  \"client_id\": \"" + properties.getClient_id() + "\",\n"
                + "  \"auth_uri\": \"https://accounts.google.com/o/oauth2/auth\",\n"
                + "  \"token_uri\": \"https://accounts.google.com/o/oauth2/token\",\n"
                + "  \"auth_provider_x509_cert_url\": \"https://www.googleapis.com/oauth2/v1/certs\",\n"
                + "  \"client_x509_cert_url\": \"" + properties.getClient_x509_cert_url() + "\"\n" + "}\n";
        //        byte[] credentials = Base64.getDecoder().decode(properties.getClient_secrets_base64());
        return new ByteArrayInputStream(credentials.getBytes());
    }

}
