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
public class YesterdaysVisitsTest {

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
    public void timestampTest() {
        collectYesterdaysVisits();
        assertThat(metrics.getDailyVisits().getTimestamp()).isEqualTo("2017-04-04T00:00:00Z");
    }

    @Test
    public void totalVisitsTest() {
        collectYesterdaysVisits();
        assertThat(metrics.getDailyVisits().getTotalVisits()).isEqualTo(27);
    }

    @Test
    public void uniqueVisitsTest() {
        collectYesterdaysVisits();
        assertThat(metrics.getDailyVisits().getUniqueVisits()).isEqualTo(5);
    }

    private void collectYesterdaysVisits() {
        String mockedResponseBody = getMockedResponse();
        when(restService.sendRequest(DataProvider.getRestURL(metrics.getRepositoryName()) + "/traffic/views")).thenReturn(response);
        when(response.getBody()).thenReturn(mockedResponseBody);
        YesterdaysVisits yesterdaysVisits = new YesterdaysVisits(restService, metrics);
        yesterdaysVisits.collect();
    }

    private String getMockedResponse() {
        return "{\"views\":[{" + "\"timestamp\": \"2017-04-04T00:00:00Z\"," + "\"count\": 27," + "\"uniques\": 5" + "}]}";
    }
}
