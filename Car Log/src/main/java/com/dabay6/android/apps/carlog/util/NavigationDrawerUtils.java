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

package com.dabay6.android.apps.carlog.util;

import android.content.Context;
import com.dabay6.android.apps.carlog.R.drawable;
import com.dabay6.android.apps.carlog.R.string;
import com.dabay6.android.apps.carlog.ui.HomeActivity;
import com.dabay6.android.apps.carlog.ui.statistics.StatisticsActivity;
import com.dabay6.android.apps.carlog.ui.vehicle.VehicleHomeActivity;
import com.dabay6.libraries.androidshared.adapters.BaseNavigationListItem;
import com.dabay6.libraries.androidshared.adapters.NavigationDrawerItem;
import com.dabay6.libraries.androidshared.util.IntentUtils;
import com.dabay6.libraries.androidshared.util.ListUtils;

import java.util.List;

/**
 * NavigationDrawerUtils
 *
 * @author Remel Pugh
 * @version 1.0
 */
public class NavigationDrawerUtils {
    public static final int HOME = 0;
    public static final int STATISTICS = 2;
    public static final int VEHICLES = 1;

    /**
     *
     * @param context
     * @return
     */
    public static List<BaseNavigationListItem> createItems(final Context context) {
        final List<BaseNavigationListItem> items = ListUtils.newList();

        items.add(new NavigationDrawerItem(context.getString(string.home), drawable.ic_action_home));
        items.add(new NavigationDrawerItem(context.getString(string.vehicles), drawable.ic_action_car));
        items.add(new NavigationDrawerItem(context.getString(string.statistics)));

        return items;
    }

    /**
     *
     * @param context
     * @param position
     * @param currentPosition
     */
    public static void navigate(final Context context, final int position, final int currentPosition) {
        if (position == currentPosition) {
            return;
        }

        switch (position) {
            case HOME: {
                IntentUtils.createActivityIntent(context, HomeActivity.class).clearTop().start();
                break;
            }
            case VEHICLES: {
                IntentUtils.createActivityIntent(context, VehicleHomeActivity.class).start();
                break;
            }
            case STATISTICS: {
                IntentUtils.createActivityIntent(context, StatisticsActivity.class).start();
                break;
            }
        }
    }
}