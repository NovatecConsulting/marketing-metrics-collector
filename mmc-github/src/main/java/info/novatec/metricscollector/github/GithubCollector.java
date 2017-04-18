package info.novatec.metricscollector.github;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.SimpleBeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import info.novatec.metricscollector.commons.RestService;
import info.novatec.metricscollector.commons.exception.UserDeniedException;
import info.novatec.metricscollector.github.metrics.GithubMetric;


@Slf4j
@Component
class GithubCollector implements ApplicationContextAware{

    public static final String BASE_URL = "https://api.github.com/repos/";

    private GithubMetricsResult metrics;

    private RestService restService;

    @Setter
    private String token;

    @Setter
    private ApplicationContext applicationContext;

    @Autowired
    public GithubCollector(GithubMetricsResult metrics, RestService restService) {
        this.metrics = metrics;
        this.restService = restService;
    }

    GithubMetricsResult collect(String githubUrl) {

        String projectName = githubUrl.substring("https://github.com/".length());
        setHeadersForGithub();
        metrics.setRepositoryName(projectName.split("/")[1]);

        try {
            for (String beanName : getMetricsBeanName()) {
                GithubMetric metric = ( GithubMetric ) applicationContext.getBean(beanName, restService, metrics);
                metric.setProjectName(projectName);
                metric.collect();
            }
        } catch (HttpClientErrorException e) {
            throw new UserDeniedException("No authorized user logged in! Please add a valid oauth token to the properties.");
        }

        return metrics;
    }

    private void setHeadersForGithub() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "token " + token);
        restService.setHeaders(httpHeaders);
    }

    private List<String> getMetricsBeanName() {
        BeanDefinitionRegistry beanRegistry = new SimpleBeanDefinitionRegistry();
        ClassPathBeanDefinitionScanner beanScanner = new ClassPathBeanDefinitionScanner(beanRegistry);

        TypeFilter typeFilter = new AssignableTypeFilter(GithubMetric.class);
        beanScanner.setIncludeAnnotationConfig(false);
        beanScanner.addIncludeFilter(typeFilter);
        beanScanner.scan(GithubMetric.class.getPackage().getName());
        return Arrays.asList(beanRegistry.getBeanDefinitionNames());
    }

}
