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

package com.dabay6.android.apps.carlog.ui.fuel.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.view.MenuItem;
import com.dabay6.android.apps.carlog.R;
import com.dabay6.android.apps.carlog.R.menu;
import com.dabay6.android.apps.carlog.R.plurals;
import com.dabay6.android.apps.carlog.R.string;
import com.dabay6.android.apps.carlog.adapters.FuelHistoryCursorAdapter;
import com.dabay6.android.apps.carlog.configuration.SortOrder;
import com.dabay6.android.apps.carlog.data.provider.CarLogContract.FuelHistory;
import com.dabay6.android.apps.carlog.data.provider.CarLogContract.FuelHistory.Columns;
import com.dabay6.android.apps.carlog.ui.base.fragments.BaseDeleteListFragment;
import com.dabay6.libraries.androidshared.adapters.BaseCheckableCursorAdapter;
import com.dabay6.libraries.androidshared.logging.Logger;
import com.dabay6.libraries.androidshared.util.AndroidUtils;

/**
 * @author Remel Pugh
 * @version 1.0
 */
@SuppressWarnings("unused")
public class FuelHistoryListFragment extends BaseDeleteListFragment {
    private final static String TAG = Logger.makeTag(FuelHistoryListFragment.class);
    private Long vehicleId;

    /**
     * Default constructor.
     */
    public FuelHistoryListFragment() {
    }

    /**
     * @return A new instance of {@link FuelHistoryListFragment}.
     */
    public static FuelHistoryListFragment newInstance() {
        return newInstance(true);
    }

    /**
     * @return A new instance of {@link FuelHistoryListFragment}.
     */
    public static FuelHistoryListFragment newInstance(final boolean isDataQueryAllowed) {
        final FuelHistoryListFragment fragment = new FuelHistoryListFragment();
        final Bundle arguments = new Bundle();

        arguments.putBoolean(KEY_IS_DATA_QUERY_ALLOWED, isDataQueryAllowed);
        fragment.setArguments(arguments);

        return fragment;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Loader<Cursor> onCreateLoader(final int i, final Bundle bundle) {
        final String selection = Columns.VEHICLE_ID.getName() + " = ?";
        final String[] selectionArgs;

        selectionArgs = (vehicleId == null) ? new String[]{"-1"} : new String[]{vehicleId.toString()};

        return new CursorLoader(getActivity(), FuelHistory.CONTENT_URI, FuelHistory.PROJECTION, selection,
                                selectionArgs, SortOrder.DEFAULT_FUEL_HISTORY_ORDER.toString());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        final int id = item.getItemId();

        switch (id) {
            case R.id.menu_fuel_history_add: {
                onEntityListListener.onEntityAdd();
                return true;
            }
            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }

    /**
     *
     * @param vehicleId
     */
    public void refresh(final Long vehicleId) {
        this.vehicleId = vehicleId;

        refresh();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected BaseCheckableCursorAdapter createListAdapter(final Bundle savedInstanceState) {
        if (AndroidUtils.isAtLeastHoneycomb()) {
            return new FuelHistoryCursorAdapter(applicationContext, savedInstanceState, null,
                                                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        }
        else {
            return new FuelHistoryCursorAdapter(applicationContext, savedInstanceState, null);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected int getDeleteMessageResourceId() {
        return plurals.fuel_history_delete;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected int getEmptyTextResourceId() {
        return string.fuel_history_empty;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Integer getMenuResourceId() {
        return menu.menu_fuel_history_list;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Integer getTitleResourceId() {
        return string.fuel_history_select;
    }
}