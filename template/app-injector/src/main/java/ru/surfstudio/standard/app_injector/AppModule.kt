package ru.surfstudio.standard.app_injector

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import ru.surfstudio.android.connection.ConnectionProvider
import ru.surfstudio.android.activity.holder.ActiveActivityHolder
import ru.surfstudio.standard.base.app.StringsProvider
import ru.surfstudio.android.core.ui.navigation.activity.navigator.GlobalNavigator
import ru.surfstudio.android.dagger.scope.PerApplication
import ru.surfstudio.android.rx.extension.scheduler.SchedulersProvider
import ru.surfstudio.android.rx.extension.scheduler.SchedulersProviderImpl

@Module
class AppModule(
        private val app: Application,
        private val activeActivityHolder: ru.surfstudio.android.activity.holder.ActiveActivityHolder
) {

    @Provides
    @PerApplication
    internal fun provideActiveActivityHolder(): ru.surfstudio.android.activity.holder.ActiveActivityHolder = activeActivityHolder

    @Provides
    @PerApplication
    internal fun provideContext(): Context = app

    @Provides
    @PerApplication
    internal fun provideApp(): Application = app

    @Provides
    @PerApplication
    internal fun provideStringsProvider(context: Context): StringsProvider = StringsProvider(context)

    @Provides
    @PerApplication
    internal fun provideGlobalNavigator(
            context: Context,
            activityHolder: ru.surfstudio.android.activity.holder.ActiveActivityHolder
    ): GlobalNavigator {
        return GlobalNavigator(context, activityHolder)
    }

    @Provides
    @PerApplication
    internal fun provideSchedulerProvider(): SchedulersProvider = SchedulersProviderImpl()

    @Provides
    @PerApplication
    internal fun provideConnectionQualityProvider(context: Context): ConnectionProvider {
        return ConnectionProvider(context)
    }
}