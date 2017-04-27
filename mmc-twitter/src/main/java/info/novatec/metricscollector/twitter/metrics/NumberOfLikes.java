package info.novatec.metricscollector.twitter.metrics;

import org.springframework.stereotype.Component;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

import info.novatec.metricscollector.twitter.TwitterMetricsResult;
import info.novatec.metricscollector.twitter.exception.TwitterRuntimeException;


@Component
public class NumberOfLikes extends TwitterMetricAbstract implements TwitterMetric {
    public NumberOfLikes(Twitter twitter, TwitterMetricsResult metrics) {
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
