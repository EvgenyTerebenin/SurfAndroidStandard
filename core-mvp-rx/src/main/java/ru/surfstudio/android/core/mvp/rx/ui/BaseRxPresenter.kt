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

package ru.surfstudio.android.core.mvp.rx.ui

import androidx.annotation.CallSuper
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import ru.surfstudio.android.core.mvp.model.ScreenModel
import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.core.mvp.rx.relation.Related
import ru.surfstudio.android.core.mvp.rx.relation.mvp.PRESENTER
import ru.surfstudio.android.core.mvp.view.CoreView
import ru.surfstudio.android.rx.extension.ConsumerSafe

abstract class BaseRxPresenter<M, V>(
        basePresenterDependency: BasePresenterDependency
) : BasePresenter<V>(basePresenterDependency), Related<PRESENTER>
        where M : ScreenModel,
              V : CoreView,
              V : BindableRxView<M> {

    override fun relationEntity() = PRESENTER

    abstract val pm: M

    @CallSuper
    override fun onFirstLoad() {
        view.bind(pm)
    }

    @CallSuper
    override fun onLoad(viewRecreated: Boolean) {
        if (viewRecreated) view.bind(pm)
    }

    override fun <T> subscribe(observable: Observable<T>,
                               onNext: Consumer<T>,
                               onError: (Throwable) -> Unit): Disposable =
            super.subscribe(observable, { onNext.accept(it) }, { onError(it) })

    fun <T> Observable<T>.subscribeIoHandleError(onNextConsumer: Consumer<T>, onError: Consumer<Throwable>? = null) {
        subscribeIoHandleError(this
                .doOnError {
                    handleError(it)
                    onError?.accept(it)
                }
                .retry(),
                onNextConsumer::accept
        )
    }

    protected fun <T> Observable<T>.subscribeIoHandleError(onNextConsumer: (T) -> Unit, onError: Consumer<Throwable>? = null) {
        subscribeIoHandleError(Consumer { onNextConsumer(it) }, onError)
    }

    protected fun <T> Observable<T>.subscribeIoHandleError(onNextConsumer: (T) -> Unit, onError: ((Throwable) -> Unit)? = null) {
        subscribeIoHandleError(Consumer { onNextConsumer(it) }, onError)
    }

    protected fun <T> Observable<T>.subscribeIoHandleError(onNextConsumer: Consumer<T>, onError: ((Throwable) -> Unit)? = null) {
        val errorConsumer: ConsumerSafe<Throwable>? = if (onError != null) ConsumerSafe { onError(it) } else null
        subscribeIoHandleError(onNextConsumer, errorConsumer)
    }
}