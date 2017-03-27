package info.novatec.metricscollector.commons;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.Setter;

@Component
public class InfluxService {

    private InfluxDB influxDB;

    @Setter
    private String dbName;

    @Setter
    private String retention;

    @Autowired
    InfluxService(ConfigProperties properties) {
        influxDB = InfluxDBFactory.connect(properties.getInfluxUrl());
        dbName = properties.getInfluxDbName();
        retention = "default";
        configureInflux();
    }

    private void configureInflux() {
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
