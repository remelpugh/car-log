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

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import com.dabay6.android.apps.carlog.R;
import com.dabay6.android.apps.carlog.R.layout;
import com.dabay6.android.apps.carlog.configuration.Intents;
import com.dabay6.android.apps.carlog.ui.base.fragments.BaseEditFragment.OnEntityEditListener;
import com.dabay6.android.apps.carlog.ui.vehicle.fragments.VehicleEditFragment;
import com.utils.android.logging.Logger;
import com.utils.android.ui.BaseFragmentActivity;
import com.utils.android.util.IntentUtils.ActivityIntentBuilder;

/**
 * VehicleEditActivity
 *
 * @author Remel Pugh
 * @version 1.0
 */
public class VehicleEditActivity extends BaseFragmentActivity implements OnEntityEditListener {
    public static final String VEHICLE_EXTRA_ID = Intents.INTENT_EXTRA_PREFIX + "vehicle.id";
    public static final String VEHICLE_EXTRA_NONE = Intents.INTENT_EXTRA_PREFIX + "vehicle.none";
    private final static String FRAGMENT_TAG = "vehicle";
    @SuppressWarnings("unused")
    private final static String TAG = Logger.makeTag(VehicleEditActivity.class);
    private boolean hasVehicles = true;
    private Long vehicleId;

    /**
     *
     */
    @Override
    public void onEntityCancel() {
        navigateUp();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onEntitySave() {
        if (!hasVehicles) {
            final Builder builder = new Builder(this);

            builder.setMessage(R.string.vehicle_create_another)
                   .setNegativeButton(android.R.string.no, new OnClickListener() {
                       @Override
                       public void onClick(final DialogInterface dialog, final int which) {
                           dialog.dismiss();
                           navigateUp();
                       }
                   })
                   .setPositiveButton(android.R.string.yes, new OnClickListener() {
                       @Override
                       public void onClick(final DialogInterface dialog, final int which) {
                           dialog.dismiss();
                       }
                   })
                   .setTitle(R.string.app_name);

            builder.create().show();
        }
        else {
            navigateUp();
        }
    }

    /**
     * @param activity
     */
    public static void startActivityForAdd(final Activity activity) {
        VehicleEditActivity.startActivityForEdit(activity, null);
    }

    /**
     * @param activity
     * @param id
     */
    public static void startActivityForEdit(final Activity activity, final Long id) {
        final ActivityIntentBuilder builder = new ActivityIntentBuilder(activity, VehicleEditActivity.class);

        builder.get().putExtra(Intents.INTENT_EXTRA_PARENT, activity.getIntent());
        if (id != null) {
            builder.get().putExtra(VEHICLE_EXTRA_ID, id);
        }
        builder.start();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void afterViews(final Bundle savedInstanceState) {
        final Bundle bundle = getIntent().getExtras();

        Logger.verbose(TAG, "VehicleEditActivity.afterViews()");
        if (bundle != null) {
            if (bundle.containsKey(VEHICLE_EXTRA_NONE)) {
                hasVehicles = bundle.getBoolean(VEHICLE_EXTRA_NONE, true);
            }
            if (bundle.containsKey(VEHICLE_EXTRA_ID)) {
                vehicleId = bundle.getLong(VEHICLE_EXTRA_ID);
            }
        }

        if (savedInstanceState == null) {
            final VehicleEditFragment fragment = VehicleEditFragment.newInstance(vehicleId);
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

    /**
     * {@inheritDoc}
     */
    @Override
    protected void navigateUp() {
        final Intent intent = getIntent();

        if (intent.hasExtra(Intents.INTENT_EXTRA_PARENT)) {
            final Intent parentIntent = intent.getParcelableExtra(Intents.INTENT_EXTRA_PARENT);

            finish();
            parentIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(parentIntent);
        }
        else {
            super.navigateUp();
        }
    }
}