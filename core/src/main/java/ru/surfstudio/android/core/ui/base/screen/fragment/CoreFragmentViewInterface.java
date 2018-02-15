package ru.surfstudio.android.core.ui.base.screen.fragment;

import ru.surfstudio.android.core.ui.base.screen.configurator.BaseFragmentViewConfigurator;
import ru.surfstudio.android.core.ui.base.screen.delegate.fragment.FragmentViewDelegate;
import ru.surfstudio.android.core.ui.base.screen.scope.FragmentViewPersistentScope;
import ru.surfstudio.android.core.ui.base.screen.view.core.PresenterHolderCoreView;

/**
 * инрефейс для вью, основанной на фрагменте
 */
public interface CoreFragmentViewInterface extends PresenterHolderCoreView, CoreFragmentInterface {

    @Override
    BaseFragmentViewConfigurator createConfigurator();

    @Override
    FragmentViewPersistentScope getPersistentScope();

    @Override
    FragmentViewDelegate createFragmentDelegate();

}
