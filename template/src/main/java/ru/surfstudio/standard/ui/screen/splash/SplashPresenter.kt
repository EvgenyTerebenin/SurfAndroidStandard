package ru.surfstudio.standard.ui.screen.splash


import ru.surfstudio.android.core.app.dagger.scope.PerScreen
import ru.surfstudio.android.core.domain.Unit
import ru.surfstudio.android.core.ui.base.navigation.activity.navigator.ActivityNavigator
import ru.surfstudio.android.core.ui.base.navigation.activity.route.ActivityRoute
import ru.surfstudio.android.core.ui.base.screen.presenter.BasePresenter
import ru.surfstudio.android.core.ui.base.screen.presenter.BasePresenterDependency
import ru.surfstudio.standard.app.intialization.InitializeAppInteractor
import ru.surfstudio.standard.ui.screen.main.MainActivityRoute
import rx.Observable
import java.util.concurrent.TimeUnit
import javax.inject.Inject


/**
 * Презентер для сплэш экрана.
 */
@PerScreen
internal class SplashPresenter @Inject
constructor(private val activityNavigator: ActivityNavigator,
            private val initializeAppInteractor: InitializeAppInteractor,
            basePresenterDependency: BasePresenterDependency,
            private val route: SplashRoute) : BasePresenter<SplashActivityView>(basePresenterDependency) {

    private val nextRoute: ActivityRoute
        get() {
            return MainActivityRoute()
        }

    override fun onLoad(viewRecreated: Boolean) {
        super.onLoad(viewRecreated)
        if (!viewRecreated) {

            val delay = Observable.just(Unit.INSTANCE).delay(TRANSITION_DELAY_MS, TimeUnit.MILLISECONDS)
            val work = Observable.just(Unit.INSTANCE).delay(0, TimeUnit.MILLISECONDS) // полезная работа
            subscribeIoHandleError(delay.zipWith(work, { _, t2 -> t2 }), { activityNavigator.start(nextRoute) })
        }
    }

    companion object {
        /**
         * Минимальное время в миллисекундах в течении которого показывается сплэш
         */
        private val TRANSITION_DELAY_MS = 2000L
    }
}
