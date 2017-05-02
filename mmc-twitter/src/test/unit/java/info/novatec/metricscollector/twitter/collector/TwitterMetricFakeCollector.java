package info.novatec.metricscollector.twitter.collector;

import org.springframework.stereotype.Component;

import twitter4j.Twitter;

import info.novatec.metricscollector.twitter.Metrics;
import info.novatec.metricscollector.twitter.util.DataProvider;


@Component
public class TwitterMetricFakeCollector extends TwitterBasicMetricCollector implements
    TwitterMetricCollectorWithImplementations {

    public TwitterMetricFakeCollector(Twitter twitter, Metrics metrics) {
        super(twitter, metrics);
    }

    public void collect() {
        DataProvider.fillMetrics(getMetrics());
    }

}
