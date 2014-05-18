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