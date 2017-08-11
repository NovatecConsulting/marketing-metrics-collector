package info.novatec.metricscollector.google;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;


public class MetricsTest {

    Metrics metrics;

    @Before
    public void init() {
        metrics = new Metrics();
    }

    @Test
    public void addDimensionsTest() {
        Map<String, String> expectedDimensions = new HashMap<>();
        expectedDimensions.put("dimensionName1", "dimension1");
        expectedDimensions.put("dimensionName2", "dimension2");
        expectedDimensions.forEach((String name, String value) -> metrics.addDimension(name, value));
        assertThat(metrics.getDimensions()).isEqualTo(expectedDimensions);
    }

    @Test
    public void addMetricsTest() {
        Map<String, Double> expectedMetrics = new HashMap<>();
        expectedMetrics.put("metricsName1", 1.0);
        expectedMetrics.put("metricsName2", 2.0);
        expectedMetrics.forEach((String name, Double value) -> metrics.addMetric(name, value));
        assertThat(metrics.getMetrics()).isEqualTo(expectedMetrics);
    }
}
