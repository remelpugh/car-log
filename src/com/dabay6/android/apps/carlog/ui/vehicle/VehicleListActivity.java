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

package com.dabay6.android.apps.carlog.ui.vehicle;

import android.os.Bundle;
import com.actionbarsherlock.view.MenuItem;
import com.dabay6.android.apps.carlog.R;
import com.dabay6.android.apps.carlog.R.id;
import com.dabay6.android.apps.carlog.R.layout;
import com.dabay6.android.apps.carlog.R.menu;
import com.dabay6.android.apps.carlog.data.provider.CarLogContract.FuelHistory;
import com.dabay6.android.apps.carlog.data.provider.CarLogContract.Vehicle;
import com.dabay6.android.apps.carlog.data.provider.CarLogContract.Vehicle.Columns;
import com.dabay6.android.apps.carlog.ui.base.fragments.BaseDeleteListFragment.OnEntityListListener;
import com.dabay6.android.apps.carlog.ui.vehicle.fragments.VehicleDetailFragment;
import com.dabay6.android.apps.carlog.ui.vehicle.fragments.VehicleListFragment;
import com.utils.android.app.FragmentFinder;
import com.utils.android.logging.Logger;
import com.utils.android.ui.BaseFragmentActivity;

import java.util.List;

/**
 * VehicleListActivity
 *
 * @author Remel Pugh
 * @version 1.0
 */
public class VehicleListActivity extends BaseFragmentActivity implements OnEntityListListener {
    @SuppressWarnings("unused")
    private final static String TAG = Logger.makeTag(VehicleListActivity.class);
    private VehicleDetailFragment detail;
    private FragmentFinder finder;
    private VehicleListFragment list;

    /**
     * {@inheritDoc}
     */
    @Override
    public void onEntityAdd() {
        VehicleEditActivity.startActivityForAdd(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onEntityDelete(final List<Long> ids) {
        if (ids.size() > 0) {
            if (list != null) {
                list.setListShown(false);
            }

            for (final long id : ids) {
                final String idValue = Long.toString(id);
                String[] selectionArgs;
                String where;

                where = FuelHistory.Columns.VEHICLE_ID.getName() + " = ?";
                selectionArgs = new String[]{idValue};

                getContentResolver().delete(FuelHistory.CONTENT_URI, where, selectionArgs);

                where = Columns.VEHICLE_ID.getName() + " = ?";
                selectionArgs = new String[]{idValue};

                getContentResolver().delete(Vehicle.CONTENT_URI, where, selectionArgs);
            }

            if (list != null) {
                list.setListShown(true);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onEntitySelected(final int position, final long id) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        final int id = item.getItemId();

        switch (id) {
            case R.id.menu_vehicle_add: {
                VehicleEditActivity.startActivityForAdd(this);
            }
            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }

    //    /**
    //     * {@inheritDoc}
    //     */
    //    @Override
    //    public void onVehicleSelected(final int index) {
    //        if (isDualPane) {
    //            final VehicleCursorAdapter adapter = (VehicleCursorAdapter) list.getListAdapter();
    //            final Cursor cursor = (Cursor) adapter.getItem(index);
    //
    //            if (cursor != null) {
    //                final VehicleDTO vehicle = VehicleDTO.newInstance(cursor);
    //
    //                detail.loadDetails(vehicle.getVehicleId());
    //            }
    //        }
    //        else {
    //            //            VehicleEditActivity.startActivityForEdit(this, id);
    //        }
    //    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void afterViews(final Bundle savedInstanceState) {
        finder = new FragmentFinder(this);

        detail = finder.find(id.vehicle_details);
        list = finder.find(id.vehicle_list);

        //        isDualPane = detail != null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Integer getLayoutResource() {
        //return layout.activity_vehicle_list;
        return layout.activity_vehicle_home;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Integer getMenuResource() {
        return menu.menu_vehicle_list;
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