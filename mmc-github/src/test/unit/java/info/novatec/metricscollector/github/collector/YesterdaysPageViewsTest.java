package info.novatec.metricscollector.github.collector;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import info.novatec.metricscollector.github.Metrics;
import info.novatec.metricscollector.github.RestService;
import info.novatec.metricscollector.github.util.DataProvider;


@RunWith(SpringRunner.class)
public class YesterdaysPageViewsTest {

    @MockBean
    private RestService restService;

    @MockBean
    private ResponseEntity<String> response;

    private Metrics metrics;

    private YesterdaysPageViews yesterdaysPageViews;

    @Before
    public void init() {
        metrics = DataProvider.createEmptyMetrics();
        yesterdaysPageViews = new YesterdaysPageViews(restService, metrics);
        Clock clock = Clock.fixed(Instant.parse(DataProvider.TIMESTAMP_TODAY), ZoneId.of("Z"));
        yesterdaysPageViews.setClock(clock);
    }

    @Test
    public void totalVisitsTest() {
        String responseBody = DataProvider.createResponseBodyWithYesterdaysData();
        collectYesterdaysPageViews(responseBody);
        assertThat(metrics.getMetrics().get("yesterdaysTotalVisits")).isEqualTo(27);
    }

    @Test
    public void uniqueVisitsTest() {
        String responseBody = DataProvider.createResponseBodyWithYesterdaysData();
        collectYesterdaysPageViews(responseBody);
        assertThat(metrics.getMetrics().get("yesterdaysUniqueVisits")).isEqualTo(5);
    }

    @Test
    public void responseAlwaysYesterdaysData() throws Exception {
        String responseBody = DataProvider.createResponseBodyWithYesterdaysAndTodaysData();
        collectYesterdaysPageViews(responseBody);
        assertThat(metrics.getMetrics().get("yesterdaysTotalVisits")).isEqualTo(27);
    }

    private void collectYesterdaysPageViews(String responseBody) {
        when(restService.sendRequest(DataProvider.getRestURL(metrics.getRepositoryName()) + "/traffic/views"))
            .thenReturn(response);
        when(response.getBody()).thenReturn(responseBody);
        yesterdaysPageViews.collect();
    }

}

