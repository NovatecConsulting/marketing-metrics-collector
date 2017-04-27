package info.novatec.metricscollector.twitter;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import twitter4j.Twitter;
import twitter4j.TwitterException;

import info.novatec.metricscollector.commons.MetricsResultCheck;
import info.novatec.metricscollector.twitter.metrics.TwitterMetricImpl;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestConfig.class)
public class TwitterIntegrationTest {

    private static final String AT_USERNAME = "NT_AQE";
    private static final String USERNAME = "Agile Quality Engineering";

    private TwitterMetricsResult metrics;

    @Autowired
    private Twitter twitter;

    @Autowired
    private TwitterCollector collector;

    @Autowired
    private MetricsResultCheck metricsResultCheck;

    @Before
    public void init() {
        metrics = new TwitterMetricsResult(AT_USERNAME, USERNAME);
    }

    @Test
    public void collectMetricsTest() throws TwitterException{
        collector.collect(new TwitterMetricImpl(twitter, metrics));
        assertThat(metricsResultCheck.hasNullValues(metrics)).isFalse();
    }

}
