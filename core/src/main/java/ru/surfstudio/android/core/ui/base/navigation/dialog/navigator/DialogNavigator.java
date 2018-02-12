package ru.surfstudio.android.core.ui.base.navigation.dialog.navigator;


import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;

import ru.surfstudio.android.core.ui.base.dagger.provider.ActivityProvider;
import ru.surfstudio.android.core.ui.base.navigation.Navigator;
import ru.surfstudio.android.core.ui.base.navigation.dialog.route.DialogRoute;
import ru.surfstudio.android.core.ui.base.screen.dialog.BaseDialogFragment;
import ru.surfstudio.android.core.ui.base.screen.dialog.simple.CoreSimpleDialogFragment;

/**
 * позволяет открывать диалоги
 */
public abstract class DialogNavigator implements Navigator { //todo сделать поддержку widget(через ScopeStorage?)

    private ActivityProvider activityProvider;

    public DialogNavigator(ActivityProvider activityProvider) {
        this.activityProvider = activityProvider;
    }

    public void show(DialogRoute dialogRoute){
        DialogFragment dialog = dialogRoute.createFragment();
        if(dialog instanceof CoreSimpleDialogFragment) {
            show((CoreSimpleDialogFragment) dialog);
        } else {
            dialog.show(activityProvider.get().getSupportFragmentManager(), dialogRoute.getTag());
        }
    }

    public void dismiss(DialogRoute dialogRoute){
        FragmentManager fragmentManager = activityProvider.get().getSupportFragmentManager();
        BaseDialogFragment dialogFragment = (BaseDialogFragment) fragmentManager
                .findFragmentByTag(dialogRoute.getTag());
        dialogFragment.dismiss();
    }

    protected abstract void show(CoreSimpleDialogFragment fragment);
    
}
