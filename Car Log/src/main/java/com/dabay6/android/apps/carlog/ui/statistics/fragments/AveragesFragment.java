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

package com.dabay6.android.apps.carlog.ui.statistics.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import com.dabay6.android.apps.carlog.R;
import com.dabay6.android.apps.carlog.R.layout;
import com.dabay6.android.apps.carlog.R.string;
import com.dabay6.android.apps.carlog.data.provider.StatisticsAverages;
import com.dabay6.android.apps.carlog.data.provider.StatisticsAverages.Columns;
import com.dabay6.libraries.androidshared.adapters.DetailsAdapter;
import com.dabay6.libraries.androidshared.adapters.DetailsItem;
import com.dabay6.libraries.androidshared.adapters.DetailsItemList;
import com.dabay6.libraries.androidshared.logging.Logger;
import com.dabay6.libraries.androidshared.ui.fragments.BaseListFragment;
import com.dabay6.libraries.androidshared.util.DataUtils;
import com.dabay6.libraries.androidshared.util.StringUtils;

import java.text.NumberFormat;

/**
 * AveragesFragment
 *
 * @author Remel Pugh
 * @version 1.0
 */
public class AveragesFragment extends BaseListFragment implements LoaderCallbacks<Cursor> {
    private final static int DATA_LIST_ID = 0x01;
    @SuppressWarnings("unused")
    private final static String TAG = Logger.makeTag(AveragesFragment.class);

    /**
     * Default constructor.
     */
    public AveragesFragment() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Loader<Cursor> onCreateLoader(final int id, final Bundle args) {
        return new CursorLoader(getActivity(), StatisticsAverages.CONTENT_URI, StatisticsAverages.PROJECTION,
                                null, null, null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onLoadFinished(final Loader<Cursor> loader, final Cursor cursor) {
        final DetailsAdapter adapter;
        final DetailsItemList items = new DetailsItemList();

        if (DataUtils.hasData(cursor)) {
            final String[] columnNames = cursor.getColumnNames();

            for (final String columnName : columnNames) {
                if (!columnName.equalsIgnoreCase(Columns.VEHICLE_ID.getName())) {
                    final DetailsItem item;
                    final int index = cursor.getColumnIndex(columnName);
                    final String text;
                    final float value = cursor.getFloat(index);

                    if (columnName.equalsIgnoreCase(Columns.AVERAGE_FUEL_AMOUNT.getName()) ||
                        columnName.equalsIgnoreCase(Columns.AVERAGE_MILES_PER_GALLON.getName()) ||
                        columnName.equalsIgnoreCase(Columns.MAX_MILES_PER_GALLON.getName())) {
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

        adapter = new DetailsAdapter(getActivity(), items, layout.list_item_statistics);

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
    public void onLoaderReset(final Loader<Cursor> loader) {
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
    protected void setupListView(final Bundle savedInstanceState) {
        setEmptyText(getString(string.statistics_empty));

        getLoaderManager().initLoader(DATA_LIST_ID, null, this);

        setListShown(false);
    }
}