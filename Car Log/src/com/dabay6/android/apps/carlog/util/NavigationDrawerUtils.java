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

package com.dabay6.android.apps.carlog.util;

import android.content.Context;
import com.dabay6.android.apps.carlog.R.drawable;
import com.dabay6.android.apps.carlog.R.string;
import com.dabay6.android.apps.carlog.ui.HomeActivity;
import com.dabay6.android.apps.carlog.ui.statistics.StatisticsActivity;
import com.dabay6.android.apps.carlog.ui.vehicle.VehicleHomeActivity;
import com.utils.android.adapters.BaseNavigationListItem;
import com.utils.android.adapters.NavigationDrawerItem;
import com.utils.android.util.IntentUtils;
import com.utils.android.util.ListUtils;

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