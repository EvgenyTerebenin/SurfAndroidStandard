package ru.surfstudio.standard.ui.base.dagger

import dagger.Module
import ru.surfstudio.android.core.ui.base.dagger.CoreFragmentScreenModule
import ru.surfstudio.standard.ui.base.error.ErrorHandlerModule

@Module(includes = arrayOf(CoreFragmentScreenModule::class, ErrorHandlerModule::class))
class FragmentScreenModule
