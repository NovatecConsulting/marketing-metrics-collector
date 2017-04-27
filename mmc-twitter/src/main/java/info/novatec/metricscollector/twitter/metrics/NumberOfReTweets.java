package info.novatec.metricscollector.twitter.metrics;

import org.springframework.stereotype.Component;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

import info.novatec.metricscollector.twitter.TwitterMetricsResult;
import info.novatec.metricscollector.twitter.exception.TwitterRuntimeException;


@Component
public class NumberOfReTweets extends TwitterMetricAbstract implements TwitterMetric{
    public NumberOfReTweets(Twitter twitter, TwitterMetricsResult metrics) {
        super(twitter, metrics);
    }

    @Override
    public void collect() {
        try {
            UserTimeLineFilter filter = tweet -> !tweet.isRetweeted();
            int reTweets = getUserTimeLine(metrics.getAtUserName(), filter).stream().mapToInt(Status::getRetweetCount).sum();
            metrics.setReTweets(reTweets);
        }catch(TwitterException e){
            throw new TwitterRuntimeException(e);
        }

    }
}
