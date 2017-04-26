package info.novatec.metricscollector.github;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import info.novatec.metricscollector.commons.RestService;
import info.novatec.metricscollector.github.metrics.GithubMetricImpl;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestConfig.class)
public class GithubIntegrationTests {

    private static final String VALID_URL = "https://github.com/nt-ca-aqe/marketing-metrics-collector";

    private GithubMetricsResult metrics;

    @Autowired
    private RestService restService;

    @Autowired
    private GithubCollector collector;

    @Before
    public void init() {
        metrics = new GithubMetricsResult(VALID_URL);
        metrics.setCommits(2);
    }

    @Test
    public void collectMetricsTest(){
        collector.collect(new GithubMetricImpl(restService, metrics));
        assertThat(metrics.hasNullValues()).isFalse();
    }

}
