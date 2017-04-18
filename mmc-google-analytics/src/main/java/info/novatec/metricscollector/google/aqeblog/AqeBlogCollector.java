package info.novatec.metricscollector.google.aqeblog;

import org.springframework.stereotype.Component;


@Component
public class AqeBlogCollector {

    public AqeBlogMetrics collect() {
        AqeBlogMetrics metrics = new AqeBlogMetrics();

        collectPageViews(metrics);
        collectUniquePageViews(metrics);
        collectNumberOfSessions(metrics);
        collectNumberOfBounces(metrics);
        collectBounceRate(metrics);
        collectAvgSessionDuration(metrics);
        collectAvgTimeOnPage(metrics);

        return metrics;
    }

    private void collectPageViews(AqeBlogMetrics metrics) {
        //TODO
    }

    private void collectUniquePageViews(AqeBlogMetrics metrics) {
        //TODO
    }


    private void collectNumberOfSessions(AqeBlogMetrics metrics) {
        //TODO
    }

    private void collectNumberOfBounces(AqeBlogMetrics metrics) {
        //TODO
    }

    private void collectBounceRate(AqeBlogMetrics metrics) {
        //TODO
    }

    private void collectAvgSessionDuration(AqeBlogMetrics metrics) {
        //TODO
    }

    private void collectAvgTimeOnPage(AqeBlogMetrics metrics) {
        //TODO
    }

    private void collectCountry(AqeBlogMetrics metrics) {
        //TODO
    }

    private void collectNumberOfComments(AqeBlogMetrics metrics) {
        //TODO
    }

    private void collectShare(AqeBlogMetrics metrics) {
        //TODO
    }
}
