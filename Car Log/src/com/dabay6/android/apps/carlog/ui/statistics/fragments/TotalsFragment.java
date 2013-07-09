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

package com.dabay6.android.apps.carlog.ui.statistics.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import com.dabay6.android.apps.carlog.R;
import com.dabay6.android.apps.carlog.R.layout;
import com.dabay6.android.apps.carlog.R.string;
import com.dabay6.android.apps.carlog.data.provider.StatisticsTotal;
import com.dabay6.android.apps.carlog.data.provider.StatisticsTotal.Columns;
import com.utils.android.logging.Logger;
import com.utils.android.ui.fragments.BaseListFragment;
import com.utils.android.util.DataUtils;
import com.utils.android.util.ListUtils;
import com.utils.android.util.StringUtils;
import com.utils.android.adapters.DetailsAdapter;
import com.utils.android.adapters.DetailsItem;

import java.text.NumberFormat;
import java.util.List;

/**
 * TotalsFragment
 *
 * @author Remel Pugh
 * @version 1.0
 */
public class TotalsFragment extends BaseListFragment implements LoaderCallbacks<Cursor> {
    private final static int DATA_LIST_ID = 0x01;
    @SuppressWarnings("unused")
    private final static String TAG = Logger.makeTag(TotalsFragment.class);

    /**
     * {@inheritDoc}
     */
    @Override
    public Loader<Cursor> onCreateLoader(final int id, final Bundle args) {
        return new CursorLoader(getSherlockActivity(), StatisticsTotal.CONTENT_URI, StatisticsTotal.PROJECTION, null,
                                null, null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onLoadFinished(final Loader<Cursor> cursorLoader, final Cursor cursor) {
        final DetailsAdapter adapter;
        final List<DetailsItem> items = ListUtils.newList();

        if (DataUtils.hasData(cursor)) {
            final String[] columnNames = cursor.getColumnNames();

            for (final String columnName : columnNames) {
                if (!columnName.equalsIgnoreCase(Columns.VEHICLE_ID.getName())) {
                    final DetailsItem item;
                    final int index = cursor.getColumnIndex(columnName);
                    final String text;
                    final float value = cursor.getFloat(index);

                    if (columnName.equalsIgnoreCase(Columns.TOTAL_DISTANCE.getName())) {
                        text = String.format(getString(R.string.fuel_history_miles), value);
                    }
                    else if (columnName.equalsIgnoreCase(Columns.TOTAL_FUEL.getName())) {
                        text = String.format(getString(R.string.gallons), value);
                    }
                    else {
                        final NumberFormat currency = NumberFormat.getCurrencyInstance();

                        text = currency.format(cursor.getFloat(index));
                    }

                    item = new DetailsItem(StringUtils.splitIntoWords(columnName), text);

                    items.add(item);
                }
            }
        }

        adapter = new DetailsAdapter(getSherlockActivity(), items, layout.list_item_statistics);

        setListAdapter(adapter);

        if (isResumed()) {
            setListShown(true);
        }
        else {
            setListShownNoAnimation(true);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onLoaderReset(final Loader<Cursor> cursorLoader) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Integer getMenuResourceId() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("deprecation")
    protected void setupListView(final Bundle savedInstanceState) {
        setEmptyText(getString(string.statistics_empty));

        getLoaderManager().initLoader(DATA_LIST_ID, null, this);

        setListShown(false);
    }
}