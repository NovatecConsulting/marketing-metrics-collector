package info.novatec.metricscollector.commons;

import static org.assertj.core.api.Assertions.assertThat;

import javax.annotation.PostConstruct;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;


@RunWith(SpringRunner.class)
public class ApplicationInitializerCommonsTest {

    ApplicationInitializerCommons initializer;

    @PostConstruct
    public void init(){
        initializer = new ApplicationInitializerCommons();
    }

    @Test
    public void createInfluxService(){
        InfluxService influxService = initializer.influxService();
        assertThat(influxService).isInstanceOf(InfluxService.class);
    }

    @Test
    public void createRestTemplate(){
        RestTemplate influxService = initializer.restTemplate();
        assertThat(influxService).isInstanceOf(RestTemplate.class);
    }
}
