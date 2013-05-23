package com.dabay6.android.apps.carlog.adapters;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.actionbarsherlock.view.ActionMode;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.dabay6.android.apps.carlog.R;
import com.dabay6.android.apps.carlog.data.DTO.FuelHistoryDTO;
import com.utils.android.adapters.BaseCheckableCursorAdapter;
import com.utils.android.logging.Logger;
import com.utils.android.util.DateUtils;
import com.utils.android.util.DateUtils.DateFormats;
import com.utils.android.view.ViewsFinder;

import java.text.NumberFormat;

/**
 * VehicleCursorAdapter
 *
 * @author Remel Pugh
 * @version 1.0
 */
@SuppressWarnings("unused")
public class FuelHistoryCursorAdapter extends BaseCheckableCursorAdapter {
    @SuppressWarnings("unused")
    private final static String TAG = Logger.makeTag(FuelHistoryCursorAdapter.class);

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("deprecation")
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

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
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

        dateTime.setText(DateUtils.getUserLocaleFormattedDate(context, history.getPurchaseDate(), DateFormats.Medium));
        mpg.setText(calculateMilesPerGallon(context, history, cursor));
        odometer.setText(context.getString(R.string.fuel_history_miles, history.getOdometerReading()));
        volume.setText(context.getString(R.string.gallons, history.getFuelAmount()));

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
    private String calculateMilesPerGallon(final Context context, final FuelHistoryDTO history, final Cursor cursor) {
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

        return String.format(context.getString(R.string.miles_per_gallon), mpg);
    }
}