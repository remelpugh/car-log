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

import android.content.*;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.net.Uri;
import android.util.Log;
import android.util.SparseArray;
import com.dabay6.android.apps.carlog.data.provider.CarLogContract.FuelHistory;
import com.dabay6.android.apps.carlog.data.provider.CarLogContract.Make;
import com.dabay6.android.apps.carlog.data.provider.CarLogContract.Model;
import com.dabay6.android.apps.carlog.data.provider.CarLogContract.Vehicle;
import com.dabay6.android.apps.carlog.data.provider.util.SelectionBuilder;
import com.dabay6.android.apps.carlog.data.provider.util.UriType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 */
 @SuppressWarnings("unused")
public class CarLogProvider extends ContentProvider {
    public static final String AUTHORITY = "com.dabay6.android.apps.carlog.provider";
    // Version 1 : Creation of the databaseInfo
    public static final String DATABASE_NAME = "carlog.db";
    public static final int DATABASE_VERSION = 1;
    public static final Uri INTEGRITY_CHECK_URI = Uri.parse("content://" + AUTHORITY + "/integrityCheck");    
    private static final String LOG_TAG = CarLogProvider.class.getSimpleName();
    private static final int FUEL_HISTORY = 0x10000;
    private static final int FUEL_HISTORY_ID = 0x10001;
    private static final int MAKE = 0x10002;
    private static final int MAKE_ID = 0x10003;
    private static final int MODEL = 0x10004;
    private static final int MODEL_ID = 0x10005;
    private static final int VEHICLE = 0x10006;
    private static final int VEHICLE_ID = 0x10007;
    protected SQLiteDatabase databaseInfo;
    protected UriMatcher uriMatcher;
    protected SparseArray<UriType> uriTypes;

