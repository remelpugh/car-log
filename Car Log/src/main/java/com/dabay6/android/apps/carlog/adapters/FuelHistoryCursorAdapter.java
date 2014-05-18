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

import android.annotation.TargetApi;
import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.view.ActionMode;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import com.dabay6.android.apps.carlog.R;
import com.dabay6.android.apps.carlog.data.DTO.FuelHistoryDTO;
import com.dabay6.libraries.androidshared.adapters.BaseCheckableCursorAdapter;
import com.dabay6.libraries.androidshared.logging.Logger;
import com.dabay6.libraries.androidshared.util.DateUtils;
import com.dabay6.libraries.androidshared.util.DateUtils.DateFormats;
import com.dabay6.libraries.androidshared.util.HashMapUtils;
import com.dabay6.libraries.androidshared.util.StringUtils;
import com.dabay6.libraries.androidshared.view.ViewsFinder;

import java.text.NumberFormat;
import java.util.HashMap;

/**
 * VehicleCursorAdapter
 *
 * @author Remel Pugh
 * @version 1.0
 */
@SuppressWarnings("unused")
public class FuelHistoryCursorAdapter extends BaseCheckableCursorAdapter {
    private final static String TAG = Logger.makeTag(FuelHistoryCursorAdapter.class);
    private HashMap<Long, Float> milesPerGallon = HashMapUtils.newHashMap();

    /**
     * {@inheritDoc}
     */
    public FuelHistoryCursorAdapter(final Context context, final Bundle savedInstanceState, final Cursor c) {
        super(context, savedInstanceState, R.layout.list_item_fuel_history, c);
    }

    /**
     * {@inheritDoc}
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public FuelHistoryCursorAdapter(final Context context, final Bundle savedInstanceState, final Cursor c,
                                    final int flags) {
        super(context, savedInstanceState, R.layout.list_item_fuel_history, c, flags);
    }

    /**
     *
     * @param id
     * @return
     */
    public Float getMilesPerGallon(final Long id) {
        if (milesPerGallon.containsKey(id)) {
            return milesPerGallon.get(id);
        }

        return null;
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
     * @param context
     * @param view
     * @param cursor
     */
    @Override
    protected void populateData(final Context context, final View view, final Cursor cursor) {
        final NumberFormat currency = NumberFormat.getCurrencyInstance();
        final ViewsFinder finder = new ViewsFinder(view);
        final TextView dateTime = finder.find(R.id.datetime);
        final TextView odometer = finder.find(R.id.odometer);
        final TextView mpg = finder.find(R.id.mpg);
        final TextView price = finder.find(R.id.price);
        final TextView total = finder.find(R.id.total);
        final TextView volume = finder.find(R.id.volume);
        final FuelHistoryDTO history = FuelHistoryDTO.newInstance(cursor);
        final Pair<Float, String> calculated = calculateMilesPerGallon(context, history, cursor);

        milesPerGallon.put(history.getHistoryId(), calculated.first);

        dateTime.setText(DateUtils.getUserLocaleFormattedDate(context, history.getPurchaseDate(), DateFormats.Medium));
        mpg.setText(calculated.second);
        odometer.setText(
                StringUtils.format(context.getString(R.string.fuel_history_miles), history.getOdometerReading()));
        volume.setText(StringUtils.format(context.getString(R.string.gallons), history.getFuelAmount()));

        currency.setMaximumFractionDigits(3);
        price.setText(
                context.getString(R.string.fuel_history_cost_per_unit, currency.format(history.getCostPerUnit())));

        currency.setMaximumFractionDigits(2);
        total.setText(currency.format(history.getTotalCost()));
    }

    /**
     * @param context The {@link Context} used to retrieve string resources.
     * @param history The current {@link FuelHistoryDTO} record.
     * @param cursor  All history records.
     */
    private Pair<Float, String> calculateMilesPerGallon(final Context context, final FuelHistoryDTO history,
                                                        final Cursor cursor) {
        final Pair<Long, String> returnValue;
        final int count = cursor.getCount() - 1;
        final int currentPosition = cursor.getPosition();
        final int next = cursor.getPosition() + 1;
        final float mileage;
        final float mpg;

        if (next <= count) {
            final FuelHistoryDTO previous;

            cursor.moveToPosition(next);

            previous = FuelHistoryDTO.newInstance(cursor);
            mileage = history.getOdometerReading() - previous.getOdometerReading();

            mpg = mileage / history.getFuelAmount();

            cursor.moveToPosition(currentPosition);
        }
        else {
            mpg = history.getOdometerReading() / history.getFuelAmount();
        }

        return new Pair<Float, String>(mpg, String.format(context.getString(R.string.miles_per_gallon), mpg));
    }
}