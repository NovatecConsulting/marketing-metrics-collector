package info.novatec.metricscollector.google.collector;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.annotation.PostConstruct;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.google.api.services.analyticsreporting.v4.AnalyticsReporting;
import com.google.api.services.analyticsreporting.v4.model.DateRange;
import com.google.api.services.analyticsreporting.v4.model.DimensionFilter;
import com.google.api.services.analyticsreporting.v4.model.ReportRequest;

import info.novatec.metricscollector.google.ApplicationInitializerGoogle;
import info.novatec.metricscollector.google.GoogleAnalyticsProperties;
import info.novatec.metricscollector.google.Metrics;
import info.novatec.metricscollector.google.RequestBuilder;
import info.novatec.metricscollector.google.ResponseParser;


/**
 * Created by DBE on 22.09.2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApplicationInitializerGoogle.class)
public class AqeBlogTest {

    private static final int FIRST = 0;

    @Autowired
    GoogleAnalyticsProperties properties;

    ReportRequest reportRequest;

    @PostConstruct
    public void init() {
        RequestBuilder requestBuilder = spy(new RequestBuilder(mock(AnalyticsReporting.class)));
        doReturn(null).when(requestBuilder).sendRequest();
        List<Metrics> metricsList = new ArrayList<>();
        AqeBlog aqeBlog = new AqeBlog(properties, requestBuilder, new ResponseParser(), metricsList);
        aqeBlog.requestMetrics();

        reportRequest = requestBuilder.getReportsRequest().getReportRequests().get(0);
    }

    @Test
    @SuppressWarnings("Duplicates")
    public void validateRequiredMetrics() {
        //The duplicate warning occurs due to same metrics of AqeBlog and AqeHomepage
        //They aren't outsourced to a method to keep this test simple and provides a quick overview of the required metrics
        List<String> requiredMetrics = new ArrayList<String>() {{
            add("ga:pageviews");
            add("ga:uniquePageviews");
            add("ga:sessions");
            add("ga:bounces");
            add("ga:bounceRate");
            add("ga:avgSessionDuration");
        }};

        List<String> actualMetrics = new ArrayList<>();
        reportRequest.getMetrics().forEach(metric -> actualMetrics.add(metric.getExpression()));
        requiredMetrics.forEach(requiredMetric -> assertThat(actualMetrics.contains(requiredMetric)).isTrue());
    }

    @Test
    public void validateRequiredDimensions() {
        List<String> requiredDimensions = new ArrayList<String>() {{
            add("ga:hostname");
            add("ga:pagePath");
        }};
        List<String> actualDimensions = new ArrayList<>();
        reportRequest.getDimensions().forEach(dimension -> actualDimensions.add(dimension.getName()));
        requiredDimensions.forEach(requiredDimension -> assertThat(actualDimensions.contains(requiredDimension)).isTrue());
    }

    @Test
    public void validateHostname() {
        Optional<DimensionFilter> optionalDimensionFilter = reportRequest.getDimensionFilterClauses()
            .get(FIRST)
            .getFilters()
            .stream()
            .filter(dimensionFilter -> dimensionFilter.getDimensionName().equals("ga:hostname"))
            .findFirst();

        String actualHostname = optionalDimensionFilter.get().getExpressions().get(FIRST);
        assertThat(actualHostname).isEqualTo("blog.novatec-gmbh.de");
    }

    @Test
    public void validateExcludedUrls() {
        List<String> expectedExcludedUrls = new ArrayList<String>() {{
            add("/");
        }};
        Optional<DimensionFilter> optionalDimensionFilter = reportRequest.getDimensionFilterClauses().get(0).getFilters().stream()
            .filter(dimensionFilter -> dimensionFilter.getDimensionName().equals("ga:pagePath")).findFirst();

        List<String> actualExcludedUrls = optionalDimensionFilter.get().getExpressions();
        expectedExcludedUrls.forEach(expectedExcludedUrl ->
            assertThat(actualExcludedUrls.contains(expectedExcludedUrl)).isTrue()
        );
    }

    @Test
    public void validateDefaultDateRangeAsYesterday(){
        DateRange actualDateRange = reportRequest.getDateRanges().get(FIRST);
        String yesterday = LocalDate.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        assertThat(actualDateRange.getStartDate()).isEqualTo(yesterday);
        assertThat(actualDateRange.getEndDate()).isEqualTo(yesterday);
    }
}
