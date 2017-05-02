package info.novatec.metricscollector.github.collector;

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
import info.novatec.metricscollector.github.Metrics;


@RunWith(SpringRunner.class)
public class StarsTest {

    @MockBean
    private RestService restService;

    private Metrics metrics;

    @Before
    public void init() {
        metrics = DataProvider.createEmptyMetrics();
    }

    @Test
    public void collectTest() {
        Stars numberOfStars = new Stars(restService, metrics);
        JsonObject mockedRepository = mock(JsonObject.class);
        when(mockedRepository.getInt("stargazers_count")).thenReturn(4);
        numberOfStars.setProjectRepository(mockedRepository);
        numberOfStars.collect();
        assertThat(metrics.getStars()).isEqualTo(4);
    }
}
