package info.novatec.metricscollector.github;

import static org.assertj.core.api.Assertions.assertThat;

import javax.annotation.PostConstruct;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;


@RunWith(SpringRunner.class)
public class GithubApplicationInitializerTest {

    private static final String TOKEN = "aToken";

    GithubApplicationInitializer initializer;

    @PostConstruct
    public void init(){
        initializer = new GithubApplicationInitializer();
    }

    @Test
    public void createToken(){
        initializer.setToken(TOKEN);
        String token = initializer.token();
        assertThat(token).isEqualTo(TOKEN);
    }

    @Test
    public void createRestTemplate(){
        RestTemplate influxService = initializer.restTemplate();
        assertThat(influxService).isNotNull();
    }
}
