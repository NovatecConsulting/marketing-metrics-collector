package info.novatec.metricscollector.github;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import info.novatec.metricscollector.commons.RestService;


@RunWith(SpringRunner.class)
public class ApplicationInitializerGithubTest {

    ApplicationInitializerGithub initializer;

    @PostConstruct
    public void init(){
        initializer = new ApplicationInitializerGithub();
    }

    @Test
    public void createGithubCollector(){
        GithubCollector githubCollector = initializer.githubCollector(mock(RestService.class));
        assertThat(githubCollector).isInstanceOf(GithubCollector.class);
    }

    @Test
    public void createUrls(){
        List<String> urls = new ArrayList<>();
        urls.add("url1");
        urls.add("url2");
        urls.add("url3");
        initializer.setUrls(urls);
        List<String> bean = initializer.urls();
        assertThat(bean.size()).isEqualTo(3);
    }
}
