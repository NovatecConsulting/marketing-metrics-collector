package info.novatec.metricCollector.commons;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Point;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
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

    public Map<String, Object> getEntry(String time, String measurement){
        Map<String, Object> entry = new TreeMap<>();
        Query query = new Query("SELECT * FROM "+measurement+" WHERE time='"+time+"'", dbName);
        QueryResult queryResult = influxDB.query(query);

        if(queryResult.getResults().get(0).getSeries()==null){
            return entry;
        }
        List<String> columns = queryResult.getResults().get(0).getSeries().get(0).getColumns();
        List<Object> values = queryResult.getResults().get(0).getSeries().get(0).getValues().get(0);
        for (int i=0; i< columns.size(); i++){
            entry.put(columns.get(i), values.get(i));
        }
        return entry;
    }

}
