package ru.surfstudio.android.core.ui.base.screen.dialog;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.agna.ferro.core.HasName;

import ru.surfstudio.android.core.app.log.LogConstants;
import ru.surfstudio.android.core.app.log.RemoteLogger;
import ru.surfstudio.android.core.ui.base.screen.activity.CoreActivityView;
import ru.surfstudio.android.core.ui.base.screen.configurator.HasScreenConfigurator;
import ru.surfstudio.android.core.ui.base.screen.fragment.CoreFragmentView;

/**
 * Базовый класс простого диалога который может возвращать результат
 * У этого диалога презентер не предусмотрен
 * Простой диалог рассматривается как часть родителького View и оповещает презентер о событиях
 * пользователя прямым вызовом метода презентера
 *
 * для получения презентера в дмалоге предусмотрен метод {@link #getScreenComponent(Class)} который
 * возвращает компонент родительского экрана.
 *
 * Этот диалог следует расширять если не требуется реализация сложной логики в диалоге и обращение
 * к слою Interactor
 */
public abstract class BaseSimpleDialogFragment extends BaseDialogFragment implements HasName {
    public static final String EXTRA_PARENT = "EXTRA_PARENT";
    private Parent parentType;

    public void show(CoreActivityView parentActivityView) {
        parentType = Parent.ACTIVITY;
        show(parentActivityView.getSupportFragmentManager());
    }

    public void show(CoreFragmentView parentFragment) {
        parentType = Parent.FRAGMENT;
        this.setTargetFragment(parentFragment, 0);
        show(parentFragment.getFragmentManager());
    }

    protected void show(FragmentManager fragmentManager) {
        super.show(fragmentManager, getName());
    }

    @Override
    public int show(FragmentTransaction transaction, String tag) {
        throw new UnsupportedOperationException("Instead this method, use method render(parentFragment) " +
                "or render(parentActivity)");
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        throw new UnsupportedOperationException("Instead this method, use method render(parentFragment) " +
                "or render(parentActivity)");
    }

    protected <T> T getScreenComponent(Class<T> componentClass) {
        HasScreenConfigurator hasScreenConfigurator;
        switch (parentType) {
            case ACTIVITY:
                hasScreenConfigurator = (HasScreenConfigurator)getActivity();
                break;
            case FRAGMENT:
                hasScreenConfigurator = (HasScreenConfigurator)getTargetFragment();
                break;
            default:
                throw new IllegalStateException("Unsupported parent type: " + parentType);
        }
        return componentClass.cast(hasScreenConfigurator.getScreenConfigurator().getScreenComponent());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (parentType == null) {
            parentType = (Parent)savedInstanceState.getSerializable(EXTRA_PARENT);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(EXTRA_PARENT, parentType);
    }

    @Override
    public void onResume() {
        super.onResume();
        RemoteLogger.logMessage(String.format(LogConstants.LOG_DIALOG_RESUME_FORMAT, getName()));
    }

    @Override
    public void onPause() {
        super.onPause();
        RemoteLogger.logMessage(String.format(LogConstants.LOG_DIALOG_PAUSE_FORMAT, getName()));
    }

    private enum Parent {
        ACTIVITY,
        FRAGMENT
    }

}
