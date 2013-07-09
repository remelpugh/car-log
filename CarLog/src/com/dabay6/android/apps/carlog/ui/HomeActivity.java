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

package com.dabay6.android.apps.carlog.ui;

import android.R.anim;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.dabay6.android.apps.carlog.R;
import com.dabay6.android.apps.carlog.R.id;
import com.dabay6.android.apps.carlog.R.menu;
import com.dabay6.android.apps.carlog.adapters.FuelHistoryCursorAdapter;
import com.dabay6.android.apps.carlog.app.InitializationIntentService;
import com.dabay6.android.apps.carlog.configuration.Intents;
import com.dabay6.android.apps.carlog.configuration.SharedPreferenceKeys;
import com.dabay6.android.apps.carlog.data.provider.CarLogContract.FuelHistory;
import com.dabay6.android.apps.carlog.ui.base.BaseNavigationVehicleSelectorActivity;
import com.dabay6.android.apps.carlog.ui.base.fragments.BaseDeleteListFragment.OnEntityListListener;
import com.dabay6.android.apps.carlog.ui.base.fragments.BaseDetailFragment.OnEntityDetailListener;
import com.dabay6.android.apps.carlog.ui.base.fragments.BaseEditFragment.OnEntityEditListener;
import com.dabay6.android.apps.carlog.ui.fuel.fragments.FuelHistoryDetailFragment;
import com.dabay6.android.apps.carlog.ui.fuel.fragments.FuelHistoryEditFragment;
import com.dabay6.android.apps.carlog.ui.fuel.fragments.FuelHistoryListFragment;
import com.dabay6.android.apps.carlog.util.NavigationDrawerUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.utils.android.helper.SharedPreferencesHelper;
import com.utils.android.logging.Logger;
import com.utils.android.ui.dialogs.DateTimePickerDialogFragment.OnDateTimePickerListener;
import com.utils.android.ui.dialogs.changelog.OnChangeLogDialogListener;
import com.utils.android.ui.dialogs.changelog.util.ChangeLogDialogUtils;
import com.utils.android.util.ActionBarUtils;
import com.utils.android.util.IntentUtils;

import java.util.List;
import java.util.Set;

/**
 * HomeActivity
 *
 * @author Remel Pugh
 * @version 1.0
 */
