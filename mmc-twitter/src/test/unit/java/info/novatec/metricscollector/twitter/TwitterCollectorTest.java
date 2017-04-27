package info.novatec.metricscollector.twitter;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import twitter4j.Twitter;
import twitter4j.TwitterException;

import info.novatec.metricscollector.commons.MetricsResultCheck;
import info.novatec.metricscollector.twitter.metrics.TwitterMetricImpl;
import info.novatec.metricscollector.twitter.util.DataProvider;


@RunWith(SpringRunner.class)
public class TwitterCollectorTest {

    @MockBean
    private Twitter twitter;

    private TwitterCollector collector;

    @Before
    public void init() {
        collector = new TwitterCollector(twitter);
    }

    @Test
    public void successfulCollectionTest() throws TwitterException{
        TwitterMetricsResult metrics = DataProvider.createEmptyMetrics();
        collector.collect(new TwitterMetricImpl(twitter, metrics));
        assertThat(new MetricsResultCheck().hasNullValues(metrics)).isFalse();
    }

}
