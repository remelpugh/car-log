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

package com.dabay6.android.apps.carlog.adapters;

import android.R.id;
import android.annotation.TargetApi;
import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import com.dabay6.android.apps.carlog.R;
import com.dabay6.android.apps.carlog.data.DTO.VehicleDTO;
import com.dabay6.libraries.androidshared.adapters.BaseCheckableCursorAdapter;
import com.dabay6.libraries.androidshared.logging.Logger;
import com.dabay6.libraries.androidshared.view.ViewsFinder;

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
        final MenuInflater inflater = ((FragmentActivity) helper.getContext()).getMenuInflater();

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