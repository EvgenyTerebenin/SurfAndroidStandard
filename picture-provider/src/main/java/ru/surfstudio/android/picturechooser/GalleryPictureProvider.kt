/*
  Copyright (c) 2018-present, SurfStudio LLC.

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */
package ru.surfstudio.android.picturechooser

import android.app.Activity
import android.content.Context
import android.content.Intent
import io.reactivex.Observable
import ru.surfstudio.android.core.ui.navigation.activity.navigator.ActivityNavigator
import ru.surfstudio.android.core.ui.navigation.activity.route.ActivityWithResultRoute

/**
 * Позволяет получить одно или несколько изображений через сторонее приложение
 */
internal class GalleryPictureProvider(
        private val activityNavigator: ActivityNavigator,
        private val activity: Activity
) {

    //region Функции для выбора одного изображения из галереи
    fun openGalleryForSingleImage(): Observable<String> {
        val route = GallerySingleImageRoute()
        val result = observeSingleScreenResult(activityNavigator, route)
        activityNavigator.startForResult(route)
        return result
    }

    fun openGalleryForSingleImageUri(): Observable<String> {
        val route = GallerySingleImageUriRoute()
        val result = observeSingleScreenResult(activityNavigator, route)
        activityNavigator.startForResult(route)
        return result
    }

    fun openGalleryForSingleImageUriWrapper(): Observable<UriWrapper> {
        val route = GallerySingleImageUriWrapperRoute()
        val result = observeSingleScreenResult(activityNavigator, route)
        activityNavigator.startForResult(route)
        return result
    }
    //endregion

    //region Функции для выбора нескольких изображений из галереи
    fun openGalleryForMultipleImage(): Observable<List<String>> {
        val route = GalleryMultipleImageRoute()
        val result = observeMultipleScreenResult(activityNavigator, route)
        activityNavigator.startForResult(route)
        return result
    }

    fun openGalleryForMultipleImageUri(): Observable<List<String>> {
        val route = GalleryMultipleImageUriRoute()
        val result = observeMultipleScreenResult(activityNavigator, route)
        activityNavigator.startForResult(route)
        return result
    }

    fun openGalleryForMultipleImageUriWrapper(): Observable<List<UriWrapper>> {
        val route = GalleryMultipleImageUriWrapperRoute()
        val result = observeMultipleScreenResult(activityNavigator, route)
        activityNavigator.startForResult(route)
        return result
    }
    //endregion

    //region Маршруты для выбора одного изображения из галереи
    /**
     * Маршрут, возвращащий путь к изображению
     */
    private inner class GallerySingleImageRoute : ActivityWithResultRoute<String>() {

        override fun prepareIntent(context: Context?) = getIntentForSingleImageFromGallery()

        override fun parseResultIntent(intent: Intent?): String? {
            return parseSingleResultIntent(intent) { it.getRealPath(activity) }
        }
    }

    /**
     * Маршрут, возвращающий Uri изображения, преобразованный в String
     */
    private inner class GallerySingleImageUriRoute : ActivityWithResultRoute<String>() {

        override fun prepareIntent(context: Context?) = getIntentForSingleImageFromGallery()

        override fun parseResultIntent(intent: Intent?): String? {
            return parseSingleResultIntent(intent) { it.toString() }
        }
    }

    /**
     * Маршрут, возвращающий класс-обертку над Uri изображения
     */
    private inner class GallerySingleImageUriWrapperRoute : ActivityWithResultRoute<UriWrapper>() {

        override fun prepareIntent(context: Context?) = getIntentForSingleImageFromGallery()

        override fun parseResultIntent(intent: Intent?): UriWrapper? {
            return parseSingleResultIntent(intent) { UriWrapper(it) }
        }
    }
    //endregion

    //region Маршруты для выбора нескольких изображений из галереи
    /**
     * Маршрут, возвращающий список путей к выбранным изображениям
     */
    private inner class GalleryMultipleImageRoute : ActivityWithResultRoute<ArrayList<String>>() {

        override fun prepareIntent(context: Context?) = getIntentForMultipleImageFromGallery()

        override fun parseResultIntent(intent: Intent?): ArrayList<String>? {
            return parseMultipleResultIntent(intent) { it.getRealPaths(activity) }
        }
    }

    /**
     * Маршрут, возвращающий список Uri выбранных изображений, преобразованных в String
     */
    private inner class GalleryMultipleImageUriRoute : ActivityWithResultRoute<ArrayList<String>>() {

        override fun prepareIntent(context: Context?) = getIntentForMultipleImageFromGallery()

        override fun parseResultIntent(intent: Intent?): ArrayList<String>? {
            return parseMultipleResultIntent(intent) { it.toStringArrayList() }
        }
    }

    /**
     * Маршрут, возвращающий список элементов типа класса-обертки над Uri выбранных изображений
     */
    private inner class GalleryMultipleImageUriWrapperRoute : ActivityWithResultRoute<ArrayList<UriWrapper>>() {

        override fun prepareIntent(context: Context?) = getIntentForMultipleImageFromGallery()

        override fun parseResultIntent(intent: Intent?): ArrayList<UriWrapper>? {
            return parseMultipleResultIntent(intent) { it.toUriWrapperList() }
        }
    }
    //endregion
}
