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
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import com.dabay6.android.apps.carlog.data.provider.util.SelectionBuilder;
import com.dabay6.android.apps.carlog.data.provider.util.UriType;

/**
 * CarLogProviderExtension
 *
 * @author Remel Pugh
 * @version 1.0
 */
public class CarLogProviderExtension extends CarLogProvider {
    private static final int STATISTICS_AVERAGES = 0x20002;
    private static final int STATISTICS_FILL_UPS = 0x20001;
    private static final int STATISTICS_TOTALS = 0x20000;

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized SQLiteDatabase getDatabase(final Context context) {
        if (databaseInfo == null || !databaseInfo.isOpen()) {
            final DatabaseHelperExtension helper;

            helper = new DatabaseHelperExtension(context, DATABASE_NAME, DATABASE_VERSION);

            databaseInfo = helper.getWritableDatabase();
        }

        return databaseInfo;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Cursor query(final Uri uri, final String[] projection, final String selection, final String[] selectionArgs,
                        final String sortOrder) {
        final Context context = getContext();
        final Cursor cursor;
        final SQLiteDatabase db = getDatabase(context);
        final Uri notificationUri = CarLogContract.CONTENT_URI;
        final int match = matchUri(uri);

        switch (match) {
            case STATISTICS_AVERAGES:
            case STATISTICS_FILL_UPS:
            case STATISTICS_TOTALS: {
                final SelectionBuilder builder = new SelectionBuilder();
                final UriType uriType = uriTypes.get(match);

                cursor = builder.table(uriType.getTableName())
                                .where(selection, selectionArgs)
                                .query(db, projection, sortOrder);
                break;
            }
            default: {
                return super.query(uri, projection, selection, selectionArgs, sortOrder);
            }
        }

        if ((cursor != null) && !isTemporary()) {
            cursor.setNotificationUri(context.getContentResolver(), notificationUri);
        }

        return cursor;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected UriMatcher buildUriMatcher() {
        final UriMatcher matcher = super.buildUriMatcher();
        UriType uriType;

        uriType = new UriType(STATISTICS_AVERAGES, StatisticsAverages.PATH, StatisticsAverages.TABLE_NAME,
                              StatisticsAverages.TYPE_DIR_TYPE, null);
        uriTypes.put(STATISTICS_AVERAGES, uriType);

        matcher.addURI(AUTHORITY, uriType.getMatchPath(), uriType.getCode());

        uriType = new UriType(STATISTICS_FILL_UPS, StatisticsFillUps.PATH, StatisticsFillUps.TABLE_NAME,
                              StatisticsFillUps.TYPE_DIR_TYPE, null);
        uriTypes.put(STATISTICS_FILL_UPS, uriType);

        matcher.addURI(AUTHORITY, uriType.getMatchPath(), uriType.getCode());

        uriType = new UriType(STATISTICS_TOTALS, StatisticsTotal.PATH, StatisticsTotal.TABLE_NAME,
                              StatisticsTotal.TYPE_DIR_TYPE, null);
        uriTypes.put(STATISTICS_TOTALS, uriType);

        matcher.addURI(AUTHORITY, uriType.getMatchPath(), uriType.getCode());

        return matcher;
    }
}