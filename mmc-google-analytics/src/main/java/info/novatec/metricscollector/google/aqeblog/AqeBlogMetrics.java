package info.novatec.metricscollector.google.aqeblog;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
class AqeBlogMetrics {
    Integer pageViews;
    Integer uniquePageViews;
    Integer sessions;
    Integer bounces;
    Double bounceRate;
    Double avgSessionDuration;
    Double avgTimeOnPage;
    String country;
    String comments;
    String share;
}
