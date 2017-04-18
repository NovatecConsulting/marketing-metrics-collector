package info.novatec.metricscollector.github.metrics;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.json.JsonObject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import info.novatec.metricscollector.commons.RestService;
import info.novatec.metricscollector.github.GithubMetricsResult;
import info.novatec.metricscollector.github.TestConfig;
import info.novatec.metricscollector.github.data.DataProvider;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestConfig.class)
public class NumberOfOpenIssuesTest {

    @MockBean
    private RestService restService;

    @Autowired
    private GithubMetricsResult metrics;

    private DataProvider data = new DataProvider();

    @Test
    public void collectTest() {
        NumberOfOpenIssues numberOfOpenIssues = new NumberOfOpenIssues(restService, metrics);
        numberOfOpenIssues.setProjectName(data.NON_EXISTING_PROJECT);
        JsonObject mockedRepository = mock(JsonObject.class);
        when(mockedRepository.getInt("open_issues_count")).thenReturn(4);
        numberOfOpenIssues.setProjectRepository(mockedRepository);
        numberOfOpenIssues.collect();
        assertThat(metrics.getOpenIssues()).isEqualTo(4);
    }
}
