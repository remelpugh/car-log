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
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.dabay6.android.apps.carlog.data.provider.CarLogContract.FuelHistory;
import com.dabay6.android.apps.carlog.data.provider.CarLogContract.Make;
import com.dabay6.android.apps.carlog.data.provider.CarLogContract.Model;
import com.dabay6.android.apps.carlog.data.provider.CarLogContract.Vehicle;

@SuppressWarnings("unused")
public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String LOG_TAG = DatabaseHelper.class.getSimpleName();

    /**
     *
     * @param context
     * @param name
     * @param version
     */
    public DatabaseHelper(final Context context, final String name, final int version) {
        super(context, name, null, version);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreate(final SQLiteDatabase db) {
        Log.d(LOG_TAG, "Creating CarLogProvider database");

        // Create all tables here; each class has its own method
        FuelHistory.createTable(db);
        Make.createTable(db);
        Model.createTable(db);
        Vehicle.createTable(db);
     }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onOpen(final SQLiteDatabase db) {
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
        Log.d(LOG_TAG, "Upgrading CarLogProvider database");

        // Upgrade all tables here; each class has its own method
        FuelHistory.upgradeTable(db, oldVersion, newVersion);
        Make.upgradeTable(db, oldVersion, newVersion);
        Model.upgradeTable(db, oldVersion, newVersion);
        Vehicle.upgradeTable(db, oldVersion, newVersion);
     }
}