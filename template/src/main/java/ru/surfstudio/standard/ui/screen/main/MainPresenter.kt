package ru.surfstudio.standard.ui.screen.main

import ru.surfstudio.android.core.ui.base.screen.presenter.BasePresenter
import ru.surfstudio.android.core.ui.base.screen.presenter.BasePresenterDependency
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.ktx.extensions.number.isBetween
import ru.surfstudio.android.logger.Logger
import javax.inject.Inject

/**
 * Презентер главного экрана
 */
@PerScreen
internal class MainPresenter @Inject constructor(basePresenterDependency: BasePresenterDependency) : BasePresenter<MainActivityView>(basePresenterDependency) {

    private val screenModel: MainScreenModel = MainScreenModel()

    override fun onLoad(viewRecreated: Boolean) {
        super.onLoad(viewRecreated)
        view.render(screenModel)
    }
}