package info.novatec.metricscollector.github.metrics;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import info.novatec.metricscollector.commons.RestService;
import info.novatec.metricscollector.github.GithubMetricsResult;
import info.novatec.metricscollector.github.data.DataProvider;


@RunWith(SpringRunner.class)
public class ReferringSitesTest {

    @MockBean
    private RestService restService;

    private GithubMetricsResult metrics;

    private DataProvider data = new DataProvider();

    @Before
    public void init(){

        metrics = new GithubMetricsResult();
    }

    @Test
    public void twoReferrersTest() {
        collectReferringSites();
        assertThat(metrics.getReferringSitesLast14Days().size()).isEqualTo(2);
    }

    @Test
    public void firstReferrerVisitsTest() {
        collectReferringSites();
        assertThat(metrics.getReferringSitesLast14Days().containsKey("github.com")).isTrue();
        assertThat(metrics.getReferringSitesLast14Days().get("github.com").getTotalVisits()).isEqualTo(27);
        assertThat(metrics.getReferringSitesLast14Days().get("github.com").getUniqueVisits()).isEqualTo(5);
    }

    @Test
    public void secondReferrerVisitsTest() {
        collectReferringSites();
        assertThat(metrics.getReferringSitesLast14Days().containsKey("github2.com")).isTrue();
        assertThat(metrics.getReferringSitesLast14Days().get("github2.com").getTotalVisits()).isEqualTo(27);
        assertThat(metrics.getReferringSitesLast14Days().get("github2.com").getUniqueVisits()).isEqualTo(5);
    }

    private void collectReferringSites(){
        String mockedResponseBody = getMockedResponse();
        ResponseEntity<String> mockedResponse = mock(ResponseEntity.class);
        when(restService.sendRequest(data.getRestURL() + "/traffic/popular/referrers")).thenReturn(mockedResponse);
        when(mockedResponse.getBody()).thenReturn(mockedResponseBody);
        ReferringSites referringSites = new ReferringSites(restService, metrics);
        referringSites.setProjectName(data.NON_EXISTING_PROJECT);
        referringSites.collect();
    }

    private String getMockedResponse(){
        return "[{"
            + "\"referrer\": \"github.com\","
            + "\"count\": 27,"
            + "\"uniques\": 5"
            + "},{"
            + "\"referrer\": \"github2.com\","
            + "\"count\": 27,"
            + "\"uniques\": 5"
            + "}]";
    }
}
