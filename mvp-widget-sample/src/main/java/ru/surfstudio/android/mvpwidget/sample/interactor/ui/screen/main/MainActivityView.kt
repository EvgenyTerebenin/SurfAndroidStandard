package ru.surfstudio.android.mvpwidget.sample.interactor.ui.screen.main

import android.graphics.Color
import android.os.Bundle
import android.os.PersistableBundle
import androidx.annotation.IdRes
import kotlinx.android.synthetic.main.activity_main.*
import ru.surfstudio.android.core.mvp.activity.BaseRenderableActivityView
import ru.surfstudio.android.core.mvp.presenter.CorePresenter
import ru.surfstudio.android.mvpwidget.sample.R
import ru.surfstudio.android.mvpwidget.sample.interactor.ui.screen.main.widget.constraint.ConstraintWidgetView
import ru.surfstudio.android.mvpwidget.sample.interactor.ui.screen.main.widget.frame.FrameWidgetView
import ru.surfstudio.android.mvpwidget.sample.interactor.ui.screen.main.widget.linear.LinearWidgetView
import ru.surfstudio.android.mvpwidget.sample.interactor.ui.screen.main.widget.relative.RelativeWidgetView
import ru.surfstudio.android.sample.dagger.ui.base.configurator.DefaultActivityScreenConfigurator
import javax.inject.Inject

/**
 * Вью главного экрана
 */
class MainActivityView : BaseRenderableActivityView<MainScreenModel>() {
    @Inject
    internal lateinit var presenter: MainPresenter

    @IdRes
    override fun getContentView(): Int = R.layout.activity_main

    override fun renderInternal(sm: MainScreenModel) {}

    override fun getScreenName(): String = "main"

    override fun getPresenters(): Array<CorePresenter<*>> = arrayOf(presenter)

    override fun createConfigurator(): DefaultActivityScreenConfigurator = MainScreenConfigurator(intent)

    override fun onCreate(savedInstanceState: Bundle?,
                          persistentState: PersistableBundle?,
                          viewRecreated: Boolean) {
        super.onCreate(savedInstanceState, persistentState, viewRecreated)

        val constraintWidgetView = ConstraintWidgetView(this)
        val frame = FrameWidgetView(this)
                .apply {
                    setBackgroundColor(Color.MAGENTA)
                }
        val linear = LinearWidgetView(this)
                .apply {
                    setBackgroundColor(Color.YELLOW)
                }
        val relative = RelativeWidgetView(this)
                .apply {
                    setBackgroundColor(Color.GREEN)
                }

        constraintWidgetView.render("First place")
        constraintWidgetView.setBackgroundColor(Color.CYAN)
        content.addView(constraintWidgetView)

        content.addView(frame)
        content.addView(linear)
        content.addView(relative)

        btn.setOnClickListener { presenter.openListScreen() }
    }
}
