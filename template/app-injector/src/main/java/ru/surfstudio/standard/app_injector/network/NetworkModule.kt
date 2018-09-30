package ru.surfstudio.standard.app_injector.network

import android.app.DownloadManager
import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.surfstudio.android.converter.gson.ResponseTypeAdapterFactory
import ru.surfstudio.android.converter.gson.SafeConverterFactory
import ru.surfstudio.android.dagger.scope.PerApplication
import ru.surfstudio.android.network.BaseUrl
import ru.surfstudio.android.network.calladapter.BaseCallAdapterFactory
import ru.surfstudio.standard.base.network.CallAdapterFactory
import ru.surfstudio.standard.i_network.BASE_API_URL

@Module
class NetworkModule {

    @Provides
    @PerApplication
    internal fun provideRetrofit(
            okHttpClient: OkHttpClient,
            callAdapterFactory: BaseCallAdapterFactory,
            gson: Gson,
            apiUrl: BaseUrl
    ): Retrofit {
        return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(apiUrl.toString())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(callAdapterFactory)
                .build()
    }

    @Provides
    @PerApplication
    internal fun provideGson(): Gson {
        return GsonBuilder()
                .registerTypeAdapterFactory(ResponseTypeAdapterFactory(SafeConverterFactory()))
                .create()
    }

    @Provides
    @PerApplication
    internal fun provideDownloadManager(context: Context): DownloadManager {
        return context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
    }

    @Provides
    @PerApplication
    internal fun provideCallAdapterFactory(): BaseCallAdapterFactory = CallAdapterFactory()

    @Provides
    @PerApplication
    internal fun provideBaseUrl(): BaseUrl = BaseUrl(BASE_API_URL, null)
}