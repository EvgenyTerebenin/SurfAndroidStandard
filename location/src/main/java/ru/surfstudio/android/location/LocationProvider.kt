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
import com.google.android.gms.location.*
import ru.surfstudio.android.location.domain.LocationPriority
import ru.surfstudio.android.location.exceptions.NoLocationPermissionException
import ru.surfstudio.android.location.exceptions.PlayServicesAreNotAvailableException
import ru.surfstudio.android.location.exceptions.ResolutionFailedException
import ru.surfstudio.android.location.location_errors_resolver.LocationErrorsResolver
import ru.surfstudio.android.location.location_errors_resolver.resolutions.LocationErrorResolution
import ru.surfstudio.android.location.location_errors_resolver.resolutions.impl.concrete.no_location_permission.NoLocationPermissionResolution
import ru.surfstudio.android.location.location_errors_resolver.resolutions.impl.concrete.play_services_are_not_available.PlayServicesAreNotAvailableResolution
import ru.surfstudio.android.location.location_errors_resolver.resolutions.impl.concrete.resolveble_api_exception.ResolvableApiExceptionResolution

/**
 * Поставщик местоположения.
 */
internal class LocationProvider(private val context: Context) {

    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    private val locationAvailability = LocationAvailability(context)

    /**
     * Проверить возможность получения местоположения.
     *
     * @param priority приоритет при получении местоположения.
     *
     * @param onResultAction метод обратного вызова, в который передается [List], содержащий исключения, связанные с
     * невозможностью получения местоположения. Если список пуст - значит есть возможность получить местоположение.
     * Возможные исключения:
     * - [NoLocationPermissionException];
     * - [PlayServicesAreNotAvailableException];
     * - [ResolvableApiException].
     */
    fun checkLocationAvailability(priority: LocationPriority, onResultAction: (List<Exception>) -> Unit) =
            locationAvailability.checkLocationAvailability(priority, onResultAction)

    /**
     * Решить проблемы связанные с невозможностью получения местоположения.
     *
     * @param priority приоритет при получении местоположения.
     *
     * @param resolutions [Array], содержащий решения проблем связанных с невозможностью получения местоположения.
     * Доступные решения:
     * - [NoLocationPermissionResolution];
     * - [PlayServicesAreNotAvailableResolution];
     * - [ResolvableApiExceptionResolution].
     *
     * @param onFinishAction метод, вызываемый при завершении решения проблем.
     *
     * @param onFailureAction метод вызываемый в случае, если попытка решения проблем не удалась.
     */
    fun resolveLocationAvailability(
            priority: LocationPriority,
            resolutions: List<LocationErrorResolution<*>>,
            onFinishAction: (unresolvedExceptions: List<Exception>) -> Unit,
            onFailureAction: (ResolutionFailedException) -> Unit
    ) {
        checkLocationAvailability(priority, onResultAction = { exceptions ->
            LocationErrorsResolver.resolve(
                    exceptions,
                    resolutions,
                    onFinishAction,
                    onFailureAction)
        })
    }

    /**
     * Запросить последнее известное местоположение.
     *
     * @param onSuccessAction метод, вызываемый в случае удачного получения местоположения.
     *
     * @param onFailureAction метод, вызываемый в случае ошибки при получении местоположения, в который передается
     * [List], содержащий исключения, связанные с невозможностью получения местоположения. Если список пуст - значит
     * есть возможность получить местоположение.
     *  Может содержать следующие исключения:
     * - [CompositeException], содержащий список из возможных исключений:
     * [NoLocationPermissionException], [PlayServicesAreNotAvailableException], [ResolvableApiException];
     * - [ResolutionFailedException], если передавались экземпляры решений и попытка решения не удалась.
     *
     * @param resolutions [Array], содержащий решения проблем связанных с невозможностью получения местоположения.
     * Доступные решения:
     * - [NoLocationPermissionResolution];
     * - [PlayServicesAreNotAvailableResolution];
     * - [ResolvableApiExceptionResolution].
     */
    @RequiresPermission(
            anyOf = ["android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"]
    )
    fun requestLastKnownLocationWithErrorResolution(
            onSuccessAction: (Location?) -> Unit,
            onFailureAction: (List<Exception>) -> Unit,
            resolutions: List<LocationErrorResolution<*>>
    ) {
        requestLastKnownLocation(
                onSuccessAction,
                onFailureAction = { exceptions ->
                    resolveLocationErrors(
                            exceptions,
                            resolutions,
                            onFinishAction = { requestLastKnownLocation(onSuccessAction, onFailureAction) },
                            onFailureAction = onFailureAction
                    )
                }
        )
    }

