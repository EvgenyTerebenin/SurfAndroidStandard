package ru.surfstudio.android.mvp.dialog.sample.ui.screen.main

import android.os.Bundle
import android.os.PersistableBundle
import android.support.annotation.IdRes
import kotlinx.android.synthetic.main.activity_main.*

import ru.surfstudio.android.core.mvp.activity.BaseRenderableActivityView
import ru.surfstudio.android.core.mvp.presenter.CorePresenter
import ru.surfstudio.android.core.ui.bus.RxBus
import ru.surfstudio.android.message.MessageController
import ru.surfstudio.android.mvp.dialog.sample.R
import ru.surfstudio.android.mvp.dialog.sample.ui.base.configurator.ActivityScreenConfigurator
import javax.inject.Inject

/**
 * Вью главного экрана
 */
class MainActivityView : BaseRenderableActivityView<MainScreenModel>() {
    override fun getScreenName(): String  = "MainActivity"

    @Inject
    internal lateinit var presenter: MainPresenter

    @IdRes
    override fun getContentView(): Int {
        return R.layout.activity_main
    }

    override fun onCreate(savedInstanceState: Bundle?,
                          persistentState: PersistableBundle?,
                          viewRecreated: Boolean) {
        super.onCreate(savedInstanceState, persistentState, viewRecreated)
        show_simple_dialog_btn.setOnClickListener { presenter.showSimpleDialog() }
        show_simple_bottomsheet_dialog_btn.setOnClickListener { presenter.showSimpleBottomSheetDialog() }
        show_complex_dialog_btn.setOnClickListener { presenter.showComplexDialog() }
        show_complex_bottomsheet_dialog_btn.setOnClickListener { presenter.showComplexBottomSheetDialog() }
    }

    override fun renderInternal(screenModel: MainScreenModel) {}

    override fun getPresenters(): Array<CorePresenter<*>> {
        return arrayOf(presenter)
    }

    override fun createConfigurator(): ActivityScreenConfigurator {
        return MainScreenConfigurator(intent)
    }
}
