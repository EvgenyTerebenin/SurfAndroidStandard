package ru.surfstudio.android.filestorage.sample.interactor.ip

import io.reactivex.Observable
import ru.surfstudio.android.connection.ConnectionProvider
import ru.surfstudio.android.dagger.scope.PerApplication
import ru.surfstudio.android.filestorage.sample.domain.ip.Ip
import ru.surfstudio.android.filestorage.sample.interactor.ip.cache.IpStorage
import ru.surfstudio.android.filestorage.sample.interactor.ip.network.IpApi
import ru.surfstudio.android.network.BaseNetworkInteractor
import ru.surfstudio.android.network.TransformUtil
import javax.inject.Inject

/**
 * Репозиторий для получения ip
 */
@PerApplication
class IpRepository @Inject constructor(connectionQualityProvider: ConnectionProvider,
                                       private val ipApi: IpApi,
                                       private val ipStorage: IpStorage
) : BaseNetworkInteractor(connectionQualityProvider) {

    fun getIp(): Observable<Ip> {
        return ipApi.getIp()
                .map { ipResponse -> TransformUtil.transform(ipResponse) }
    }

    fun getIpFromCache(): Ip? = ipStorage.ip

    /**
     * Кэширование ip
     */
    fun saveIp(ip: Ip) {
        ipStorage.ip = ip
    }

    /**
     * Очистка кэша
     */
    fun clearIpStorage() = ipStorage.clear()
}