/*
 * Copyright (c) 2019-present, SurfStudio LLC.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.surfstudio.android.core.mvp.rx.sample.easyadapter.ui.screen.pagination

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.core.mvp.rx.sample.easyadapter.domain.Element
import ru.surfstudio.android.core.mvp.rx.sample.easyadapter.domain.datalist.DataList
import ru.surfstudio.android.core.mvp.rx.sample.easyadapter.interactor.element.DEFAULT_PAGE
import ru.surfstudio.android.core.mvp.rx.sample.easyadapter.interactor.element.ElementRepository
import ru.surfstudio.android.core.mvp.rx.ui.BaseRxPresenter
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.easyadapter.pagination.PaginationState
import ru.surfstudio.android.utilktx.data.wrapper.selectable.SelectableData
import ru.surfstudio.android.utilktx.data.wrapper.selectable.getSelectedDataNullable
import ru.surfstudio.android.utilktx.data.wrapper.selectable.setSelected
import javax.inject.Inject

@PerScreen
class PaginationPresenter @Inject constructor(
        basePresenterDependency: BasePresenterDependency,
        private val elementRepository: ElementRepository
) : BaseRxPresenter<PaginationPresentationModel, PaginationActivityView>(basePresenterDependency) {

    override val pm = PaginationPresentationModel()

    private var loadMainSubscription: Disposable? = null
    private var loadMoreSubscription: Disposable? = null

    override fun onFirstLoad() {
        super.onFirstLoad()

        pm.reloadAction bindTo ::reloadData
        pm.getMoreAction.observable.filter { pm.hasData } bindTo ::loadMore
        pm.selectElementAction bindTo { element ->
            pm.elementsState.apply {
                value.setSelected(element)
                update()
            }
        }

        loadMainData()
    }

    private fun loadMainData() {
        loadMainSubscription?.dispose()
        loadMoreSubscription?.dispose()

        loadMainSubscription = subscribeIo(
                elementRepository.getElements(DEFAULT_PAGE),
                { elements ->
                    view.showText("Data loaded")
                    val newElements = updateDataList(elements)

                    pm.elementsState.accept(newElements)
                    pm.hasData = elements.isNotEmpty()
                    setNormalLoadState(elements)
                    setNormalPaginationState(elements)
                },
                {
                    setErrorLoadState(pm.elementsState.value)
                    setErrorPaginationState(pm.elementsState.value)
                    view.showText("Imitate load data error")
                })
    }

    private fun loadMore() {
        view.showText("Start pagination request")
        loadMoreSubscription =
                elementRepository.getElements(pm.elementsState.value.nextPage)
                        .observeOn(AndroidSchedulers.mainThread())
                        .bindTo(
                                { elements ->
                                    val newElements = updateDataList(elements)
                                    pm.elementsState.accept(pm.elementsState.value.merge(newElements))
                                    setNormalPaginationState(pm.elementsState.value)
                                    view.showText("Pagination request complete")
                                },
                                {
                                    setErrorPaginationState(pm.elementsState.value)
                                    view.showText("Imitate pagination request error")
                                })
    }

    /**
     *  Трансформирует DataList<Element>) в DataList<SelectableData<Element>> и устанавливает выбранное значение
     */
    private fun updateDataList(elements: DataList<Element>): DataList<SelectableData<Element>> {
        val selected = pm.elementsState.value.getSelectedDataNullable()
        return elements.map { SelectableData(it) }
                .let {
                    it.setSelected(selected)
                    DataList(it,
                            elements.startPage,
                            elements.pageSize,
                            elements.totalItemsCount,
                            elements.totalPagesCount)
                }
    }

    private fun reloadData() {
        loadMainData()
    }

    private fun setNormalLoadState(elements: DataList<*>) {
        pm.loadState.accept(if (elements.isEmpty())
            LS.EMPTY else
            LS.NONE)
    }

    private fun setErrorLoadState(elements: DataList<*>) {
        pm.loadState.accept(if (elements.isEmpty())
            LS.ERROR else
            LS.NONE)
    }

    private fun setNormalPaginationState(elements: DataList<*>) {
        pm.paginationState.accept(if (elements.canGetMore())
            PaginationState.READY else
            PaginationState.COMPLETE)

    }

    private fun setErrorPaginationState(elements: DataList<*>) {
        pm.paginationState.accept(if (elements.isEmpty())
            PaginationState.COMPLETE else
            PaginationState.ERROR)
    }
}


