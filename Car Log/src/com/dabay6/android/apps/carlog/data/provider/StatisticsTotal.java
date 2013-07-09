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
 * StatisticsTotal
 *
 * @author Remel Pugh
 * @version 1.0
 */
@SuppressWarnings("unused")
public class StatisticsTotal extends CarLogContract {
    public static final String PATH = "statistics/total";
    public static final Uri CONTENT_URI = Uri.parse(CarLogContract.CONTENT_URI + "/" + PATH);
    public static final String[] PROJECTION = new String[]{
            Columns.VEHICLE_ID.getName(),
            Columns.TOTAL_DISTANCE.getName(),
            Columns.TOTAL_FUEL.getName(),
            Columns.TOTAL_FUEL_COST.getName(),
            Columns.TOTAL_CURRENT_YEAR.getName(),
            Columns.TOTAL_PREVIOUS_YEAR.getName(),
            Columns.TOTAL_CURRENT_MONTH.getName(),
            Columns.TOTAL_PREVIOUS_MONTH.getName()
    };
    public static final String TABLE_NAME = "vw_StatisticsTotals";
    public static final String TYPE_DIR_TYPE = "vnd.android.cursor.dir/carlog-statistics-total";
    public static final String TYPE_ELEM_TYPE = "vnd.android.cursor.item/carlog-statistics-total";
    private static final String LOG_TAG = StatisticsTotal.class.getSimpleName();

    public static enum Columns implements ColumnMetadata {
        VEHICLE_ID("VehicleId", "integer"),
        TOTAL_DISTANCE("TotalDistance", "integer"),
        TOTAL_FUEL("TotalFuel", "integer"),
        TOTAL_FUEL_COST("TotalFuelCost", "integer"),
        TOTAL_CURRENT_MONTH("TotalCurrentMonth", "real"),
        TOTAL_PREVIOUS_MONTH("TotalPreviousMonth", "real"),
        TOTAL_CURRENT_YEAR("TotalCurrentYear", "real"),
        TOTAL_PREVIOUS_YEAR("TotalPreviousYear", "real");
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