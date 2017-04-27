package info.novatec.metricscollector.twitter.metrics;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

import info.novatec.metricscollector.twitter.TwitterMetricsResult;
import info.novatec.metricscollector.twitter.exception.TwitterRuntimeException;
import info.novatec.metricscollector.twitter.util.DataProvider;


@RunWith(SpringRunner.class)
public class NumberOfLikesOfMentionsTest {

    @MockBean
    private Twitter twitter;

    private TwitterMetricsResult metrics;

    DataProvider data;

    @Before
    public void init() {
        metrics = new TwitterMetricsResult();
        data = new DataProvider();
    }

    @Test
    public void collectOneNumberOfLikesOfMentionsTest() throws TwitterException {
        QueryResult result = data.mockQueryResult(createMockedTweets(1, 7));
        when(twitter.search(any(Query.class))).thenReturn(result);
        new NumberOfLikesOfMentions(twitter, metrics).collect();
        int totalLikesOfMentions = metrics.getLikesOfMentions().entrySet().stream().mapToInt(Map.Entry::getValue).sum();
        assertThat(totalLikesOfMentions).isEqualTo(7);
    }

    @Test
    public void collect2NumberOfLikesOfMentionsTest() throws TwitterException {
        ResponseList<Status> tweets = createMockedTweets(1, 7);
        ResponseList<Status> tweets2 = createMockedTweets(1, 10);
        tweets.addAll(tweets2);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1); // current date minus 1 day to distinguish timestamps
        when(tweets.get(0).getCreatedAt()).thenReturn(cal.getTime());
        QueryResult result = data.mockQueryResult(tweets);
        when(twitter.search(any(Query.class))).thenReturn(result);
        new NumberOfLikesOfMentions(twitter, metrics).collect();
        int totalLikesOfMentions = metrics.getLikesOfMentions().entrySet().stream().mapToInt(Map.Entry::getValue).sum();
        assertThat(totalLikesOfMentions).isEqualTo(17);
    }

    @Test
    public void tweetWillBeIgnoredIfTweetWithIdenticTimestampExist() throws TwitterException {
        QueryResult result = data.mockQueryResult(createMockedTweets(2, 7));
        when(twitter.search(any(Query.class))).thenReturn(result);
        new NumberOfLikesOfMentions(twitter, metrics).collect();
        int totalLikesOfMentions = metrics.getLikesOfMentions().entrySet().stream().mapToInt(Map.Entry::getValue).sum();
        assertThat(totalLikesOfMentions).isEqualTo(7);
    }

    private ResponseList<Status> createMockedTweets(int numberOfTweets, int favoritesCount) throws TwitterException {
        ResponseList<Status> tweets = data.createTweets(numberOfTweets);
        data.setUsername(tweets, 100);
        data.setFavoritesOfRetweetedTweet(tweets, 100, favoritesCount);
        data.setCreatedAt(tweets, 100, new Date());
        return tweets;
    }

    @Test(expected = TwitterRuntimeException.class)
    public void twitterRuntimeExceptionInsteadOfTwitterExceptionIsThrown() throws Exception{
        NumberOfLikesOfMentions thisMetric = spy(new NumberOfLikesOfMentions(twitter, metrics));
        doThrow(TwitterException.class).when(thisMetric).getAllTweets(any(Query.class));
        thisMetric.collect();
    }
}
