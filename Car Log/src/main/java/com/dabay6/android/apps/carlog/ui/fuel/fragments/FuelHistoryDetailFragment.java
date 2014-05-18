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

package com.dabay6.android.apps.carlog.ui.fuel.fragments;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.view.MenuItem;
import com.dabay6.android.apps.carlog.R;
import com.dabay6.android.apps.carlog.R.layout;
import com.dabay6.android.apps.carlog.R.menu;
import com.dabay6.android.apps.carlog.R.string;
import com.dabay6.android.apps.carlog.data.DTO.FuelHistoryDTO;
import com.dabay6.android.apps.carlog.data.provider.CarLogContract.FuelHistory;
import com.dabay6.android.apps.carlog.data.provider.CarLogContract.FuelHistory.Columns;
import com.dabay6.android.apps.carlog.ui.base.fragments.BaseDetailFragment;
import com.dabay6.libraries.androidshared.logging.Logger;
import com.dabay6.libraries.androidshared.util.DataUtils;
import com.dabay6.libraries.androidshared.util.DateUtils;
import com.dabay6.libraries.androidshared.util.DateUtils.DateFormats;

import java.text.NumberFormat;

/**
 * FuelHistoryDetailFragment
 *
 * @author Remel Pugh
 * @version 1.0
 */
@SuppressWarnings("unused")
public class FuelHistoryDetailFragment extends BaseDetailFragment {
    private final static String PARAMS_MPG = "PARAMS_MPG";
    private final static String TAG = Logger.makeTag(FuelHistoryDetailFragment.class);

    /**
     * Default constructor.
     */
    public FuelHistoryDetailFragment() {
    }

    /**
     *
     * @param id
     * @return
     */
    public static FuelHistoryDetailFragment newInstance(final Long id) {
        return newInstance(id, null);
    }

    /**
     *
     * @param id
     * @param mpg
     * @return
     */
    public static FuelHistoryDetailFragment newInstance(final Long id, final Float mpg) {
        final Bundle arguments = new Bundle();
        final FuelHistoryDetailFragment fragment = new FuelHistoryDetailFragment();

        if (id != null) {
            arguments.putLong(PARAMS_ENTITY_ID, id);
        }

        if (mpg != null) {
            arguments.putFloat(PARAMS_MPG, mpg);
        }

        fragment.setArguments(arguments);

        return fragment;
    }

    /**
     *
     * @return
     */
    public static FuelHistoryDetailFragment newInstance() {
        return newInstance(null);
    }

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
            case R.id.menu_fuel_history_edit: {
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
    @SuppressWarnings("unchecked")
    @Override
    protected void createListItems(final Cursor cursor) {
        if (DataUtils.hasData(cursor)) {
            final Bundle bundle = getArguments();
            final FuelHistoryDTO history = FuelHistoryDTO.newInstance(cursor);
            final NumberFormat currency = NumberFormat.getCurrencyInstance();
            final String purchaseDate;

            purchaseDate = DateUtils.getUserLocaleFormattedDate(getActivity(), history.getPurchaseDate(),
                                                                DateFormats.Medium);

            setTitle(string.fuel_history_details);
            setSubtitle(history.getName());

            if (isDualPane()) {
                adapter.clear();
            }

            adapter.add(createDetailItem(Columns.PURCHASE_DATE, purchaseDate));
            adapter.add(createDetailItem(Columns.ODOMETER_READING,
                                         getString(R.string.fuel_history_miles, history.getOdometerReading())));

            if (bundle != null && bundle.containsKey(PARAMS_MPG)) {
                final float mpg = bundle.getFloat(PARAMS_MPG);

                adapter.add(getString(string.fuel_history_mpg), String.format(getString(string.miles_per_gallon), mpg));
            }

            currency.setMaximumFractionDigits(2);
            adapter.add(createDetailItem(Columns.TOTAL_COST, currency.format(history.getTotalCost())));

            adapter.add(createDetailItem(Columns.FUEL_AMOUNT, history.getFuelAmount().toString()));

            currency.setMaximumFractionDigits(3);
            adapter.add(createDetailItem(Columns.COST_PER_UNIT, currency.format(history.getCostPerUnit())));

            getActivity().supportInvalidateOptionsMenu();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected int getEmptyTextResourceId() {
        return string.fuel_history_detail_empty;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getIdentityColumnName() {
        return Columns.HISTORY_ID.getName();
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
            return menu.menu_fuel_history_detail;
        }

        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String[] getProjection() {
        return FuelHistory.PROJECTION;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Uri getUri() {
        return FuelHistory.CONTENT_URI;
    }
}