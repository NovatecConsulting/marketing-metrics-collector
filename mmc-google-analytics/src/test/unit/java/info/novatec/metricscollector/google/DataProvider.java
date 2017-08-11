package info.novatec.metricscollector.google;

import java.util.ArrayList;
import java.util.List;


public class DataProvider {

    static final String GA_PAGE_PATH = "ga:pagePath";
    static final String PAGE_PATH = "pagePath";
    public static final String PAGE_PATH_VALUE = "/?testPagePath";

    static final String GA_METRIC = "ga:metric";
    static final String METRIC = "metric";
    static final Double METRIC_VALUE = 1.12;

    static final String GA_DIMENSION = "ga:dimension";
    static final String DIMENSION_VALUE = "aDimension";

    public static final String MEASUREMENT_NAME_VALUE = "testMeasurement";

    public static final Long TIME_VALUE = 123l;

    static List<String> createGoogleAnalyticsMetrics(int numberOfMetrics) {
        List<String> metrics = new ArrayList<>();
        for (int i = 1; i <= numberOfMetrics; i++) {
            metrics.add(GA_METRIC + i);
        }
        return metrics;
    }

    static List<String> createGoogleAnalyticsDimensions(int numberOfDimensions) {
        List<String> dimensions = new ArrayList<>();
        for (int i = 1; i <= numberOfDimensions; i++) {
            dimensions.add(GA_DIMENSION + i);
        }
        return dimensions;
    }

    public static Metrics createMetrics() {
        Metrics metrics = new Metrics();
        metrics.setPagePath(PAGE_PATH_VALUE);
        metrics.getDimensions().put(GA_PAGE_PATH, PAGE_PATH_VALUE);
        metrics.getMetrics().put(GA_METRIC, METRIC_VALUE);
        return metrics;
    }

    public static List<Metrics> createMetricsForPages(int numberOfPages) {
        List<Metrics> metricsForPages = new ArrayList<>();
        for (int numberOfCreatedMetrics = 0; numberOfCreatedMetrics < numberOfPages; numberOfCreatedMetrics++) {
            metricsForPages.add(createMetrics());
        }
        return metricsForPages;
    }

    public static String createInfluxPointAsString() {
        return "Point [name=" + MEASUREMENT_NAME_VALUE + ", time=" + TIME_VALUE + ", " + "tags={" + PAGE_PATH + "="
            + PAGE_PATH_VALUE + "}, " + "precision=MILLISECONDS, " + "fields={" + METRIC + "=" + METRIC_VALUE + "}]";
    }
}
