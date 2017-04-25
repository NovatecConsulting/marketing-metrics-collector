package info.novatec.metricscollector.github;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.junit4.SpringRunner;

import info.novatec.metricscollector.github.util.DataProvider;

import info.novatec.metricscollector.commons.RestService;


@RunWith(SpringRunner.class)
public class GithubCollectorTest {

    private static final String VALID_URL = "https://github.com/nt-ca-aqe/marketing-metrics-collector";

    @MockBean
    private RestService restService;

    private GithubCollector collector;

    @Before
    public void init() {
        DataProvider data = new DataProvider();
        GithubMetricsResult metrics = data.fillMetrics(new GithubMetricsResult());
        collector = new GithubCollector(metrics, restService);
    }

    @Test
    public void extractValidProjectNameTest() throws Throwable {
        String projectName = collector.extractProjectName(VALID_URL);
        assertThat(projectName).isEqualTo("nt-ca-aqe/marketing-metrics-collector");
    }

    @Test(expected = IllegalArgumentException.class)
    public void extractProjectNameWithInvalidUrlStringTest() throws Throwable {
        String url = "https:github.comnt-ca-aqemarketing-metrics-collector";
        collector.extractProjectName(url);
    }

    @Test(expected = IllegalArgumentException.class)
    public void extractProjectNameWithInvalidProjectNameInUrlStringTest() throws Throwable {
        String url = "https://github.com/nt-ca-aqemarketing-metrics-collector";
        collector.extractProjectName(url);
    }


    @Test(expected = IllegalArgumentException.class)
    public void extractProjectNameWithEmptyUrlStringTest() throws Throwable {
        String url = "";
        collector.extractProjectName(url);
    }

    @Test
    public void setHeadersIsInvokedTest() throws Throwable {
        collector.setHeadersForGithub();
        verify(restService, times(1)).setHeaders(any(HttpHeaders.class));
    }

    @Test
    public void setCorrectTokenTest() throws Throwable {
        collector.setToken("aToken");
        ArgumentCaptor<HttpHeaders> argument = ArgumentCaptor.forClass(HttpHeaders.class);
        collector.setHeadersForGithub();
        verify(restService).setHeaders(argument.capture());
        assertThat(argument.getValue().get("Authorization").get(0)).isEqualTo("token aToken");
    }

    @Test
    public void setEmptyTokenTest() throws Throwable {
        collector.setToken("");
        ArgumentCaptor<HttpHeaders> argument = ArgumentCaptor.forClass(HttpHeaders.class);
        collector.setHeadersForGithub();
        verify(restService).setHeaders(argument.capture());
        assertThat(argument.getValue().get("Authorization").get(0)).isEqualTo("token ");
    }

}
