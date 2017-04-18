package info.novatec.metricscollector.twitter.data;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.stream.IntStream;

import org.springframework.stereotype.Component;

import twitter4j.Paging;
import twitter4j.QueryResult;
import twitter4j.RateLimitStatus;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.TwitterException;
import twitter4j.User;


@Component
public class DataProvider {

    public static final String AT_USERNAME = "NT_AQE";

    private static final String USERNAME = "Agile Quality Engineering";

    public QueryResult mockQueryResult(ResponseList<Status> tweets) throws TwitterException {
        QueryResult queryResult = mock(QueryResult.class);
        ResponseList<Status> emptyList = new CustomResponseList<>();
        when(queryResult.getTweets()).thenReturn(tweets).thenReturn(emptyList);
        return queryResult;
    }

    public Paging getPaging(int pagingIndex) {
        return new Paging(pagingIndex, 200);
    }

    public ResponseList<Status> createTweets(int numberOfTweets) throws TwitterException {
        ResponseList<Status> tweets = new CustomResponseList<>();
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
