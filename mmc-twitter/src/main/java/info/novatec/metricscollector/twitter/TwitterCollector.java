package info.novatec.metricscollector.twitter;

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
import org.springframework.stereotype.Component;

import lombok.Setter;
import twitter4j.Twitter;
import twitter4j.TwitterException;

import info.novatec.metricscollector.twitter.metrics.TwitterMetric;


@Component
public class TwitterCollector implements ApplicationContextAware{

    @Setter
    private ApplicationContext applicationContext;

    private Twitter twitter;

    private TwitterMetricsResult metrics;

    @Autowired
    public TwitterCollector(TwitterMetricsResult metrics, Twitter twitter) {
        this.metrics = metrics;
        this.twitter = twitter;
    }

    TwitterMetricsResult collect(String atUserName, String userName) throws TwitterException {
        metrics.setUserName(userName);
        metrics.setAtUserName(atUserName);

        for (String beanName : getMetricsBeanName()) {
            TwitterMetric metric = ( TwitterMetric ) applicationContext.getBean(beanName, twitter, metrics);
            metric.collect();
        }
        return metrics;
    }

    private List<String> getMetricsBeanName() {
        BeanDefinitionRegistry beanRegistry = new SimpleBeanDefinitionRegistry();
        ClassPathBeanDefinitionScanner beanScanner = new ClassPathBeanDefinitionScanner(beanRegistry);

        TypeFilter typeFilter = new AssignableTypeFilter(TwitterMetric.class);
        beanScanner.setIncludeAnnotationConfig(false);
        beanScanner.addIncludeFilter(typeFilter);
        beanScanner.scan(TwitterMetric.class.getPackage().getName());
        return Arrays.asList(beanRegistry.getBeanDefinitionNames());
    }

}
