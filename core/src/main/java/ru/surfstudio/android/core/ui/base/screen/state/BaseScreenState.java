package ru.surfstudio.android.core.ui.base.screen.state;

import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * базовый {@link ScreenState} для активити и фрагмента, для виджета свой ScreenState
 */

public abstract class BaseScreenState implements ScreenState {
    private boolean viewRecreated = false;
    private boolean screenRecreated = false;
    private boolean completelyDestroyed = false;

    private boolean restoredFromDisk = false;

    private boolean viewDestroyedAtListOnce = false;
    private boolean screenDestroyedAtListOnce = false;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        viewRecreated = viewDestroyedAtListOnce;
        screenRecreated = screenDestroyedAtListOnce;
        restoredFromDisk = restoredFromDisk ||
                !screenDestroyedAtListOnce && savedInstanceState != null;
    }

    public void onDestroyView() {
        viewDestroyedAtListOnce = true;
    }

    public void onDestroy() {
        screenDestroyedAtListOnce = true;
    }

    public void onCompletelyDestroy() {
        completelyDestroyed = true;
    }

    @Override
    public boolean isViewRecreated() {
        return viewRecreated;
    }

    @Override
    public boolean isScreenRecreated() {
        return screenRecreated;
    }

    @Override
    public boolean isCompletelyDestroyed() {
        return completelyDestroyed;
    }

    @Override
    public boolean isRestoredFromDisk() {
        return restoredFromDisk;
    }

    @Override
    public boolean isRestoredFromDiskJustNow() {
        return isRestoredFromDisk() && !isViewRecreated();
    }
}
