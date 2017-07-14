package info.novatec.metricscollector.commons.database;

import java.util.List;

import org.influxdb.InfluxDB;
import org.influxdb.dto.Point;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import info.novatec.metricscollector.commons.CommonsProperties;


@Slf4j
@Setter
@Component
@RequiredArgsConstructor
public class InfluxService {

    private final InfluxDB influxDB;

    private final CommonsProperties properties;

    public void savePoint(List<Point> points) {
        points.forEach(point -> influxDB.write(properties.getDbName(), properties.getRetention(), point));
        log.info("Saved " + points.size() + " points to database '" + properties.getDbName() + "' with retention '"
            + properties.getRetention() + "'.");
    }

    public void close() {
        influxDB.close();
    }

}
