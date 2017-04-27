package info.novatec.metricscollector.twitter.util;

import twitter4j.Twitter;
import twitter4j.TwitterException;

import info.novatec.metricscollector.twitter.TwitterMetricsResult;
import info.novatec.metricscollector.twitter.metrics.TwitterMetricAbstract;


public class CustomTwitterMetric extends TwitterMetricAbstract {
    public CustomTwitterMetric(Twitter twitter, TwitterMetricsResult metrics) {
        super(twitter, metrics);
    }

    @Override
    public void collect() throws TwitterException {
    }
}