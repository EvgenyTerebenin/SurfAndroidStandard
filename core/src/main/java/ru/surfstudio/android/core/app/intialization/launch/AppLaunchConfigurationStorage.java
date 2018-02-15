package ru.surfstudio.android.core.app.intialization.launch;

import android.content.SharedPreferences;

import javax.inject.Inject;
import javax.inject.Named;

import ru.surfstudio.android.core.app.SharedPrefModule;
import ru.surfstudio.android.core.util.SettingsUtil;
import ru.surfstudio.android.dagger.scope.PerApplication;

/**
 * хранилище конфигурации запуска устройства
 */
@PerApplication
public class AppLaunchConfigurationStorage {

    private static final String LAST_LAUNCH_VERSION = "LAST_LAUNCH_VERSION";
    private static final String IS_FIRST_LAUNCH = "IS_FIRST_LAUNCH";

    private SharedPreferences noBackupSharedPref;

    @Inject
    public AppLaunchConfigurationStorage(@Named(SharedPrefModule.NO_BACKUP_SHARED_PREF) SharedPreferences noBackupSharedPref) {
        this.noBackupSharedPref = noBackupSharedPref;
    }

    /**
     * Метод, возвращающий последнюю запущенную текущую версию приложения
     *
     * @return - последняя версия
     */
    public int getLastLaunchVersion() {
        return SettingsUtil.getInt(noBackupSharedPref, LAST_LAUNCH_VERSION);
    }


    /**
     * Метод, устанавливающий последнюю запущенную текущую версию приложения
     *
     * @param version - последняя версия
     */
    public void setLaunchVersion(int version) {
        SettingsUtil.putInt(noBackupSharedPref, LAST_LAUNCH_VERSION, version);
    }

    /**
     * Метод, возвращающий флаг признака первого запуска приложения
     *
     * @return - true - первый запуск, false - последующие запуски
     */
    public boolean isFirstLaunch() {
        return SettingsUtil.getBoolean(noBackupSharedPref, IS_FIRST_LAUNCH, true);
    }

    /**
     * Метод, устанавливающий флаг признака первого запуска приложения
     * После вызова этого метода, {@link #isFirstLaunch()} будет всегда возвращать {@code true}
     */
    public void markFirstLaunchDone() {
        SettingsUtil.putBoolean(noBackupSharedPref, IS_FIRST_LAUNCH, false);
    }

}
