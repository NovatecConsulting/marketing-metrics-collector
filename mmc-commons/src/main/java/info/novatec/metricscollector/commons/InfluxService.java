package info.novatec.metricscollector.commons;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Point;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Component
@NoArgsConstructor
@ConfigurationProperties(prefix = "influx")
public class InfluxService {

    @Getter
    private InfluxDB influxDB;

    private String dbName;

    private String retention;

    private String url;

    public void connect() {
        influxDB = createInfluxDb();
        // Flush every 2000 Points, at least every 100ms
        influxDB.enableBatch(2000, 100, TimeUnit.MILLISECONDS);
    }

    public InfluxService(InfluxDB influxDb) {
        this.influxDB = influxDb;
    }

    public void savePoint(List<Point> points) {

        points.forEach(point -> influxDB.write(dbName, retention, point));
    }

    public void close() {
        influxDB.close();
    }

    InfluxDB createInfluxDb(){
        return InfluxDBFactory.connect(url);
    }

}
