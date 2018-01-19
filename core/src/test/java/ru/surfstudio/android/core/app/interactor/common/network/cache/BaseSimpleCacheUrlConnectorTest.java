package ru.surfstudio.android.core.app.interactor.common.network.cache;

import android.text.TextUtils;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import okhttp3.HttpUrl;
import ru.surfstudio.android.core.app.interactor.common.network.HttpMethods;
import ru.surfstudio.android.core.domain.network.url.BaseUrl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;

/**
 *
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest(TextUtils.class)
public class BaseSimpleCacheUrlConnectorTest {

    private final BaseUrl baseUrl = new BaseUrl("http://ya.ru/", "v2");
    private SimpleCacheInfo yaSci = new SimpleCacheInfo(HttpMethods.GET, "me/fu", "ya_test", 1);
    private SimpleCacheInfo paramPath = new SimpleCacheInfo(HttpMethods.GET, "me/{param_one}/like", "param_test", 1);
    private SimpleCacheInfo paramVal = new SimpleCacheInfo(HttpMethods.GET, "me/job&ginger=1", "param_test", 1);
    private BaseSimpleCacheUrlConnector connector = new BaseSimpleCacheUrlConnector(baseUrl) {
        @Override
        Collection<SimpleCacheInfo> getSimpleCacheInfo() {
            Set<SimpleCacheInfo> set = new HashSet<>();
            set.add(yaSci);
            set.add(paramPath);
            set.add(paramVal);
            return set;
        }
    };

    @Before
    public void setup() {
        PowerMockito.mockStatic(TextUtils.class);
        PowerMockito.when(TextUtils.isEmpty(any(CharSequence.class)))
                .thenAnswer(invocation -> {
                    CharSequence a = (CharSequence) invocation.getArguments()[0];
                    return !(a != null && a.length() > 0);
                });
    }

    @Test
    public void getSimpleCacheInfo() throws Exception {
        assertFalse(connector.getSimpleCacheInfo().isEmpty());
    }

    @Test
    public void getByUrl() throws Exception {
        assertEquals(connector.getByUrl(HttpUrl.parse("http://ya.ru/v2/me/fu"), HttpMethods.GET), yaSci);
        assertNull(connector.getByUrl(HttpUrl.parse("http://ya.ru/v2/me/ful"), HttpMethods.GET));

        assertEquals(connector.getByUrl(HttpUrl.parse("http://ya.ru/v2/me/123/like"), HttpMethods.GET), paramPath);
        assertEquals(connector.getByUrl(HttpUrl.parse("http://ya.ru/v2/me/321/like"), HttpMethods.GET), paramPath);

        assertEquals(connector.getByUrl(HttpUrl.parse("http://ya.ru/v2/me/job&ginger=1"), HttpMethods.GET), paramVal);
        assertNull(connector.getByUrl(HttpUrl.parse("http://ya.ru/v2/me/job&ginger=2"), HttpMethods.GET));

        assertEquals(connector.getByUrl(HttpUrl.parse("http://ya.ru/v3/me/fu"), HttpMethods.GET), yaSci); //api version template
    }

}