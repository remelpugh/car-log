package com.dabay6.android.apps.carlog.app;

import android.app.backup.BackupManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import com.dabay6.android.apps.carlog.BuildConfig;
import com.dabay6.android.apps.carlog.R.string;
import com.dropbox.sync.android.DbxAccountManager;
import com.utils.android.app.ApplicationExtension;
import com.utils.android.enums.LogLevels;
import com.utils.android.util.SharedPreferenceUtils;

/**
 * CarLogApplication
 *
 * @author Remel Pugh
 * @version 1.0
 */
@SuppressWarnings("unused")
public class CarLogApplication extends ApplicationExtension implements OnSharedPreferenceChangeListener {
    private static final String appKey = "uo324va9vacupmm";
    private static final String appSecret = "y40l6u05ut9370r";
    private BackupManager backupManager;

    /**
     *
     * @return
     */
    public BackupManager getBackupManager() {
        return backupManager;
    }

    /**
     *
     * @param context
     * @return
     */
    public static DbxAccountManager getDbxAccountManager(final Context context) {
        return DbxAccountManager.getInstance(context.getApplicationContext(), appKey, appSecret);
    }

    /**
     *
     * @return
     */
    public static String getDbxAppKey() {
        return appKey;
    }

    /**
     *
     * @return
     */
    public static String getDbxAppSecret() {
        return appSecret;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LogLevels getLogLevel() {
        if (BuildConfig.DEBUG) {
            return LogLevels.VERBOSE;
        }
        else {
            return LogLevels.ERROR;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTagPrefix() {
        return getString(string.app_name).replace(" ", "");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreate() {
        super.onCreate();

        backupManager = new BackupManager(this);

        SharedPreferenceUtils.get(this).registerOnSharedPreferenceChangeListener(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onSharedPreferenceChanged(final SharedPreferences sharedPreferences, final String key) {
        backupManager.dataChanged();
    }
}