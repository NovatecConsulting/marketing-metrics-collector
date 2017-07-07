package info.novatec.metricscollector.google.collector;

import com.google.api.services.analyticsreporting.v4.model.GetReportsResponse;
import info.novatec.metricscollector.commons.MetricCollector;
import info.novatec.metricscollector.google.GoogleAnalyticsProperties;
import info.novatec.metricscollector.google.Metrics;
import info.novatec.metricscollector.google.RequestBuilder;
import info.novatec.metricscollector.google.ResponseParser;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AqeHomePage implements MetricCollector {

    private final GoogleAnalyticsProperties properties;

    private final RequestBuilder requestBuilder;

    private final ResponseParser responseParser;

    @Getter
    private final List<Metrics> metrics;

    @Override
    public void collect() {
        GetReportsResponse getReportsResponse = requestMetrics();
        responseParser.parse(getReportsResponse, metrics);
    }

    private GetReportsResponse requestMetrics() {
        mergeMetrics();
        mergeDimensions();

        return requestBuilder
                .prepareRequest()
                .addDimensions(properties.getSharedDimensions())
                .addMetrics(properties.getSharedMetrics())
                //.addDimensionFilters(GA_PAGEPATH, NOT, properties.AqeHomePage().getExcludedUrls())
                .buildRequest()
                .sendRequest();
    }

    public void mergeMetrics() {
        properties.getSharedMetrics().addAll(properties.getAqeHomePage().getSpecificMetrics());
    }

    public void mergeDimensions() {
        properties.getSharedDimensions().addAll(properties.getAqeHomePage().getSpecificDimensions());
    }

}
