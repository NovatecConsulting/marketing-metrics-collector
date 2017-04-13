package info.novatec.metricscollector.twitter.metriken;

import org.springframework.stereotype.Component;

import twitter4j.Twitter;
import twitter4j.TwitterException;

import info.novatec.metricscollector.twitter.TwitterMetricsResult;


@Component
public class NumberOfFollowers extends TwitterMetric {
    public NumberOfFollowers(Twitter twitter, TwitterMetricsResult metrics) {
        super(twitter, metrics);
    }

    @Override
    public void collect() throws TwitterException {
        int followers = twitter.getFollowersIDs(-1).getIDs().length;
        metrics.setFollowers(followers);
    }
}
