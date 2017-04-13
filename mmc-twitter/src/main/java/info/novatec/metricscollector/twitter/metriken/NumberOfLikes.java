package info.novatec.metricscollector.twitter.metriken;

import org.springframework.stereotype.Component;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

import info.novatec.metricscollector.twitter.TwitterMetricsResult;


@Component
public class NumberOfLikes extends TwitterMetric {
    public NumberOfLikes(Twitter twitter, TwitterMetricsResult metrics) {
        super(twitter, metrics);
    }

    @Override
    public void collect() throws TwitterException {
        int likes =
            getUserTimeLine(metrics.getAtUserName(), acceptTweets -> true).stream().mapToInt(Status::getFavoriteCount).sum();
        metrics.setLikes(likes);
    }
}
