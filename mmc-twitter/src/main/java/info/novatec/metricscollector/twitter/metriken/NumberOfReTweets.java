package info.novatec.metricscollector.twitter.metriken;

import org.springframework.stereotype.Component;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

import info.novatec.metricscollector.twitter.TwitterMetricsResult;


@Component
public class NumberOfReTweets extends TwitterMetric {
    public NumberOfReTweets(Twitter twitter, TwitterMetricsResult metrics) {
        super(twitter, metrics);
    }

    @Override
    public void collect() throws TwitterException {
        UserTimeLineFilter filter = tweet -> !tweet.isRetweeted();
        int reTweets = getUserTimeLine(metrics.getAtUserName(), filter).stream().mapToInt(Status::getRetweetCount).sum();
        metrics.setReTweets(reTweets);
    }
}
