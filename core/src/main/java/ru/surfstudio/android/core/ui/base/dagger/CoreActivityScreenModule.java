package ru.surfstudio.android.core.ui.base.dagger;



import dagger.Module;
import dagger.Provides;
import ru.surfstudio.android.core.app.dagger.scope.PerScreen;
import ru.surfstudio.android.core.ui.base.dagger.provider.ActivityProvider;
import ru.surfstudio.android.core.ui.base.message.DefaultMessageController;
import ru.surfstudio.android.core.ui.base.message.MessageController;
import ru.surfstudio.android.core.ui.base.navigation.activity.navigator.ActivityNavigator;
import ru.surfstudio.android.core.ui.base.navigation.activity.navigator.ActivityNavigatorForActivity;
import ru.surfstudio.android.core.ui.base.navigation.dialog.navigator.DialogNavigator;
import ru.surfstudio.android.core.ui.base.navigation.dialog.navigator.DialogNavigatorForActivityScreen;
import ru.surfstudio.android.core.ui.base.navigation.fragment.FragmentNavigator;
import ru.surfstudio.android.core.ui.base.permission.PermissionManager;
import ru.surfstudio.android.core.ui.base.permission.PermissionManagerForActivity;
import ru.surfstudio.android.core.ui.base.scope.PersistentScope;

@Module
public class CoreActivityScreenModule {

    private PersistentScope persistentScreenScope;

    public CoreActivityScreenModule(PersistentScope persistentScreenScope) {
        this.persistentScreenScope = persistentScreenScope;
    }

    @Provides
    @PerScreen
    ActivityProvider provideActivityProvider() {
        return new ActivityProvider(persistentScreenScope);
    }

    @Provides
    @PerScreen
    DialogNavigator provideDialogNavigator(ActivityProvider activityProvider){
        return new DialogNavigatorForActivityScreen(activityProvider);
    }

    @Provides
    @PerScreen
    ActivityNavigator provideActivityNavigator(ActivityProvider activityProvider){
        return new ActivityNavigatorForActivity(activityProvider);
    }

    @Provides
    @PerScreen
    FragmentNavigator provideFragmentNavigator(ActivityProvider activityProvider){
        return new FragmentNavigator(activityProvider);
    }

    @Provides
    @PerScreen
    ScreenEventDelegateManagerProvider provideEventDelegateManagerProvider(ActivityProvider activityProvider){
        return new ActivityScreenEventDelegateManagerProvider(activityProvider);
    }

    @Provides
    @PerScreen
    PermissionManager providePermissionManager(ActivityProvider activityProvider){
        return new PermissionManagerForActivity(activityProvider);
    }

    @Provides
    @PerScreen
    MessageController provideMessageController(ActivityProvider activityProvider){
        return new DefaultMessageController(activityProvider);
    }

}