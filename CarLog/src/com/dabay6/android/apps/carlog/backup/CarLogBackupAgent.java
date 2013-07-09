/*
 * Copyright (c) 2013 Remel Pugh
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dabay6.android.apps.carlog.backup;

import android.app.backup.BackupAgentHelper;
import android.app.backup.BackupDataInput;
import android.app.backup.BackupDataOutput;
import android.app.backup.SharedPreferencesBackupHelper;
import android.os.ParcelFileDescriptor;
import com.dabay6.android.apps.carlog.data.provider.CarLogProvider;
import com.dabay6.android.apps.carlog.ui.SettingsActivity;

import java.io.IOException;

/**
 * CarLogBackupAgent
 *
 * @author Remel Pugh
 * @version 1.0
 */
public class CarLogBackupAgent extends BackupAgentHelper {
    public static final String PREFERENCES_BACKUP_KEY = "carlog_prefs";
    static final String DATABASE_BACKUP_KEY = "carlog_database";

    /**
     * {@inheritDoc}
     */
    @Override
    public void onBackup(final ParcelFileDescriptor oldState, final BackupDataOutput data,
                         final ParcelFileDescriptor newState) throws IOException {
        synchronized (SettingsActivity.dataLock) {
            super.onBackup(oldState, data, newState);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreate() {
        addHelper(PREFERENCES_BACKUP_KEY, new SharedPreferencesBackupHelper(this, getPackageName() + "_preferences"));
        addHelper(DATABASE_BACKUP_KEY, new DatabaseFileBackupHelper(this, CarLogProvider.DATABASE_NAME));

        super.onCreate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onRestore(final BackupDataInput data, final int appVersionCode, final ParcelFileDescriptor newState)
            throws IOException {
        synchronized (SettingsActivity.dataLock) {
            super.onRestore(data, appVersionCode, newState);
        }
    }
}