package ru.surfstudio.android.loadstate.sample.ui.base.loadstate.presentations

import ru.surfstudio.android.core.mvp.loadstate.renderer.LoadStatePresentation
import ru.surfstudio.android.core.mvp.model.state.LoadStateInterface
import ru.surfstudio.android.easyadapter.EasyAdapter
import ru.surfstudio.android.easyadapter.ItemList
import ru.surfstudio.android.loadstate.sample.ui.base.loadstate.presentations.controllers.StubData
import ru.surfstudio.android.loadstate.sample.ui.base.loadstate.presentations.controllers.StubLoadStateController
import ru.surfstudio.android.loadstate.sample.ui.base.loadstate.states.MainLoadingState
import ru.surfstudio.android.recycler.extension.add

class MainLoadingWithStubsPresentation(private val adapter: EasyAdapter) : LoadStatePresentation<MainLoadingState> {

    companion object {
        private const val STUBS_COUNT = 4
    }

    override fun showPresentation(loadStateFrom: LoadStateInterface, loadStateTo: MainLoadingState) {
        val stubLoadStateController =
                StubLoadStateController()
        adapter.setItems(ItemList.create().apply {
            for (i in 1..STUBS_COUNT) {
                this.add(StubData(i, true), stubLoadStateController)
            }
        })
    }

    override fun hidePresentation(loadStateFrom: MainLoadingState, loadStateTo: LoadStateInterface) {
    }
}