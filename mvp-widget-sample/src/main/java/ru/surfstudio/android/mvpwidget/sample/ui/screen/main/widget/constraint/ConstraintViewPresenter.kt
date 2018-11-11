package ru.surfstudio.android.mvpwidget.sample.ui.screen.main.widget.constraint

import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.dagger.scope.PerScreen
import javax.inject.Inject

/**
 * Просто пример презентера для виджета
 * */
@PerScreen
class ConstraintViewPresenter @Inject constructor(basePresenterDependency: BasePresenterDependency
) : BasePresenter<ConstraintWidgetView>(basePresenterDependency)