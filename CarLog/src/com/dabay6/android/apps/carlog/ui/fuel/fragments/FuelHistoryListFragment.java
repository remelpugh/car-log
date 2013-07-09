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

package com.dabay6.android.apps.carlog.ui.fuel.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import com.actionbarsherlock.view.MenuItem;
import com.dabay6.android.apps.carlog.R;
import com.dabay6.android.apps.carlog.R.menu;
import com.dabay6.android.apps.carlog.R.plurals;
import com.dabay6.android.apps.carlog.R.string;
import com.dabay6.android.apps.carlog.adapters.FuelHistoryCursorAdapter;
import com.dabay6.android.apps.carlog.configuration.SortOrder;
import com.dabay6.android.apps.carlog.data.provider.CarLogContract.FuelHistory;
import com.dabay6.android.apps.carlog.data.provider.CarLogContract.FuelHistory.Columns;
import com.dabay6.android.apps.carlog.ui.base.fragments.BaseDeleteListFragment;
import com.utils.android.adapters.BaseCheckableCursorAdapter;
import com.utils.android.logging.Logger;
import com.utils.android.util.AndroidUtils;

/**
 * @author Remel Pugh
 * @version 1.0
 */
@SuppressWarnings("unused")
public class FuelHistoryListFragment extends BaseDeleteListFragment {
    private final static String TAG = Logger.makeTag(FuelHistoryListFragment.class);
    private Long vehicleId;

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