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
public class NumberOfCommitsTest {

    @MockBean
    private RestService restService;

    @Autowired
    private GithubMetricsResult metrics;

    private DataProvider data = new DataProvider();

    @Test
    public void collectTest() {
        String mockedResponseBody = "[{},{},{}]";
        ResponseEntity<String> mockedResponse = mock(ResponseEntity.class);

        when(restService.sendRequest(data.getRestURL() + "/commits")).thenReturn(mockedResponse);
        when(mockedResponse.getBody()).thenReturn(mockedResponseBody);

        NumberOfCommits numberOfCommits = new NumberOfCommits(restService, metrics);
        numberOfCommits.setProjectName(data.NON_EXISTING_PROJECT);
        numberOfCommits.collect();
        assertThat(metrics.getCommits()).isEqualTo(3);
    }
}
