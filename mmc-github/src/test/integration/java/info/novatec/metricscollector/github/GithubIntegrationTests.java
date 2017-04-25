package info.novatec.metricscollector.github;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import info.novatec.metricscollector.github.metrics.GithubMetricDummy;
import info.novatec.metricscollector.github.metrics.GithubMetricDummyNoImplementations;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestConfig.class)
public class GithubIntegrationTests {

    private static final String VALID_URL = "https://github.com/nt-ca-aqe/marketing-metrics-collector";

    private GithubMetricsResult metrics;

    @Autowired
    private GithubCollector collector;

    @Before
    public void init() {
        metrics = new GithubMetricsResult();
        metrics.setCommits(2);
    }

    @Test
    public void collectMetricsTest(){
        collector.setMetrics(metrics);
        collector.collect(VALID_URL, GithubMetricDummy.class);
        assertThat(metrics.hasNullValues()).isFalse();
    }

    @Test
    public void findImplementationsWithExistingImplementationsTest() throws Throwable {
        Set<? extends Map.Entry<String, ?>> beanEntries = collector.getBeanNameFromApplicationContext(GithubMetricDummy.class);
        assertThat(beanEntries.size()).isEqualTo(1);
    }

    @Test
    public void findImplementationsWithNonExistingImplementationsTest() throws Throwable {
        Set<? extends Map.Entry<String, ?>> beanEntries = collector.getBeanNameFromApplicationContext(GithubMetricDummyNoImplementations.class);
        assertThat(beanEntries.size()).isEqualTo(0);
    }

}
