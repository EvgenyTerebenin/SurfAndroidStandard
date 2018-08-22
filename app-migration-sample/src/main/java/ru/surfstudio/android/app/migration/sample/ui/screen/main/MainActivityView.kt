package ru.surfstudio.android.app.migration.sample.ui.screen.main

import android.support.annotation.IdRes
import ru.surfstudio.android.app.migration.sample.R
import ru.surfstudio.android.app.migration.sample.ui.base.configurator.ActivityScreenConfigurator
import ru.surfstudio.android.core.mvp.activity.BaseRenderableActivityView
import ru.surfstudio.android.core.mvp.presenter.CorePresenter
import javax.inject.Inject

/**
 * Вью главного экрана
 */
class MainActivityView : BaseRenderableActivityView<MainScreenModel>() {
    @Inject
    internal lateinit var presenter: MainPresenter

    @IdRes
    override fun getContentView(): Int = R.layout.activity_main

    override fun renderInternal(screenModel: MainScreenModel) {}

    override fun getPresenters(): Array<CorePresenter<*>> = arrayOf(presenter)

    override fun createConfigurator(): ActivityScreenConfigurator = MainScreenConfigurator(intent)

    override fun getScreenName(): String  = "MainActivity"
}
