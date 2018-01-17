package ru.surfstudio.android.core.ui.base.scope.activity;


import android.app.Activity;

import ru.surfstudio.android.core.ui.ScreenType;
import ru.surfstudio.android.core.ui.base.event.delegate.ActivityScreenEventDelegateManager;
import ru.surfstudio.android.core.ui.base.scope.PersistentScope;


public class ActivityPersistentScope extends PersistentScope<ActivityScreenEventDelegateManager> {
    private Activity activity;
    private boolean activityRecreated;
    private boolean activityRestoredFromDisk;

    public ActivityPersistentScope(String name, ActivityScreenEventDelegateManager screenEventDelegateManager) {
        super(name, ScreenType.ACTIVITY, screenEventDelegateManager);
        screenEventDelegateManager.registerDelegate(new ActivityPersistentScopeChangeObserver(this));
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public boolean isActivityRecreated() {
        return activityRecreated;
    }

    public void setActivityRecreated(boolean activityRecreated) {
        this.activityRecreated = activityRecreated;
    }

    public boolean isActivityRestoredFromDisk() {
        return activityRestoredFromDisk;
    }

    public void setActivityRestoredFromDisk(boolean activityRestoredFromDisk) {
        this.activityRestoredFromDisk = activityRestoredFromDisk;
    }
}
