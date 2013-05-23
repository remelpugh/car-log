package com.dabay6.android.apps.carlog.ui.fuel;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import com.dabay6.android.apps.carlog.R;
import com.dabay6.android.apps.carlog.R.layout;
import com.dabay6.android.apps.carlog.configuration.Intents;
import com.dabay6.android.apps.carlog.ui.base.fragments.BaseEditFragment.OnEntityEditListener;
import com.dabay6.android.apps.carlog.ui.fuel.fragments.FuelHistoryEditFragment;
import com.utils.android.app.FragmentFinder;
import com.utils.android.logging.Logger;
import com.utils.android.ui.BaseFragmentActivity;
import com.utils.android.ui.dialogs.DateTimePickerDialogFragment.OnDateTimePickerListener;
import com.utils.android.util.IntentUtils.ActivityIntentBuilder;


/**
 * FuelHistoryEditActivity
 *
 * @author Remel Pugh
 * @version 1.0
 */
@SuppressWarnings("unused")
public class FuelHistoryEditActivity extends BaseFragmentActivity implements OnDateTimePickerListener,
                                                                             OnEntityEditListener {
    public static final String EXTRA_FUEL_HISTORY_ID = Intents.INTENT_EXTRA_PREFIX + "fuelhistory.id";
    public static final String EXTRA_VEHICLE_ID = Intents.INTENT_EXTRA_PREFIX + "vehicle.id";
    private final static String FRAGMENT_TAG = "fuelhistory";
    private final static String TAG = Logger.makeTag(FuelHistoryEditActivity.class);
    private Long fuelHistoryId;
    private Long vehicleId;

    /**
     * {@inheritDoc}
     */
    @Override
    public void onEntityCancel() {
        navigateUp();
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
        FragmentFinder finder = new FragmentFinder(this);
        FuelHistoryEditFragment fragment = finder.find(FRAGMENT_TAG);

        if (fragment != null) {
            fragment.setDateTime(milliseconds);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDateTimeSet(final long milliseconds) {
        FragmentFinder finder = new FragmentFinder(this);
        FuelHistoryEditFragment fragment = finder.find(FRAGMENT_TAG);

        if (fragment != null) {
            fragment.setDateTime(milliseconds);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onEntitySave() {
        navigateUp();
    }

    /**
     * @param activity
     * @param vehicleId
     */
    public static void startActivityForAdd(final Activity activity, final Long vehicleId) {
        FuelHistoryEditActivity.startActivityForEdit(activity, null, vehicleId);
    }

    /**
     * @param activity
     * @param id
     * @param vehicleId
     */
    public static void startActivityForEdit(final Activity activity, final Long id, final Long vehicleId) {
        final ActivityIntentBuilder builder = new ActivityIntentBuilder(activity, FuelHistoryEditActivity.class);

        if (vehicleId == null) {
            throw new IllegalArgumentException("Vehicle Id is required");
        }

        builder.get().putExtra(EXTRA_VEHICLE_ID, vehicleId);
        builder.get().putExtra(Intents.INTENT_EXTRA_PARENT, activity.getIntent());
        if (id != null) {
            builder.get().putExtra(EXTRA_FUEL_HISTORY_ID, id);
        }
        builder.start();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void afterViews(final Bundle savedInstanceState) {
        final Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            if (bundle.containsKey(EXTRA_FUEL_HISTORY_ID)) {
                fuelHistoryId = bundle.getLong(EXTRA_FUEL_HISTORY_ID);
            }
            if (bundle.containsKey(EXTRA_VEHICLE_ID)) {
                vehicleId = bundle.getLong(EXTRA_VEHICLE_ID);
            }
        }
        if (savedInstanceState == null) {
            final FuelHistoryEditFragment fragment = FuelHistoryEditFragment.newInstance(vehicleId, fuelHistoryId);
            final FragmentTransaction transaction;

            transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, fragment, FRAGMENT_TAG).commit();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Integer getLayoutResource() {
        return layout.activity_fragment_container;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Integer getMenuResource() {
        return null;
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
    protected boolean isHomeButtonEnabled() {
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