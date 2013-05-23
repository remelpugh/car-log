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

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;
import com.dabay6.android.apps.carlog.data.provider.util.ColumnMetadata;

/**
 */
@SuppressWarnings("unused")
public abstract class CarLogContract {
    public static final Uri CONTENT_URI = Uri.parse("content://" + CarLogProvider.AUTHORITY);

    public CarLogContract() {
    }

    /**
     * Created in version 1
     */
    public static final class FuelHistory extends CarLogContract {
        public static final String TABLE_NAME = "FuelHistory";
        public static final Uri CONTENT_URI = Uri.parse(CarLogContract.CONTENT_URI + "/" + TABLE_NAME);
        public static final String[] PROJECTION = new String[]{
                Columns.HISTORY_ID.getName(),
                Columns.VEHICLE_ID.getName(),
                Columns.ODOMETER_READING.getName(),
                Columns.FUEL_AMOUNT.getName(),
                Columns.COST_PER_UNIT.getName(),
                Columns.TOTAL_COST.getName(),
                Columns.PURCHASE_DATE.getName(),
                Columns.NOTES.getName(),
                Columns.LATITUDE.getName(),
                Columns.LONGITUDE.getName(),
                Columns.LOCATION.getName(),
                Columns.NAME.getName()
        };
        public static final String TYPE_DIR_TYPE = "vnd.android.cursor.dir/carlog-fuelhistory";
        public static final String TYPE_ELEM_TYPE = "vnd.android.cursor.item/carlog-fuelhistory";
        private static final String LOG_TAG = FuelHistory.class.getSimpleName();

        private FuelHistory() {
            // No private constructor
        }

        public static void createTable(final SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + TABLE_NAME + " (" + Columns.HISTORY_ID.getName() + " " +
                       Columns.HISTORY_ID.getType() + ", " + Columns.VEHICLE_ID.getName() + " " +
                       Columns.VEHICLE_ID.getType() + " NOT NULL" + ", " + Columns.ODOMETER_READING.getName() + " " +
                       Columns.ODOMETER_READING.getType() + " NOT NULL" + ", " + Columns.FUEL_AMOUNT.getName() + " " +
                       Columns.FUEL_AMOUNT.getType() + " NOT NULL" + ", " + Columns.COST_PER_UNIT.getName() + " " +
                       Columns.COST_PER_UNIT.getType() + ", " + Columns.TOTAL_COST.getName() + " " +
                       Columns.TOTAL_COST.getType() + ", " + Columns.PURCHASE_DATE.getName() + " " +
                       Columns.PURCHASE_DATE.getType() + ", " + Columns.NOTES.getName() + " " +
                       Columns.NOTES.getType() + ", " + Columns.LATITUDE.getName() + " " + Columns.LATITUDE.getType() +
                       ", " + Columns.LONGITUDE.getName() + " " + Columns.LONGITUDE.getType() + ", " +
                       Columns.LOCATION.getName() + " " + Columns.LOCATION.getType() + ", PRIMARY KEY (" +
                       Columns.HISTORY_ID.getName() + ")" + ");");
        }

        // Version 1 : Creation of the table
        public static void upgradeTable(final SQLiteDatabase db, int oldVersion, final int newVersion) {
            if (oldVersion < 1) {
                Log.i(LOG_TAG, "Upgrading from version " + oldVersion + " to " + newVersion + ", data will be lost!");

                db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME + ";");
                createTable(db);
                return;
            }

