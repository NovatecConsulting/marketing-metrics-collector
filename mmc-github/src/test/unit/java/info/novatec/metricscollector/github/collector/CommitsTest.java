package info.novatec.metricscollector.github.collector;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import info.novatec.metricscollector.commons.rest.RestService;
import info.novatec.metricscollector.github.Metrics;
import info.novatec.metricscollector.github.util.DataProvider;


@RunWith(SpringRunner.class)
public class CommitsTest {

    @MockBean
    private RestService restService;

    @MockBean
    private ResponseEntity<String> response;

    private Metrics metrics;

    @Before
    public void init() {
        metrics = DataProvider.createEmptyMetrics();
    }

    @Test
    public void collectTest() {
        String mockedResponseBody = "[{},{},{}]";

        when(restService.sendRequest(DataProvider.getRestURL(metrics.getRepositoryName()) + "/commits")).thenReturn(
            response);
        when(response.getBody()).thenReturn(mockedResponseBody);

        Commits numberOfCommits = new Commits(restService, metrics);
        numberOfCommits.collect();
        assertThat(metrics.getMetrics().size()).isEqualTo(1);
        assertThat(metrics.getMetrics().entrySet().iterator().next().getValue()).isEqualTo(3);
    }
}
