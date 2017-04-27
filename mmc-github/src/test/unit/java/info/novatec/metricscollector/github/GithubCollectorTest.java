package info.novatec.metricscollector.github;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;

import info.novatec.metricscollector.commons.MetricsResultCheck;
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

    @Test(expected = UserDeniedException.class)
    public void checkIfCollectionIsIgnoredWithNonAuthorizedUserTest() throws Exception{
        collector = spy(collector);
        GithubMetricImpl mockedMetric = mock(GithubMetricImpl.class);
        doNothing().when(restService).setDefaultHttpHeaders();
        doThrow(HttpClientErrorException.class).when(mockedMetric).collect();
        collector.collect(mockedMetric);
    }

    @Test
    public void successfulCollectionTest(){
        GithubMetricsResult metrics = DataProvider.createEmptyMetrics();
        collector.collect(new GithubMetricImpl(restService, metrics));
        assertThat(new MetricsResultCheck().hasNullValues(metrics)).isFalse();
    }
}
