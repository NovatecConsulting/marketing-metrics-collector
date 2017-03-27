package info.novatec.metricscollector.aqeblog;

import lombok.Getter;
import lombok.Setter;
import info.novatec.metricscollector.commons.DailyVisitsEntity;


@Getter
@Setter
class AQEBlogMetrics {

    DailyVisitsEntity dailyVisits;
    Integer sessions;
    Integer bounces;
    Integer bounceRate;
    Integer averageSessionDuration;
    String country;
    String comments;
    String share;
}
