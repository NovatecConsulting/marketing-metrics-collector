package info.novatec.metricscollector.twitter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApplicationInitializerTwitter.class)
public class TwitterSystemTest {

    @Autowired
    private TwitterScheduler twitterScheduler;

    @Test
    public void updateAllGithubProjectsMetricsTest(){
        twitterScheduler.scheduleUpdateAllTwitterUsersMetrics();
    }
}
