package info.novatec.metricscollector.github.metrics;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.json.JsonObject;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import info.novatec.metricscollector.commons.RestService;
import info.novatec.metricscollector.github.GithubMetricsResult;
import info.novatec.metricscollector.github.data.DataProvider;


@RunWith(SpringRunner.class)
public class NumberOfForksTest {

    @MockBean
    private RestService restService;

    private GithubMetricsResult metrics;

    private DataProvider data = new DataProvider();

    @Before
    public void init(){

        metrics = new GithubMetricsResult();
    }

    @Test
    public void collectTest() {
        NumberOfForks numberOfForks = new NumberOfForks(restService, metrics);
        numberOfForks.setProjectName(data.NON_EXISTING_PROJECT);
        JsonObject mockedRepository = mock(JsonObject.class);
        when(mockedRepository.getInt("forks_count")).thenReturn(4);
        numberOfForks.setProjectRepository(mockedRepository);
        numberOfForks.collect();
        assertThat(metrics.getForks()).isEqualTo(4);
    }
}
