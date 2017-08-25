package info.novatec.metricscollector.commons.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;


@Data
@Slf4j
@Component
public class RestService {

    private final RestTemplate restTemplate;
    private HttpHeaders httpHeaders;
    private HttpMethod httpMethod;
    private Object body;

    @Autowired
    public RestService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public RestService prepareRequest() {
        httpMethod = HttpMethod.GET;
        httpHeaders = new HttpHeaders();
        return this;
    }

    public RestService setHttpMethod(HttpMethod httpMethod){
        this.httpMethod = httpMethod;
        return this;
    }

    public RestService addHttpHeader(String headerName, String headerValue) {
        httpHeaders.add(headerName, headerValue);
        return this;
    }

    public RestService setBody(Object body) {
        this.body = body;
        return this;
    }

    public ResponseEntity<String> sendRequest(String url) {
        HttpEntity entity = body == null ? new HttpEntity<>(httpHeaders) : new HttpEntity<>(body, httpHeaders);
        return restTemplate.exchange(url, httpMethod, entity, String.class);
    }

}
