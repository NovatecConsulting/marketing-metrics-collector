package info.novatec.metricscollector.twitter.metrics;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

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
public class NumberOfMentionsTest {

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
    public void collectNumberOfMentionsTest() throws TwitterException {
        Query query = new Query("@" + data.AT_USERNAME);
        ResponseList<Status> tweets = data.createTweets(200);
        data.setUsername(tweets, 0);
        QueryResult result = data.mockQueryResult(tweets);
        when(twitter.search(query)).thenReturn(result);
        new NumberOfMentions(twitter, metrics).collect();
        assertThat(metrics.getMentions()).isEqualTo(200);
    }

    @Test(expected = TwitterRuntimeException.class)
    public void twitterRuntimeExceptionInsteadOfTwitterExceptionIsThrown() throws Exception{
        NumberOfMentions thisMetric = spy(new NumberOfMentions(twitter, metrics));
        doThrow(TwitterException.class).when(thisMetric).getAllTweets(any(Query.class));
        thisMetric.collect();
    }
}
