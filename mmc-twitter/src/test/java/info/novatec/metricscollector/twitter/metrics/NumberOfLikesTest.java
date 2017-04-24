package info.novatec.metricscollector.twitter.metrics;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

import info.novatec.metricscollector.twitter.TwitterMetricsResult;
import info.novatec.metricscollector.twitter.data.DataProvider;


@RunWith(SpringRunner.class)
public class NumberOfLikesTest {

    @MockBean
    private Twitter twitter;

    private TwitterMetricsResult metrics;

    DataProvider data;

    @Before
    public void init(){
        metrics = new TwitterMetricsResult();
        metrics.setAtUserName(data.AT_USERNAME);
        data = new DataProvider();
    }

    @Test
    public void collectNumberOfLikesTest() throws TwitterException {
        ResponseList<Status> tweets = data.createTweets(20);
        data.setFavoriteCount(tweets, 25, 1);
        when(twitter.getUserTimeline(data.AT_USERNAME, data.getPaging(1))).thenReturn(tweets);
        tweets = data.createTweets(0);
        when(twitter.getUserTimeline(data.AT_USERNAME, data.getPaging(2))).thenReturn(tweets);
        new NumberOfLikes(twitter, metrics).collect();
        assertThat(metrics.getLikes()).isEqualTo(5);
    }
}
