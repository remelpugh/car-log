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