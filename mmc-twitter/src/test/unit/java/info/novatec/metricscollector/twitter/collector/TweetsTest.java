package info.novatec.metricscollector.twitter.collector;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import twitter4j.Paging;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

import info.novatec.metricscollector.twitter.Metrics;
import info.novatec.metricscollector.twitter.exception.TwitterRuntimeException;
import info.novatec.metricscollector.twitter.util.DataProvider;


@RunWith(SpringRunner.class)
public class TweetsTest {

    @MockBean
    private Twitter twitter;

    private Metrics metrics;

    DataProvider data;

    @Before
    public void init() {
        metrics = new Metrics();
        metrics.setAtUserName(data.AT_USERNAME);
        data = new DataProvider();
    }

    @Test
    public void collectNumberOfTweetsTest() throws TwitterException {
        when(twitter.getUserTimeline(data.AT_USERNAME, data.getPaging(1))).thenReturn(data.createTweets(10));
        new Tweets(twitter, metrics).collect();
        assertThat(metrics.getMetrics().size()).isEqualTo(1);
        assertThat(metrics.getMetrics().entrySet().iterator().next().getValue()).isEqualTo(10);
    }

    @Test
    public void collectNumberOfTweetsIgnoringRetweetsTest() throws TwitterException {
        ResponseList<Status> tweets = data.setAsRetweeted(data.createTweets(200), 25);
        when(twitter.getUserTimeline(data.AT_USERNAME, data.getPaging(1))).thenReturn(tweets);
        tweets = data.setAsRetweeted(data.createTweets(0), 25);
        when(twitter.getUserTimeline(data.AT_USERNAME, data.getPaging(2))).thenReturn(tweets);
        new Tweets(twitter, metrics).collect();
        assertThat(metrics.getMetrics().size()).isEqualTo(1);
        assertThat(metrics.getMetrics().entrySet().iterator().next().getValue()).isEqualTo(150);
    }

    @Test(expected = TwitterRuntimeException.class)
    public void twitterRuntimeExceptionInsteadOfTwitterExceptionIsThrown() throws Exception {
        doThrow(TwitterException.class).when(twitter).getUserTimeline(eq(metrics.getAtUserName()), any(Paging.class));
        new Tweets(twitter, metrics).collect();
    }
}
