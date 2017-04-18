package info.novatec.metricscollector.google;

/**
 * This enum supports the metrics defined by Google Analytics for quering the GA Reporting API based on metrics.
 * When a new metric is need it has to be added to the enum.
 */
public enum GaMetricsEnum {
    GA_PAGE_VIEWS("ga:pageviews"),
    GA_UNIQUE_PAGE_VIEWS("ga:uniquePageviews"),
    GA_SESSION("ga:sessions"),
    GA_BOUNCES("ga:bounces"),
    GA_BOUNCE_RATE("ga:bounceRate"),
    GA_AVG_SESSION_DURATION("ga:avgSessionDuration"),
    GA_AVG_TIME_ON_PAGE("ga:avgTimeOnPage");
    //TODO if country will be used --- GA_COUNTRY("ga:country")

    private String metricName;

    GaMetricsEnum(String metricName) {
        this.metricName = metricName;
    }

    @Override
    public String toString() {
        return metricName;
    }
}
