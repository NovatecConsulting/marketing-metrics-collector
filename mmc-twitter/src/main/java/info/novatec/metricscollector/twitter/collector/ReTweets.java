package info.novatec.metricscollector.twitter.collector;

import org.springframework.stereotype.Component;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

import info.novatec.metricscollector.twitter.Metrics;
import info.novatec.metricscollector.twitter.exception.TwitterRuntimeException;


@Component
public class ReTweets extends TwitterBasicMetricCollector implements TwitterMetricCollector {
    public ReTweets(Twitter twitter, Metrics metrics) {
        super(twitter, metrics);
    }

    @Override
    public void collect() {
        try {
            UserTimeLineFilter filter = tweet -> !tweet.isRetweeted();
            int reTweets = getUserTimeLine(metrics.getAtUserName(), filter).stream().mapToInt(Status::getRetweetCount).sum();
            metrics.addMetric("retweets", reTweets);
        }catch(TwitterException e){
            throw new TwitterRuntimeException(e);
        }

    }
}
