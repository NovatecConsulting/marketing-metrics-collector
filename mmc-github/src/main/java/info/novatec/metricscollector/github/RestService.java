package info.novatec.metricscollector.github;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Setter
@Component
public class RestService {

    private RestTemplate restTemplate;

    @Getter
    private HttpHeaders httpHeaders;

    private String token;

    @Autowired
    public RestService(RestTemplate restTemplate, String token){
        this.token = token;
        this.restTemplate = restTemplate;
        setDefaultHttpHeaders();
    }

    public ResponseEntity<String> sendRequest(String url){
        HttpEntity entity = new HttpEntity(httpHeaders);
        return restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
    }

    public void setDefaultHttpHeaders() {
        if (token == null || token.equals("")) {
            log.error("Token '" + token + "' is not a valid Token. Please specify in properties.");
            token = "";
        }
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "token " + token);
        this.httpHeaders = httpHeaders;
    }

    public void setToken(String token){
        this.token = token;
        setDefaultHttpHeaders();
    }
}
