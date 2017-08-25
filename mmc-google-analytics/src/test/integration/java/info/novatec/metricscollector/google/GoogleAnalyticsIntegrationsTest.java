package info.novatec.metricscollector.google;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import info.novatec.metricscollector.google.scheduler.AqeBlogScheduler;
import info.novatec.metricscollector.google.scheduler.AqeHomePageScheduler;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApplicationInitializerGoogle.class)
public class GoogleAnalyticsIntegrationsTest {

    @Autowired
    private AqeBlogScheduler aqeBlogScheduler;

    @Autowired
    private AqeHomePageScheduler aqeHomePageScheduler;

    @Test
    public void collectAndSaveAqeBlogHomePageData() throws IOException {
        aqeBlogScheduler.updateMetrics();
    }

    @Test
    public void collectAndSaveAqeHomePageData() throws IOException {
        aqeHomePageScheduler.updateMetrics();
    }
}
