package info.novatec.metricscollector.twitter.metrics;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

import info.novatec.metricscollector.twitter.data.DataProvider;
import info.novatec.metricscollector.twitter.TestConfig;
import info.novatec.metricscollector.twitter.TwitterMetricsResult;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestConfig.class)
public class NumberOfMentionsTest {

    @MockBean
    private Twitter twitter;

    @Autowired
    private TwitterMetricsResult metrics;

    @Autowired
    DataProvider data;

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
}
