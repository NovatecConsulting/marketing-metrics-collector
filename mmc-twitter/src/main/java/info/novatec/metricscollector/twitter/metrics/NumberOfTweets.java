package info.novatec.metricscollector.twitter.metrics;

import org.springframework.stereotype.Component;

import twitter4j.Twitter;
import twitter4j.TwitterException;

import info.novatec.metricscollector.twitter.TwitterMetricsResult;


@Component
public class NumberOfTweets extends TwitterMetric {
    public NumberOfTweets(Twitter twitter, TwitterMetricsResult metrics) {
        super(twitter, metrics);
    }

    @Override
    public void collect() throws TwitterException {
        UserTimeLineFilter filter = tweet -> !tweet.isRetweeted();
        int tweets = getUserTimeLine(metrics.getAtUserName(), filter).size();
        metrics.setTweets(tweets);
    }
}
