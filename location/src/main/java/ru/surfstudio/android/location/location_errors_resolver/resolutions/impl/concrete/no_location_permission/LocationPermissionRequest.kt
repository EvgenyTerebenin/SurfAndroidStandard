package ru.surfstudio.android.location.location_errors_resolver.resolutions.impl.concrete.no_location_permission

import android.Manifest
import ru.surfstudio.android.core.ui.permission.PermissionRequest

/**
 * Запрос разрешения доступа к местоположению.
 */
class LocationPermissionRequest : PermissionRequest() {

    override fun getPermissions() = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    )
}