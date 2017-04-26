package info.novatec.metricscollector.github;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.influxdb.dto.Point;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import info.novatec.metricscollector.github.util.DataProvider;

import info.novatec.metricscollector.commons.InfluxService;


@RunWith(SpringRunner.class)
public class GithubRepositoryTest {

    @MockBean
    private InfluxService influxService;

    private GithubRepository githubRepository;

    private GithubMetricsResult metrics;

    @Before
    public void init(){
        metrics = DataProvider.createMetrics();
        githubRepository = new GithubRepository(influxService);
    }

    @Test
    public void createPointsWithValidMetricsTest() {
        metrics = spy(metrics);
        doReturn(false).when(metrics).hasNullValues();
        List<Point> points = githubRepository.createPoints(metrics);
        assertThat(points.size()).isEqualTo(3);
    }

    @Test
    public void createPointsWithNullValueMetricsTest() {
        metrics = mock(GithubMetricsResult.class);
        when(metrics.hasNullValues()).thenReturn(true);
        List<Point> points = githubRepository.createPoints(metrics);
        assertThat(points.size()).isEqualTo(0);
    }

    @Test
    public void checkIfSaveMetricsMethodWasInvokedTest() {
        githubRepository.saveMetrics(metrics);
        verify(influxService, times(1)).savePoint(anyListOf(Point.class));
        verify(influxService, times(1)).close();
    }

    @Test
    public void checkIfSetRetentionMethodWithValidRetentionWasInvokedTest() {
        String retention = "daily";
        githubRepository = new GithubRepository(influxService);
        githubRepository.setRetention(retention);
        verify(influxService, times(1)).setRetention(retention);
    }

    @Test
    public void checkIfSetRetentionMethodWithEmptyParameterWasInvokedTest() {
        String retention = "";
        githubRepository = new GithubRepository(influxService);
        githubRepository.setRetention(retention);
        verify(influxService, times(0)).setRetention(retention);
    }

    @Test
    public void checkIfSetRetentionMethodWithNullParameterWasInvokedTest() {
        githubRepository = new GithubRepository(influxService);
        githubRepository.setRetention(null);
        verify(influxService, times(0)).setRetention(null);
    }

}
