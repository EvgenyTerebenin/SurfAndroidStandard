package ru.surfstudio.android.rx.downloader.sample.ui.screen.main

import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.dagger.scope.PerScreen
import javax.inject.Inject

@PerScreen
class MainPresenter @Inject constructor(basePresenterDependency: BasePresenterDependency)
    : BasePresenter<MainActivityView>(basePresenterDependency) {

    override fun onLoad(viewRecreated: Boolean) {
        super.onLoad(viewRecreated)
    }


}
