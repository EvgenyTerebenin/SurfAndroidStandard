package ru.surfstudio.standard.app_injector.ui.error

import dagger.Module
import dagger.Provides
import ru.surfstudio.android.core.mvp.error.ErrorHandler
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.standard.base_ui.error.StandardErrorHandler

@Module
class ErrorHandlerModule {

    @Provides
    @PerScreen
    fun provideNetworkErrorHandler(standardErrorHandler: StandardErrorHandler): ErrorHandler {
        return standardErrorHandler
    }
}