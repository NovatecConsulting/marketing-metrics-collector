package info.novatec.metricscollector.google;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;


@Data
@Component
@ConfigurationProperties("ga")
public class GoogleAnalyticsProperties {

    public static final String GA_HOSTNAME = "ga:hostname";
    public static final String GA_PAGEPATH = "ga:pagePath";

    private AqeBlog aqeBlog = new AqeBlog();
    private AqeHomePage aqeHomePage = new AqeHomePage();

    private String applicationName;

    private String project_id;
    private String private_key_id;
    private String private_key;
    private String client_email;
    private String client_id;
    private String client_x509_cert_url;

    private String client_secrets_base64;

    private List<String> sharedMetrics;
    private List<String> sharedDimensions;

    @Data
    public static class AqeBlog {
        private String viewId;
        private String hostName;
        private List<String> uniqueMetrics;
        private List<String> uniqueDimensions;
        private List<String> excludedUrls;
    }

    @Data
    public static class AqeHomePage {
        private String viewId;
        private String hostName;
        private List<String> uniqueMetrics;
        private List<String> uniqueDimensions;
    }
}
