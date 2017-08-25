package info.novatec.metricscollector.google;

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

import static info.novatec.metricscollector.google.DataProvider.*;
import static org.assertj.core.api.Assertions.assertThat;


/**
 * This test verifies if all of the required dimensions for Aqe Blog are presented before sending a request to GA API
 */
@RunWith(SpringRunner.class)
public class AqeBlogDimensionsListTest {
    private RequestBuilder requestBuilder;

    @SpyBean
    private GoogleAnalyticsProperties properties;

    @MockBean
    private GoogleAnalyticsProperties.AqeBlog aqeBlogData;

    @MockBean
    private AnalyticsReporting analyticsReporting;

    private List<String> inputDimensions = new ArrayList<>(Arrays.asList(HOST_NAME, PAGE_PATH));

    @Before
    public void init() {
        requestBuilder = new RequestBuilder(analyticsReporting);
        requestBuilder.prepareRequest();
    }

    @Test
    public void getAqeBlogDimensionsListTest() {
        Mockito.doReturn(Collections.singletonList(TEST_DIMENSION)).when(aqeBlogData).getSpecificDimensions();
        Mockito.doReturn(aqeBlogData).when(properties).getAqeBlog();
        Mockito.doReturn(inputDimensions).when(properties).getSharedDimensions();

        List<String> actualSharedDimensions = properties.getSharedDimensions();
        List<String> actualSpecificDimensions = properties.getAqeBlog().getSpecificDimensions();

        assertThat(actualSharedDimensions).hasSize(2);
        assertThat(actualSpecificDimensions).hasSize(1);

        requestBuilder.addDimensions(actualSharedDimensions);
        requestBuilder.addDimensions(actualSpecificDimensions);
        List<String> actualDimensions = getDimensionsAsString(requestBuilder);

        assertThat(actualDimensions).hasSize(3).contains(HOST_NAME, PAGE_PATH, TEST_DIMENSION);
    }

    @Test
    public void getAqeBlogEmptyUniqueDimensionsListTest() {
        List<String> inputUniqueEmptyDimensions = new ArrayList<>();

        Mockito.doReturn(inputUniqueEmptyDimensions).when(aqeBlogData).getSpecificDimensions();
        Mockito.doReturn(aqeBlogData).when(properties).getAqeBlog();
        Mockito.doReturn(inputDimensions).when(properties).getSharedDimensions();

        List<String> actualSharedDimensions = properties.getSharedDimensions();
        List<String> actualSpecificDimensions = properties.getAqeBlog().getSpecificDimensions();

        assertThat(actualSharedDimensions).hasSize(2);
        assertThat(actualSpecificDimensions).hasSize(0);

        requestBuilder.addDimensions(actualSharedDimensions);
        requestBuilder.addDimensions(actualSpecificDimensions);
        List<String> actualDimensions = getDimensionsAsString(requestBuilder);

        assertThat(actualDimensions).hasSize(2).contains(HOST_NAME, PAGE_PATH);
    }

    private List<String> getDimensionsAsString(RequestBuilder requestBuilder) {
        List<String> dimensionsAsString = new ArrayList<>();
        requestBuilder.getDimensions().forEach(dimension -> dimensionsAsString.add(dimension.getName()));
        return dimensionsAsString;
    }

}
