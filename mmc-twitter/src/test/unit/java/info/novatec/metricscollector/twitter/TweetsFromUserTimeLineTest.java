package info.novatec.metricscollector.twitter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

import info.novatec.metricscollector.twitter.data.CustomTwitterMetric;
import info.novatec.metricscollector.twitter.data.DataProvider;
import info.novatec.metricscollector.twitter.metrics.TwitterMetric;


@RunWith(SpringRunner.class)
public class TweetsFromUserTimeLineTest {

    @MockBean
    private Twitter twitter;

    private TwitterMetricsResult metrics;

    DataProvider data;

    @Before
    public void init(){
        metrics = new TwitterMetricsResult();
        data = new DataProvider();
    }

    @Test
    public void getSomeUnfilteredTweetsFromUserTimeLine() throws TwitterException {
        TwitterMetric customTwitterMetric = new CustomTwitterMetric(twitter, metrics);
        when(twitter.getUserTimeline(data.AT_USERNAME, data.getPaging(1))).thenReturn(data.createTweets(10));
        List<Status> tweets = customTwitterMetric.getUserTimeLine(data.AT_USERNAME);
        assertThat(tweets.size()).isEqualTo(10);
    }

    @Test
    public void getManyUnfilteredTweetsFromUserTimeLine() throws TwitterException {
        CustomTwitterMetric customTwitterMetric = new CustomTwitterMetric(twitter, metrics);
        when(twitter.getUserTimeline(data.AT_USERNAME, data.getPaging(1))).thenReturn(data.createTweets(200));
        when(twitter.getUserTimeline(data.AT_USERNAME, data.getPaging(2))).thenReturn(data.createTweets(200));
        when(twitter.getUserTimeline(data.AT_USERNAME, data.getPaging(3))).thenReturn(data.createTweets(1));
        List<Status> tweets = customTwitterMetric.getUserTimeLine(data.AT_USERNAME);
        assertThat(tweets.size()).isEqualTo(401);
    }

    @Test
    public void getSomeFilteredTweetsFromUserTimeLine() throws TwitterException {
        CustomTwitterMetric customTwitterMetric = new CustomTwitterMetric(twitter, metrics);
        ResponseList<Status> allTweets = data.createTweets(12);
        data.setAsRetweeted(allTweets, 25);
        when(twitter.getUserTimeline(data.AT_USERNAME, data.getPaging(1))).thenReturn(allTweets);
        List<Status> filteredTweets = customTwitterMetric.getUserTimeLine(data.AT_USERNAME, Status::isRetweeted);
        assertThat(filteredTweets.size()).isEqualTo(3);
    }

    @Test
    public void getZeroTweetsFromUserTimeLine() throws TwitterException {
        CustomTwitterMetric customTwitterMetric = new CustomTwitterMetric(twitter, metrics);
        when(twitter.getUserTimeline(data.AT_USERNAME, data.getPaging(1))).thenReturn(data.createTweets(0));
        List<Status> tweets = customTwitterMetric.getUserTimeLine(data.AT_USERNAME, Status::isRetweeted);
        assertThat(tweets.size()).isEqualTo(0);
    }

}
