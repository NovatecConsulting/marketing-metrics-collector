package info.novatec.metricscollector.github;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import info.novatec.metricscollector.github.util.DataProvider;


public class GithubMetricsResultTest {

    @Test
    public void extractValidProjectNameTest() throws Throwable {
        GithubMetricsResult metrics = new GithubMetricsResult(DataProvider.VALID_GITHUB_URL);
        assertThat(metrics.getProjectName()).isEqualTo(DataProvider.VALID_GITHUB_PROJECTNAME);
    }

    @Test
    public void extractValidRepositoryNameTest() throws Throwable {
        GithubMetricsResult metrics = new GithubMetricsResult(DataProvider.VALID_GITHUB_URL);
        assertThat(metrics.getRepositoryName()).isEqualTo(DataProvider.VALID_GITHUB_REPOSITORY);
    }

    @Test(expected = IllegalArgumentException.class)
    public void extractProjectNameWithInvalidUrlStringTest() throws Throwable {
        new GithubMetricsResult("https:github.comnt-ca-aqemarketing-metrics-collector");
    }

    @Test(expected = IllegalArgumentException.class)
    public void extractProjectNameWithInvalidProjectNameInUrlStringTest() throws Throwable {
        new GithubMetricsResult("https://github.com/nt-ca-aqemarketing-metrics-collector");
    }

    @Test(expected = IllegalArgumentException.class)
    public void extractProjectNameWithEmptyUrlStringTest() throws Throwable {
        new GithubMetricsResult("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void extractProjectNameWithNullUrlStringTest() throws Throwable {
        new GithubMetricsResult(null);
    }
}
