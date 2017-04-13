package info.novatec.metricscollector.twitter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import twitter4j.IDs;
import twitter4j.Paging;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.RateLimitStatus;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;

import info.novatec.metricscollector.twitter.metriken.NumberOfFollowers;
import info.novatec.metricscollector.twitter.metriken.NumberOfLikes;
import info.novatec.metricscollector.twitter.metriken.NumberOfLikesOfMentions;
import info.novatec.metricscollector.twitter.metriken.NumberOfMentions;
import info.novatec.metricscollector.twitter.metriken.NumberOfReTweets;
import info.novatec.metricscollector.twitter.metriken.NumberOfTweets;
import info.novatec.metricscollector.twitter.metriken.TwitterMetric;


@RunWith(SpringRunner.class)
@SpringBootTest
public class MetricsUnitTest {

    private static final String AT_USERNAME = "NT_AQE";

    private static final String USERNAME = "Agile Quality Engineering";

    private TwitterMetricsResult metrics;

    @MockBean
    private Twitter twitter;

    @Before
    public void initTest() throws TwitterException {
        metrics = new TwitterMetricsResult(AT_USERNAME);
    }

    @Test
    public void getSomeUnfilteredTweetsFromUserTimeLine() throws TwitterException {
        TwitterMetric customTwitterMetric = new CustomTwitterMetric(twitter, metrics);
        when(twitter.getUserTimeline(AT_USERNAME, getPaging(1))).thenReturn(createTweets(10));
        List<Status> tweets = customTwitterMetric.getUserTimeLine(AT_USERNAME);
        assertThat(tweets.size()).isEqualTo(10);
    }

    class CustomTwitterMetric extends TwitterMetric {
        CustomTwitterMetric(Twitter twitter, TwitterMetricsResult metrics) {
            super(twitter, metrics);
        }

        @Override
        public void collect() throws TwitterException {
        }
    }

    @Test
    public void getManyUnfilteredTweetsFromUserTimeLine() throws TwitterException {
        CustomTwitterMetric customTwitterMetric = new CustomTwitterMetric(twitter, metrics);
        when(twitter.getUserTimeline(AT_USERNAME, getPaging(1))).thenReturn(createTweets(200));
        when(twitter.getUserTimeline(AT_USERNAME, getPaging(2))).thenReturn(createTweets(200));
        when(twitter.getUserTimeline(AT_USERNAME, getPaging(3))).thenReturn(createTweets(1));
        List<Status> tweets = customTwitterMetric.getUserTimeLine(AT_USERNAME);
        assertThat(tweets.size()).isEqualTo(401);
    }

    @Test
    public void getSomeFilteredTweetsFromUserTimeLine() throws TwitterException {
        CustomTwitterMetric customTwitterMetric = new CustomTwitterMetric(twitter, metrics);
        ResponseList<Status> allTweets = createTweets(12);
        setAsRetweeted(allTweets, 25);
        when(twitter.getUserTimeline(AT_USERNAME, getPaging(1))).thenReturn(allTweets);
        List<Status> filteredTweets = customTwitterMetric.getUserTimeLine(AT_USERNAME, Status::isRetweeted);
        assertThat(filteredTweets.size()).isEqualTo(3);
    }

    @Test
    public void getZeroTweetsFromUserTimeLine() throws TwitterException {
        CustomTwitterMetric customTwitterMetric = new CustomTwitterMetric(twitter, metrics);
        when(twitter.getUserTimeline(AT_USERNAME, getPaging(1))).thenReturn(createTweets(0));
        List<Status> tweets = customTwitterMetric.getUserTimeLine(AT_USERNAME, Status::isRetweeted);
        assertThat(tweets.size()).isEqualTo(0);
    }

    @Test
    public void collectNumberOfTweetsTest() throws TwitterException {
        when(twitter.getUserTimeline(AT_USERNAME, getPaging(1))).thenReturn(createTweets(10));
        new NumberOfTweets(twitter, metrics).collect();
        assertThat(metrics.getTweets()).isEqualTo(10);
    }

    @Test
    public void collectNumberOfTweetsIgnoringRetweetsTest() throws TwitterException {
        ResponseList<Status> tweets = setAsRetweeted(createTweets(200), 25);
        when(twitter.getUserTimeline(AT_USERNAME, getPaging(1))).thenReturn(tweets);
        tweets = setAsRetweeted(createTweets(0), 25);
        when(twitter.getUserTimeline(AT_USERNAME, getPaging(2))).thenReturn(tweets);
        new NumberOfTweets(twitter, metrics).collect();
        assertThat(metrics.getTweets()).isEqualTo(150);
    }

