package info.novatec.metricscollector.twitter.metriken;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import twitter4j.Query;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

import info.novatec.metricscollector.twitter.TwitterMetricsResult;


@Component
public class NumberOfLikesOfMentions extends TwitterMetric{
    public NumberOfLikesOfMentions(Twitter twitter, TwitterMetricsResult metrics) {
        super(twitter, metrics);
    }

    @Override
    public void collect() throws TwitterException{
        Query query = new Query("@" + metrics.getAtUserName());
        query.setCount(20);

        List<Status> allTweets = getAllTweets(query);

        //filter out mentions from user itself
        Map<String, Integer> likesOfMentions = allTweets.stream()
            .filter(tweet -> !tweet.getUser().getName().equals(metrics.getUserName()))
            .filter(tweet -> tweet.getRetweetedStatus() != null)
            .filter(tweet -> tweet.getRetweetedStatus().getFavoriteCount() > 0)
            .collect(Collectors.toMap(
                tweet -> tweet.getCreatedAt().toString(),
                tweet -> tweet.getRetweetedStatus().getFavoriteCount(),
                (tweet1, tweet2) -> tweet1)
            );

        metrics.setLikesOfMentions(likesOfMentions);
    }
}
