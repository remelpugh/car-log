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

package com.dabay6.android.apps.carlog.configuration;

import com.dabay6.android.apps.carlog.data.provider.CarLogContract.FuelHistory;
import com.dabay6.android.apps.carlog.data.provider.CarLogContract.Vehicle;

/**
 * SortOrder
 *
 * @author Remel Pugh
 * @version 1.0
 */
public final class SortOrder {
    /**
     *
     */
    public static final StringBuilder DEFAULT_FUEL_HISTORY_ORDER;
    /**
     *
     */
    public static final StringBuilder DEFAULT_VEHICLE_ORDER;

    static {
        DEFAULT_FUEL_HISTORY_ORDER = new StringBuilder();
        DEFAULT_FUEL_HISTORY_ORDER.append(FuelHistory.Columns.PURCHASE_DATE.getName()).append(" DESC");

        DEFAULT_VEHICLE_ORDER = new StringBuilder();
        DEFAULT_VEHICLE_ORDER.append(Vehicle.Columns.YEAR.getName())
                             .append(" DESC, ")
                             .append(Vehicle.Columns.MAKE_NAME.getName())
                             .append(" ASC, ")
                             .append(Vehicle.Columns.MODEL_NAME.getName())
                             .append(" ASC");
    }
}