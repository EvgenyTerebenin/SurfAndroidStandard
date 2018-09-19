package ru.surfstudio.android.filestorage.sample.ui.base.dagger.activity

import dagger.Component
import ru.surfstudio.android.dagger.scope.PerActivity
import ru.surfstudio.android.filestorage.sample.app.dagger.CustomAppComponent
import ru.surfstudio.android.filestorage.sample.interactor.ip.IpRepository
import ru.surfstudio.android.filestorage.sample.interactor.ip.cache.IpJsonStorage
import ru.surfstudio.android.filestorage.sample.interactor.ip.cache.IpSerializableStorage
import ru.surfstudio.android.sample.dagger.ui.base.dagger.activity.DefaultActivityComponent
import ru.surfstudio.android.sample.dagger.ui.base.dagger.activity.DefaultActivityModule

/**
 * Компонент для @PerActivity скоупа
 */

@PerActivity
@Component(dependencies = [(CustomAppComponent::class)],
        modules = [(DefaultActivityModule::class)])
interface CustomActivityComponent : DefaultActivityComponent {
    fun ipSerializableStorage(): IpSerializableStorage
    fun ipJsonStorage(): IpJsonStorage
    fun ipRepository(): IpRepository
}