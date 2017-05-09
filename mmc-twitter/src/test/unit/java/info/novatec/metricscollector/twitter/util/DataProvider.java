package info.novatec.metricscollector.twitter.util;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.IntStream;

import twitter4j.Paging;
import twitter4j.QueryResult;
import twitter4j.RateLimitStatus;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.TwitterException;
import twitter4j.User;

import info.novatec.metricscollector.twitter.Metrics;


public class DataProvider {

    public static final String USERNAME = "Agile Quality Engineering";
    public static final String AT_USERNAME = "NT_AQE";

    private static final String LIKES_OF_MENTIONS_TIMESTAMP_2001_01_01 = "Mon Jan 01 00:00:00 CET 2001";
    private static final String LIKES_OF_MENTIONS_TIMESTAMP_2002_02_02 = "Sat Feb 02 00:00:00 CET 2002";

    public static Metrics fillMetrics(Metrics metrics) {
        metrics.setUserName(USERNAME);
        metrics.setAtUserName(AT_USERNAME);
        metrics.addMetric("tweets", 1);
        metrics.addMetric("retweets", 2);
        metrics.addMetric("mentions", 3);
        metrics.addMetric("likes", 4);
        metrics.addMetric("followers", 5);
        SortedMap<String, Integer> likesOfMentions = new TreeMap<>();
        likesOfMentions.put(LIKES_OF_MENTIONS_TIMESTAMP_2001_01_01, 6);
        likesOfMentions.put(LIKES_OF_MENTIONS_TIMESTAMP_2002_02_02, 7);
        metrics.setLikesOfMentions(likesOfMentions);
        return metrics;
    }

    public static Metrics createMetrics(){
        return fillMetrics(createEmptyMetrics());
    }

    public static Metrics createEmptyMetrics(){
        return new Metrics();
    }

    public QueryResult mockQueryResult(ResponseList<Status> tweets) throws TwitterException {
        QueryResult queryResult = mock(QueryResult.class);
        ResponseList<Status> emptyList = new DataProvider.CustomResponseList<>();
        when(queryResult.getTweets()).thenReturn(tweets).thenReturn(emptyList);
        return queryResult;
    }

    public Paging getPaging(int pagingIndex) {
        return new Paging(pagingIndex, 200);
    }

    public ResponseList<Status> createTweets(int numberOfTweets) throws TwitterException {
        ResponseList<Status> tweets = new DataProvider.CustomResponseList<>();
        for (int i = 0; i < numberOfTweets; i++) {
            tweets.add(mock(Status.class));
        }
        return tweets;
    }

    /**
     * Returns list of tweets with x% retweeted tweets
     *
     * @param tweets list of tweets
     * @param percent percent of tweets that has to be set to 'retweeted'
     */
    public ResponseList<Status> setAsRetweeted(ResponseList<Status> tweets, int percent) throws TwitterException {
        IntStream.range(0, tweets.size())
            .filter(index -> index % (100 / percent) == 0)
            .mapToObj(tweets::get)
            .forEach(tweet -> when(tweet.isRetweeted()).thenReturn(true));
        return tweets;
    }

    /**
     * Returns list of tweets with x% retweeted tweets
     *
     * @param tweets list of tweets
     * @param percent percent of tweets that has to be set to 'retweeted'
     */
    public ResponseList<Status> setRetweetCount(ResponseList<Status> tweets, int percent, int count)
        throws TwitterException {
        IntStream.range(0, tweets.size())
            .filter(index -> index % (100 / percent) == 0)
            .mapToObj(tweets::get)
            .forEach(tweet -> when(tweet.getRetweetCount()).thenReturn(count));
        return tweets;
    }

    public void setUsername(ResponseList<Status> tweets, int percent) {
        for (int i = 0; i < tweets.size(); i++) {
            User user = mock(User.class);
            when(tweets.get(i).getUser()).thenReturn(user);
            if (percent > 0 && i % (100 / percent) == 0) {
                when(user.getName()).thenReturn(USERNAME);
            } else {
                when(user.getName()).thenReturn("somebody");
            }
        }
    }

    public void setFavoriteCount(ResponseList<Status> tweets, int percent, int count) throws TwitterException {
        IntStream.range(0, tweets.size())
            .filter(index -> (percent > 0) && index % (100 / percent) == 0)
            .mapToObj(tweets::get)
            .forEach(tweet -> when(tweet.getFavoriteCount()).thenReturn(count));
    }

    public void setFavoritesOfRetweetedTweet(ResponseList<Status> tweets, int percent, int favoritesCount) {
        for (int i = 0; i < tweets.size(); i++) {
            Status retweetedTweet = mock(Status.class);
            if (percent > 0 && i % (100 / percent) == 0) {
                when(retweetedTweet.getFavoriteCount()).thenReturn(favoritesCount);
            } else {
                when(retweetedTweet.getFavoriteCount()).thenReturn(0);
            }
            when(tweets.get(i).getRetweetedStatus()).thenReturn(retweetedTweet);
        }
    }

    public void setCreatedAt(ResponseList<Status> tweets, int percent, Date date) {
        IntStream.range(0, tweets.size())
            .filter(index -> (percent > 0) && index % (100 / percent) == 0 )
            .mapToObj(tweets::get)
            .forEach(tweet -> when(tweet.getCreatedAt()).thenReturn(date));
    }

    private class CustomResponseList<Status> extends ArrayList<Status> implements ResponseList<Status> {
        @Override
        public int getAccessLevel() {
            return 0;
        }

        @Override
        public RateLimitStatus getRateLimitStatus() {
            return null;
        }
    }
}