    /**
     * {@inheritDoc}
     */
    @Override
    public ContentProviderResult[] applyBatch(final ArrayList<ContentProviderOperation> operations) 
            throws OperationApplicationException {
        final SQLiteDatabase db = getDatabase(getContext());

        db.beginTransaction();

        try {
            final int numOperations = operations.size();
            final ContentProviderResult[] results = new ContentProviderResult[numOperations];

            for (int i = 0; i < numOperations; i++) {
                results[i] = operations.get(i).apply(this, results, i);
                db.yieldIfContendedSafely();
            }

            db.setTransactionSuccessful();

            return results;
        }
        finally {
            db.endTransaction();
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public int bulkInsert(final Uri uri, final ContentValues[] values) {
        final Context context = getContext();
        final SQLiteDatabase db = getDatabase(context);
        final SQLiteStatement insertStmt;
        final int match = matchUri(uri);
        int numberInserted = 0;

        db.beginTransaction();

        try {
            switch (match) {
                case FUEL_HISTORY: {
                    insertStmt = db.compileStatement(FuelHistory.getBulkInsertString());

                    for (final ContentValues value : values) {
                        FuelHistory.bindValuesInBulkInsert(insertStmt, value);
                        insertStmt.execute();
                        insertStmt.clearBindings();
                    }

                    insertStmt.close();

                    db.setTransactionSuccessful();

                    numberInserted = values.length;
                    break;
                }
                case MAKE: {
                    insertStmt = db.compileStatement(Make.getBulkInsertString());

                    for (final ContentValues value : values) {
                        Make.bindValuesInBulkInsert(insertStmt, value);
                        insertStmt.execute();
                        insertStmt.clearBindings();
                    }

                    insertStmt.close();

                    db.setTransactionSuccessful();

                    numberInserted = values.length;
                    break;
                }
                case MODEL: {
                    insertStmt = db.compileStatement(Model.getBulkInsertString());

                    for (final ContentValues value : values) {
                        Model.bindValuesInBulkInsert(insertStmt, value);
                        insertStmt.execute();
                        insertStmt.clearBindings();
                    }

                    insertStmt.close();

                    db.setTransactionSuccessful();

                    numberInserted = values.length;
                    break;
                }
                case VEHICLE: {
                    insertStmt = db.compileStatement(Vehicle.getBulkInsertString());

                    for (final ContentValues value : values) {
                        Vehicle.bindValuesInBulkInsert(insertStmt, value);
                        insertStmt.execute();
                        insertStmt.clearBindings();
                    }

                    insertStmt.close();

                    db.setTransactionSuccessful();

                    numberInserted = values.length;
                    break;
                }
                default: {
                    throw new IllegalArgumentException("Unknown URI " + uri);
                }
            }
        }
        finally {
            db.endTransaction();
        }

        // Notify with the base uri, not the new uri (nobody is watching a new record)
        context.getContentResolver().notifyChange(uri, null);

        return numberInserted;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public int delete(final Uri uri, final String selection, final String[] selectionArgs) {
        final Context context = getContext();
        final SQLiteDatabase db = getDatabase(context);
        final SelectionBuilder builder = buildSimpleSelection(uri);
        final int result;

        result = builder.where(selection, selectionArgs).delete(db);

        context.getContentResolver().notifyChange(uri, null);

        return result;
    }

    public synchronized SQLiteDatabase getDatabase(final Context context) {
        // Always return the cached databaseInfo, if we've got one
        if (databaseInfo == null || !databaseInfo.isOpen()) {
            final DatabaseHelper helper = new DatabaseHelper(context, DATABASE_NAME, DATABASE_VERSION);

            databaseInfo = helper.getWritableDatabase();
		}

        return databaseInfo;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String getType(final Uri uri) {
        return uriTypes.get(matchUri(uri)).getType();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Uri insert(final Uri uri, final ContentValues values) {
        final Context context = getContext();
        final long id;
        final SQLiteDatabase db = getDatabase(context);
        final int match = matchUri(uri);
        final Uri resultUri;

        switch (match) {
            case FUEL_HISTORY:
            case MAKE:
            case MODEL:
            case VEHICLE: {
                id = db.insertOrThrow(uriTypes.get(match).getTableName(), "foo", values);
                resultUri = id == -1 ? null : ContentUris.withAppendedId(uri, id);
                break;
            }
            default: {
                throw new IllegalArgumentException("Unknown URI " + uri);
            }
        }

        // Notify with the base uri, not the new uri (nobody is watching a new record)
        context.getContentResolver().notifyChange(uri, null);

        return resultUri;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onCreate() {
        uriMatcher = buildUriMatcher();

        return true;
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
        final int match = matchUri(uri);

        switch (match) {
            default: {
                final SelectionBuilder builder = buildExpandedSelection(uri, uriTypes.get(match));

                cursor = builder.where(selection, selectionArgs).query(db, projection, sortOrder);
            }
        }

        if ((cursor != null) && !isTemporary()) {
            cursor.setNotificationUri(context.getContentResolver(), uri);
        }

        return cursor;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public int update(final Uri uri, final ContentValues values, final String selection, final String[] selectionArgs) {
        final Context context = getContext();
        final SQLiteDatabase db = getDatabase(context);
        final SelectionBuilder builder = buildSimpleSelection(uri);
        final int result;

        result = builder.where(selection, selectionArgs).update(db, values);

        context.getContentResolver().notifyChange(uri, null);

        return result;
    }

    /**
     * 
     * @param uri
     * @param uriType
     * @return
     */
    protected SelectionBuilder buildExpandedSelection(final Uri uri, final UriType uriType) {
        final SelectionBuilder builder = new SelectionBuilder();

        switch (uriType.getCode()) {
            case MAKE_ID:
            case MODEL_ID: {       
                final String id = uri.getPathSegments().get(1);

                return builder.table(uriType.getTableName()).where(uriType.getIdColumnName() + " = ?", id);
            }
            case MAKE:
            case MODEL: {
                return builder.table(uriType.getTableName());
            }
            case FUEL_HISTORY_ID: {
                final String id = uri.getPathSegments().get(1);

                return builder.table("FuelHistory LEFT OUTER JOIN Vehicle ON FuelHistory.VehicleId = Vehicle._id").mapToTable(uriType.getIdColumnName(), "FuelHistory").mapToTable("Notes", "FuelHistory").mapToTable("VehicleId", "FuelHistory").mapToTable("Name", "Vehicle").where("FuelHistory." + uriType.getIdColumnName() + " = ?", id);
            }
            case VEHICLE_ID: {
                final String id = uri.getPathSegments().get(1);

                return builder.table("Vehicle LEFT OUTER JOIN Make ON Vehicle.MakeId = Make._id LEFT OUTER JOIN Model ON Vehicle.ModelId = Model._id").mapToTable(uriType.getIdColumnName(), "Vehicle").mapToTable("MakeId", "Vehicle").mapToTable("MakeName", "Make").mapToTable("ModelId", "Vehicle").mapToTable("ModelName", "Model").where("Vehicle." + uriType.getIdColumnName() + " = ?", id);
            }
            case FUEL_HISTORY: {
                return builder.table("FuelHistory LEFT OUTER JOIN Vehicle ON FuelHistory.VehicleId = Vehicle._id").mapToTable(uriType.getIdColumnName(), "FuelHistory").mapToTable("Notes", "FuelHistory").mapToTable("VehicleId", "FuelHistory").mapToTable("Name", "Vehicle");
            }
            case VEHICLE: {
                return builder.table("Vehicle LEFT OUTER JOIN Make ON Vehicle.MakeId = Make._id LEFT OUTER JOIN Model ON Vehicle.ModelId = Model._id").mapToTable(uriType.getIdColumnName(), "Vehicle").mapToTable("MakeId", "Vehicle").mapToTable("MakeName", "Make").mapToTable("ModelId", "Vehicle").mapToTable("ModelName", "Model");
            }
            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }
    }

    /**
     * 
     * @param uri
     * @return
     */
    protected SelectionBuilder buildSimpleSelection(final Uri uri) {
        final SelectionBuilder builder = new SelectionBuilder();
        final int match = matchUri(uri);

        switch (match) {
            case FUEL_HISTORY_ID:
            case MAKE_ID:
            case MODEL_ID:
            case VEHICLE_ID: {               
                final String id = uri.getPathSegments().get(1);
                final UriType uriType = uriTypes.get(match);

                return builder.table(uriType.getTableName()).where(uriType.getIdColumnName() + " = ?", id);
            }
            case FUEL_HISTORY:
            case MAKE:
            case MODEL:
            case VEHICLE: {
                final UriType uriType = uriTypes.get(match);

                return builder.table(uriType.getTableName());
            }
            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }
    }

    /**
     * 
     * @return
     */
    protected UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriTypes = new SparseArray<UriType>();
        uriTypes.put(FUEL_HISTORY, new UriType(FUEL_HISTORY, FuelHistory.TABLE_NAME, FuelHistory.TABLE_NAME, FuelHistory.TYPE_ELEM_TYPE, FuelHistory.Columns.HISTORY_ID.getName()));
        uriTypes.put(FUEL_HISTORY_ID, new UriType(FUEL_HISTORY_ID, FuelHistory.TABLE_NAME + "/#", FuelHistory.TABLE_NAME, FuelHistory.TYPE_DIR_TYPE, FuelHistory.Columns.HISTORY_ID.getName()));
        uriTypes.put(MAKE, new UriType(MAKE, Make.TABLE_NAME, Make.TABLE_NAME, Make.TYPE_ELEM_TYPE, Make.Columns.MAKE_ID.getName()));
        uriTypes.put(MAKE_ID, new UriType(MAKE_ID, Make.TABLE_NAME + "/#", Make.TABLE_NAME, Make.TYPE_DIR_TYPE, Make.Columns.MAKE_ID.getName()));
        uriTypes.put(MODEL, new UriType(MODEL, Model.TABLE_NAME, Model.TABLE_NAME, Model.TYPE_ELEM_TYPE, Model.Columns.MODEL_ID.getName()));
        uriTypes.put(MODEL_ID, new UriType(MODEL_ID, Model.TABLE_NAME + "/#", Model.TABLE_NAME, Model.TYPE_DIR_TYPE, Model.Columns.MODEL_ID.getName()));
        uriTypes.put(VEHICLE, new UriType(VEHICLE, Vehicle.TABLE_NAME, Vehicle.TABLE_NAME, Vehicle.TYPE_ELEM_TYPE, Vehicle.Columns.VEHICLE_ID.getName()));
        uriTypes.put(VEHICLE_ID, new UriType(VEHICLE_ID, Vehicle.TABLE_NAME + "/#", Vehicle.TABLE_NAME, Vehicle.TYPE_DIR_TYPE, Vehicle.Columns.VEHICLE_ID.getName()));

        for (int i = 0; i < uriTypes.size(); i += 1) {
            final int key = uriTypes.keyAt(i);
            final UriType uriType = uriTypes.get(key);

            matcher.addURI(AUTHORITY, uriType.getMatchPath(), uriType.getCode());
        }

        return matcher;
    }

    /**
     * 
     * @param uri
     * @return
     */
    protected int matchUri(final Uri uri) {
        final int match = uriMatcher.match(uri);

        if (match < 0) {
            throw new IllegalArgumentException("Unknown URI " + uri);
        }

        return match;
    }
}