package info.novatec.metricscollector.twitter;

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

import info.novatec.metricscollector.commons.InfluxService;
import info.novatec.metricscollector.commons.MetricsResultCheck;
import info.novatec.metricscollector.twitter.util.DataProvider;


@RunWith(SpringRunner.class)
public class TwitterRepositoryTest {

    @MockBean
    private InfluxService influxService;

    @MockBean
    private MetricsResultCheck metricsResultCheck;

    private TwitterRepository twitterRepository;

    private TwitterMetricsResult metrics;

    @Before
    public void init(){
        metrics = DataProvider.createMetrics();
        twitterRepository = new TwitterRepository(influxService, metricsResultCheck);
    }

    @Test
    public void createPointsWithValidMetricsTest() {
        metrics = spy(metrics);
        doReturn(false).when(metricsResultCheck).hasNullValues(metrics);
        List<Point> points = twitterRepository.createPoints(metrics);
        assertThat(points.size()).isEqualTo(3);
    }

    @Test
    public void createPointsWithNullValueMetricsTest() {
        metrics = mock(TwitterMetricsResult.class);
        when(metricsResultCheck.hasNullValues(metrics)).thenReturn(true);
        List<Point> points = twitterRepository.createPoints(metrics);
        assertThat(points.size()).isEqualTo(0);
    }

    @Test
    public void checkIfSaveMetricsMethodWasInvokedTest() {
        twitterRepository.saveMetrics(metrics);
        verify(influxService, times(1)).savePoint(anyListOf(Point.class));
        verify(influxService, times(1)).close();
    }

}
