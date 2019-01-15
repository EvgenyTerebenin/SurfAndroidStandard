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

import ru.surfstudio.android.core.mvp.rx.ui.BaseRxActivityView
import ru.surfstudio.sample.R
import javax.inject.Inject

/**
 * Экран демострирующий возможность работы со связными данными типа чекбоксов
 */
class CycledActivityView : BaseRxActivityView<CycledScreenModel>() {

    @Inject
    lateinit var presenter: CycledPresenter

    override fun bind(sm: CycledScreenModel) {

    }

    override fun createConfigurator() = CycledScreenConfigurator(intent)

    override fun getScreenName(): String = "CycledActivityView"

    override fun getContentView(): Int = R.layout.activity_cycled

    override fun getPresenters() = arrayOf(presenter)

}
