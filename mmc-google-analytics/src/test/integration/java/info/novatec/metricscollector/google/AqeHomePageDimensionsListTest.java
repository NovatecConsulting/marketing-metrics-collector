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

import static info.novatec.metricscollector.google.DataProvider.HOST_NAME;
import static info.novatec.metricscollector.google.DataProvider.PAGE_PATH;
import static info.novatec.metricscollector.google.DataProvider.TEST_DIMENSION;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * This test verifies if all of the required dimensions for Aqe Homepage are presented before sending a request to GA API
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApplicationInitializerGoogle.class)
public class AqeHomePageDimensionsListTest {
    @Autowired
    private AqeHomePage aqeHomePage;

    @SpyBean
    private GoogleAnalyticsProperties properties;

    @SpyBean
    private GoogleAnalyticsProperties.AqeHomePage aqeHomePageData;

    private List<String> inputDimensions = new ArrayList<>(Arrays.asList(HOST_NAME, PAGE_PATH));

    @Test
    public void getAqeBlogDimensionsListTest() {
        Mockito.doReturn(Collections.singletonList(TEST_DIMENSION)).when(aqeHomePageData).getSpecificDimensions();
        Mockito.doReturn(aqeHomePageData).when(properties).getAqeHomePage();
        Mockito.doReturn(inputDimensions).when(properties).getSharedDimensions();

        List<String> actualSharedDimensions = properties.getSharedDimensions();
        List<String> actualUniqueDimensions = properties.getAqeHomePage().getSpecificDimensions();

        assertThat(actualSharedDimensions).hasSize(2);
        assertThat(actualUniqueDimensions).hasSize(1);

        aqeHomePage.mergeDimensions();

        assertThat(actualSharedDimensions).hasSize(3)
                .contains(HOST_NAME, PAGE_PATH, TEST_DIMENSION);
    }

    @Test
    public void getAqeBlogEmptyUniqueDimensionsListTest() {
        List<String> inputUniqueEmptyDimensions = new ArrayList<>();

        Mockito.doReturn(inputUniqueEmptyDimensions).when(aqeHomePageData).getSpecificDimensions();
        Mockito.doReturn(aqeHomePageData).when(properties).getAqeHomePage();
        Mockito.doReturn(inputDimensions).when(properties).getSharedDimensions();

        List<String> actualSharedDimesions = properties.getSharedDimensions();
        List<String> actualUniqueDimensions = properties.getAqeHomePage().getSpecificDimensions();

        assertThat(actualSharedDimesions).hasSize(2);
        assertThat(actualUniqueDimensions).hasSize(0);

        aqeHomePage.mergeDimensions();

        assertThat(actualSharedDimesions).hasSize(2)
                .contains(HOST_NAME, PAGE_PATH);
    }

}
