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

import android.R.anim;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import com.dabay6.android.apps.carlog.R;
import com.dabay6.android.apps.carlog.R.id;
import com.dabay6.android.apps.carlog.R.layout;
import com.dabay6.android.apps.carlog.adapters.VehicleCursorAdapter;
import com.dabay6.android.apps.carlog.configuration.Intents;
import com.dabay6.android.apps.carlog.data.provider.CarLogContract.FuelHistory;
import com.dabay6.android.apps.carlog.data.provider.CarLogContract.Vehicle;
import com.dabay6.android.apps.carlog.data.provider.CarLogContract.Vehicle.Columns;
import com.dabay6.android.apps.carlog.ui.base.fragments.BaseDeleteListFragment.OnEntityListListener;
import com.dabay6.android.apps.carlog.ui.base.fragments.BaseDetailFragment.OnEntityDetailListener;
import com.dabay6.android.apps.carlog.ui.base.fragments.BaseEditFragment.OnEntityEditListener;
import com.dabay6.android.apps.carlog.ui.vehicle.fragments.VehicleDetailFragment;
import com.dabay6.android.apps.carlog.ui.vehicle.fragments.VehicleEditFragment;
import com.dabay6.android.apps.carlog.ui.vehicle.fragments.VehicleListFragment;
import com.utils.android.logging.Logger;
import com.utils.android.ui.BaseFragmentActivity;

import java.util.List;
import java.util.Set;

/**
 * VehicleHomeActivity
 *
 * @author Remel Pugh
 * @version 1.0
 */
@SuppressWarnings("unused")
public class VehicleHomeActivity extends BaseFragmentActivity
        implements OnEntityDetailListener, OnEntityEditListener, OnEntityListListener {
    public static final String VEHICLE_EXTRA_NONE = Intents.INTENT_EXTRA_PREFIX + "vehicle.none";
    private final static String TAG = Logger.makeTag(VehicleHomeActivity.class);
    private VehicleDetailFragment detailFragment;
    private VehicleEditFragment editFragment;
    private VehicleListFragment listFragment;
    private boolean noVehicles = false;
    private CharSequence savedTitle;

    /**
     * {@inheritDoc}
     */
    @Override
    public void onBackStackChanged() {
        final int count = getSupportFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            setTitle(savedTitle);
            getSupportActionBar().setSubtitle(null);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onEntityCancel() {
        if (!isDualPane()) {
            onBackPressed();
        }
        else {
            final VehicleEditFragment fragment = fragmentFinder.find("vehicle_edit");

            if (fragment != null) {
                fragment.dismiss();
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onEntityAdd() {
        final VehicleEditFragment fragment = VehicleEditFragment.newInstance();

        if (!isDualPane()) {
            final FragmentTransaction transaction = startTransaction();

            transaction.setCustomAnimations(anim.fade_in, anim.fade_out);

            replaceFragment(fragment, R.id.entity_list, "vehicle_edit");

            transaction.addToBackStack(null).commit();
        }
        else {
            fragment.show(getSupportFragmentManager(), "vehicle_edit");
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
                final VehicleCursorAdapter adapter = (VehicleCursorAdapter) listFragment.getListAdapter();
                final Set<Long> selectedItems = adapter.getSelectedItems();
                final Long entityId = detailFragment.getEntityId();

                if (entityId != null && selectedItems.contains(entityId)) {
                    detailFragment.loadDetails(null);
                    setTitle(savedTitle);
                }
            }

            for (final long id : ids) {
                final String idValue = Long.toString(id);
                String[] selectionArgs;
                String where;

                where = FuelHistory.Columns.VEHICLE_ID.getName() + " = ?";
                selectionArgs = new String[]{idValue};

                getContentResolver().delete(FuelHistory.CONTENT_URI, where, selectionArgs);

                where = Columns.VEHICLE_ID.getName() + " = ?";
                selectionArgs = new String[]{idValue};

                getContentResolver().delete(Vehicle.CONTENT_URI, where, selectionArgs);
            }

            listFragment.setListShown(true);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onEntityEdit(final Long entityId) {
        final VehicleEditFragment fragment = VehicleEditFragment.newInstance(entityId);

        if (!isDualPane()) {
            final FragmentTransaction transaction = startTransaction();

            transaction.setCustomAnimations(anim.fade_in, anim.fade_out);

            replaceFragment(fragment, R.id.entity_list, "vehicle_edit");

            transaction.addToBackStack(null).commit();
        }
        else {
            fragment.show(getSupportFragmentManager(), "vehicle_edit");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onEntitySelected(final int position, final long id) {
        final VehicleCursorAdapter adapter = (VehicleCursorAdapter) listFragment.getListAdapter();
        final Cursor cursor = (Cursor) adapter.getItem(position);

        if (cursor != null) {
            if (!isDualPane()) {
                final FragmentTransaction transaction = startTransaction();

                detailFragment = VehicleDetailFragment.newInstance(id, cursor.getString(Columns.NAME.getIndex()));

                transaction.setCustomAnimations(anim.fade_in, anim.fade_out);
                replaceFragment(detailFragment, R.id.entity_list, "vehicle_detail");

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
    public void onEntitySave() {
        final VehicleEditFragment fragment = fragmentFinder.find("vehicle_edit");

        if (noVehicles) {
            final Builder builder = new Builder(this);

            builder.setMessage(R.string.vehicle_create_another)
                   .setNegativeButton(android.R.string.no, new OnClickListener() {
                       @Override
                       public void onClick(final DialogInterface dialog, final int which) {
                           dialog.dismiss();
                           if (!isDualPane()) {
                               onBackPressed();
                           }
                           else {
                               if (fragment != null) {
                                   fragment.dismiss();
                               }
                           }
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
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void afterViews(final Bundle savedInstanceState) {
        final Bundle bundle = getIntent().getExtras();
        final View view = finder.find(id.entity_details);

        if (TextUtils.isEmpty(savedTitle)) {
            savedTitle = getTitle();
        }

        if (bundle != null) {
            if (bundle.containsKey(VEHICLE_EXTRA_NONE)) {
                noVehicles = bundle.getBoolean(VEHICLE_EXTRA_NONE, false);
            }
        }

        setDualPane(view != null);

        if (savedInstanceState == null) {
            final FragmentTransaction transaction = startTransaction();

            listFragment = VehicleListFragment.newInstance();

            addFragment(listFragment, id.entity_list, "vehicle_list");

            if (isDualPane()) {
                detailFragment = VehicleDetailFragment.newInstance();

                addFragment(detailFragment, id.entity_details, "vehicle_details");
            }

            transaction.commit();

            if (noVehicles) {
                onEntityEdit(null);
            }
        }
        else {
            listFragment = fragmentFinder.find("vehicle_list");
            listFragment.setDualPane(isDualPane());

            if (isDualPane()) {
                detailFragment = fragmentFinder.find("vehicle_details");
                detailFragment.setDualPane(isDualPane());
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Integer getLayoutResource() {
        return layout.activity_entity_home;
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
    protected boolean isTitleEnabled() {
        return true;
    }
}