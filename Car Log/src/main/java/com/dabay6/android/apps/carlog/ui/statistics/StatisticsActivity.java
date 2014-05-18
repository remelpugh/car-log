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

package com.dabay6.android.apps.carlog.ui.statistics;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar.Tab;
import com.dabay6.android.apps.carlog.R.array;
import com.dabay6.android.apps.carlog.ui.statistics.fragments.AveragesFragment;
import com.dabay6.android.apps.carlog.ui.statistics.fragments.FillUpsFragment;
import com.dabay6.android.apps.carlog.ui.statistics.fragments.TotalsFragment;
import com.dabay6.libraries.androidshared.ui.BaseFragmentTabNavigationActivity;
import com.dabay6.libraries.androidshared.util.ListUtils;

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