package info.novatec.metricscollector.github;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import info.novatec.metricscollector.commons.exception.UserDeniedException;
import info.novatec.metricscollector.github.metrics.GithubMetric;
import info.novatec.metricscollector.github.metrics.GithubMetricAbstract;
import info.novatec.metricscollector.github.metrics.GithubMetricDummy;
import info.novatec.metricscollector.github.metrics.GithubMetricImpl;
import info.novatec.metricscollector.github.util.DataProvider;


@RunWith(SpringRunner.class)
public class GithubSchedulerTest {

    @MockBean
    private GithubCollector collector;

    @MockBean
    private GithubRepository repository;

    private GithubScheduler scheduler;

    @Before
    public void init(){
        List<String> urls = new ArrayList<>();
        urls.add(DataProvider.VALID_GITHUB_URL+1);
        urls.add(DataProvider.VALID_GITHUB_URL+2);
        scheduler = new GithubScheduler(collector, repository);
        scheduler.setUrls(urls);
    }

    @Test
    public void checkIfCollectorAndRepositoryAreInvokedWithUrlsTest() {
        mockApplicationContext(scheduler);
        scheduler.updateAllGithubProjectsMetrics(GithubMetricDummy.class);
        verify(collector, times(2)).collect(any(GithubMetricAbstract.class));
        verify(collector, times(2)).collect(any(GithubMetricAbstract.class));
    }

    @Test
    public void checkIfCollectionIsIgnoredWithNonAuthorizedUserTest(){
        mockApplicationContext(scheduler);
        doThrow(UserDeniedException.class).when(collector).collect(any(GithubMetricImpl.class));
        scheduler.updateAllGithubProjectsMetrics(GithubMetricDummy.class);
        verify(repository, times(0)).saveMetrics(any(GithubMetricsResult.class));
        verify(collector, times(1)).collect(any(GithubMetricImpl.class));
    }

    @Test
    public void checkIfCollectionIsInvokedBySchedulerMethodTest(){
        scheduler = spy(scheduler);
        doNothing().when(scheduler).updateAllGithubProjectsMetrics(GithubMetric.class);
        scheduler.scheduleUpdateAllGithubProjectsMetrics();
        verify(scheduler).updateAllGithubProjectsMetrics(GithubMetric.class);
    }

    @SuppressWarnings("unchecked")
    private void mockApplicationContext(GithubScheduler scheduler){
        ApplicationContext mockedContext = mock(ApplicationContext.class);
        scheduler.setApplicationContext(mockedContext);
        Map<String, GithubMetricImpl> mockedBeanMap = mock(Map.class);
        GithubMetricImpl mockedBean = mock(GithubMetricImpl.class);
        List<GithubMetricImpl> beanList = new ArrayList<>();
        beanList.add(mockedBean);
        doReturn(mockedBeanMap).when(mockedContext).getBeansOfType(GithubMetricDummy.class);
        doReturn(beanList).when(mockedBeanMap).values();
    }

}