            if (oldVersion != newVersion) {
                throw new IllegalStateException("Error upgrading the databaseInfo to version " + newVersion);
            }
        }

        public static enum Columns implements ColumnMetadata {
            HISTORY_ID(BaseColumns._ID, "integer"),
            VEHICLE_ID("VehicleId", "integer"),
            ODOMETER_READING("OdometerReading", "real"),
            FUEL_AMOUNT("FuelAmount", "real"),
            COST_PER_UNIT("CostPerUnit", "real"),
            TOTAL_COST("TotalCost", "real"),
            PURCHASE_DATE("PurchaseDate", "integer"),
            NOTES("Notes", "text"),
            LATITUDE("latitude", "real"),
            LONGITUDE("longitude", "real"),
            LOCATION("Location", "text"),
            NAME("Name", "text", true);
            private final Boolean isForeign;
            private final String name;
            private final String type;

            private Columns(final String name, final String type) {
                this(name, type, false);
            }

            private Columns(final String name, final String type, final Boolean isForeign) {
                this.isForeign = isForeign;
                this.name = name;
                this.type = type;
            }

            @Override
            public int getIndex() {
                return ordinal();
            }

            @Override
            public Boolean isForeign() {
                return isForeign;
            }

            @Override
            public String getName() {
                return name;
            }

            @Override
            public String getType() {
                return type;
            }
        }

        static String getBulkInsertString() {
            return new StringBuilder("INSERT INTO ").append(TABLE_NAME)
                                                    .append(" ( ")
                                                    .append(Columns.VEHICLE_ID.getName())
                                                    .append(", ")
                                                    .append(Columns.ODOMETER_READING.getName())
                                                    .append(", ")
                                                    .append(Columns.FUEL_AMOUNT.getName())
                                                    .append(", ")
                                                    .append(Columns.COST_PER_UNIT.getName())
                                                    .append(", ")
                                                    .append(Columns.TOTAL_COST.getName())
                                                    .append(", ")
                                                    .append(Columns.PURCHASE_DATE.getName())
                                                    .append(", ")
                                                    .append(Columns.NOTES.getName())
                                                    .append(", ")
                                                    .append(Columns.LATITUDE.getName())
                                                    .append(", ")
                                                    .append(Columns.LONGITUDE.getName())
                                                    .append(", ")
                                                    .append(Columns.LOCATION.getName())
                                                    .append(" ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")
                                                    .toString();
        }

        static void bindValuesInBulkInsert(final SQLiteStatement stmt, final ContentValues values) {
            int i = 1;
            String value;

            stmt.bindLong(i++, values.getAsLong(Columns.VEHICLE_ID.getName()));
            stmt.bindDouble(i++, values.getAsDouble(Columns.ODOMETER_READING.getName()));
            stmt.bindDouble(i++, values.getAsDouble(Columns.FUEL_AMOUNT.getName()));
            stmt.bindDouble(i++, values.getAsDouble(Columns.COST_PER_UNIT.getName()));
            stmt.bindDouble(i++, values.getAsDouble(Columns.TOTAL_COST.getName()));
            stmt.bindLong(i++, values.getAsLong(Columns.PURCHASE_DATE.getName()));
            value = values.getAsString(Columns.NOTES.getName());
            stmt.bindString(i++, value != null ? value : "");
            stmt.bindDouble(i++, values.getAsDouble(Columns.LATITUDE.getName()));
            stmt.bindDouble(i++, values.getAsDouble(Columns.LONGITUDE.getName()));
            value = values.getAsString(Columns.LOCATION.getName());
            stmt.bindString(i++, value != null ? value : "");
        }
    }

    /**
     * Created in version 1
     */
    public static final class Make extends CarLogContract {
        public static final String TABLE_NAME = "Make";
        public static final Uri CONTENT_URI = Uri.parse(CarLogContract.CONTENT_URI + "/" + TABLE_NAME);
        public static final String[] PROJECTION = new String[]{
                Columns.MAKE_ID.getName(), Columns.MAKE_NAME.getName()
        };
        public static final String TYPE_DIR_TYPE = "vnd.android.cursor.dir/carlog-make";
        public static final String TYPE_ELEM_TYPE = "vnd.android.cursor.item/carlog-make";
        private static final String LOG_TAG = Make.class.getSimpleName();

        private Make() {
            // No private constructor
        }

        public static void createTable(final SQLiteDatabase db) {
            db.execSQL(
                    "CREATE TABLE " + TABLE_NAME + " (" + Columns.MAKE_ID.getName() + " " + Columns.MAKE_ID.getType() +
                    ", " + Columns.MAKE_NAME.getName() + " " + Columns.MAKE_NAME.getType() + " UNIQUE" + " NOT NULL" +
                    ", PRIMARY KEY (" + Columns.MAKE_ID.getName() + ")" + ");");
            db.execSQL("CREATE INDEX Make_MakeName on " + TABLE_NAME + "(" + Columns.MAKE_NAME.getName() + ");");
        }

        // Version 1 : Creation of the table
        public static void upgradeTable(final SQLiteDatabase db, int oldVersion, final int newVersion) {
            if (oldVersion < 1) {
                Log.i(LOG_TAG, "Upgrading from version " + oldVersion + " to " + newVersion + ", data will be lost!");

                db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME + ";");
                createTable(db);
                return;
            }

            if (oldVersion != newVersion) {
                throw new IllegalStateException("Error upgrading the databaseInfo to version " + newVersion);
            }
        }

        public static enum Columns implements ColumnMetadata {
            MAKE_ID(BaseColumns._ID, "integer"),
            MAKE_NAME("MakeName", "text");
            private final Boolean isForeign;
            private final String name;
            private final String type;

            private Columns(final String name, final String type) {
                this(name, type, false);
            }

            private Columns(final String name, final String type, final Boolean isForeign) {
                this.isForeign = isForeign;
                this.name = name;
                this.type = type;
            }

            @Override
            public int getIndex() {
                return ordinal();
            }

            @Override
            public Boolean isForeign() {
                return isForeign;
            }

            @Override
            public String getName() {
                return name;
            }

            @Override
            public String getType() {
                return type;
            }
        }

        static String getBulkInsertString() {
            return new StringBuilder("INSERT INTO ").append(TABLE_NAME)
                                                    .append(" ( ")
                                                    .append(Columns.MAKE_NAME.getName())
                                                    .append(" ) VALUES (?)")
                                                    .toString();
        }

        static void bindValuesInBulkInsert(final SQLiteStatement stmt, final ContentValues values) {
            int i = 1;
            String value;

            value = values.getAsString(Columns.MAKE_NAME.getName());
            stmt.bindString(i++, value != null ? value : "");
        }
    }

    /**
     * Created in version 1
     */
    public static final class Model extends CarLogContract {
        public static final String TABLE_NAME = "Model";
        public static final Uri CONTENT_URI = Uri.parse(CarLogContract.CONTENT_URI + "/" + TABLE_NAME);
        public static final String[] PROJECTION = new String[]{
                Columns.MODEL_ID.getName(), Columns.MODEL_NAME.getName(), Columns.MAKE_ID.getName()
        };
        public static final String TYPE_DIR_TYPE = "vnd.android.cursor.dir/carlog-model";
        public static final String TYPE_ELEM_TYPE = "vnd.android.cursor.item/carlog-model";
        private static final String LOG_TAG = Model.class.getSimpleName();

        private Model() {
            // No private constructor
        }

        public static void createTable(final SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + TABLE_NAME + " (" + Columns.MODEL_ID.getName() + " " +
                       Columns.MODEL_ID.getType() + ", " + Columns.MODEL_NAME.getName() + " " +
                       Columns.MODEL_NAME.getType() + " UNIQUE" + " NOT NULL" + ", " + Columns.MAKE_ID.getName() + " " +
                       Columns.MAKE_ID.getType() + " NOT NULL" + ", PRIMARY KEY (" + Columns.MODEL_ID.getName() + ")" +
                       ");");
            db.execSQL("CREATE INDEX Model_ModelName on " + TABLE_NAME + "(" + Columns.MODEL_NAME.getName() + ");");
        }

        // Version 1 : Creation of the table
        public static void upgradeTable(final SQLiteDatabase db, int oldVersion, final int newVersion) {
            if (oldVersion < 1) {
                Log.i(LOG_TAG, "Upgrading from version " + oldVersion + " to " + newVersion + ", data will be lost!");

                db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME + ";");
                createTable(db);
                return;
            }

            if (oldVersion != newVersion) {
                throw new IllegalStateException("Error upgrading the databaseInfo to version " + newVersion);
            }
        }

        public static enum Columns implements ColumnMetadata {
            MODEL_ID(BaseColumns._ID, "integer"),
            MODEL_NAME("ModelName", "text"),
            MAKE_ID("MakeId", "integer");
            private final Boolean isForeign;
            private final String name;
            private final String type;

            private Columns(final String name, final String type) {
                this(name, type, false);
            }

            private Columns(final String name, final String type, final Boolean isForeign) {
                this.isForeign = isForeign;
                this.name = name;
                this.type = type;
            }

            @Override
            public int getIndex() {
                return ordinal();
            }

            @Override
            public Boolean isForeign() {
                return isForeign;
            }

            @Override
            public String getName() {
                return name;
            }

            @Override
            public String getType() {
                return type;
            }
        }

        static String getBulkInsertString() {
            return new StringBuilder("INSERT INTO ").append(TABLE_NAME)
                                                    .append(" ( ")
                                                    .append(Columns.MODEL_NAME.getName())
                                                    .append(", ")
                                                    .append(Columns.MAKE_ID.getName())
                                                    .append(" ) VALUES (?, ?)")
                                                    .toString();
        }

        static void bindValuesInBulkInsert(final SQLiteStatement stmt, final ContentValues values) {
            int i = 1;
            String value;

            value = values.getAsString(Columns.MODEL_NAME.getName());
            stmt.bindString(i++, value != null ? value : "");
            stmt.bindLong(i++, values.getAsLong(Columns.MAKE_ID.getName()));
        }
    }

    /**
     * Created in version 1
     */
    public static final class Vehicle extends CarLogContract {
        public static final String TABLE_NAME = "Vehicle";
        public static final Uri CONTENT_URI = Uri.parse(CarLogContract.CONTENT_URI + "/" + TABLE_NAME);
        public static final String[] PROJECTION = new String[]{
                Columns.VEHICLE_ID.getName(),
                Columns.NAME.getName(),
                Columns.YEAR.getName(),
                Columns.MAKE_ID.getName(),
                Columns.MODEL_ID.getName(),
                Columns.LICENSE_PLATE.getName(),
                Columns.VIN.getName(),
                Columns.NOTES.getName(),
                Columns.ACTIVE.getName(),
                Columns.MAKE_NAME.getName(),
                Columns.MODEL_NAME.getName()
        };
        public static final String TYPE_DIR_TYPE = "vnd.android.cursor.dir/carlog-vehicle";
        public static final String TYPE_ELEM_TYPE = "vnd.android.cursor.item/carlog-vehicle";
        private static final String LOG_TAG = Vehicle.class.getSimpleName();

        private Vehicle() {
            // No private constructor
        }

        public static void createTable(final SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + TABLE_NAME + " (" + Columns.VEHICLE_ID.getName() + " " +
                       Columns.VEHICLE_ID.getType() + ", " + Columns.NAME.getName() + " " + Columns.NAME.getType() +
                       " UNIQUE" + ", " + Columns.YEAR.getName() + " " + Columns.YEAR.getType() + ", " +
                       Columns.MAKE_ID.getName() + " " + Columns.MAKE_ID.getType() + " NOT NULL" + ", " +
                       Columns.MODEL_ID.getName() + " " + Columns.MODEL_ID.getType() + " NOT NULL" + ", " +
                       Columns.LICENSE_PLATE.getName() + " " + Columns.LICENSE_PLATE.getType() + ", " +
                       Columns.VIN.getName() + " " + Columns.VIN.getType() + ", " + Columns.NOTES.getName() + " " +
                       Columns.NOTES.getType() + ", " + Columns.ACTIVE.getName() + " " + Columns.ACTIVE.getType() +
                       ", PRIMARY KEY (" + Columns.VEHICLE_ID.getName() + ")" + ");");
        }

        // Version 1 : Creation of the table
        public static void upgradeTable(final SQLiteDatabase db, int oldVersion, final int newVersion) {
            if (oldVersion < 1) {
                Log.i(LOG_TAG, "Upgrading from version " + oldVersion + " to " + newVersion + ", data will be lost!");

                db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME + ";");
                createTable(db);
                return;
            }

            if (oldVersion != newVersion) {
                throw new IllegalStateException("Error upgrading the databaseInfo to version " + newVersion);
            }
        }

        public static enum Columns implements ColumnMetadata {
            VEHICLE_ID(BaseColumns._ID, "integer"),
            NAME("Name", "text"),
            YEAR("Year", "integer"),
            MAKE_ID("MakeId", "integer"),
            MODEL_ID("ModelId", "integer"),
            LICENSE_PLATE("LicensePlate", "text"),
            VIN("Vin", "text"),
            NOTES("Notes", "text"),
            ACTIVE("Active", "integer"),
            MAKE_NAME("MakeName", "text", true),
            MODEL_NAME("ModelName", "text", true);
            private final Boolean isForeign;
            private final String name;
            private final String type;

            private Columns(final String name, final String type) {
                this(name, type, false);
            }

            private Columns(final String name, final String type, final Boolean isForeign) {
                this.isForeign = isForeign;
                this.name = name;
                this.type = type;
            }

            @Override
            public int getIndex() {
                return ordinal();
            }

            @Override
            public Boolean isForeign() {
                return isForeign;
            }

            @Override
            public String getName() {
                return name;
            }

            @Override
            public String getType() {
                return type;
            }
        }

        static String getBulkInsertString() {
            return new StringBuilder("INSERT INTO ").append(TABLE_NAME)
                                                    .append(" ( ")
                                                    .append(Columns.NAME.getName())
                                                    .append(", ")
                                                    .append(Columns.YEAR.getName())
                                                    .append(", ")
                                                    .append(Columns.MAKE_ID.getName())
                                                    .append(", ")
                                                    .append(Columns.MODEL_ID.getName())
                                                    .append(", ")
                                                    .append(Columns.LICENSE_PLATE.getName())
                                                    .append(", ")
                                                    .append(Columns.VIN.getName())
                                                    .append(", ")
                                                    .append(Columns.NOTES.getName())
                                                    .append(", ")
                                                    .append(Columns.ACTIVE.getName())
                                                    .append(" ) VALUES (?, ?, ?, ?, ?, ?, ?, ?)")
                                                    .toString();
        }

        static void bindValuesInBulkInsert(final SQLiteStatement stmt, final ContentValues values) {
            int i = 1;
            String value;

            value = values.getAsString(Columns.NAME.getName());
            stmt.bindString(i++, value != null ? value : "");
            stmt.bindLong(i++, values.getAsLong(Columns.YEAR.getName()));
            stmt.bindLong(i++, values.getAsLong(Columns.MAKE_ID.getName()));
            stmt.bindLong(i++, values.getAsLong(Columns.MODEL_ID.getName()));
            value = values.getAsString(Columns.LICENSE_PLATE.getName());
            stmt.bindString(i++, value != null ? value : "");
            value = values.getAsString(Columns.VIN.getName());
            stmt.bindString(i++, value != null ? value : "");
            value = values.getAsString(Columns.NOTES.getName());
            stmt.bindString(i++, value != null ? value : "");
            stmt.bindLong(i++, values.getAsLong(Columns.ACTIVE.getName()));
        }
    }
}