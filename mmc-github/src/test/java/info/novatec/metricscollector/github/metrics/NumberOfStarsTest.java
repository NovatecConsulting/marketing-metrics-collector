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
public class NumberOfStarsTest {

    @MockBean
    private RestService restService;

    @Autowired
    private GithubMetricsResult metrics;

    private DataProvider data = new DataProvider();

    @Test
    public void collectTest() {
        NumberOfStars numberOfStars = new NumberOfStars(restService, metrics);
        numberOfStars.setProjectName(data.NON_EXISTING_PROJECT);
        JsonObject mockedRepository = mock(JsonObject.class);
        when(mockedRepository.getInt("stargazers_count")).thenReturn(4);
        numberOfStars.setProjectRepository(mockedRepository);
        numberOfStars.collect();
        assertThat(metrics.getStars()).isEqualTo(4);
    }
}
