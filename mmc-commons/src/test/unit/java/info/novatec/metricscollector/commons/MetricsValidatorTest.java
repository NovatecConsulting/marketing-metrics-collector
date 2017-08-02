package info.novatec.metricscollector.commons;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;


public class MetricsValidatorTest {

    private MetricsValidator metricsResultCheck;

    @Before
    public void init() {
        metricsResultCheck = new MetricsValidator();
    }

    @Test
    public void checkMetricsHavingNullValuesTest() {
        CustomMetrics customMetrics = new CustomMetrics(null, 42);
        assertThat(metricsResultCheck.hasNullValues(customMetrics)).isTrue();
    }

    @Test
    public void checkMetricsHavingNoNullValuesTest() {
        CustomMetrics customMetrics = new CustomMetrics("foo", 42);
        assertThat(metricsResultCheck.hasNullValues(customMetrics)).isFalse();
    }
}

class CustomMetrics {
    private String aString;
    private int aInt;

    public CustomMetrics(String aString, int aInt) {
        this.aString = aString;
        this.aInt = aInt;
    }
}