package info.novatec.metricscollector.twitter.collector;

import org.springframework.stereotype.Component;

import twitter4j.Twitter;
import twitter4j.TwitterException;

import info.novatec.metricscollector.twitter.Metrics;
import info.novatec.metricscollector.twitter.exception.TwitterRuntimeException;


@Component
public class Tweets extends TwitterBasicMetricCollector implements TwitterMetricCollector {
    public Tweets(Twitter twitter, Metrics metrics) {
        super(twitter, metrics);
    }

    @Override
    public void collect() {
        try {
            UserTimeLineFilter filter = tweet -> !tweet.isRetweeted();
            int tweets = getUserTimeLine(metrics.getAtUserName(), filter).size();
            metrics.setTweets(tweets);
        }catch(TwitterException e){
            throw new TwitterRuntimeException(e);
        }
    }
}
