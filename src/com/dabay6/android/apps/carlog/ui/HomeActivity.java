package com.dabay6.android.apps.carlog.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.view.ActionMode;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.MenuItem;
import com.dabay6.android.apps.carlog.R;
import com.dabay6.android.apps.carlog.R.menu;
import com.dabay6.android.apps.carlog.R.string;
import com.dabay6.android.apps.carlog.adapters.DualLineCursorAdapter;
import com.dabay6.android.apps.carlog.app.InitializationIntentService;
import com.dabay6.android.apps.carlog.configuration.Intents;
import com.dabay6.android.apps.carlog.configuration.SharedPreferenceKeys;
import com.dabay6.android.apps.carlog.configuration.SortOrder;
import com.dabay6.android.apps.carlog.data.provider.CarLogContract.FuelHistory;
import com.dabay6.android.apps.carlog.data.provider.CarLogContract.Vehicle;
import com.dabay6.android.apps.carlog.ui.base.fragments.BaseDeleteListFragment.OnEntityListListener;
import com.dabay6.android.apps.carlog.ui.base.fragments.BaseEditFragment.OnEntityEditListener;
import com.dabay6.android.apps.carlog.ui.fuel.FuelHistoryEditActivity;
import com.dabay6.android.apps.carlog.ui.fuel.fragments.FuelHistoryListFragment;
import com.dabay6.android.apps.carlog.ui.statistics.StatisticsActivity;
import com.dabay6.android.apps.carlog.ui.vehicle.VehicleHomeActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.utils.android.helper.SharedPreferencesHelper;
import com.utils.android.logging.Logger;
import com.utils.android.ui.BaseFragmentListNavigationActivity;
import com.utils.android.ui.dialogs.changelog.OnChangeLogDialogListener;
import com.utils.android.ui.dialogs.changelog.util.ChangeLogDialogUtils;
import com.utils.android.ui.fragments.FragmentLifeCycleListener;
import com.utils.android.util.AndroidUtils;
import com.utils.android.util.DataUtils;
import com.utils.android.util.IntentUtils;
import com.utils.android.util.IntentUtils.ActivityIntentBuilder;

import java.util.List;

/**
 * HomeActivity
 *
 * @author Remel Pugh
 * @version 1.0
 */
public class HomeActivity extends BaseFragmentListNavigationActivity implements FragmentLifeCycleListener,
                                                                                LoaderManager.LoaderCallbacks<Cursor>,
                                                                                OnChangeLogDialogListener,
                                                                                OnEntityEditListener,
                                                                                OnEntityListListener {
    private static final int GOOGLE_PLAY_SERVICE_REQUEST = 1;
    private final static String KEY_ACTION_MODE_STARTED = "KEY_ACTION_MODE_STARTED";
    private final static String KEY_VEHICLE_ID = "KEY_VEHICLE_ID";
    @SuppressWarnings("unused")
    private final static String TAG = Logger.makeTag(HomeActivity.class);
    private final static int VEHICLE_LOADER_ID = 0;
    private final BroadcastReceiver initReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, final Intent intent) {
            if (!isFinishing()) {
                startLoader();
            }
        }
    };
    private DualLineCursorAdapter adapter;
    private FuelHistoryListFragment fuelHistory;
    private boolean isActionModeShowing = false;
    private boolean isInitialized = false;
    private Long vehicleId = null;

    /**
     * {@inheritDoc}
     */
    @Override
    public void onActionModeStarted(final ActionMode mode) {
        super.onActionModeStarted(mode);

        isActionModeShowing = true;
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
    public Loader<Cursor> onCreateLoader(final int i, final Bundle bundle) {
        return new CursorLoader(getBaseContext(), Vehicle.CONTENT_URI, Vehicle.PROJECTION, null, null,
                                SortOrder.DEFAULT_VEHICLE_ORDER.toString());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onEntityAdd() {
        FuelHistoryEditActivity.startActivityForAdd(this, vehicleId);
    }

    /**
     *
     */
    @Override
    public void onEntityCancel() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onEntityDelete(final List<Long> ids) {
        if (ids.size() > 0) {
            fuelHistory.setListShown(false);

            for (final long id : ids) {
                final String idValue = Long.toString(id);
                final String[] selectionArgs;
                final String where;

                where = FuelHistory.Columns.VEHICLE_ID.getName() + " = ?";
                selectionArgs = new String[]{idValue};

                getContentResolver().delete(FuelHistory.CONTENT_URI, where, selectionArgs);
            }

            fuelHistory.setListShown(true);
        }
    }

    /**
     *
     */
    @Override
    public void onEntitySave() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onEntitySelected(final int position, final long id) {
        FuelHistoryEditActivity.startActivityForEdit(this, id, vehicleId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onFragmentAttached(final Fragment fragment) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onFragmentViewCreated(final Fragment fragment) {
        if (!fragment.getTag().equalsIgnoreCase("fuel_history")) {
            return;
        }

        fuelHistory = (FuelHistoryListFragment) fragment;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onLoadFinished(final Loader<Cursor> cursorLoader, final Cursor cursor) {
        if (DataUtils.hasData(cursor)) {
            final ActionBar actionBar = getSupportActionBar();

            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
            actionBar.setDisplayShowTitleEnabled(false);

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

        fuelHistory.refresh(vehicleId);

        return true;
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
            case R.id.menu_statistics_list: {
                IntentUtils.createActivityIntent(this, StatisticsActivity.class).start();
                return true;
            }
            case R.id.menu_vehicle_list: {
                IntentUtils.createActivityIntent(this, VehicleHomeActivity.class).start();
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
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    protected FuelHistoryListFragment getFragment() {
        return FuelHistoryListFragment.newInstance(false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getFragmentTag() {
        return "fuel_history";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Integer getLayoutResource() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Integer getMenuResource() {
        return (isInitialized) ? menu.menu_app : null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Integer getNavigationResource() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isHomeButtonEnabled() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isTitleEnabled() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onConfigureActionBar() {
        final ActionBar actionBar = getSupportActionBar();
        final Context context = actionBar.getThemedContext();

        if (!isActionModeShowing) {
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
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(KEY_ACTION_MODE_STARTED)) {
                isActionModeShowing = savedInstanceState.getBoolean(KEY_ACTION_MODE_STARTED);
            }
            if (savedInstanceState.containsKey(KEY_VEHICLE_ID)) {
                vehicleId = savedInstanceState.getLong(KEY_VEHICLE_ID);
                vehicleId = (vehicleId == -1) ? null : vehicleId;
            }
        }

        super.onCreate(savedInstanceState);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onPause() {
        super.onPause();

        this.unregisterReceiver(this.initReceiver);
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
    protected void onSaveInstanceState(final Bundle outState) {
        outState.putBoolean(KEY_ACTION_MODE_STARTED, isActionModeShowing);
        outState.putLong(KEY_VEHICLE_ID, vehicleId == null ? -1 : vehicleId);

        super.onSaveInstanceState(outState);
    }

    /**
     *
     */
    private void startInitializationService() {
        final Intent intent = new Intent(this, InitializationIntentService.class);

        this.startService(intent);
    }

    private void startLoader() {
        getSupportLoaderManager().restartLoader(VEHICLE_LOADER_ID, null, this);
    }
}