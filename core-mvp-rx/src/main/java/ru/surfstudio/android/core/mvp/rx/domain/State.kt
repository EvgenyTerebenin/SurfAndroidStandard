package ru.surfstudio.android.core.mvp.rx.domain

import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Observable
import io.reactivex.functions.Consumer

/**
 * Rx-обертка над командами для View
 *
 * За отправку событий отвечает Presenter
 * Подписывается на события View
 */
class State<T> : Relation<T, PRESENTER, VIEW> {

    private val relay = BehaviorRelay.create<T>()

    override val hasValue: Boolean get() = relay.hasValue()

    override val value: T get() = relay.value?: throw NoSuchElementException()

    override fun getConsumer(source: PRESENTER): Consumer<T> = relay

    override fun getObservable(target: VIEW): Observable<T> = relay
}