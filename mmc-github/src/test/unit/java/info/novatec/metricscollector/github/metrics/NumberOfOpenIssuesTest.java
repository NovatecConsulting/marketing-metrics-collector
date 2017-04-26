package info.novatec.metricscollector.github.metrics;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.json.JsonObject;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import info.novatec.metricscollector.github.util.DataProvider;

import info.novatec.metricscollector.commons.RestService;
import info.novatec.metricscollector.github.GithubMetricsResult;


@RunWith(SpringRunner.class)
public class NumberOfOpenIssuesTest {

    @MockBean
    private RestService restService;

    private GithubMetricsResult metrics;

    @Before
    public void init() {
        metrics = DataProvider.createEmptyMetrics();
    }

    @Test
    public void collectTest() {
        NumberOfOpenIssues numberOfOpenIssues = new NumberOfOpenIssues(restService, metrics);
        numberOfOpenIssues.setProjectName(DataProvider.NON_EXISTING_PROJECT);
        JsonObject mockedRepository = mock(JsonObject.class);
        when(mockedRepository.getInt("open_issues_count")).thenReturn(4);
        numberOfOpenIssues.setProjectRepository(mockedRepository);
        numberOfOpenIssues.collect();
        assertThat(metrics.getOpenIssues()).isEqualTo(4);
    }
}
