package info.novatec.metricscollector.aqeblog;

import lombok.Getter;
import lombok.Setter;
import info.novatec.metricscollector.commons.DailyClicks;


@Getter
@Setter
class AQEBlogMetrics {

    DailyClicks dailyVisits;
    Integer sessions;
    Integer bounces;
    Integer bounceRate;
    Integer averageSessionDuration;
    String country;
    String comments;
    String share;
}
