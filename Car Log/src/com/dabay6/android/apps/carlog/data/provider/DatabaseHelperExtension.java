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

package com.dabay6.android.apps.carlog.data.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import com.utils.android.logging.Logger;
import com.utils.android.util.AssetUtils;
import com.utils.android.util.DataUtils;

import java.io.IOException;

/**
 * DatabaseHelperExtension
 *
 * @author Remel Pugh
 * @version 1.0
 */
@SuppressWarnings("unused")
public class DatabaseHelperExtension extends DatabaseHelper {
    @SuppressWarnings("unused")
    private final static String TAG = Logger.makeTag(DatabaseHelperExtension.class);
    private final static int VERSION_ORIGINAL = 1;
    private final Context context;

    /**
     * @param context The {@link Context} used to createItems the database.
     * @param name    The name of the database.
     * @param version The current database version.
     */
    public DatabaseHelperExtension(final Context context, final String name, final int version) {
        super(context, name, version);

        this.context = context;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreate(final SQLiteDatabase db) {
        super.onCreate(db);

        db.beginTransaction();

        try {
            final String script = getCreateScript(CarLogProvider.DATABASE_VERSION);

            if (!TextUtils.isEmpty(script)) {
                DataUtils.execMultipleSQL(db, getCreateScript(CarLogProvider.DATABASE_VERSION));
            }

            db.setTransactionSuccessful();
        }
        catch (Exception e) {
            Logger.error(TAG, e.getMessage(), e);
        }
        finally {
            db.endTransaction();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
        super.onUpgrade(db, oldVersion, newVersion);

        db.beginTransaction();

        try {
            @SuppressWarnings("UnnecessaryLocalVariable")
            final int version = oldVersion;

            // NOTE: This switch statement is designed to handle cascading database updates,
            // starting at the current version and falling through to all future upgrade cases.
            // Only use "break;" when you want to drop and recreate the entire database.
            //            switch (version) {
            //                case VERSION_ORIGINAL: {
            //                    DataUtils.execMultipleSQL(db, getUpgradeScript(version));
            //
            //                    version = VERSION_NEXT;
            //                }
            //            }

            db.setTransactionSuccessful();

            if (version != CarLogProvider.DATABASE_VERSION) {
                onCreate(db);
            }
        }
        catch (Exception e) {
            Logger.error(TAG, e.getMessage(), e);
        }
        finally {
            db.endTransaction();
        }
    }

    /**
     * Retrieve database creation script for the specified version.
     *
     * @param version The current database version.
     */
    private String getCreateScript(final int version) {
        final String assetName = "database/%d.createItems.sql";

        try {
            return AssetUtils.read(context, String.format(assetName, version));
        }
        catch (IOException e) {
            return null;
        }
    }

    /**
     * Retrieve database upgrade script for the specified version.
     *
     * @param version The current database version.
     */
    private String getUpgradeScript(final int version) {
        final String assetName = "database/%d.upgrade.sql";

        try {
            return AssetUtils.read(context, String.format(assetName, version));
        }
        catch (IOException e) {
            return null;
        }
    }
}