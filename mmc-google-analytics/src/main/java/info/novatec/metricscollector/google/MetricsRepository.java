package info.novatec.metricscollector.google;

import java.util.ArrayList;
import java.util.List;

import org.influxdb.dto.Point;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import info.novatec.metricscollector.commons.InfluxService;
import info.novatec.metricscollector.commons.MetricsValidator;


@Slf4j
@Component
@RequiredArgsConstructor
public class MetricsRepository {

    private static final String GA_PREFIX = "ga:";

    private final InfluxService influx;

    private final MetricsValidator metricsValidator;

    void saveMetrics(List<Metrics> metricsForAllPages, String measurementName) {
        List<Point> points = new ArrayList<>();
        metricsForAllPages.forEach(metrics -> points.addAll(createPoints(metrics, measurementName)));
        log.info("Created {} points for measurement '{}'.", points.size(), measurementName);
        influx.savePoint(points);
        influx.close();
    }

    private List<Point> createPoints(Metrics metrics, String measurementName) {
        List<Point> points = new ArrayList<>();

        if (metricsValidator.hasNullValues(metrics)) {
            log.error(
                "Since there are null values in metrics, creating points for page '{}' isn't possible! Metrics content:\n{}",
                metrics.getPagePath(), metrics.toString());
            return points;
        }

        Point.Builder point = Point.measurement(measurementName).tag("pagePath", metrics.getPagePath());
        metrics.getMetrics().forEach((key, value) -> {
            key = key.startsWith(GA_PREFIX) ? key.substring(3) : key;
            point.addField(key, value);
        });

        points.add(point.build());

        return points;
    }

}
