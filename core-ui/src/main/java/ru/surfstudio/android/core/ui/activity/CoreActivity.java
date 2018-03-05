package ru.surfstudio.android.core.ui.activity;


import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import ru.surfstudio.android.core.ui.delegate.activity.ActivityDelegate;
import ru.surfstudio.android.core.ui.delegate.factory.ScreenDelegateFactoryContainer;
import ru.surfstudio.android.core.ui.scope.ActivityPersistentScope;
import ru.surfstudio.android.logger.LogConstants;
import ru.surfstudio.android.logger.Logger;

/**
 * базовая активити для всего приложения
 * см {@link ActivityDelegate}
 */
public abstract class CoreActivity extends AppCompatActivity implements CoreActivityInterface {

    private ActivityDelegate activityDelegate;

    @Override
    public ActivityDelegate createActivityDelegate() {
        return ScreenDelegateFactoryContainer.get().createActivityDelegate(this);
    }

    @Override
    public ActivityPersistentScope getPersistentScope() {
        return activityDelegate.getPersistentScope();
    }

    @Override
    public String getScreenName() {
        return this.getClass().getCanonicalName();
    }

    @Override
    protected final void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityDelegate = createActivityDelegate();
        activityDelegate.initialize(savedInstanceState);

        onPreCreate(savedInstanceState);
        setContentView(getContentView());
        activityDelegate.onCreate(savedInstanceState, null);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState,
                         @Nullable PersistableBundle persistentState,
                         boolean viewRecreated) {

        //empty
    }

    /**
     * @return layout resource of the screen
     */
    @LayoutRes
    abstract protected int getContentView();

    /**
     * Called before Presenter is bound to the View and content view is created
     *
     * @param savedInstanceState
     */
    void onPreCreate(Bundle savedInstanceState) {
        //empty
    }

    @Override
    protected void onStart() {
        super.onStart();
        activityDelegate.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Logger.d(LogConstants.LOG_SCREEN_RESUME_FORMAT, getScreenName());
        activityDelegate.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Logger.d(LogConstants.LOG_SCREEN_PAUSE_FORMAT, getScreenName());
        activityDelegate.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        activityDelegate.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        activityDelegate.onDestroyView();
        activityDelegate.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        activityDelegate.onOnSaveInstantState(outState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        activityDelegate.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        activityDelegate.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        activityDelegate.onNewIntent(intent);
    }

    @Override
    public void onBackPressed() {
        if (!activityDelegate.onBackPressed()) { //поддерживается пока только один обработчик
            super.onBackPressed();
        }
    }
}
