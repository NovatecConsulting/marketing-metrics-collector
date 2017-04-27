package info.novatec.metricscollector.github.metrics;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import info.novatec.metricscollector.github.util.DataProvider;

import info.novatec.metricscollector.github.RestService;
import info.novatec.metricscollector.github.GithubMetricsResult;


@RunWith(SpringRunner.class)
public class NumberOfClosedIssuesTest {

    @MockBean
    private RestService restService;

    @MockBean
    private ResponseEntity<String> response;

    private GithubMetricsResult metrics;
  
    @Before
    public void init() {
        metrics = DataProvider.createEmptyMetrics();
    }

    @Test
    public void collectTest() {
        String mockedResponseBody = "[{},{},{}]";
        when(restService.sendRequest(DataProvider.getRestURL(metrics.getRepositoryName()) + "/issues/events")).thenReturn(response);
        when(response.getBody()).thenReturn(mockedResponseBody);

        NumberOfClosedIssues numberOfClosedIssues = new NumberOfClosedIssues(restService, metrics);
        numberOfClosedIssues.collect();
        assertThat(metrics.getClosedIssues()).isEqualTo(3);
    }
}
