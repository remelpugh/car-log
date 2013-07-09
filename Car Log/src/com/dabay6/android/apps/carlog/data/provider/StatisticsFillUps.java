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

import android.net.Uri;
import com.dabay6.android.apps.carlog.data.provider.util.ColumnMetadata;

/**
 * StatisticsFillUps
 *
 * @author Remel Pugh
 * @version 1.0
 */
@SuppressWarnings("unused")
public class StatisticsFillUps extends CarLogContract {
    public static final String PATH = "statistics/fillups";
    public static final Uri CONTENT_URI = Uri.parse(CarLogContract.CONTENT_URI + "/" + PATH);
    public static final String[] PROJECTION = new String[]{
            Columns.VEHICLE_ID.getName(),
            Columns.TOTAL_FILL_UPS.getName(),
            Columns.MAX_FUEL_AMOUNT.getName(),
            Columns.MIN_FUEL_AMOUNT.getName(),
            Columns.MAX_TOTAL_COST.getName(),
            Columns.MIN_TOTAL_COST.getName(),
            Columns.MAX_COST_PER_UNIT.getName(),
            Columns.MIN_COST_PER_UNIT.getName(),
            Columns.MAX_COST_PER_MILE.getName(),
            Columns.MIN_COST_PER_MILE.getName()
    };
    public static final String TABLE_NAME = "vw_StatisticsFillups";
    public static final String TYPE_DIR_TYPE = "vnd.android.cursor.dir/carlog-statistics-fillups";
    public static final String TYPE_ELEM_TYPE = "vnd.android.cursor.item/carlog-statistics-fillups";
    private static final String LOG_TAG = StatisticsTotal.class.getSimpleName();

    public static enum Columns implements ColumnMetadata {
        VEHICLE_ID("VehicleId", "integer"),
        TOTAL_FILL_UPS("TotalFillUps", "integer"),
        MAX_FUEL_AMOUNT("MaxFuelAmount", "integer"),
        MIN_FUEL_AMOUNT("MinFuelAmount", "integer"),
        MAX_TOTAL_COST("MaxTotalCost", "real"),
        MIN_TOTAL_COST("MinTotalCost", "real"),
        MAX_COST_PER_UNIT("MaxCostPerUnit", "real"),
        MIN_COST_PER_UNIT("MinCostPerUnit", "real"),
        MAX_COST_PER_MILE("MaxCostPerMile", "real"),
        MIN_COST_PER_MILE("MinCostPerMile", "real");
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
        public String getName() {
            return name;
        }

        @Override
        public String getType() {
            return type;
        }

        @Override
        public Boolean isForeign() {
            return isForeign;
        }
    }
}