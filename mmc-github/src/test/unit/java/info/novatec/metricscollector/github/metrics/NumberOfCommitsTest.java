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

import info.novatec.metricscollector.commons.RestService;
import info.novatec.metricscollector.github.GithubMetricsResult;


@RunWith(SpringRunner.class)
public class NumberOfCommitsTest {

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

        when(restService.sendRequest(DataProvider.getRestURL() + "/commits")).thenReturn(response);
        when(response.getBody()).thenReturn(mockedResponseBody);

        NumberOfCommits numberOfCommits = new NumberOfCommits(restService, metrics);
        numberOfCommits.setProjectName(DataProvider.NON_EXISTING_PROJECT);
        numberOfCommits.collect();
        assertThat(metrics.getCommits()).isEqualTo(3);
    }
}
