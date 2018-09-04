package ru.surfstudio.android.broadcast.extension.sample.app.dagger

import dagger.Component
import ru.surfstudio.android.broadcast.extension.sample.interactor.SmsBroadcastReceiver
import ru.surfstudio.android.dagger.scope.PerApplication
import ru.surfstudio.android.sample.dagger.app.dagger.DefaultAppComponent
import ru.surfstudio.android.sample.dagger.app.dagger.DefaultAppModule

@PerApplication
@Component(modules = [DefaultAppModule::class])
interface CustomAppComponent : DefaultAppComponent {
    fun smsBroadcastReceiver(): SmsBroadcastReceiver
}