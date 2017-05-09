package info.novatec.metricscollector.twitter.collector;

import java.util.List;

import org.springframework.stereotype.Component;

import com.google.common.primitives.Ints;

import twitter4j.Query;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

import info.novatec.metricscollector.twitter.Metrics;
import info.novatec.metricscollector.twitter.exception.TwitterRuntimeException;


@Component
public class Mentions extends TwitterBasicMetricCollector implements TwitterMetricCollector {
    public Mentions(Twitter twitter, Metrics metrics) {
        super(twitter, metrics);
    }

    @Override
    public void collect() {
        try {
            List<Status> tweets = getAllTweets(new Query("@" + metrics.getAtUserName()));
            //filter out mentions from user itself
            long mentioned = tweets.stream().filter(tweet -> !tweet.getUser().getName().equals(metrics.getUserName())).count();
            metrics.addMetric("mentions", Ints.checkedCast(mentioned));
        }catch(TwitterException e){
            throw new TwitterRuntimeException(e);
        }

    }
}