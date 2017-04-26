package info.novatec.metricscollector.github;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;

import info.novatec.metricscollector.commons.RestService;
import info.novatec.metricscollector.commons.exception.UserDeniedException;
import info.novatec.metricscollector.github.metrics.GithubMetricImpl;
import info.novatec.metricscollector.github.util.DataProvider;


@RunWith(SpringRunner.class)
public class GithubCollectorTest {

    @MockBean
    private RestService restService;

    private GithubCollector collector;

    @Before
    public void init() {
        collector = new GithubCollector(restService);
    }

    @Test
    public void setHeadersIsInvokedTest() throws Throwable {
        collector.setHeadersForGithub();
        verify(restService, times(1)).setHttpHeaders(any(HttpHeaders.class));
    }

    @Test
    public void setCorrectTokenTest() throws Throwable {
        collector.setToken("aToken");
        ArgumentCaptor<HttpHeaders> argument = ArgumentCaptor.forClass(HttpHeaders.class);
        collector.setHeadersForGithub();
        verify(restService).setHttpHeaders(argument.capture());
        assertThat(argument.getValue().get("Authorization").get(0)).isEqualTo("token aToken");
    }

    @Test
    public void setEmptyTokenTest() throws Throwable {
        collector.setToken("");
        ArgumentCaptor<HttpHeaders> argument = ArgumentCaptor.forClass(HttpHeaders.class);
        collector.setHeadersForGithub();
        verify(restService).setHttpHeaders(argument.capture());
        assertThat(argument.getValue().get("Authorization").get(0)).isEqualTo("token ");
    }

    @Test(expected = UserDeniedException.class)
    public void checkIfCollectionIsIgnoredWithNonAuthorizedUserTest() throws Exception{
        collector = spy(collector);
        GithubMetricImpl mockedMetric = mock(GithubMetricImpl.class);
        doNothing().when(collector).setHeadersForGithub();
        doThrow(HttpClientErrorException.class).when(mockedMetric).collect();
        collector.collect(mockedMetric);
    }

    @Test
    public void successfulCollectionTest(){
        GithubMetricsResult metrics = DataProvider.createEmptyMetrics();
        collector.collect(new GithubMetricImpl(restService, metrics));
        assertThat(metrics.hasNullValues()).isFalse();
    }
}
