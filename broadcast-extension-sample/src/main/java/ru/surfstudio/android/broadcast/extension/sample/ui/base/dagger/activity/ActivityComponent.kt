package ru.surfstudio.android.broadcast.extension.sample.ui.base.dagger.activity

import android.content.Context
import dagger.Component
import ru.surfstudio.android.broadcast.extension.sample.app.dagger.AppComponent
import ru.surfstudio.android.broadcast.extension.sample.interactor.SmsBroadcastReceiver
import ru.surfstudio.android.connection.ConnectionProvider
import ru.surfstudio.android.core.app.StringsProvider
import ru.surfstudio.android.core.ui.provider.ActivityProvider
import ru.surfstudio.android.core.ui.scope.ActivityPersistentScope
import ru.surfstudio.android.dagger.scope.PerActivity
import ru.surfstudio.android.rx.extension.scheduler.SchedulersProvider
import ru.surfstudio.android.rxbus.RxBus

/**
 * Компонент для @PerActivity скоупа
 */

@PerActivity
@Component(dependencies = [(AppComponent::class)],
        modules = [(ActivityModule::class)])
interface ActivityComponent {
    fun schedulerProvider(): SchedulersProvider
    fun connectionProvider(): ConnectionProvider
    fun stringsProvider(): StringsProvider
    fun smsBroadcastReceiver(): SmsBroadcastReceiver

    fun activityProvider(): ActivityProvider
    fun activityPersistentScope(): ActivityPersistentScope
    fun context(): Context
    fun rxBus(): RxBus
}