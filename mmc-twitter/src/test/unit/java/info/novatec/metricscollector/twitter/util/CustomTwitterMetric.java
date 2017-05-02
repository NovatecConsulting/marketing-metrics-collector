package info.novatec.metricscollector.twitter.util;

import twitter4j.Twitter;

import info.novatec.metricscollector.twitter.Metrics;
import info.novatec.metricscollector.twitter.collector.TwitterBasicMetricCollector;


public class CustomTwitterMetric extends TwitterBasicMetricCollector {
    public CustomTwitterMetric(Twitter twitter, Metrics metrics) {
        super(twitter, metrics);
    }

    @Override
    public void collect() {
    }
}