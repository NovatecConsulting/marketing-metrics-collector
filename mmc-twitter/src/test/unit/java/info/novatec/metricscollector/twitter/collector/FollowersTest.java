package info.novatec.metricscollector.twitter.collector;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import twitter4j.IDs;
import twitter4j.Twitter;
import twitter4j.TwitterException;

import info.novatec.metricscollector.twitter.Metrics;
import info.novatec.metricscollector.twitter.exception.TwitterRuntimeException;


@RunWith(SpringRunner.class)
public class FollowersTest {

    @MockBean
    private Twitter twitter;

    private Metrics metrics;

    @Before
    public void init(){
        metrics = new Metrics();
    }

    @Test
    public void collectNumberOfFollowers() throws TwitterException {
        long[] listOfIds = new long[100];
        IDs ids = mock(IDs.class);
        when(twitter.getFollowersIDs(-1)).thenReturn(ids);
        when(ids.getIDs()).thenReturn(listOfIds);
        new Followers(twitter, metrics).collect();
        assertThat(metrics.getMetrics().size()).isEqualTo(1);
        assertThat(metrics.getMetrics().entrySet().iterator().next().getValue()).isEqualTo(100);
    }

    @Test(expected = TwitterRuntimeException.class)
    public void twitterRuntimeExceptionInsteadOfTwitterExceptionIsThrown() throws Exception{
        doThrow(TwitterException.class).when(twitter).getFollowersIDs(-1);
        new Followers(twitter, metrics).collect();
    }
}
