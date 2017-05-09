package info.novatec.metricscollector.commons;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.influxdb.InfluxDB;
import org.influxdb.dto.Point;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Setter
@Component
@ConfigurationProperties(prefix = "influx")
public class InfluxService {

    @Getter
    private InfluxDB influxDB;

    private String dbName;

    private String retention;

    private String url;

    public InfluxService(InfluxDB influxDb) {
        this.influxDB = influxDb;
        configure();
    }

    public void configure() {
        // Flush every 2000 Points, at least every 100ms
        influxDB.enableBatch(2000, 100, TimeUnit.MILLISECONDS);
    }

    public void savePoint(List<Point> points) {
        points.forEach(point -> influxDB.write(dbName, retention, point));
        log.info("Saved " + points.size() + " points to database '"+dbName+"' with retention '"+retention+"'.");
    }

    public void close() {
        influxDB.close();
    }

}
