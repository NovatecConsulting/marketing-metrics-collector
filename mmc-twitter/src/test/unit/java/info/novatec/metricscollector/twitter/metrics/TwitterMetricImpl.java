package info.novatec.metricscollector.twitter.metrics;

import org.springframework.stereotype.Component;

import twitter4j.Twitter;

import info.novatec.metricscollector.twitter.TwitterMetricsResult;
import info.novatec.metricscollector.twitter.util.DataProvider;


@Component
public class TwitterMetricImpl extends TwitterMetricAbstract implements TwitterMetricDummy {

    public TwitterMetricImpl(Twitter twitter, TwitterMetricsResult metrics) {
        super(twitter, metrics);
    }

    public void collect() {
        DataProvider.fillMetrics(getMetrics());
    }

}
