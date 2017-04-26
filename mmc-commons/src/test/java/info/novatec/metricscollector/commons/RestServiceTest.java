package info.novatec.metricscollector.commons;

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

    private static final String REQUEST_URL = "aURL";

    @MockBean
    RestTemplate restTemplate;

    private RestService restService;

    @Before
    public void init(){
        this.restService = new RestService(restTemplate);
    }

    @Test
    public void checkIfExchangeMethodIsInvokedTest() {
        restService.sendRequest(REQUEST_URL);
        HttpEntity entity = new HttpEntity(null);
        verify(restTemplate, times(1)).exchange(REQUEST_URL, HttpMethod.GET, entity, String.class);
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
}
