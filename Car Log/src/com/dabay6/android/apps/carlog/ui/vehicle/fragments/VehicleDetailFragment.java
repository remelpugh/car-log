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
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.Loader;
import com.actionbarsherlock.view.MenuItem;
import com.dabay6.android.apps.carlog.R;
import com.dabay6.android.apps.carlog.R.layout;
import com.dabay6.android.apps.carlog.R.menu;
import com.dabay6.android.apps.carlog.data.DTO.VehicleDTO;
import com.dabay6.android.apps.carlog.data.provider.CarLogContract.Vehicle;
import com.dabay6.android.apps.carlog.data.provider.CarLogContract.Vehicle.Columns;
import com.dabay6.android.apps.carlog.ui.base.fragments.BaseDetailFragment;
import com.utils.android.logging.Logger;
import com.utils.android.util.DataUtils;

/**
 * VehicleDetailFragment
 *
 * @author Remel Pugh
 * @version 1.0
 */
@SuppressWarnings("unused")
public class VehicleDetailFragment extends BaseDetailFragment {
    private final static String TAG = Logger.makeTag(VehicleDetailFragment.class);
    private VehicleDTO vehicle;

    /**
     *
     * @return
     */
    public static VehicleDetailFragment newInstance() {
        return newInstance(null);
    }

    /**
     *
     * @param id
     * @return
     */
    public static VehicleDetailFragment newInstance(final Long id) {
        final Bundle arguments = new Bundle();
        final VehicleDetailFragment fragment = new VehicleDetailFragment();

        if (id != null) {
            arguments.putLong(PARAMS_ENTITY_ID, id);
        }

        fragment.setArguments(arguments);

        return fragment;
    }

    //    /**
    //     *
    //     * @param id
    //     * @param title
    //     * @return
    //     */
    //    public static VehicleDetailFragment newInstance(final Long id, final String title) {
    //        final Bundle arguments = new Bundle();
    //        final VehicleDetailFragment fragment = new VehicleDetailFragment();
    //
    //        if (id != null) {
    //            arguments.putLong(PARAMS_ENTITY_ID, id);
    //        }
    //
    //        if (!TextUtils.isEmpty(title)) {
    //            arguments.putString(PARAMS_TITLE, title);
    //        }
    //
    //        fragment.setArguments(arguments);
    //
    //        return fragment;
    //    }

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
    public boolean onOptionsItemSelected(final MenuItem item) {
        final int id = item.getItemId();

        switch (id) {
            case R.id.menu_vehicle_edit: {
                onEntityDetailListener.onEntityEdit(entityId);
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
    @SuppressWarnings("unchecked")
    protected void createListItems(final Cursor cursor) {
        if (DataUtils.hasData(cursor)) {
            vehicle = VehicleDTO.newInstance(cursor);

            setTitle(vehicle.getName());

            if (isDualPane()) {
                adapter.clear();
            }

            adapter.add(createDetailItem(Columns.NAME, vehicle.getName()));
            adapter.add(createDetailItem(Columns.YEAR, vehicle.getYear().toString()));
            adapter.add(createDetailItem(Columns.MAKE_NAME, vehicle.getMakeName()));
            adapter.add(createDetailItem(Columns.MODEL_NAME, vehicle.getModelName()));
            adapter.add(createDetailItem(Columns.LICENSE_PLATE, vehicle.getLicensePlate()));
            adapter.add(createDetailItem(Columns.VIN, vehicle.getVin()));
            adapter.add(createDetailItem(Columns.NOTES, vehicle.getNotes()));

            getActivity().supportInvalidateOptionsMenu();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected int getEmptyTextResourceId() {
        return R.string.vehicle_detail_empty;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getIdentityColumnName() {
        return Vehicle.Columns.VEHICLE_ID.getName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected int getListItemResourceId() {
        return layout.list_item_vehicle_detail;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Integer getMenuResourceId() {
        if (entityId != null && entityId != 0) {
            return menu.menu_vehicle_detail;
        }

        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String[] getProjection() {
        return Vehicle.PROJECTION;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Uri getUri() {
        return Vehicle.CONTENT_URI;
    }
}