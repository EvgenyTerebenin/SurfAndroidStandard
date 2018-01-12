package ru.surfstudio.standard.app.dagger

import android.content.Context
import android.content.SharedPreferences
import dagger.Component
import ru.surfstudio.android.core.app.SharedPrefModule
import ru.surfstudio.android.core.app.dagger.scope.PerApplication
import ru.surfstudio.android.core.app.intialization.InitializationModule
import ru.surfstudio.android.core.app.intialization.launch.AppLaunchConfigurationStorage
import ru.surfstudio.android.core.app.intialization.migration.AppMigrationManager
import ru.surfstudio.android.core.app.intialization.migration.AppMigrationStorage
import ru.surfstudio.android.core.app.scheduler.SchedulersProvider
import ru.surfstudio.android.core.util.ActiveActivityHolder
import ru.surfstudio.standard.app.intialization.InitializeAppInteractor
import ru.surfstudio.standard.app.intialization.migration.MigrationModule
import javax.inject.Named

@PerApplication
@Component(modules = [
(AppModule::class),
(MigrationModule::class),
(ActiveActivityHolderModule::class)])
interface AppComponent {
    fun initializeAppInteractor(): InitializeAppInteractor
    fun appMigrationManager(): AppMigrationManager
    fun appLaunchConfigurationStorage(): AppLaunchConfigurationStorage
    fun context(): Context
    fun appMigrationStorage(): AppMigrationStorage
    fun schedulerProvider(): SchedulersProvider
    fun activeActivityHolder(): ActiveActivityHolder
    @Named(SharedPrefModule.NO_BACKUP_SHARED_PREF)
    fun noBackupSharedPreferences(): SharedPreferences

    @Named(InitializationModule.VERSION_CODE_PARAM)
    fun versionCode(): Int

}
