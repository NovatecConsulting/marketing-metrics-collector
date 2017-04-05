package info.novatec.metricscollector.commons;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.influxdb.InfluxDB;
import org.influxdb.dto.Point;
import org.springframework.stereotype.Component;

import lombok.Setter;

@Component
public class InfluxService {

    @Setter
    private InfluxDB influxDB;

    @Setter
    private String dbName;

    @Setter
    private String retention;

    void configureInflux() {
        // Flush every 2000 Points, at least every 100ms
        influxDB.enableBatch(2000, 100, TimeUnit.MILLISECONDS);
    }

    public void savePoint(List<Point> points) {
        points.forEach(point -> influxDB.write(dbName, retention, point));
    }

    public void close() {
        influxDB.close();
    }

}
