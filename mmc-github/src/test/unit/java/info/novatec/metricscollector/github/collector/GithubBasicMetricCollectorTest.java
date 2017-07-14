package info.novatec.metricscollector.github.collector;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import javax.json.JsonObject;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import info.novatec.metricscollector.commons.rest.RestService;
import info.novatec.metricscollector.github.Metrics;
import info.novatec.metricscollector.github.util.DataProvider;


@RunWith(SpringRunner.class)
public class GithubBasicMetricCollectorTest {

    @MockBean
    RestService restService;

    Metrics metrics;

    GithubMetricFakeCollector collector;

    @Before
    public void init() {
        metrics = new Metrics(DataProvider.VALID_GITHUB_URL);
        collector = spy(new GithubMetricFakeCollector(restService, metrics));
    }

    @Test
    public void getAlreadyRequestedProjectRepositoryTest() {
        JsonObject mockedRepository = mock(JsonObject.class);
        when(mockedRepository.getString("keyForAString")).thenReturn("aString");
        collector.setProjectRepository(mockedRepository);

        JsonObject projectRepository = collector.getProjectRepository();

        assertThat(projectRepository.getString("keyForAString")).isEqualTo("aString");
    }

    @Test
    public void getProjectRepositoryTest() {
        ResponseEntity<String> mockedResponse = mock(ResponseEntity.class);
        String mockedResponseBody = "{\"keyForAString\":\"aString\"}";
        when(restService.sendRequest(anyObject())).thenReturn(mockedResponse);
        when(mockedResponse.getBody()).thenReturn(mockedResponseBody);

        JsonObject projectRepository = collector.getProjectRepository();

        assertThat(projectRepository.getString("keyForAString")).isEqualTo("aString");
    }
}
