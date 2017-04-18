package info.novatec.metricscollector.github.metrics;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import info.novatec.metricscollector.commons.RestService;
import info.novatec.metricscollector.github.GithubMetricsResult;
import info.novatec.metricscollector.github.TestConfig;
import info.novatec.metricscollector.github.data.DataProvider;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestConfig.class)
public class YesterdaysVisitsTest {

    @MockBean
    private RestService restService;

    @Autowired
    private GithubMetricsResult metrics;

    private DataProvider data = new DataProvider();

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

    private void collectYesterdaysVisits(){
        String mockedResponseBody = getMockedResponse();
        ResponseEntity<String> mockedResponse = mock(ResponseEntity.class);
        when(restService.sendRequest(data.getRestURL() + "/traffic/views")).thenReturn(mockedResponse);
        when(mockedResponse.getBody()).thenReturn(mockedResponseBody);
        YesterdaysVisits yesterdaysVisits = new YesterdaysVisits(restService, metrics);
        yesterdaysVisits.setProjectName(data.NON_EXISTING_PROJECT);
        yesterdaysVisits.collect();
    }

    private String getMockedResponse(){
        return "{\"views\":[{"
            + "\"timestamp\": \"2017-04-04T00:00:00Z\","
            + "\"count\": 27,"
            + "\"uniques\": 5"
            + "}]}";
    }
}
