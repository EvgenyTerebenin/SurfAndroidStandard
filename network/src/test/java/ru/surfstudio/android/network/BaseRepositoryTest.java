package ru.surfstudio.android.network;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.verification.VerificationModeFactory;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import io.reactivex.Observable;
import ru.surfstudio.android.core.app.interactor.common.DataPriority;
import ru.surfstudio.android.core.app.log.Logger;
import ru.surfstudio.android.network.connection.ConnectionQualityProvider;
import ru.surfstudio.android.network.error.CacheEmptyException;
import ru.surfstudio.android.network.error.NotModifiedException;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;
import static ru.surfstudio.android.network.BaseRepositoryTest.Response.CACHE;
import static ru.surfstudio.android.network.BaseRepositoryTest.Response.SERVER;

/**
 * unit tests for {@link BaseRepository}
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(Logger.class)
public class BaseRepositoryTest {
    @Mock
    ConnectionQualityProvider qualityProvider;

    private BaseRepository repository;
    private Observable<Response> cacheRequest;
    private Observable<Response> networkRequest;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        PowerMockito.mockStatic(Logger.class);

        networkRequest = Observable.just(SERVER);
        cacheRequest = Observable.just(CACHE);
        repository = new BaseRepository(qualityProvider);

        doReturn(true).when(qualityProvider).isConnectedFast();
    }

    @Test
    public void testFromCache() throws Exception {
        repository.hybridQuery(DataPriority.CACHE, cacheRequest, integer -> networkRequest)
                .test()
                .assertValues(CACHE, SERVER);
    }

    @Test
    public void testFromServer() {
        repository.hybridQuery(DataPriority.SERVER, cacheRequest, integer -> networkRequest)
                .test().assertValues(SERVER);
    }

    @Test
    public void testNonActualFastConnection() throws Exception {
        networkRequest = Observable.error(new NotModifiedException(new IllegalArgumentException(), 300, "http://ya.ru"));
        repository.hybridQuery(DataPriority.ONLY_ACTUAL, cacheRequest, integer -> networkRequest)
                .test()
                .assertValues(CACHE);
    }

    @Test
    public void testActualFastConnection() throws Exception {
        repository.hybridQuery(DataPriority.ONLY_ACTUAL, cacheRequest, integer -> networkRequest)
                .test()
                .assertValues(SERVER);
    }

    @Test
    public void testActualSlowConnection() throws Exception {
        doReturn(false).when(qualityProvider).isConnectedFast();
        repository.hybridQuery(DataPriority.ONLY_ACTUAL, cacheRequest, integer -> networkRequest)
                .test()
                .assertValues(SERVER);
    }

    @Test
    public void testAutoFastConnection() {
        repository.hybridQuery(DataPriority.AUTO, cacheRequest, integer -> networkRequest)
                .test()
                .assertValues(SERVER);
    }

    @Test
    public void testAutoSlowConnection() {
        doReturn(false).when(qualityProvider).isConnectedFast();
        repository.hybridQuery(DataPriority.AUTO, cacheRequest, integer -> networkRequest)
                .test()
                .assertValues(CACHE, SERVER);
    }

    @Test
    public void testException() {
        cacheRequest = Observable.error(new TestCacheException());
        networkRequest = Observable.error(new TestServerException());
        repository.hybridQuery(DataPriority.AUTO, cacheRequest, integer -> networkRequest)
                .test()
                .assertError(new TestServerException());
    }

    @Test
    public void testCacheException() {
        cacheRequest = Observable.error(new TestCacheException());
        repository.hybridQuery(DataPriority.AUTO, cacheRequest, integer -> networkRequest)
                .test()
                .assertNoErrors() // ошибка проглатывается, но логгируется
                .assertValues(SERVER);
        PowerMockito.verifyStatic(VerificationModeFactory.times(1));
        Logger.e((Throwable) anyObject(), anyString());
    }

    @Test
    public void testEmptyCacheException() {
        cacheRequest = Observable.error(new CacheEmptyException());
        repository.hybridQuery(DataPriority.AUTO, cacheRequest, integer -> networkRequest)
                .test()
                .assertValues(SERVER);
    }

    enum Response {
        SERVER, CACHE
    }

    private class TestServerException extends Exception {
        @Override
        public boolean equals(Object obj) {
            return obj instanceof TestServerException;
        }
    }

    private class TestCacheException extends Exception {
        @Override
        public boolean equals(Object obj) {
            return obj instanceof TestCacheException;
        }
    }

}