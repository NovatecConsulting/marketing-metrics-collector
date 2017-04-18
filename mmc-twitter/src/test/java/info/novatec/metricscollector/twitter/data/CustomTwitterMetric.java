package info.novatec.metricscollector.twitter.data;

import twitter4j.Twitter;
import twitter4j.TwitterException;

import info.novatec.metricscollector.twitter.TwitterMetricsResult;
import info.novatec.metricscollector.twitter.metrics.TwitterMetric;


public class CustomTwitterMetric extends TwitterMetric {
    public CustomTwitterMetric(Twitter twitter, TwitterMetricsResult metrics) {
        super(twitter, metrics);
    }

    @Override
    public void collect() throws TwitterException {
    }
}