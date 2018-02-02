package ru.surfstudio.android.easyadapter.screen;


import ru.surfstudio.android.core.ui.base.screen.fragment.BaseLdsSwrFragmentView;
import ru.surfstudio.android.core.ui.base.screen.model.state.LoadState;
import ru.surfstudio.android.core.ui.base.screen.model.state.SwipeRefreshState;
import ru.surfstudio.android.easyadapter.impl.pagination.BasePaginationableAdapter;
import ru.surfstudio.android.easyadapter.impl.pagination.PaginationState;
import ru.surfstudio.android.easyadapter.screen.model.LdsSwrPgnScreenModel;

/**
 * базовый класс FragmentView c поддержкой
 * состояния загрузки {@link LoadState}
 * состояния SwipeRefresh {@link SwipeRefreshState}
 * состояния пагинации {@link PaginationState}
 * @param <M>
 */
public abstract class BaseLdsSwrPgnFragmentView<M extends LdsSwrPgnScreenModel>
        extends BaseLdsSwrFragmentView<M> {

    protected abstract BasePaginationableAdapter getPaginationableAdapter();

    @Override
    public void render(M screenModel) {
        renderLoadState(screenModel.getLoadState());
        renderSwipeRefreshState(screenModel.getSwipeRefreshState());
        renderPaginationState(screenModel.getPaginationState());
        renderInternal(screenModel);
    }

    protected void renderPaginationState(PaginationState paginationState) {
        getPaginationableAdapter().setState(paginationState);
    }
}
