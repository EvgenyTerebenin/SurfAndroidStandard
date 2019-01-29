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

package ru.surfstudio.android.core.mvp.rx.sample

import com.jakewharton.rxbinding2.view.clicks
import com.jakewharton.rxbinding2.widget.textChanges
import kotlinx.android.synthetic.main.activity_main.*
import ru.surfstudio.android.core.mvp.rx.ui.BaseRxActivityView
import ru.surfstudio.sample.R
import javax.inject.Inject

/**
 * Главный экран примеров
 */
class MainActivityView : BaseRxActivityView<MainViewBinding>() {

    @Inject
    lateinit var presenter: MainPresenter

    override fun bind(vb: MainViewBinding) {
        vb.counterState.observable.map { it.toString() } bindTo main_counter_tv::setText
        vb.textEditState bindTo main_text_et::setText
        vb.sampleState bindTo text_tv::setText

        main_text_et.textChanges().map { it.toString() } bindTo vb.textEditState

        main_inc_btn.clicks() bindTo vb.incAction
        main_dec_btn.clicks() bindTo vb.decAction
        main_double_text_btn.clicks() bindTo vb.doubleTextAction

        checkbox_sample_btn.clicks() bindTo vb.checkboxSampleActivityOpen
        easy_adapter_sample_btn.clicks() bindTo vb.easyadapterSampleActivityOpen
    }

    override fun createConfigurator() = MainScreenConfigurator(intent)

    override fun getScreenName(): String = "MainActivityView"

    override fun getContentView(): Int = R.layout.activity_main

    override fun getPresenters() = arrayOf(presenter)

}
