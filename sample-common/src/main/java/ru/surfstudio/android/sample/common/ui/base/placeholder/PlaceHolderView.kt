package ru.surfstudio.android.sample.common.ui.base.placeholder

import android.content.Context
import android.util.AttributeSet
import ru.surfstudio.android.core.mvp.loadstate.renderer.LoadStateRendererInterface
import ru.surfstudio.android.core.mvp.model.state.LoadStateInterface
import ru.surfstudio.android.custom.view.placeholder.StandardPlaceHolderView
import ru.surfstudio.standard.base_ui.placeholder.LoadState

class PlaceHolderView(context: Context, attributeSet: AttributeSet
) : StandardPlaceHolderView(context, attributeSet), LoadStateRendererInterface {

    override fun render(loadState: LoadStateInterface) {
        when (loadState) {
            LoadState.NONE -> setNoneState()
            LoadState.MAIN_LOADING -> setMainLoadingState()
            LoadState.TRANSPARENT_LOADING -> setTransparentLoadingState()
            LoadState.EMPTY -> setEmptyState()
            LoadState.ERROR -> setErrorState()
            LoadState.NOT_FOUND -> setNotFoundState()
            LoadState.NO_INTERNET -> setNoInternetState()
        }
    }
}