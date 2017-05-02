package info.novatec.metricscollector.twitter.collector;

import org.springframework.stereotype.Component;

import twitter4j.Twitter;
import twitter4j.TwitterException;

import info.novatec.metricscollector.twitter.Metrics;
import info.novatec.metricscollector.twitter.exception.TwitterRuntimeException;


@Component
public class Followers extends TwitterBasicMetricCollector implements TwitterMetricCollector {
    public Followers(Twitter twitter, Metrics metrics) {
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
