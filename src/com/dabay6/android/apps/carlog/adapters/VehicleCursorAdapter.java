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

package com.dabay6.android.apps.carlog.adapters;

import android.R.id;
import android.annotation.TargetApi;
import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.ActionMode;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.dabay6.android.apps.carlog.R;
import com.dabay6.android.apps.carlog.data.DTO.VehicleDTO;
import com.utils.android.adapters.BaseCheckableCursorAdapter;
import com.utils.android.logging.Logger;
import com.utils.android.view.ViewsFinder;

/**
 * VehicleCursorAdapter
 *
 * @author Remel Pugh
 * @version 1.0
 */
@SuppressWarnings("unused")
public class VehicleCursorAdapter extends BaseCheckableCursorAdapter {
    private final static String TAG = Logger.makeTag(VehicleCursorAdapter.class);

    /**
     * {@inheritDoc}
     */
    public VehicleCursorAdapter(final Context context, final Bundle savedInstanceState, final Cursor c) {
        super(context, savedInstanceState, R.layout.list_item_vehicle, c);
    }

    /**
     * {@inheritDoc}
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public VehicleCursorAdapter(final Context context, final Bundle savedInstanceState, final Cursor c,
                                final int flags) {
        super(context, savedInstanceState, R.layout.list_item_vehicle, c, flags);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onActionItemClicked(final ActionMode mode, final MenuItem item) {
        final int id = item.getItemId();

        if (id == R.id.action_delete) {
            if (getOnActionModeCallbackListener() != null) {
                getOnActionModeCallbackListener().onActionDelete(mode);
            }

            return true;
        }

        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onCreateActionMode(final ActionMode mode, final Menu menu) {
        final MenuInflater inflater = ((SherlockFragmentActivity) helper.getContext()).getSupportMenuInflater();

        if (getOnActionModeCallbackListener() != null) {
            getOnActionModeCallbackListener().onActionModeCreated(mode);
        }

        inflater.inflate(R.menu.menu_delete_contextual, menu);

        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onPrepareActionMode(final ActionMode mode, final Menu menu) {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void populateData(final Context context, final View view, final Cursor cursor) {
        final ViewsFinder finder = new ViewsFinder(view);
        final TextView vehicleName = finder.find(id.text1);
        final TextView makeModel = finder.find(id.text2);
        final StringBuilder text = new StringBuilder();
        final VehicleDTO vehicle = VehicleDTO.newInstance(cursor);

        if (vehicle.getYear() > 0) {
            text.append(vehicle.getYear()).append(" ");
        }

        text.append(vehicle.getMakeName()).append(" ").append(vehicle.getModelName());

        vehicleName.setText(vehicle.getName());
        makeModel.setText(text);
    }
}