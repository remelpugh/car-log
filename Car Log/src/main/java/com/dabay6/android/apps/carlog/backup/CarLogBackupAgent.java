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