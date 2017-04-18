package info.novatec.metricscollector.twitter.metrics;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

import info.novatec.metricscollector.twitter.data.DataProvider;
import info.novatec.metricscollector.twitter.TestConfig;
import info.novatec.metricscollector.twitter.TwitterMetricsResult;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestConfig.class)
public class NumberOfTweetsTest {

    @MockBean
    private Twitter twitter;

    @Autowired
    private TwitterMetricsResult metrics;

    @Autowired
    DataProvider data;

    @Test
    public void collectNumberOfTweetsTest() throws TwitterException {
        when(twitter.getUserTimeline(data.AT_USERNAME, data.getPaging(1))).thenReturn(data.createTweets(10));
        new NumberOfTweets(twitter, metrics).collect();
        assertThat(metrics.getTweets()).isEqualTo(10);
    }

    @Test
    public void collectNumberOfTweetsIgnoringRetweetsTest() throws TwitterException {
        ResponseList<Status> tweets = data.setAsRetweeted(data.createTweets(200), 25);
        when(twitter.getUserTimeline(data.AT_USERNAME, data.getPaging(1))).thenReturn(tweets);
        tweets = data.setAsRetweeted(data.createTweets(0), 25);
        when(twitter.getUserTimeline(data.AT_USERNAME, data.getPaging(2))).thenReturn(tweets);
        new NumberOfTweets(twitter, metrics).collect();
        assertThat(metrics.getTweets()).isEqualTo(150);
    }
}
