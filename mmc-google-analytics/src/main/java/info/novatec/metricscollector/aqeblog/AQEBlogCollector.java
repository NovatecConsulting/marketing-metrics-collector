package info.novatec.metricscollector.aqeblog;

import org.springframework.stereotype.Component;


@Component
public class AQEBlogCollector {

    AQEBlogMetrics collect() {
        AQEBlogMetrics metrics = new AQEBlogMetrics();

        collectDailyVisits(metrics);
        collectNumberOfSessions(metrics);
        collectNumberOfBounces(metrics);
        calculateBounceRate(metrics);
        collectAverageSessionDuration(metrics);
        collectCountry(metrics);
        collectNumberOfComments(metrics);
        collectShare(metrics);

        return metrics;
    }

    private void collectDailyVisits(AQEBlogMetrics metrics){
        //TODO
    }

    private void collectNumberOfSessions(AQEBlogMetrics metrics){
        //TODO
    }

    private void collectNumberOfBounces(AQEBlogMetrics metrics){
        //TODO
    }

    private void calculateBounceRate(AQEBlogMetrics metrics){
        //TODO
    }

    private void collectAverageSessionDuration(AQEBlogMetrics metrics){
        if(metrics.getSessions()!=null){
            metrics.setBounceRate(metrics.getBounces()/metrics.getSessions());
        }
    }

    private void collectCountry(AQEBlogMetrics metrics){
        //TODO
    }

    private void collectNumberOfComments(AQEBlogMetrics metrics){
        //TODO
    }

    private void collectShare(AQEBlogMetrics metrics){
        //TODO
    }
}
