package info.novatec.metricscollector.google;

import static info.novatec.metricscollector.google.DataProvider.*;
import static org.assertj.core.api.Assertions.assertThat;

import info.novatec.metricscollector.google.collector.AqeBlog;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * This test verifies if all of the required metrics for Aqe Blog are presented before sending a request to GA API
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApplicationInitializerGoogle.class)
public class AqeBlogMetricsListTest {
    @Autowired
    private AqeBlog aqeBlog;

    @SpyBean
    private GoogleAnalyticsProperties properties;

    @SpyBean
    private GoogleAnalyticsProperties.AqeBlog aqeBlogData;

    private List<String> inputMetrics = new ArrayList<>(Arrays.asList(AVG_SESSION_DURATION, BOUNCE_RATE, UNIQUE_PAGE_VIEWS));

    @Test
    public void getAqeBlogMetricsListTest() {
        Mockito.doReturn(Collections.singletonList(TEST_METRIC)).when(aqeBlogData).getUniqueMetrics();
        Mockito.doReturn(aqeBlogData).when(properties).getAqeBlog();
        Mockito.doReturn(inputMetrics).when(properties).getSharedMetrics();

        List<String> actualSharedMetrics = properties.getSharedMetrics();
        List<String> actualUniqueMetrics = properties.getAqeBlog().getUniqueMetrics();

        assertThat(actualSharedMetrics).hasSize(3);
        assertThat(actualUniqueMetrics).hasSize(1);

        aqeBlog.mergeMetrics();

        assertThat(actualSharedMetrics).hasSize(4)
                .contains(AVG_SESSION_DURATION, BOUNCE_RATE, UNIQUE_PAGE_VIEWS, TEST_METRIC);
    }

    @Test
    public void getAqeBlogEmptyUniqueMetricsListTest() {
        List<String> inputUniqueEmptyMetrics = new ArrayList<>();

        Mockito.doReturn(inputUniqueEmptyMetrics).when(aqeBlogData).getUniqueMetrics();
        Mockito.doReturn(aqeBlogData).when(properties).getAqeBlog();
        Mockito.doReturn(inputMetrics).when(properties).getSharedMetrics();

        List<String> actualSharedMetrics = properties.getSharedMetrics();
        List<String> actualUniqueMetrics = properties.getAqeBlog().getUniqueMetrics();

        assertThat(actualSharedMetrics).hasSize(3);
        assertThat(actualUniqueMetrics).hasSize(0);

        aqeBlog.mergeMetrics();

        assertThat(actualSharedMetrics).hasSize(3)
                .contains(AVG_SESSION_DURATION, BOUNCE_RATE, UNIQUE_PAGE_VIEWS);
    }

}
