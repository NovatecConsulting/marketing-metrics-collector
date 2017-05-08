package info.novatec.metricscollector.google;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApplicationInitializerGoogle.class)
public class GoogleAnalyticsSystemTest {

    @Autowired
    private Scheduler scheduler;

    @Test
    public void collectTest() throws IOException {
        scheduler.scheduleUpdateMetricsForAllWebpages();
    }
}
