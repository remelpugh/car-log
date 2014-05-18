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

package com.dabay6.android.apps.carlog.data.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import com.dabay6.libraries.androidshared.logging.Logger;
import com.dabay6.libraries.androidshared.util.AssetUtils;
import com.dabay6.libraries.androidshared.util.DataUtils;

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