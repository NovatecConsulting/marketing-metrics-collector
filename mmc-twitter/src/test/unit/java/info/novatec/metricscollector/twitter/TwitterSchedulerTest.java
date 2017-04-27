package info.novatec.metricscollector.twitter;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import twitter4j.TwitterException;

import info.novatec.metricscollector.twitter.metrics.TwitterMetric;
import info.novatec.metricscollector.twitter.metrics.TwitterMetricAbstract;
import info.novatec.metricscollector.twitter.metrics.TwitterMetricDummy;
import info.novatec.metricscollector.twitter.metrics.TwitterMetricImpl;
import info.novatec.metricscollector.twitter.util.DataProvider;


@RunWith(SpringRunner.class)
public class TwitterSchedulerTest {

    @MockBean
    private TwitterCollector collector;

    @MockBean
    private TwitterRepository repository;

    private TwitterScheduler scheduler;

    @Before
    public void init() {
        Map<String, String> users = new HashMap<>();
        users.put(DataProvider.AT_USERNAME + "1", DataProvider.USERNAME + "1");
        users.put(DataProvider.AT_USERNAME + "2", DataProvider.USERNAME + "2");
        scheduler = new TwitterScheduler(collector, repository, users);
    }

    @Test
    public void checkIfCollectorAndRepositoryAreInvokedWithUsersTest() throws TwitterException {
        mockApplicationContext(scheduler);
        scheduler.updateAllTwitterUsersMetrics(TwitterMetricDummy.class);
        verify(collector, times(2)).collect(any(TwitterMetricAbstract.class));
        verify(collector, times(2)).collect(any(TwitterMetricAbstract.class));
    }

    @Test
    public void checkIfCollectionContinuesWhenTwitterExceptionIsThrownTest() throws TwitterException {
        mockApplicationContext(scheduler);
        doThrow(TwitterException.class).when(collector).collect(any(TwitterMetricImpl.class));
        scheduler.updateAllTwitterUsersMetrics(TwitterMetricDummy.class);
        verify(collector, times(2)).collect(any(TwitterMetricImpl.class));
    }

    @Test
    public void checkIfCollectionIsInvokedBySchedulerMethodTest() {
        scheduler = spy(scheduler);
        doNothing().when(scheduler).updateAllTwitterUsersMetrics(TwitterMetric.class);
        scheduler.scheduleUpdateAllTwitterUsersMetrics();
        verify(scheduler).updateAllTwitterUsersMetrics(TwitterMetric.class);
    }

    private void mockApplicationContext(TwitterScheduler scheduler) {
        ApplicationContext mockedContext = mock(ApplicationContext.class);
        scheduler.setApplicationContext(mockedContext);
        Map<String, TwitterMetricImpl> mockedBeanMap = mock(Map.class);
        TwitterMetricImpl mockedBean = mock(TwitterMetricImpl.class);
        List<TwitterMetricImpl> beanList = new ArrayList<>();
        beanList.add(mockedBean);
        doReturn(mockedBeanMap).when(mockedContext).getBeansOfType(TwitterMetricDummy.class);
        doReturn(beanList).when(mockedBeanMap).values();
    }
}
