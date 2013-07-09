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
public class StatisticsAverages extends CarLogContract {
    public static final String PATH = "statistics/averages";
    public static final Uri CONTENT_URI = Uri.parse(CarLogContract.CONTENT_URI + "/" + PATH);
    public static final String[] PROJECTION = new String[]{
            Columns.VEHICLE_ID.getName(),
            Columns.AVERAGE_FUEL_AMOUNT.getName(),
            Columns.AVERAGE_TOTAL_COST.getName(),
            Columns.AVERAGE_COST_PER_UNIT.getName(),
            Columns.AVERAGE_MILES_PER_GALLON.getName(),
            Columns.MAX_MILES_PER_GALLON.getName()
    };
    public static final String TABLE_NAME = "vw_StatisticsAverages";
    public static final String TYPE_DIR_TYPE = "vnd.android.cursor.dir/carlog-statistics-averages";
    public static final String TYPE_ELEM_TYPE = "vnd.android.cursor.item/carlog-statistics-averages";
    private static final String LOG_TAG = StatisticsTotal.class.getSimpleName();

    public static enum Columns implements ColumnMetadata {
        VEHICLE_ID("VehicleId", "integer"),
        AVERAGE_FUEL_AMOUNT("AvgFuelAmount", "integer"),
        AVERAGE_TOTAL_COST("AvgTotalCost", "integer"),
        AVERAGE_COST_PER_UNIT("AvgCostPerUnit", "integer"),
        AVERAGE_MILES_PER_GALLON("AvgMilesPerGallon", "integer"),
        MAX_MILES_PER_GALLON("MaxMilesPerGallon", "real");
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