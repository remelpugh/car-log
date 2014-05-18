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

package com.dabay6.android.apps.carlog.ui.base;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.OnNavigationListener;
import com.dabay6.android.apps.carlog.R;
import com.dabay6.android.apps.carlog.R.string;
import com.dabay6.android.apps.carlog.adapters.DualLineCursorAdapter;
import com.dabay6.android.apps.carlog.configuration.Intents;
import com.dabay6.android.apps.carlog.configuration.SortOrder;
import com.dabay6.android.apps.carlog.data.provider.CarLogContract.Vehicle;
import com.dabay6.android.apps.carlog.ui.vehicle.VehicleHomeActivity;
import com.dabay6.libraries.androidshared.logging.Logger;
import com.dabay6.libraries.androidshared.ui.BaseNavigationDrawerActivity;
import com.dabay6.libraries.androidshared.util.ActionBarUtils;
import com.dabay6.libraries.androidshared.util.AndroidUtils;
import com.dabay6.libraries.androidshared.util.DataUtils;
import com.dabay6.libraries.androidshared.util.IntentUtils.ActivityIntentBuilder;

/**
 * BaseVehicleSelectorActivity
 *
 * @author Remel Pugh
 * @version 1.0
 */
public abstract class BaseVehicleSelectorActivity extends BaseNavigationDrawerActivity
        implements LoaderManager.LoaderCallbacks<Cursor>, OnNavigationListener {
    private final static String KEY_VEHICLE_ID = "KEY_VEHICLE_ID";
    @SuppressWarnings("unused")
    private final static String TAG = Logger.makeTag(BaseVehicleSelectorActivity.class);
    private final static int VEHICLE_LOADER_ID = 0;
    private DualLineCursorAdapter adapter;
    private Long vehicleId = null;

    /**
     * {@inheritDoc}
     */
    @Override
    public Loader<Cursor> onCreateLoader(final int i, final Bundle bundle) {
        return new CursorLoader(getBaseContext(), Vehicle.CONTENT_URI, Vehicle.PROJECTION, null, null,
                                SortOrder.DEFAULT_VEHICLE_ORDER.toString());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onLoadFinished(final Loader<Cursor> cursorLoader, final Cursor cursor) {
        if (DataUtils.hasData(cursor)) {
            ActionBarUtils.configureListNavigation(this);

            adapter.swapCursor(cursor);
        }
        else {
            final ActivityIntentBuilder builder = new ActivityIntentBuilder(this, VehicleHomeActivity.class);

            builder.get().putExtra(Intents.INTENT_EXTRA_PARENT, getIntent());
            builder.get().putExtra(VehicleHomeActivity.VEHICLE_EXTRA_NONE, true);
            builder.start();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onLoaderReset(final Loader<Cursor> cursorLoader) {
        adapter.swapCursor(null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onNavigationItemSelected(final int itemPosition, final long itemId) {
        vehicleId = itemId;

        onVehicleSelected(vehicleId);

        return true;
    }

    /**
     *
     * @return
     */
    protected Long getVehicleId() {
        return vehicleId;
    }

    /**
     *
     * @return
     */
    protected boolean isLoadedOnCreate() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onConfigureActionBar() {
        final ActionBar actionBar = getSupportActionBar();
        final Context context = actionBar.getThemedContext();

        super.onConfigureActionBar();

        if (AndroidUtils.isAtLeastHoneycomb()) {
            adapter = new DualLineCursorAdapter(context, null, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        }
        else {
            adapter = new DualLineCursorAdapter(context, null);
        }

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter.setTitle(string.fuel_history);

        actionBar.setListNavigationCallbacks(adapter, this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(KEY_VEHICLE_ID)) {
                vehicleId = savedInstanceState.getLong(KEY_VEHICLE_ID);
                vehicleId = (vehicleId == -1) ? null : vehicleId;
            }
        }

        super.onCreate(savedInstanceState);

        if (isLoadedOnCreate()) {
            startLoader();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        outState.putLong(KEY_VEHICLE_ID, vehicleId == null ? -1 : vehicleId);

        super.onSaveInstanceState(outState);
    }

    /**
     *
     * @param vehicleId
     */
    protected abstract void onVehicleSelected(final long vehicleId);

    /**
     *
     */
    protected void startLoader() {
        getSupportLoaderManager().restartLoader(VEHICLE_LOADER_ID, null, this);
    }
}