    @Test
    public void collectNumberOfRetweetsTest() throws TwitterException {
        ResponseList<Status> tweets = setRetweetCount(createTweets(200), 25, 1);
        when(twitter.getUserTimeline(AT_USERNAME, getPaging(1))).thenReturn(tweets);
        when(twitter.getUserTimeline(AT_USERNAME, getPaging(2))).thenReturn(createTweets(0));
        new NumberOfReTweets(twitter, metrics).collect();
        assertThat(metrics.getReTweets()).isEqualTo(50);
    }

    @Test
    public void collectRetweetsIgnoringOwnRetweetsTest() throws TwitterException {
        ResponseList<Status> tweets = createTweets(200);
        setAsRetweeted(tweets, 25);
        setRetweetCount(tweets, 100, 1);
        when(twitter.getUserTimeline(AT_USERNAME, getPaging(1))).thenReturn(tweets);
        when(twitter.getUserTimeline(AT_USERNAME, getPaging(2))).thenReturn(tweets);
        when(twitter.getUserTimeline(AT_USERNAME, getPaging(3))).thenReturn(createTweets(0));
        new NumberOfReTweets(twitter, metrics).collect();
        assertThat(metrics.getReTweets()).isEqualTo(300);
    }

    @Test
    public void collectNumberOfMentionsTest() throws TwitterException {
        Query query = new Query("@" + AT_USERNAME);
        ResponseList<Status> tweets = createTweets(200);
        setUsername(tweets, 0);
        QueryResult result = mockQueryResult(tweets);
        when(twitter.search(query)).thenReturn(result);
        new NumberOfMentions(twitter, metrics).collect();
        assertThat(metrics.getMentions()).isEqualTo(200);
    }

    @Test
    public void collectNumberOfLikesTest() throws TwitterException {
        ResponseList<Status> tweets = createTweets(20);
        setFavoriteCount(tweets, 25, 1);
        when(twitter.getUserTimeline(AT_USERNAME, getPaging(1))).thenReturn(tweets);
        tweets = createTweets(0);
        when(twitter.getUserTimeline(AT_USERNAME, getPaging(2))).thenReturn(tweets);
        new NumberOfLikes(twitter, metrics).collect();
        assertThat(metrics.getLikes()).isEqualTo(5);
    }

    @Test
    public void collectNumberOfLikesOfMentionsTest() throws TwitterException {
        ResponseList<Status> tweets = createTweets(1);
        setUsername(tweets, 100);
        setFavoritesOfRetweetedTweet(tweets, 100, 7);
        setCreatedAt(tweets, 100, new Date());

        QueryResult result = mockQueryResult(tweets);
        when(twitter.search(any(Query.class))).thenReturn(result);
        new NumberOfLikesOfMentions(twitter, metrics).collect();
        int totalLikesOfMentions =
            metrics.getLikesOfMentions().entrySet().stream()
                .mapToInt(Map.Entry::getValue).sum();
        assertThat(totalLikesOfMentions).isEqualTo(7);
    }

    @Test
    public void collectNumberOfFollowers() throws TwitterException {
        long[] listOfIds = new long[100];
        IDs ids = mock(IDs.class);
        when(twitter.getFollowersIDs(-1)).thenReturn(ids);
        when(ids.getIDs()).thenReturn(listOfIds);
        new NumberOfFollowers(twitter, metrics).collect();
        assertThat(metrics.getFollowers()).isEqualTo(100);
    }

    private QueryResult mockQueryResult(ResponseList<Status> tweets) throws TwitterException {
        QueryResult queryResult = mock(QueryResult.class);
        ResponseList<Status> emptyList = new CustomResponseList<>();
        when(queryResult.getTweets()).thenReturn(tweets).thenReturn(emptyList);
        return queryResult;
    }

    private Paging getPaging(int pagingIndex) {
        return new Paging(pagingIndex, 200);
    }

    private ResponseList<Status> createTweets(int numberOfTweets) throws TwitterException {
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
    private ResponseList<Status> setAsRetweeted(ResponseList<Status> tweets, int percent) throws TwitterException {
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
    private ResponseList<Status> setRetweetCount(ResponseList<Status> tweets, int percent, int count)
        throws TwitterException {
        IntStream.range(0, tweets.size())
            .filter(index -> index % (100 / percent) == 0)
            .mapToObj(tweets::get)
            .forEach(tweet -> when(tweet.getRetweetCount()).thenReturn(count));
        return tweets;
    }

    private void setUsername(ResponseList<Status> tweets, int percent) {
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

    private void setFavoriteCount(ResponseList<Status> tweets, int percent, int count) throws TwitterException {
        IntStream.range(0, tweets.size())
            .filter(index -> (percent > 0) && index % (100 / percent) == 0)
            .mapToObj(tweets::get)
            .forEach(tweet -> when(tweet.getFavoriteCount()).thenReturn(count));
    }

    private void setFavoritesOfRetweetedTweet(ResponseList<Status> tweets, int percent, int favoritesCount) {
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

    private void setCreatedAt(ResponseList<Status> tweets, int percent, Date date) {
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
