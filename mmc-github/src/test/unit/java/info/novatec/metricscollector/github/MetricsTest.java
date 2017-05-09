package info.novatec.metricscollector.github;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import info.novatec.metricscollector.github.util.DataProvider;


public class MetricsTest {

    @Test
    public void extractValidProjectNameTest() throws Throwable {
        Metrics metrics = new Metrics(DataProvider.VALID_GITHUB_URL);
        assertThat(metrics.getProjectName()).isEqualTo(DataProvider.VALID_GITHUB_PROJECTNAME);
    }

    @Test
    public void extractValidRepositoryNameTest() throws Throwable {
        Metrics metrics = new Metrics(DataProvider.VALID_GITHUB_URL);
        assertThat(metrics.getRepositoryName()).isEqualTo(DataProvider.VALID_GITHUB_REPOSITORY);
    }

    @Test
    public void extractValidGithubUrlTest() throws Throwable {
        Metrics metrics = new Metrics(DataProvider.VALID_GITHUB_URL);
        assertThat(metrics.getGithubUrl()).isEqualTo(DataProvider.VALID_GITHUB_URL);
    }

    @Test(expected = IllegalArgumentException.class)
    public void extractProjectNameWithInvalidUrlStringTest() throws Throwable {
        new Metrics("https:github.comnt-ca-aqemarketing-metrics-collector");
    }

    @Test(expected = IllegalArgumentException.class)
    public void extractProjectNameWithInvalidProjectNameInUrlStringTest() throws Throwable {
        new Metrics("https://github.com/nt-ca-aqemarketing-metrics-collector");
    }

    @Test(expected = IllegalArgumentException.class)
    public void extractProjectNameWithEmptyUrlStringTest() throws Throwable {
        new Metrics("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void extractProjectNameWithNullUrlStringTest() throws Throwable {
        new Metrics(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void extractProjectNameWithNoArgsConstructor() throws Throwable {
        Metrics metrics = new Metrics();
        metrics.extractProjectAndRepositoryNameFromGithubUrl();
    }
}
