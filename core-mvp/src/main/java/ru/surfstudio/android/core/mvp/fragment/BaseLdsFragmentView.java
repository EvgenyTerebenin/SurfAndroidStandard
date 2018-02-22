package ru.surfstudio.android.core.mvp.fragment;

import ru.surfstudio.android.core.mvp.model.LdsScreenModel;
import ru.surfstudio.android.core.mvp.model.state.LoadState;
import ru.surfstudio.android.core.mvp.placeholder.PlaceHolderView;

/**
 * базовый класс FragmentView c поддержкой
 * состояния загрузки {@link LoadState}
 *
 * @param <M>
 */
public abstract class BaseLdsFragmentView<M extends LdsScreenModel>
        extends BaseRenderableHandleableErrorFragmentView<M> {

    protected abstract PlaceHolderView getPlaceHolderView();

    @Override
    public void render(M screenModel) {
        renderLoadState(screenModel.getLoadState());
        renderInternal(screenModel);
    }

    protected void renderLoadState(LoadState loadState) {
        getPlaceHolderView().render(loadState);
    }
}