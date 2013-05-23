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

package com.dabay6.android.apps.carlog.ui.statistics;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.dabay6.android.apps.carlog.R.array;
import com.dabay6.android.apps.carlog.ui.statistics.fragments.AveragesFragment;
import com.dabay6.android.apps.carlog.ui.statistics.fragments.FillUpsFragment;
import com.dabay6.android.apps.carlog.ui.statistics.fragments.TotalsFragment;
import com.utils.android.ui.BaseFragmentTabNavigationActivity;
import com.utils.android.util.ListUtils;

import java.util.List;

/**
 * StatisticsActivity
 *
 * @author Remel Pugh
 * @version 1.0
 */
public class StatisticsActivity extends BaseFragmentTabNavigationActivity {

    /**
     * {@inheritDoc}
     */
    @Override
    public void onTabReselected(final Tab tab, final FragmentTransaction ft) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onTabUnselected(final Tab tab, final FragmentTransaction ft) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void afterViews(final Bundle savedInstanceState) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected List<NavigationTab> generateTabs(final List<String> tabNames) {
        final List<NavigationTab> tabs = ListUtils.newList(tabNames.size());

        for (final String tabName : tabNames) {
            final NavigationTab info;
            Class<?> fragment = null;

            if (tabName.equalsIgnoreCase("averages")) {
                fragment = AveragesFragment.class;
            }
            else if (tabName.equalsIgnoreCase("fill-ups")) {
                fragment = FillUpsFragment.class;
            }
            else if (tabName.equalsIgnoreCase("totals")) {
                fragment = TotalsFragment.class;
            }

            if (fragment != null) {
                info = new NavigationTab(fragment, null, tabName);

                tabs.add(info);
            }
        }

        return tabs;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Integer getNavigationResource() {
        return array.statistics_tabs;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isHomeAsUpEnabled() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isTitleEnabled() {
        return true;
    }
}