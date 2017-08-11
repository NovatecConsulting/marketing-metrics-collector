package info.novatec.metricscollector.google;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.influxdb.dto.Point;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import info.novatec.metricscollector.commons.MetricsValidator;
import info.novatec.metricscollector.commons.database.InfluxService;


@RunWith(SpringRunner.class)
public class MetricsRepositoryTest {

    @MockBean
    private InfluxService influxService;

    @MockBean
    private MetricsValidator metricsValidator;

    @MockBean
    private Metrics metrics;

    private MetricsRepository repository;

    @Before
    public void init() {
        metrics = DataProvider.createMetrics();
        repository = new MetricsRepository(influxService, metricsValidator);
    }

    @Test
    public void createPointWithValidMetricsTest() {
        doReturn(false).when(metricsValidator).hasNullValues(metrics);
        Point.Builder point = repository.createPointBuilder(metrics, DataProvider.MEASUREMENT_NAME_VALUE);

        //createPointBuilder uses current system time to set the timestamp for the point. This test has a static expected
        //value. Therefor the timestamp for the point has to be set to the static value.
        point.time(DataProvider.TIME_VALUE, TimeUnit.MILLISECONDS);
        assertThat(point.build().toString()).isEqualTo(DataProvider.createInfluxPointAsString());
    }

    @Test
    public void createPointBuilderWithNullValueMetricsTest() {
        when(metricsValidator.hasNullValues(metrics)).thenReturn(true);
        Point.Builder pointBuilder = repository.createPointBuilder(metrics, DataProvider.MEASUREMENT_NAME_VALUE);
        assertThat(pointBuilder).isNull();
    }

    @Test
    public void createPointsWithEmptyListOfMetricsTest() {
        List<Metrics> metricsForPages = new ArrayList<>();
        List<Point> points = repository.createPoints(metricsForPages, DataProvider.MEASUREMENT_NAME_VALUE);
        assertThat(points.size()).isEqualTo(0);
    }

    @Test
    public void createEmptyPointListWhenMetricsInvalidTest() {
        Metrics invalidMetrics = new Metrics();
        List<Metrics> metricsForPages = new ArrayList<Metrics>() {{
            add(invalidMetrics);
        }};
        doReturn(true).when(metricsValidator).hasNullValues(invalidMetrics);
        List<Point> points = repository.createPoints(metricsForPages, DataProvider.MEASUREMENT_NAME_VALUE);
        assertThat(points.size()).isEqualTo(0);
    }

    @Test
    public void invocationInfluxSavePointsMethodTest() {
        final int NUMBER_OF_PAGES = 2;
        List<Metrics> metricsForPages = DataProvider.createMetricsForPages(NUMBER_OF_PAGES);
        repository.saveMetrics(metricsForPages, DataProvider.MEASUREMENT_NAME_VALUE);
        verify(influxService, times(1)).savePoint(anyListOf(Point.class));
        verify(influxService, times(1)).close();
    }

}
