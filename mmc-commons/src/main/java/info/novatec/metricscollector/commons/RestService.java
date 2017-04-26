package info.novatec.metricscollector.commons;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import lombok.Setter;


@Component
public class RestService {

    private RestTemplate restTemplate;

    @Setter
    private HttpHeaders httpHeaders;

    @Autowired
    public RestService(RestTemplate restTemplate){
        this.restTemplate = restTemplate;
        setDefaultHttpHeaders();
    }

    private void setDefaultHttpHeaders(){
        this.httpHeaders = new HttpHeaders();
    }

    public ResponseEntity<String> sendRequest(String url){
        HttpEntity entity = new HttpEntity(httpHeaders);
        return restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
    }

}
