package ru.surfstudio.standard.app.dagger

import android.content.Context
import dagger.Component
import ru.surfstudio.android.connection.ConnectionProvider
import ru.surfstudio.android.core.app.ActiveActivityHolder
import ru.surfstudio.android.core.app.dagger.CoreAppModule
import ru.surfstudio.android.dagger.scope.PerApplication
import ru.surfstudio.android.rx.extension.scheduler.SchedulerModule
import ru.surfstudio.android.rx.extension.scheduler.SchedulersProvider
import ru.surfstudio.android.shared.pref.SharedPrefModule
import ru.surfstudio.standard.app.intialization.InitializeAppInteractor
import ru.surfstudio.standard.app.intialization.migration.MigrationModule
import ru.surfstudio.standard.interactor.analytics.AnalyticsModule
import ru.surfstudio.standard.interactor.analytics.AnalyticsService
import ru.surfstudio.standard.interactor.auth.AuthModule
import ru.surfstudio.standard.interactor.auth.SessionChangedInteractor
import ru.surfstudio.standard.interactor.common.network.NetworkModule
import ru.surfstudio.standard.interactor.common.network.OkHttpModule

@PerApplication
@Component(modules = [
CoreAppModule::class,
MigrationModule::class,
SharedPrefModule::class,
SchedulerModule::class,
AuthModule::class,
NetworkModule::class,
OkHttpModule::class,
AnalyticsModule::class])
interface AppComponent {
    fun initializeAppInteractor(): InitializeAppInteractor
    fun context(): Context
    fun activeActivityHolder(): ActiveActivityHolder
    fun connectionProvider(): ConnectionProvider
    fun sessionChangeInteractor(): SessionChangedInteractor
    fun analyticsService(): AnalyticsService
    fun schedulerProvider(): SchedulersProvider
}
