package ru.surfstudio.android.mvp.widget.delegate;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewParent;

import java.util.List;

import ru.surfstudio.android.core.ui.fragment.CoreFragmentInterface;
import ru.surfstudio.android.core.ui.scope.PersistentScope;
import ru.surfstudio.android.core.ui.scope.PersistentScopeStorage;
import ru.surfstudio.android.mvp.widget.view.CoreWidgetViewInterface;

/**
 * ищет родительский PersistentScope для WidgetView
 */

public class ParentPersistentScopeFinder {

    private View child;
    private PersistentScopeStorage scopeStorage;

    public <V extends View & CoreWidgetViewInterface> ParentPersistentScopeFinder(V child, PersistentScopeStorage scopeStorage) {
        this.child = child;
        this.scopeStorage = scopeStorage;
    }

    public PersistentScope find() {
        PersistentScope parentScope = null;
        FragmentActivity activity = (FragmentActivity) child.getContext();
        List<Fragment> fragments = activity.getSupportFragmentManager().getFragments();
        ViewParent parent = child.getParent();
        while (parent != null) {
            for (Fragment fragment : fragments) {
                if (fragment.getView() != null
                        && fragment.getView() == parent
                        && fragment instanceof CoreFragmentInterface) {
                    parentScope = scopeStorage.get(
                            ((CoreFragmentInterface) fragment).getName());

                }
            }
            parent = child.getParent();
        }
        if (parentScope == null) {
            parentScope = scopeStorage.getActivityScope();
        }
        return parentScope;
    }
}
