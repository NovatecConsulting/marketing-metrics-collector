package info.novatec.metricscollector.commons;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class RestRequester {

    private RestTemplate restTemplate;

    private HttpHeaders httpHeaders;

    @Autowired
    public RestRequester(RestTemplate restTemplate){
        this.restTemplate = restTemplate;
        setDefaultHeaders();
    }

    private void setDefaultHeaders(){
        this.httpHeaders = new HttpHeaders();
    }

    public void setHeaders(HttpHeaders httpHeaders){
        this.httpHeaders = httpHeaders;
    }

    public ResponseEntity<String> sendRequest(String url){
        HttpEntity entity = new HttpEntity(httpHeaders);
        return restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
    }

}
