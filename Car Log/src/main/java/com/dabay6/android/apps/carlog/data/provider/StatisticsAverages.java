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