package info.novatec.metricscollector.commons.rest;

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
    private RestTemplate restTemplate;

    private RestService restService;

    @Before
    public void init() {
        this.restService = spy(new RestService(restTemplate));
        restService.prepareRequest().addHttpHeader("Authorization", "token " + DEFAULT_TOKEN);
    }

    @Test
    public void verifyThatCustomHeaderIsUsed() {
        restService.setHttpHeaders(createHttpHeaders());
        restService.sendRequest(REQUEST_URL);
        HttpEntity entity = new HttpEntity(createHttpHeaders());
        verify(restTemplate, times(1)).exchange(REQUEST_URL, HttpMethod.GET, entity, String.class);
    }

    private HttpHeaders createHttpHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("headerKey", "headerValue");
        return httpHeaders;
    }

    @Test
    public void addHeaderTest() {
        restService.addHttpHeader("HeaderKey", "HeaderValue");
        assertThat(restService.getHttpHeaders().get("HeaderKey")).contains("HeaderValue");
    }

    @Test
    public void useHttpMethodPostTest(){
        restService.setHttpMethod(HttpMethod.POST);
        restService.setHttpHeaders(createHttpHeaders());
        restService.sendRequest(REQUEST_URL);
        HttpEntity entity = new HttpEntity(createHttpHeaders());
        verify(restTemplate, times(1)).exchange(REQUEST_URL, HttpMethod.POST, entity, String.class);
    }

    @Test
    public void useBodyTest(){
        restService.setHttpHeaders(createHttpHeaders());
        restService.setBody("a body");
        restService.sendRequest(REQUEST_URL);
        HttpEntity<String> entity = new HttpEntity<>("a body", createHttpHeaders());
        verify(restTemplate, times(1)).exchange(REQUEST_URL, HttpMethod.GET, entity, String.class);
    }
}
