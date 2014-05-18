/*
 * Copyright (c) 2014 Remel Pugh
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.dabay6.android.apps.carlog.app;

import android.app.backup.BackupManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import com.dabay6.android.apps.carlog.BuildConfig;
import com.dabay6.android.apps.carlog.R.string;
import com.dropbox.sync.android.DbxAccountManager;
import com.dabay6.libraries.androidshared.app.ApplicationExtension;
import com.dabay6.libraries.androidshared.enums.LogLevels;
import com.dabay6.libraries.androidshared.util.SharedPreferenceUtils;

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