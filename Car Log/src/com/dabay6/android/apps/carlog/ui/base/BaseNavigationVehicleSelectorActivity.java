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

package com.dabay6.android.apps.carlog.ui.base;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.OnNavigationListener;
import com.dabay6.android.apps.carlog.R;
import com.dabay6.android.apps.carlog.R.string;
import com.dabay6.android.apps.carlog.adapters.DualLineCursorAdapter;
import com.dabay6.android.apps.carlog.configuration.Intents;
import com.dabay6.android.apps.carlog.configuration.SortOrder;
import com.dabay6.android.apps.carlog.data.provider.CarLogContract.Vehicle;
import com.dabay6.android.apps.carlog.ui.vehicle.VehicleHomeActivity;
import com.dabay6.android.apps.carlog.util.NavigationDrawerUtils;
import com.utils.android.adapters.BaseNavigationListItem;
import com.utils.android.logging.Logger;
import com.utils.android.ui.BaseNavigationDrawerActivity;
import com.utils.android.util.ActionBarUtils;
import com.utils.android.util.AndroidUtils;
import com.utils.android.util.DataUtils;
import com.utils.android.util.IntentUtils.ActivityIntentBuilder;

import java.util.List;

/**
 * BaseNavigationVehicleSelectorActivity
 *
 * @author Remel Pugh
 * @version 1.0
 */
public abstract class BaseNavigationVehicleSelectorActivity extends BaseNavigationDrawerActivity
        implements LoaderManager.LoaderCallbacks<Cursor>, OnNavigationListener {
    private final static String KEY_VEHICLE_ID = "KEY_VEHICLE_ID";
    @SuppressWarnings("unused")
    private final static String TAG = Logger.makeTag(BaseNavigationVehicleSelectorActivity.class);
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
     * {@inheritDoc}
     */
    @Override
    protected List<BaseNavigationListItem> createNavigationItems() {
        return NavigationDrawerUtils.createItems(this);
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

        adapter.setDropDownViewResource(R.layout.sherlock_spinner_dropdown_item);
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