package info.novatec.metricscollector.google;

import info.novatec.metricscollector.google.collector.AqeHomePage;
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

import static info.novatec.metricscollector.google.DataProvider.*;
import static info.novatec.metricscollector.google.DataProvider.UNIQUE_PAGE_VIEWS;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * This test verifies if all of the required metrics for Aqe Homepage are presented before sending a request to GA API
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApplicationInitializerGoogle.class)
public class AqeHomePageMetricsListTest {
    @Autowired
    private AqeHomePage aqeHomePage;

    @SpyBean
    private GoogleAnalyticsProperties properties;

    @SpyBean
    private GoogleAnalyticsProperties.AqeHomePage aqeHomePageData;

    private List<String> inputMetrics = new ArrayList<>(Arrays.asList(AVG_SESSION_DURATION, BOUNCE_RATE, UNIQUE_PAGE_VIEWS));

    @Test
    public void getAqeHomepageMetricsListTest() {
        Mockito.doReturn(Collections.singletonList(TEST_METRIC)).when(aqeHomePageData).getSpecificMetrics();
        Mockito.doReturn(aqeHomePageData).when(properties).getAqeHomePage();
        Mockito.doReturn(inputMetrics).when(properties).getSharedMetrics();

        List<String> actualSharedMetrics = properties.getSharedMetrics();
        List<String> actualUniqueMetrics = properties.getAqeHomePage().getSpecificMetrics();

        assertThat(actualSharedMetrics).hasSize(3);
        assertThat(actualUniqueMetrics).hasSize(1);

        aqeHomePage.mergeMetrics();

        assertThat(actualSharedMetrics).hasSize(4)
                .contains(AVG_SESSION_DURATION, BOUNCE_RATE, UNIQUE_PAGE_VIEWS, TEST_METRIC);
    }

    @Test
    public void getAqeHomepageEmptyUniqueMetricsListTest() {
        List<String> inputUniqueEmptyMetrics = new ArrayList<>();

        Mockito.doReturn(inputUniqueEmptyMetrics).when(aqeHomePageData).getSpecificMetrics();
        Mockito.doReturn(aqeHomePageData).when(properties).getAqeHomePage();
        Mockito.doReturn(inputMetrics).when(properties).getSharedMetrics();

        List<String> actualSharedMetrics = properties.getSharedMetrics();
        List<String> actualUniqueMetrics = properties.getAqeHomePage().getSpecificMetrics();

        assertThat(actualSharedMetrics).hasSize(3);
        assertThat(actualUniqueMetrics).hasSize(0);

        aqeHomePage.mergeMetrics();

        assertThat(actualSharedMetrics).hasSize(3)
                .contains(AVG_SESSION_DURATION, BOUNCE_RATE, UNIQUE_PAGE_VIEWS);
    }
}
