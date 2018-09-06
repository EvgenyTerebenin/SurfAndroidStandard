/*
 * Copyright 2016 Valeriy Shtaits.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ru.surfstudio.android.location

import android.content.Context
import android.location.Location
import android.support.annotation.RequiresPermission
import io.reactivex.Completable
import io.reactivex.CompletableEmitter
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.exceptions.CompositeException
import ru.surfstudio.android.location.domain.LocationPriority
import ru.surfstudio.android.location.location_errors_resolver.resolutions.LocationErrorResolution

/**
 * Сервис для работы с местоположением (Rx обёртка над [LocationProvider]).
 */
class LocationService(context: Context) {

    private val locationProvider = LocationProvider(context)

    /**
     * Запросить последнее известное местоположение.
     *
     * @param resolutions [Array], содержащий решения проблем связанных с невозможностью получения местоположения.
     */
    @RequiresPermission(
            anyOf = ["android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"]
    )
    fun observeLastKnownLocation(
            vararg resolutions: LocationErrorResolution<*> = emptyArray()
    ): Maybe<Location> =
            Maybe.create { maybeEmitter ->
                locationProvider.requestLastKnownLocationWithErrorResolution(
                        onSuccessAction = { location ->
                            if (location != null) {
                                maybeEmitter.onSuccess(location)
                            } else {
                                maybeEmitter.onComplete()
                            }
                        },
                        onFailureAction = { exceptions -> maybeEmitter.onError(CompositeException(exceptions)) },
                        resolutions = resolutions.toList()
                )
            }

    /**
     * Подписаться на получение обновлений местоположения.
     *
     * @param intervalMillis интервал в миллисекундах, при котором предпочтительно получать обновления местоположения.
     * Тем не менее, обновления местоположения могут быть чаще, чем этот интервал, если другое приложение получает
     * обновления с меньшим интервалом. Или, наоборот, реже (например, если у устройства нет возможности подключения).
     * @param fastestIntervalMillis максимальный интервал в миллисекундах, при котором возможно обрабатывать обновления
     * местоположения. Следует устанавливать этот параметр, потому что другие приложения также влияют на скорость
     * отправки обновлений. Google Play Services отправляют обновления с максимальной скоростью, которую запросило любое
     * приложение. Если этот показатель быстрее, чем может обрабатывать приложение, можно столкнуться с соответствующими
     * проблемами.
     * @param priority приоритет запроса (точность метостоположения/заряд батареи), который дает Google Play Services
     * знать, какие источники данных использовать.
     * @param resolutions [Array], содержащий решения проблем связанных с невозможностью получения местоположения.
     */
    @RequiresPermission(
            anyOf = ["android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"]
    )
    fun observeLocationUpdates(
            intervalMillis: Long? = null,
            fastestIntervalMillis: Long? = null,
            priority: LocationPriority? = null,
            vararg resolutions: LocationErrorResolution<*> = emptyArray()
    ): Observable<Location> {
        var locationUpdatesSubscription: LocationUpdatesSubscription? = null

        return Observable.create<Location> { observableEmitter ->
            locationUpdatesSubscription = locationProvider.requestLocationUpdatesWithErrorResolution(
                    intervalMillis,
                    fastestIntervalMillis,
                    priority,
                    onLocationUpdateAction = { location ->
                        if (location != null) {
                            observableEmitter.onNext(location)
                        }
                    },
                    onFailureAction = { exceptions -> observableEmitter.onError(CompositeException(exceptions)) },
                    resolutions = resolutions.toList()
            )
        }.doOnDispose { locationProvider.removeLocationUpdates(locationUpdatesSubscription ?: return@doOnDispose) }
    }

    /**
     * Проверить возможность получения местоположения.
     *
     * @param priority приоритет при получении местоположения.
     *
     * @return [Completable], вызывающий onComplete, если есть возможность получить местоположение, или onError,
     * содержащий [CompositeException] с исключениями, связанными с невозможностью получения местоположения.
     */
    fun checkLocationAvailability(priority: LocationPriority): Completable =
            Completable.create { completableEmitter: CompletableEmitter ->
                locationProvider.checkLocationAvailability(
                        priority,
                        onResultAction = { exceptions ->
                            if (exceptions.isEmpty()) {
                                completableEmitter.onComplete()
                            } else {
                                completableEmitter.onError(CompositeException(exceptions))
                            }
                        }
                )
            }
}