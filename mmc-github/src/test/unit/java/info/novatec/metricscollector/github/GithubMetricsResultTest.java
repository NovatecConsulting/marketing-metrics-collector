package info.novatec.metricscollector.github;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import info.novatec.metricscollector.github.util.DataProvider;


public class GithubMetricsResultTest {

    private GithubMetricsResult metrics;

    @Test
    public void checkMetricsHavingNullValuesTest() {
        metrics = new GithubMetricsResult();
        metrics.setCommits(2);
        assertThat(metrics.hasNullValues()).isTrue();
    }

    @Test
    public void checkMetricsHavingNoNullValuesTest() {
        metrics = new DataProvider().fillMetrics(new GithubMetricsResult());
        assertThat(metrics.hasNullValues()).isFalse();
    }
}
