package info.novatec.metricscollector.google;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.google.api.services.analyticsreporting.v4.AnalyticsReporting;
import com.google.api.services.analyticsreporting.v4.model.Dimension;
import com.google.api.services.analyticsreporting.v4.model.DimensionFilter;
import com.google.api.services.analyticsreporting.v4.model.DimensionFilterClause;
import com.google.api.services.analyticsreporting.v4.model.Metric;
import com.google.api.services.analyticsreporting.v4.model.ReportRequest;

import info.novatec.metricscollector.google.exception.IORuntimeException;


public class RequestBuilderTest {

    private RequestBuilder requestBuilder;
    private AnalyticsReporting analyticsReporting;

    @Before
    public void init() {
        analyticsReporting = mock(AnalyticsReporting.class);
        requestBuilder = new RequestBuilder(analyticsReporting).prepareRequest();
    }

    @Test
    public void initialStateOfRequestBuilderIsValidTest() {
        assertInitialRequestBuilder();
    }

    @Test
    public void initialStateOfSecondRequestBuilderIsValidTest() {
        requestBuilder.getMetrics().add(new Metric());
        requestBuilder.getDimensions().add(new Dimension());
        requestBuilder.getDimensionFilters().add(new DimensionFilter());
        requestBuilder.prepareRequest();

        assertInitialRequestBuilder();
    }

    private void assertInitialRequestBuilder() {
        assertThat(requestBuilder.getMetrics()).isEmpty();
        assertThat(requestBuilder.getDimensions()).isEmpty();
        assertThat(requestBuilder.getDimensionFilters().size()).isEqualTo(0);
        assertThat(requestBuilder.getDateRange()).isNotNull();
    }

    @Test
    public void addOneMetricTest() {
        requestBuilder.addMetrics(Collections.singletonList(DataProvider.GA_METRIC));
        assertThat(requestBuilder.getMetrics().size()).isEqualTo(1);
        assertThat(requestBuilder.getMetrics().get(0).getExpression()).isEqualTo(DataProvider.GA_METRIC);
    }

    @Test
    public void addThreeMetricsTest() {
        List<String> expectedMetrics = DataProvider.createGoogleAnalyticsMetrics(3);
        requestBuilder.addMetrics(expectedMetrics);
        assertThat(requestBuilder.getMetrics().size()).isEqualTo(3);
        requestBuilder.getMetrics()
            .forEach(actualMetric -> assertThat(expectedMetrics.contains(actualMetric.getExpression())));
    }

    @Test
    public void addOneDimensionTest() {
        requestBuilder.addDimensions(Collections.singletonList(DataProvider.GA_DIMENSION));
        assertThat(requestBuilder.getDimensions().size()).isEqualTo(1);
        assertThat(requestBuilder.getDimensions().get(0).getName()).isEqualTo(DataProvider.GA_DIMENSION);
    }

    @Test
    public void addThreeDimensionsTest() {
        List<String> expectedDimensions = DataProvider.createGoogleAnalyticsDimensions(3);
        requestBuilder.addDimensions(expectedDimensions);
        assertThat(requestBuilder.getDimensions().size()).isEqualTo(3);
        requestBuilder.getDimensions()
            .forEach(actualDimension -> assertThat(expectedDimensions.contains(actualDimension.getName())));
    }

    @Test
    public void addDimensionFilterWithNotOperatorTest() {
        requestBuilder.addDimensionFilter(DataProvider.GA_DIMENSION, DimensionFilterOperators.NOT,
            Collections.singletonList(DataProvider.DIMENSION_VALUE));
        assertThat(requestBuilder.getDimensionFilters().get(0).getOperator()).isEqualTo(
            DimensionFilterOperators.EXACT.name());
        assertThat(requestBuilder.getDimensionFilters().get(0).getNot()).isEqualTo(true);
    }

    @Test
    public void addDimensionFilterTest() {
        requestBuilder.addDimensionFilter(DataProvider.GA_DIMENSION, DimensionFilterOperators.EXACT,
            Collections.singletonList(DataProvider.DIMENSION_VALUE));
        assertThat(requestBuilder.getDimensionFilters().size()).isEqualTo(1);
        assertThat(requestBuilder.getDimensionFilters().get(0).getDimensionName()).isEqualTo(DataProvider.GA_DIMENSION);
    }

    @Test
    public void setDateRangeTest() {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(1);
        requestBuilder.setDateRange(startDate, endDate);

        assertThat(requestBuilder.getDateRange().getStartDate()).isEqualTo(startDate.toString());
        assertThat(requestBuilder.getDateRange().getEndDate()).isEqualTo(endDate.toString());
    }

    @Test
    public void createDimensionFilterClauseTest() {
        List<DimensionFilter> dimensionFilters =
            Collections.singletonList(new DimensionFilter().setDimensionName(DataProvider.GA_DIMENSION));
        DimensionFilterClause filterClause = requestBuilder.createDimensionFilterClause("AND", dimensionFilters);

        assertThat(filterClause.getFilters().size()).isEqualTo(1);
        assertThat(filterClause.getFilters().get(0).getDimensionName()).isEqualTo(DataProvider.GA_DIMENSION);
    }

    @Test
    public void createReportRequestTest() {
        String viewId = "123";
        DimensionFilterClause expectedFilterClause =
            requestBuilder.addMetrics(Collections.singletonList(DataProvider.GA_METRIC))
                .addDimensions(Collections.singletonList(DataProvider.GA_DIMENSION))
                .addDimensionFilter(DataProvider.GA_DIMENSION, DimensionFilterOperators.EXACT,
                    Collections.singletonList(DataProvider.DIMENSION_VALUE))
                .createDimensionFilterClause("AND", requestBuilder.getDimensionFilters());
        List<Metric> expectedMetrics = requestBuilder.getMetrics();
        List<Dimension> expectedDimensions = requestBuilder.getDimensions();

        ReportRequest actualRequest = requestBuilder.buildRequest(viewId).getReportsRequest().getReportRequests().get(0);

        assertThat(actualRequest.getMetrics()).isEqualTo(expectedMetrics);
        assertThat(actualRequest.getDimensions()).isEqualTo(expectedDimensions);
        assertThat(actualRequest.getDimensionFilterClauses().size()).isEqualTo(1);
        assertThat(actualRequest.getDimensionFilterClauses().get(0)).isEqualTo(expectedFilterClause);
        assertThat(actualRequest.getDateRanges().size()).isEqualTo(1);
        assertThat(actualRequest.getDateRanges().get(0)).isEqualTo(requestBuilder.getDateRange());
        assertThat(actualRequest.getViewId()).isEqualTo(viewId);
    }

    @Test
    public void sendRequestTest() throws IOException {
        requestBuilder.buildRequest("123");
        AnalyticsReporting.Reports reports = mock(AnalyticsReporting.Reports.class);
        AnalyticsReporting.Reports.BatchGet batchGet = mock(AnalyticsReporting.Reports.BatchGet.class);
        doReturn(null).when(batchGet).execute();
        doReturn(batchGet).when(reports).batchGet(requestBuilder.getReportsRequest());
        doReturn(reports).when(analyticsReporting).reports();

        requestBuilder.sendRequest();

        verify(batchGet, times(1)).execute();
    }

    @Test(expected = IORuntimeException.class)
    public void sendRequestThrowsExceptionTest() throws IOException {
        requestBuilder.buildRequest("123");
        doThrow(IOException.class).when(analyticsReporting).reports();

        requestBuilder.sendRequest();
    }

}
