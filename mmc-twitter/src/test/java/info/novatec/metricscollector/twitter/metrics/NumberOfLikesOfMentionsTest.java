package info.novatec.metricscollector.twitter.metrics;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

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
import info.novatec.metricscollector.twitter.data.DataProvider;


@RunWith(SpringRunner.class)
public class NumberOfLikesOfMentionsTest {

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
    public void collectNumberOfLikesOfMentionsTest() throws TwitterException {
        ResponseList<Status> tweets = data.createTweets(1);
        data.setUsername(tweets, 100);
        data.setFavoritesOfRetweetedTweet(tweets, 100, 7);
        data.setCreatedAt(tweets, 100, new Date());

        QueryResult result = data.mockQueryResult(tweets);
        when(twitter.search(any(Query.class))).thenReturn(result);
        new NumberOfLikesOfMentions(twitter, metrics).collect();
        int totalLikesOfMentions =
            metrics.getLikesOfMentions().entrySet().stream()
                .mapToInt(Map.Entry::getValue).sum();
        assertThat(totalLikesOfMentions).isEqualTo(7);
    }
}
