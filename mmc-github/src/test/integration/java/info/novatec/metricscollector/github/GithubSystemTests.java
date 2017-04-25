package info.novatec.metricscollector.github;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApplicationInitializerGithub.class)
public class GithubSystemTests {

    @Autowired
    private GithubScheduler githubScheduler;

    @Test
    public void updateAllGithubProjectsMetricsTest(){
        githubScheduler.updateAllGithubProjectsMetrics();
    }

}
