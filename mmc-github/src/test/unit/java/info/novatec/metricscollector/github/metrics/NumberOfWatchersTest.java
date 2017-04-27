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

import info.novatec.metricscollector.github.util.DataProvider;

import info.novatec.metricscollector.github.RestService;
import info.novatec.metricscollector.github.GithubMetricsResult;


@RunWith(SpringRunner.class)
public class NumberOfWatchersTest {

    @MockBean
    private RestService restService;

    private GithubMetricsResult metrics;

    @Before
    public void init(){
        metrics = DataProvider.createEmptyMetrics();
    }

    @Test
    public void collectTest() {
        NumberOfWatchers numberOfWatchers = new NumberOfWatchers(restService, metrics);
        JsonObject mockedRepository = mock(JsonObject.class);
        when(mockedRepository.getInt("subscribers_count")).thenReturn(4);
        numberOfWatchers.setProjectRepository(mockedRepository);
        numberOfWatchers.collect();
        assertThat(metrics.getWatchers()).isEqualTo(4);
    }
}
