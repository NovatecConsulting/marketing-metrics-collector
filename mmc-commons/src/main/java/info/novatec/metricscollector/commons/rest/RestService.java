package info.novatec.metricscollector.commons.rest;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

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
    private Map<String, String> uriParameters;
    private Object body;

    @Autowired
    public RestService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public RestService prepareRequest() {
        httpMethod = HttpMethod.GET;
        httpHeaders = new HttpHeaders();
        uriParameters = new HashMap<>();
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

    public RestService addUriParameter(String key, String value){
        uriParameters.put(urlEncode(key), urlEncode(value));
        return this;
    }

    public ResponseEntity<String> sendRequest(String url) {
        HttpEntity entity = body == null ? new HttpEntity<>(httpHeaders) : new HttpEntity<>(body, httpHeaders);
        String fullUrl = url + "?" + uriParameters.entrySet().stream()
            .map( entry -> entry.getKey() + "=" + entry.getValue() )
            .collect(Collectors.joining("&"));
        log.info("Requesting '{}'", fullUrl);
        return restTemplate.exchange(fullUrl, httpMethod, entity, String.class);
    }

    public String urlEncode(String textToEncode) {
        try {
            return URLEncoder.encode(textToEncode, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            log.warn(e.getMessage());
            return textToEncode;
        }
    }

}
