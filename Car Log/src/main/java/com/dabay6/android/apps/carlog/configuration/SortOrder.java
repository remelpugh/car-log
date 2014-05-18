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