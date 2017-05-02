package info.novatec.metricscollector.github;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = GithubApplicationInitializer.class)
public class GithubSystemTest {

    @Autowired
    private Scheduler githubScheduler;

    @Test
    public void updateAllGithubProjectsMetricsTest(){
        githubScheduler.scheduleUpdateAllGithubProjectsMetrics();
    }

}
