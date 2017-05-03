package info.novatec.metricscollector.google;

import com.google.api.services.analyticsreporting.v4.model.GetReportsResponse;

import java.io.IOException;

public interface ReportRequestBuilder {
    GetReportsResponse sendReportRequest() throws IOException;
}