public class HomeActivity extends BaseNavigationVehicleSelectorActivity implements OnChangeLogDialogListener,
                                                                                   OnDateTimePickerListener,
                                                                                   OnEntityDetailListener,
                                                                                   OnEntityEditListener,
                                                                                   OnEntityListListener {
    private static final int GOOGLE_PLAY_SERVICE_REQUEST = 1;
    @SuppressWarnings("unused")
    private final static String TAG = Logger.makeTag(HomeActivity.class);
    private final BroadcastReceiver initReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, final Intent intent) {
            if (!isFinishing()) {
                startLoader();
            }
        }
    };
    private FuelHistoryDetailFragment detailFragment;
    private boolean isInitialized = false;
    private FuelHistoryListFragment listFragment;

    /**
     * {@inheritDoc}
     */
    @Override
    public void onBackStackChanged() {
        final int count = getSupportFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            ActionBarUtils.configureListNavigation(this);

            return;
        }

        ActionBarUtils.configureStandardNavigation(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onChangeLogDismissed() {
        ChangeLogDialogUtils.setChangeLogShown(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDateTimeCancel() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDateTimeNow(final long milliseconds) {
        onDateTimeSet(milliseconds);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDateTimeSet(final long milliseconds) {
        final FuelHistoryEditFragment fragment = fragmentFinder.find("fuel_history_edit");

        if (fragment != null) {
            fragment.setDateTime(milliseconds);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onEntityAdd() {
        final FuelHistoryEditFragment fragment = FuelHistoryEditFragment.newInstance(getVehicleId());

        if (!isDualPane()) {
            final FragmentTransaction transaction = startTransaction();

            transaction.setCustomAnimations(anim.fade_in, anim.fade_out);

            replaceFragment(fragment, R.id.entity_list, "fuel_history_edit");

            transaction.addToBackStack(null).commit();
        }
        else {
            fragment.setDualPane(isDualPane());
            fragment.show(getSupportFragmentManager(), "fuel_history_edit");
        }
    }

    /**
     *
     */
    @Override
    public void onEntityCancel() {
        if (!isDualPane()) {
            onBackPressed();
        }
        else {
            final FuelHistoryEditFragment fragment = fragmentFinder.find("fuel_history_edit");

            if (fragment != null) {
                fragment.dismiss();
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onEntityDelete(final List<Long> ids) {
        if (ids.size() > 0) {
            listFragment.setListShown(false);

            if (detailFragment != null) {
                final FuelHistoryCursorAdapter adapter = (FuelHistoryCursorAdapter) listFragment.getListAdapter();
                final Set<Long> selectedItems = adapter.getSelectedItems();
                final Long entityId = detailFragment.getEntityId();

                if (entityId != null && selectedItems.contains(entityId)) {
                    detailFragment.loadDetails(null);
                }
            }

            for (final long id : ids) {
                final String idValue = Long.toString(id);
                final String[] selectionArgs;
                final String where;

                where = FuelHistory.Columns.VEHICLE_ID.getName() + " = ?";
                selectionArgs = new String[]{idValue};

                getContentResolver().delete(FuelHistory.CONTENT_URI, where, selectionArgs);
            }

            listFragment.setListShown(true);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onEntityEdit(final Long entityId) {
        final FuelHistoryEditFragment fragment = FuelHistoryEditFragment.newInstance(getVehicleId(), entityId);

        if (!isDualPane()) {
            final FragmentTransaction transaction = startTransaction();

            transaction.setCustomAnimations(anim.fade_in, anim.fade_out);

            replaceFragment(fragment, R.id.entity_list, "fuel_history_edit");

            transaction.addToBackStack(null).commit();
        }
        else {
            fragment.setDualPane(isDualPane());
            fragment.show(getSupportFragmentManager(), "fuel_history_edit");
        }
    }

    /**
     *
     */
    @Override
    public void onEntitySave() {
        final FuelHistoryEditFragment fragment = fragmentFinder.find("fuel_history_edit");

        if (!isDualPane()) {
            onBackPressed();
        }
        else {
            if (fragment != null) {
                fragment.dismiss();

                detailFragment.refresh();
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onEntitySelected(final int position, final long id) {
        final FuelHistoryCursorAdapter adapter = (FuelHistoryCursorAdapter) listFragment.getListAdapter();
        final Cursor cursor = (Cursor) adapter.getItem(position);

        if (cursor != null) {
            if (!isDualPane()) {
                final FragmentTransaction transaction = startTransaction();

                detailFragment = FuelHistoryDetailFragment.newInstance(id, adapter.getMilesPerGallon(id));

                transaction.setCustomAnimations(anim.fade_in, anim.fade_out);
                replaceFragment(detailFragment, R.id.entity_list, "fuel_history_details");

                transaction.addToBackStack(null).commit();
            }
            else {
                detailFragment.loadDetails(id);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        final int id = item.getItemId();

        switch (id) {
            case R.id.menu_settings: {
                IntentUtils.createActivityIntent(this, SettingsActivity.class).start();
                return true;
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
    protected void afterViews(final Bundle savedInstanceState) {
        final View view = finder.find(id.entity_details);

        setDualPane(view != null);

        if (savedInstanceState == null) {
            final FragmentTransaction transaction = startTransaction();

            listFragment = FuelHistoryListFragment.newInstance();

            addFragment(listFragment, id.entity_list, "fuel_history_list");

            if (isDualPane()) {
                detailFragment = FuelHistoryDetailFragment.newInstance();

                addFragment(detailFragment, id.entity_details, "fuel_history_details");
            }

            transaction.commit();
        }
        else {
            listFragment = fragmentFinder.find("fuel_history_list");
            listFragment.setDualPane(isDualPane());

            if (isDualPane()) {
                detailFragment = fragmentFinder.find("fuel_history_details");
                detailFragment.setDualPane(isDualPane());
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    protected int getContentResourceId() {
        return id.entity_list;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Integer getLayoutResource() {
        return R.layout.activity_entity_home;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Integer getMenuResource() {
        return (isInitialized) ? menu.menu_app : null;
    }

    /**
     * {inheritDoc}
     */
    @Override
    protected boolean isLoadedOnCreate() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onHideOptionsMenuItems(final Menu menu, final boolean isDrawerOpen) {
        MenuItem item;

        item = menu.findItem(id.menu_settings);
        if (item != null) {
            item.setVisible(!isDrawerOpen);
        }

        item = menu.findItem(id.menu_fuel_history_add);
        if (item != null) {
            item.setVisible(!isDrawerOpen);
        }

        item = menu.findItem(id.menu_fuel_history_edit);
        if (item != null) {
            item.setVisible(!isDrawerOpen);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onNavigationDrawerClosed() {
        ActionBarUtils.configureListNavigation(this);
        selectItem(NavigationDrawerUtils.HOME, false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onNavigationDrawerItemSelected(final int position) {
        NavigationDrawerUtils.navigate(this, position, NavigationDrawerUtils.HOME);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onNavigationDrawerOpened() {
        ActionBarUtils.configureStandardNavigation(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onPause() {
        super.onPause();

        this.unregisterReceiver(initReceiver);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onResume() {
        final IntentFilter filter = new IntentFilter();
        final SharedPreferencesHelper helper = new SharedPreferencesHelper(this);

        super.onResume();

        isInitialized = helper.booleanValue(SharedPreferenceKeys.PREF_INITIALIZED, false);

        filter.addAction(Intents.INTENT_INIT_FINISHED);
        this.registerReceiver(this.initReceiver, filter);

        if (!isInitialized) {
            startInitializationService();
        }
        else if (!isFinishing()) {
            final int result;

            result = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());
            if (result != ConnectionResult.SUCCESS) {
                GooglePlayServicesUtil.getErrorDialog(result, this, GOOGLE_PLAY_SERVICE_REQUEST);
            }

            if (!ChangeLogDialogUtils.hasShownChangeLog(this)) {
                ChangeLogDialogUtils.displayChangeLogDialogFragment(this);
            }

            supportInvalidateOptionsMenu();

            startLoader();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onVehicleSelected(final long vehicleId) {
        listFragment.refresh(vehicleId);
    }

    /**
     *
     */
    private void startInitializationService() {
        final Intent intent = new Intent(this, InitializationIntentService.class);

        this.startService(intent);
    }
}