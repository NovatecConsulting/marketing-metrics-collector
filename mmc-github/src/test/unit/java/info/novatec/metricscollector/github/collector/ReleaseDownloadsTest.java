package info.novatec.metricscollector.github.collector;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.json.JsonObject;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import info.novatec.metricscollector.github.Metrics;
import info.novatec.metricscollector.github.RestService;
import info.novatec.metricscollector.github.util.DataProvider;


@RunWith(SpringRunner.class)
public class ReleaseDownloadsTest {

    @MockBean
    private RestService restService;

    @MockBean
    private ResponseEntity<String> response;

    private Metrics metrics;

    ReleaseDownloads releaseDownloads;

    @Before
    public void init() {
        metrics = DataProvider.createEmptyMetrics();
        releaseDownloads = new ReleaseDownloads(restService, metrics);
    }

    @Test
    public void skipRequestingWhenRepositoryHasNoValues() {
        JsonObject mockedRepository = mock(JsonObject.class);
        when(mockedRepository.getBoolean("has_downloads")).thenReturn(false);
        releaseDownloads.setProjectRepository(mockedRepository);
        collectReleaseDownloads(false);
        assertThat(metrics.getReleaseDownloads().size()).isEqualTo(0);
    }

    @Test
    public void numberOfDownloadedAssetsTest() {
        collectReleaseDownloads(false);
        assertThat(metrics.getReleaseDownloads().size()).isEqualTo(4);
    }

    @Test
    public void downloadCountOfFirstEntryTest() {
        collectReleaseDownloads(false);
        String firstKey = metrics.getReleaseDownloads().firstKey();
        assertThat(firstKey).isNotNull();
        assertThat(metrics.getReleaseDownloads().get(firstKey)).isEqualTo(5);
    }

    @Test
    public void nameOfFirstEntryTest() {
        collectReleaseDownloads(false);
        String firstKey = metrics.getReleaseDownloads().firstKey();
        assertThat(firstKey).isEqualTo("v1:testproject1.jar");
    }

    @Test
    public void zeroDownloadsWillBeIgnoredTest() {
        collectReleaseDownloads(true);
        assertThat(metrics.getReleaseDownloads().size()).isEqualTo(1);
    }

    private void collectReleaseDownloads(boolean dataWithZeroDownloads) {
        String mockedResponseBody = dataWithZeroDownloads ? getMockedResponseWithZeroDownloads() : getMockedResponse();
        when(restService.sendRequest(anyObject())).thenReturn(response);
        when(response.getBody()).thenReturn(mockedResponseBody);
        releaseDownloads.collect();
    }

    private String getMockedResponse() {
        return "[{ " + "\"tag_name\":\"v1\"," + "\"assets\":["

            //release1 asset1
            + "{" + "\"name\": \"testproject1.jar\"," + "\"download_count\": 5" + "},"

            //release1 asset2
            + "{" + "\"name\": \"testproject2.jar\"," + "\"download_count\": 10" + "}"

            + "]" //end assets release1
            + "}," //end release1

            + "{" + "\"tag_name\":\"v2\"," + "\"assets\":["

            //release2 asset1
            + "{" + "\"name\": \"testproject1.jar\"," + "\"download_count\": 15" + "},"

            //release2 asset2
            + "{" + "\"name\": \"testproject2.jar\"," + "\"download_count\": 30" + "}"

            + "]" //end assets release2
            + "}"

            + "]"; //end releases
    }

    private String getMockedResponseWithZeroDownloads() {
        return "[{ " + "\"tag_name\":\"v1\"," + "\"assets\":["

            //release1 asset1
            + "{" + "\"name\": \"testproject1.jar\"," + "\"download_count\": 5" + "},"

            //release1 asset2
            + "{" + "\"name\": \"testproject2.jar\"," + "\"download_count\": 0" + "}" + "]" //end assets release2
            + "}"

            + "]"; //end releases
    }
}
