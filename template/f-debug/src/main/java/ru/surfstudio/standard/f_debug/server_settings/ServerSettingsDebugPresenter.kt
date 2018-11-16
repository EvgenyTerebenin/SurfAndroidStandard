package ru.surfstudio.standard.f_debug.server_settings

import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.core.ui.navigation.activity.navigator.ActivityNavigator
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.standard.f_debug.server_settings.reboot.RebootDebugActivityRoute
import ru.surfstudio.standard.f_debug.server_settings.storage.DebugServerSettingsStorage
import javax.inject.Inject

/**
 * Презентер экрана настроек сервера
 */
@PerScreen
class ServerSettingsDebugPresenter @Inject constructor(
        basePresenterDependency: BasePresenterDependency,
        private val debugServerSettingsStorage: DebugServerSettingsStorage,
        private val activityNavigator: ActivityNavigator
) : BasePresenter<ServerSettingsDebugActivityView>(basePresenterDependency) {

    private val sm: ServerSettingsDebugScreenModel =
            ServerSettingsDebugScreenModel(debugServerSettingsStorage.isChuckEnabled)

    override fun onLoad(viewRecreated: Boolean) {
        super.onLoad(viewRecreated)
        view.render(sm)
    }

    fun setChuckEnabled(value: Boolean) {
        debugServerSettingsStorage.isChuckEnabled = value
        activityNavigator.start(RebootDebugActivityRoute())
        activityNavigator.finishAffinity()
    }
}