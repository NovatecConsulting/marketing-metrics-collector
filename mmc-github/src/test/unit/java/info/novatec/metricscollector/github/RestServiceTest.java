package info.novatec.metricscollector.github;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;


@RunWith(SpringRunner.class)
public class RestServiceTest {

    private static final String DEFAULT_TOKEN = "aDefaultToken";

    private static final String REQUEST_URL = "theRequestUrl";

    @MockBean
    RestTemplate restTemplate;

    private RestService restService;

    @Before
    public void init(){
        this.restService = new RestService(restTemplate, DEFAULT_TOKEN);
    }

    @Test
    public void verifyThatCustomHeaderIsUsed(){
        restService.setHttpHeaders(createHttpHeaders());
        restService.sendRequest(REQUEST_URL);
        HttpEntity entity = new HttpEntity(createHttpHeaders());
        verify(restTemplate, times(1)).exchange(REQUEST_URL, HttpMethod.GET, entity, String.class);
    }

    private HttpHeaders createHttpHeaders(){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("headerKey", "headerValue");
        return httpHeaders;
    }

    @Test
    public void setCorrectTokenTest() {
        restService = spy(restService);
        restService.setToken(DEFAULT_TOKEN);
        verify(restService, times(1)).setDefaultHttpHeaders();
        assertThat(restService.getHttpHeaders().get("Authorization")).contains("token "+DEFAULT_TOKEN);
    }

    @Test
    public void setEmptyTokenTest() {
        restService = spy(restService);
        restService.setToken("");
        verify(restService, times(1)).setDefaultHttpHeaders();
        assertThat(restService.getHttpHeaders().get("Authorization")).doesNotContain("");
    }
}