    /**
     * Подписаться на получение обновлений местоположения.
     *
     * @param intervalMillis интервал в миллисекундах, при котором предпочтительно получать обновления местоположения.
     * Тем не менее, обновления местоположения могут быть чаще, чем этот интервал, если другое приложение получает
     * обновления с меньшим интервалом. Или, наоборот, реже (например, если у устройства нет возможности подключения).
     *
     * @param fastestIntervalMillis максимальный интервал в миллисекундах, при котором возможно обрабатывать обновления
     * местоположения. Следует устанавливать этот параметр, потому что другие приложения также влияют на скорость
     * отправки обновлений. Google Play Services отправляют обновления с максимальной скоростью, которую запросило любое
     * приложение. Если этот показатель быстрее, чем может обрабатывать приложение, можно столкнуться с соответствующими
     * проблемами.
     *
     * @param priority Приоритет запроса (точность метостоположения/заряд батареи), который дает Google Play Services
     * знать, какие источники данных использовать.
     *
     * @param onLocationUpdateAction метод, вызываемый при очередном получении обновления местоположения.
     *
     * @param onFailureAction метод, вызываемый в случае ошибки при получении местоположения, в который передается
     * [List], содержащий исключения, связанные с невозможностью получения местоположения. Если список пуст - значит
     * есть возможность получить местоположение.
     *  Может содержать следующие исключения:
     * - [CompositeException], содержащий список из возможных исключений:
     * [NoLocationPermissionException], [PlayServicesAreNotAvailableException],[ResolvableApiException];
     * - [ResolutionFailedException], если передавались экземпляры решений и попытка решения не удалась.
     *
     * @param resolutions [Array], содержащий решения проблем связанных с невозможностью получения местоположения.
     * Доступные решения:
     * - [NoLocationPermissionResolution];
     * - [PlayServicesAreNotAvailableResolution];
     * - [ResolvableApiExceptionResolution].
     *
     * @return подписка на обновления местоположения.
     */
    fun requestLocationUpdatesWithErrorResolution(
            intervalMillis: Long?,
            fastestIntervalMillis: Long?,
            priority: LocationPriority?,
            onLocationUpdateAction: (Location?) -> Unit,
            onFailureAction: (List<Exception>) -> Unit,
            resolutions: List<LocationErrorResolution<*>>
    ): LocationUpdatesSubscription? {
        return requestLocationUpdates(
                intervalMillis,
                fastestIntervalMillis,
                priority,
                onLocationUpdateAction,
                onFailureAction = { exceptions ->
                    resolveLocationErrors(
                            exceptions,
                            resolutions,
                            onFinishAction = {
                                requestLocationUpdates(
                                        intervalMillis,
                                        fastestIntervalMillis,
                                        priority,
                                        onLocationUpdateAction,
                                        onFailureAction)
                            },
                            onFailureAction = onFailureAction
                    )
                }
        )
    }

    /**
     * Отписаться от получения обновлений местоположения.
     *
     * @param locationUpdatesSubscription подписка по получение обновлений местоположения.
     */
    fun removeLocationUpdates(locationUpdatesSubscription: LocationUpdatesSubscription) {
        fusedLocationClient.removeLocationUpdates(locationUpdatesSubscription.locationCallback)
    }

    @RequiresPermission(
            anyOf = ["android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"]
    )
    private fun requestLastKnownLocation(
            onSuccessAction: (Location?) -> Unit,
            onFailureAction: (List<Exception>) -> Unit
    ) {
        locationAvailability.checkLocationAvailability(
                null,
                onResultAction = { exceptions ->
                    if (exceptions.isNotEmpty()) {
                        onFailureAction(exceptions)
                        return@checkLocationAvailability
                    }

                    fusedLocationClient
                            .lastLocation
                            .addOnSuccessListener { location -> onSuccessAction(location) }
                            .addOnFailureListener { exception -> onFailureAction(listOf(exception)) }
                }
        )
    }

    @RequiresPermission(
            anyOf = ["android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"]
    )
    private fun requestLocationUpdates(
            intervalMillis: Long?,
            fastestIntervalMillis: Long?,
            priority: LocationPriority?,
            onLocationUpdateAction: (Location?) -> Unit,
            onFailureAction: (List<Exception>) -> Unit
    ): LocationUpdatesSubscription? {
        val locationCallback = createLocationCallback(onLocationUpdateAction)

        locationAvailability.checkLocationAvailability(
                priority,
                onResultAction = { exceptions ->
                    if (exceptions.isNotEmpty()) {
                        onFailureAction(exceptions)
                        return@checkLocationAvailability
                    }

                    val locationRequest =
                            LocationUtil.createLocationRequest(intervalMillis, fastestIntervalMillis, priority)

                    fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
                }
        )

        return LocationUpdatesSubscription(locationCallback)
    }

    private fun createLocationCallback(onLocationUpdateAction: (Location?) -> Unit): LocationCallback =
            object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult?) {
                    locationResult ?: return
                    onLocationUpdateAction(locationResult.lastLocation)
                }
            }

    private fun resolveLocationErrors(
            exceptions: List<Exception>,
            resolutions: List<LocationErrorResolution<*>>,
            onFinishAction: () -> Unit,
            onFailureAction: (List<Exception>) -> Unit
    ) {
        LocationErrorsResolver.resolve(
                exceptions,
                resolutions,
                onFinishAction = { unresolvedExceptions ->
                    if (unresolvedExceptions.isEmpty()) {
                        onFinishAction()
                    } else {
                        onFailureAction(unresolvedExceptions)
                    }
                },
                onFailureAction = { resolvingException ->
                    onFailureAction(listOf(resolvingException))
                }
        )
    }
}