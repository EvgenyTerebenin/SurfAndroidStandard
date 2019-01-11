package ru.surfstudio.android.core.mvp.rx.ui

import androidx.annotation.CallSuper
import io.reactivex.Observable
import io.reactivex.functions.Consumer
import ru.surfstudio.android.core.mvp.loadstate.LoadStateInterface
import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.core.mvp.rx.domain.*
import ru.surfstudio.android.core.mvp.rx.domain.Target
import ru.surfstudio.android.core.mvp.view.CoreView
import ru.surfstudio.android.rx.extension.ConsumerSafe
import ru.surfstudio.android.core.mvp.rx.ui.lds.LdsRxModel

abstract class BaseRxPresenter<M, V>(
        basePresenterDependency: BasePresenterDependency
) : BasePresenter<V>(basePresenterDependency), Related<PRESENTER, VIEW>
        where M : RxModel,
              V : CoreView,
              V : BindableRxView<M> {

    override fun source() = PRESENTER

    override fun target() = VIEW

    abstract fun getRxModel(): M

    @CallSuper
    override fun onFirstLoad() {
        view.bind(getRxModel())
    }

    @CallSuper
    override fun onLoad(viewRecreated: Boolean) {
        if (viewRecreated) view.bind(getRxModel())
    }

    protected fun applyLoadState(new: LoadStateInterface) {
        val sm = getRxModel()
        if (sm is LdsRxModel) sm.loadState.accept(new)
    }

    protected fun <T> Observable<T>.applyLoadState(new: LoadStateInterface) = map {
        this@BaseRxPresenter.applyLoadState(new)
        it
    }

    protected fun <T> Observable<T>.applyLoadState(loadStateFromDataAction: (T) -> LoadStateInterface) = map {
        this@BaseRxPresenter.applyLoadState(loadStateFromDataAction(it))
        it
    }

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

    protected fun <T> Action<T>.subscribe(consumer: Consumer<T>) {
        subscribe(this.getTargetObservable(PRESENTER), consumer as ConsumerSafe<T>)
    }

    protected fun <T> Action<T>.subscribe(consumer: (T) -> Unit) {
        subscribe(this.getTargetObservable(PRESENTER), consumer)
    }

    fun <T> StateB<T>.a() =
            this.getTargetObservable(PRESENTER)
}