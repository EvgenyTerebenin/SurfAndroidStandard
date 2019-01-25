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

package ru.surfstudio.android.core.mvp.rx.relation.mvp

import io.reactivex.Observable
import io.reactivex.functions.Consumer
import ru.surfstudio.android.core.mvp.rx.relation.Relation
import java.util.concurrent.atomic.AtomicReference

/**
 * Связь View -> Presenter
 *      Presenter -> View
 *
 * Хранит в себе последнее прошедшее значение.
 * При подписке сообщает это значение или initialValue
 */
class Bond<T>() : Relation<T, StateSource, StateTarget>() {

    constructor(initialValue: T) : this() {
        this.initialValue = initialValue
    }

    private var initialValue: T? = null

    val hasValue: Boolean get() = cachedValue.get() != null

    private val action = initialValue?.let { Action(it) } ?: Action()
    private val command = initialValue?.let { State(it) } ?: State()

    private val cachedValue = AtomicReference<T>()
    val value: T get() = cachedValue.get()

    override fun getConsumer(source: StateSource): Consumer<T> =
            when (source) {
                is VIEW -> action.getConsumer(source)
                is PRESENTER -> command.getConsumer(source)
                else -> throw IllegalArgumentException("Illegal relationEntity $source")
            }

    override fun getObservable(target: StateTarget): Observable<T> =
            when (target) {
                is PRESENTER -> action.getObservable(target)
                is VIEW -> command.getObservable(target)
                else -> throw IllegalArgumentException("Illegal relationEntity $target")
            }
                    .doOnNext(cachedValue::set)
}