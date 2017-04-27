package info.novatec.metricscollector.twitter.metrics;

import org.springframework.stereotype.Component;

import twitter4j.Twitter;
import twitter4j.TwitterException;

import info.novatec.metricscollector.twitter.TwitterMetricsResult;
import info.novatec.metricscollector.twitter.exception.TwitterRuntimeException;


@Component
public class NumberOfFollowers extends TwitterMetricAbstract implements TwitterMetric{
    public NumberOfFollowers(Twitter twitter, TwitterMetricsResult metrics) {
        super(twitter, metrics);
    }

    @Override
    public void collect() {
        try {
            int followers = twitter.getFollowersIDs(-1).getIDs().length;
            metrics.setFollowers(followers);
        }catch(TwitterException e){
            throw new TwitterRuntimeException(e);
        }

    }
}
