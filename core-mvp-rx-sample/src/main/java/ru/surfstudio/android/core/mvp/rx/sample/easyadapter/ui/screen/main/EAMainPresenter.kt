/*
 * Copyright (c) 2019-present, SurfStudio LLC.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.surfstudio.android.core.mvp.rx.sample.easyadapter.ui.screen.main

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.core.mvp.rx.sample.easyadapter.interactor.data.MainModelRepository
import ru.surfstudio.android.core.mvp.rx.sample.easyadapter.ui.screen.pagination.PaginationScreenRoute
import ru.surfstudio.android.core.mvp.rx.ui.BaseRxPresenter
import ru.surfstudio.android.core.ui.navigation.activity.navigator.ActivityNavigator
import ru.surfstudio.android.dagger.scope.PerScreen
import java.util.concurrent.TimeUnit
import javax.inject.Inject

private const val INTERVAL_MS: Long = 2500

/**
 * Презентер [EAMainActivityView]
 */
@PerScreen
class EAMainPresenter @Inject constructor(basePresenterDependency: BasePresenterDependency,
                                          private val activityNavigator: ActivityNavigator)
    : BaseRxPresenter<MainViewBinding, EAMainActivityView>(basePresenterDependency) {

    private val screenModelFactory = MainModelRepository()

    override val vb = MainViewBinding(screenModelFactory.next()) // передаем изначальное состояние, может приходжить из роута, интерактора etc

    override fun onLoad(viewRecreated: Boolean) {
        super.onLoad(viewRecreated)
        if (!viewRecreated) {
            val timeEmitter =
                    Observable.interval(INTERVAL_MS, TimeUnit.MILLISECONDS)
                            .observeOn(AndroidSchedulers.mainThread())
                            .map { screenModelFactory.next() }
                            .share()


            timeEmitter.map { it.bottomCarousel } bindTo vb.bottomCarouselState
            timeEmitter.map { it.carousels } bindTo vb.carouselState
            timeEmitter.map { it.elements } bindTo vb.elementsState
            timeEmitter.map { it.hasCommercial } bindTo vb.hasCommercialState
        }

        vb.openPaginationScreen bindTo { activityNavigator.start(PaginationScreenRoute()) }
    }
}
