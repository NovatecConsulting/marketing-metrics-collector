package info.novatec.metricscollector.twitter.collector;

import org.springframework.stereotype.Component;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

import info.novatec.metricscollector.twitter.Metrics;
import info.novatec.metricscollector.twitter.exception.TwitterRuntimeException;


@Component
public class Likes extends TwitterBasicMetricCollector implements TwitterMetricCollector {
    public Likes(Twitter twitter, Metrics metrics) {
        super(twitter, metrics);
    }

    @Override
    public void collect() {
        try {
            int likes =
                getUserTimeLine(metrics.getAtUserName(), acceptTweets -> true).stream().mapToInt(Status::getFavoriteCount).sum();
            metrics.setLikes(likes);
        }catch(TwitterException e){
            throw new TwitterRuntimeException(e);
        }

    }
}
