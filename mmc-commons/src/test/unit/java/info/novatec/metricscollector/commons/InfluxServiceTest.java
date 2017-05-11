package info.novatec.metricscollector.commons;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.influxdb.InfluxDB;
import org.influxdb.dto.Point;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
public class InfluxServiceTest {

    private static final String DB_NAME = "aDbName";
    private static final String RETENTION = "aRetention";

    @MockBean
    private InfluxDB influxDB;

    @MockBean
    private CommonsProperties properties;

    private InfluxService influxService;

    @Before
    public void init(){
        InfluxService influxService = new InfluxService(influxDB, properties);
        when(properties.getDbName()).thenReturn(DB_NAME);
        when(properties.getRetention()).thenReturn(RETENTION);
        this.influxService = influxService;
    }

    @Test
    public void checkIfWriteMethodIsInvoked3TimesTest() {
        List<Point> points = new ArrayList<>();
        points.add(mock(Point.class));
        points.add(mock(Point.class));
        points.add(mock(Point.class));
        influxService.savePoint(points);
        verify(influxDB, times(3)).write(eq(DB_NAME), eq(RETENTION), any(Point.class));
    }

    @Test
    public void checkIfCloseMethodIsInvokedTest() {
        influxService.close();
        verify(influxDB, times(1)).close();
    }

}
