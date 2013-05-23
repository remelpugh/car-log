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

import android.app.backup.FileBackupHelper;
import android.content.Context;

/**
 * DatabaseFileBackupHelper
 *
 * @author Remel Pugh
 * @version 1.0
 */
public class DatabaseFileBackupHelper extends FileBackupHelper {
    /**
     * Construct a helper to manage backup/restore of application's database file.
     *
     * @param context      The backup agent's Context object
     * @param databaseName The database filename
     */
    public DatabaseFileBackupHelper(final Context context, final String databaseName) {
        super(context, context.getDatabasePath(databaseName).getAbsolutePath());
    }
}