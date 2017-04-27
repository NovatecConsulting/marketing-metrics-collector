package info.novatec.metricscollector.twitter.metrics;

import org.springframework.stereotype.Component;

import twitter4j.Twitter;
import twitter4j.TwitterException;

import info.novatec.metricscollector.twitter.TwitterMetricsResult;
import info.novatec.metricscollector.twitter.exception.TwitterRuntimeException;


@Component
public class NumberOfTweets extends TwitterMetricAbstract implements TwitterMetric{
    public NumberOfTweets(Twitter twitter, TwitterMetricsResult metrics) {
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
