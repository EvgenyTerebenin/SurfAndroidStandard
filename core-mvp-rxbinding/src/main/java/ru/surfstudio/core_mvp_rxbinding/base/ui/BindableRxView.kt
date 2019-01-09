package ru.surfstudio.core_mvp_rxbinding.base.ui

import android.widget.EditText
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import ru.surfstudio.core_mvp_rxbinding.base.domain.Action
import ru.surfstudio.core_mvp_rxbinding.base.domain.Command
import ru.surfstudio.core_mvp_rxbinding.base.domain.TextStateManager
import ru.surfstudio.core_mvp_rxbinding.base.domain.State

interface BindableRxView<M : RxModel> {

    fun bind(sm: M)

    fun getDisposable(): CompositeDisposable

    fun Disposable.removeOnDestroy() = getDisposable().add(this)

    infix fun <T> Observable<T>.bindTo(consumer: Consumer<in T>) =
            this.observeOn(AndroidSchedulers.mainThread())
                    .subscribe(consumer)
                    .removeOnDestroy()


    infix fun <T> Observable<T>.bindTo(consumer: (T) -> Unit) =
            this.observeOn(AndroidSchedulers.mainThread())
                    .subscribe(consumer)
                    .removeOnDestroy()

    infix fun <T> State<T>.bindTo(consumer: Consumer<in T>) =
            this.observable.bindTo(consumer)

    infix fun <T> State<T>.bindTo(consumer: (T) -> Unit) =
            this.observable.bindTo(consumer)

    infix fun <T> Command<T>.bindTo(consumer: Consumer<in T>) =
            this.observable.bindTo(consumer)

    infix fun <T> Command<T>.bindTo(consumer: (T) -> Unit) =
            this.observable.bindTo(consumer)

    infix fun <T> Observable<T>.bindTo(action: Action<T>) =
            this.bindTo(action.consumer)

    infix fun TextStateManager.bindTo(editText: EditText) =
            bind(editText, getDisposable())
}