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

package ru.surfstudio.android.core.mvp.rx.sample.cycled

import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.core.mvp.rx.sample.cycled.domen.Origin
import ru.surfstudio.android.core.mvp.rx.sample.cycled.interactor.NomenInteractor
import ru.surfstudio.android.core.mvp.rx.ui.BaseRxPresenter
import ru.surfstudio.android.dagger.scope.PerScreen
import javax.inject.Inject

@PerScreen
class CycledPresenter @Inject constructor(
        basePresenterDependency: BasePresenterDependency,
        private val nomenInteractor: NomenInteractor
) : BaseRxPresenter<CycledScreenModel, CycledActivityView>(basePresenterDependency) {

    private val model = CycledScreenModel()
    override fun getRxModel() = model

    override fun onFirstLoad() {
        super.onFirstLoad()

        with(model) {
            Observable.combineLatest<String, Origin, Pair<String, Origin>>(
                    baseOfNomen.getObservable(),
                    origin.getObservable(), BiFunction { t1, t2 -> t1 to t2 }) bindTo { nomenInteractor.composeNomen(it.first, it.second) bindTo nomen }

            Observable.combineLatest<String, Origin, Pair<String, Origin>>(
                    nomen.getObservable(),
                    origin.getObservable(), BiFunction { t1, t2 -> t1 to t2 }) bindTo { nomenInteractor.extractBaseOfNomen(it.first, it.second) bindTo baseOfNomen }

            nomen bindTo { nomenInteractor.detectOrgin(it) bindTo origin }
        }
    }
}