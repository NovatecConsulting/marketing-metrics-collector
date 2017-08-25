package info.novatec.metricscollector.google;

import static info.novatec.metricscollector.google.DataProvider.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.google.api.services.analyticsreporting.v4.AnalyticsReporting;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
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
public class AqeBlogMetricsListTest {

    private RequestBuilder requestBuilder;

    @SpyBean
    private GoogleAnalyticsProperties properties;

    @MockBean
    private GoogleAnalyticsProperties.AqeBlog aqeBlogData;

    @MockBean
    private AnalyticsReporting analyticsReporting;

    private List<String> inputMetrics = new ArrayList<>(Arrays.asList(AVG_SESSION_DURATION, BOUNCE_RATE, UNIQUE_PAGE_VIEWS));

    @Before
    public void init() {
        requestBuilder = new RequestBuilder(analyticsReporting);
        requestBuilder.prepareRequest();
    }

    @Test
    public void getAqeBlogMetricsListTest() {
        Mockito.doReturn(Collections.singletonList(TEST_METRIC)).when(aqeBlogData).getSpecificMetrics();
        Mockito.doReturn(aqeBlogData).when(properties).getAqeBlog();
        Mockito.doReturn(inputMetrics).when(properties).getSharedMetrics();

        List<String> actualSharedMetrics = properties.getSharedMetrics();
        List<String> actualSpecificMetrics = properties.getAqeBlog().getSpecificMetrics();

        assertThat(actualSharedMetrics).hasSize(3);
        assertThat(actualSpecificMetrics).hasSize(1);

        requestBuilder.addMetrics(actualSharedMetrics);
        requestBuilder.addMetrics(actualSpecificMetrics);
        List<String> actualMetrics = getMetricsAsString(requestBuilder);

        assertThat(actualMetrics).hasSize(4).contains(AVG_SESSION_DURATION, BOUNCE_RATE, UNIQUE_PAGE_VIEWS, TEST_METRIC);
    }

    @Test
    public void getAqeBlogEmptyUniqueMetricsListTest() {
        List<String> inputUniqueEmptyMetrics = new ArrayList<>();

        Mockito.doReturn(inputUniqueEmptyMetrics).when(aqeBlogData).getSpecificMetrics();
        Mockito.doReturn(aqeBlogData).when(properties).getAqeBlog();
        Mockito.doReturn(inputMetrics).when(properties).getSharedMetrics();

        List<String> actualSharedMetrics = properties.getSharedMetrics();
        List<String> actualSpecificMetrics = properties.getAqeBlog().getSpecificMetrics();

        assertThat(actualSharedMetrics).hasSize(3);
        assertThat(actualSpecificMetrics).hasSize(0);

        requestBuilder.addMetrics(actualSharedMetrics);
        requestBuilder.addMetrics(actualSpecificMetrics);
        List<String> actualMetrics = getMetricsAsString(requestBuilder);

        assertThat(actualSharedMetrics).hasSize(3).contains(AVG_SESSION_DURATION, BOUNCE_RATE, UNIQUE_PAGE_VIEWS);
    }

    private List<String> getMetricsAsString(RequestBuilder requestBuilder) {
        List<String> metricsAsString = new ArrayList<>();
        requestBuilder.getMetrics().forEach(metric -> metricsAsString.add(metric.getExpression()));
        return metricsAsString;
    }

}
