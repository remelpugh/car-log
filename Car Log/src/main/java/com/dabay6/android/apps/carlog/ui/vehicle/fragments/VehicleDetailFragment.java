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

package com.dabay6.android.apps.carlog.ui.vehicle.fragments;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.view.MenuItem;
import com.dabay6.android.apps.carlog.R;
import com.dabay6.android.apps.carlog.R.layout;
import com.dabay6.android.apps.carlog.R.menu;
import com.dabay6.android.apps.carlog.data.DTO.VehicleDTO;
import com.dabay6.android.apps.carlog.data.provider.CarLogContract.Vehicle;
import com.dabay6.android.apps.carlog.data.provider.CarLogContract.Vehicle.Columns;
import com.dabay6.android.apps.carlog.ui.base.fragments.BaseDetailFragment;
import com.dabay6.libraries.androidshared.logging.Logger;
import com.dabay6.libraries.androidshared.util.DataUtils;

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
     * Default constructor.
     */
    public VehicleDetailFragment() {
    }

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