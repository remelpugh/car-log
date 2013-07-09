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

package com.dabay6.android.apps.carlog.ui.vehicle.fragments;

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
import com.dabay6.android.apps.carlog.adapters.VehicleCursorAdapter;
import com.dabay6.android.apps.carlog.configuration.SortOrder;
import com.dabay6.android.apps.carlog.data.provider.CarLogContract.Vehicle;
import com.dabay6.android.apps.carlog.ui.base.fragments.BaseDeleteListFragment;
import com.utils.android.adapters.BaseCheckableCursorAdapter;
import com.utils.android.logging.Logger;
import com.utils.android.util.AndroidUtils;

/**
 * @author Remel Pugh
 * @version 1.0
 */
@SuppressWarnings("unused")
public class VehicleListFragment extends BaseDeleteListFragment {
    private final static String TAG = Logger.makeTag(VehicleListFragment.class);

    /**
     * @return A new instance of {@link VehicleListFragment}.
     */
    public static VehicleListFragment newInstance() {
        return new VehicleListFragment();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Loader<Cursor> onCreateLoader(final int i, final Bundle bundle) {
        return new CursorLoader(getSherlockActivity(), Vehicle.CONTENT_URI, Vehicle.PROJECTION, null, null,
                                SortOrder.DEFAULT_VEHICLE_ORDER.toString());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        final int id = item.getItemId();

        switch (id) {
            case R.id.menu_vehicle_add: {
                onEntityListListener.onEntityAdd();
            }
            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected BaseCheckableCursorAdapter createListAdapter(final Bundle savedInstanceState) {
        if (AndroidUtils.isAtLeastHoneycomb()) {
            return new VehicleCursorAdapter(applicationContext, savedInstanceState, null,
                                            CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        }
        else {
            return new VehicleCursorAdapter(applicationContext, savedInstanceState, null);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected int getDeleteMessageResourceId() {
        return plurals.vehicle_delete;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected int getEmptyTextResourceId() {
        return string.vehicle_empty;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Integer getMenuResourceId() {
        return menu.menu_vehicle_list;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Integer getTitleResourceId() {
        return R.string.vehicle_select;
    }
}