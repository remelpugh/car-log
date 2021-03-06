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

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import com.dabay6.android.apps.carlog.R;
import com.dabay6.android.apps.carlog.R.id;
import com.dabay6.android.apps.carlog.R.layout;
import com.dabay6.android.apps.carlog.R.menu;
import com.dabay6.android.apps.carlog.configuration.Constants;
import com.dabay6.android.apps.carlog.data.DTO.FuelHistoryDTO;
import com.dabay6.android.apps.carlog.data.provider.CarLogContract.FuelHistory;
import com.dabay6.android.apps.carlog.data.provider.CarLogContract.FuelHistory.Columns;
import com.dabay6.android.apps.carlog.ui.base.fragments.BaseEditFragment;
import com.dabay6.libraries.androidshared.logging.Logger;
import com.dabay6.libraries.androidshared.ui.dialogs.DateTimePickerDialogFragment;
import com.dabay6.libraries.androidshared.util.DateUtils;
import com.dabay6.libraries.androidshared.util.ViewUtils;
import com.dabay6.libraries.androidshared.validation.RequiredValidator;

/**
 * FuelHistoryEditFragment
 *
 * @author Remel Pugh
 * @version 1.0
 */
public class FuelHistoryEditFragment extends BaseEditFragment {//implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String PARAMS_VEHICLE_ID = "PARAMS_VEHICLE_ID";
    @SuppressWarnings("unused")
    private final static String TAG = Logger.makeTag(FuelHistoryEditFragment.class);
    private final TextWatcher priceWatcher = new TextWatcher() {
        @Override
        public void afterTextChanged(final Editable s) {
        }

        @Override
        public void beforeTextChanged(final CharSequence s, final int start, final int count, final int after) {
        }

        @Override
        public void onTextChanged(final CharSequence s, final int start, final int before, final int count) {
            total.removeTextChangedListener(totalWatcher);
            volume.removeTextChangedListener(volumeWatcher);
            performCalculations(CalculationField.Price);
            total.addTextChangedListener(totalWatcher);
            volume.addTextChangedListener(volumeWatcher);
        }
    };
    private final TextWatcher totalWatcher = new TextWatcher() {
        @Override
        public void afterTextChanged(final Editable s) {
        }

        @Override
        public void beforeTextChanged(final CharSequence s, final int start, final int count, final int after) {
        }

        @Override
        public void onTextChanged(final CharSequence s, final int start, final int before, final int count) {
            price.removeTextChangedListener(priceWatcher);
            volume.removeTextChangedListener(volumeWatcher);
            performCalculations(CalculationField.Total);
            price.addTextChangedListener(priceWatcher);
            volume.addTextChangedListener(volumeWatcher);
        }
    };
    private final TextWatcher volumeWatcher = new TextWatcher() {
        @Override
        public void afterTextChanged(final Editable s) {
        }

        @Override
        public void beforeTextChanged(final CharSequence s, final int start, final int count, final int after) {
        }

        @Override
        public void onTextChanged(final CharSequence s, final int start, final int before, final int count) {
            price.removeTextChangedListener(priceWatcher);
            total.removeTextChangedListener(totalWatcher);
            performCalculations(CalculationField.Volume);
            price.addTextChangedListener(priceWatcher);
            total.addTextChangedListener(totalWatcher);
        }
    };
    private EditText dateTime;
    private FuelHistoryDTO history;
    private Long milliseconds = DateUtils.getCurrentDateTime();
    private EditText notes;
    private EditText odometer;
    private EditText price;
    private EditText total;
    private Long vehicleId;
    private EditText volume;

    /**
     * Default constructor.
     */
    public FuelHistoryEditFragment() {
    }

    /**
     * Creates a new instance of {@link FuelHistoryEditFragment}.
     *
     * @param vehicleId The vehicle id that will be associated to the fuel history record.
     *
     * @return An instance of {@link FuelHistoryEditFragment}.
     */
    public static FuelHistoryEditFragment newInstance(final long vehicleId) {
        return newInstance(vehicleId, null);
    }

    /**
     * Creates a new instance of {@link FuelHistoryEditFragment}.
     *
     * @param id The fuel history id to be loaded.
     *
     * @return An instance of {@link FuelHistoryEditFragment}.
     */
    public static FuelHistoryEditFragment newInstance(final long vehicleId, final Long id) {
        final Bundle bundle = new Bundle();
        final FuelHistoryEditFragment fragment = new FuelHistoryEditFragment();

        bundle.putLong(PARAMS_VEHICLE_ID, vehicleId);
        if (id != null) {
            bundle.putLong(PARAMS_ENTITY_ID, id);
        }

        fragment.setArguments(bundle);

        return fragment;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        final Bundle bundle = getArguments();

        super.onActivityCreated(savedInstanceState);

        vehicleId = bundle.getLong(PARAMS_VEHICLE_ID);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onLoaderReset(final Loader<Cursor> cursorLoader) {
    }

    /**
     * {@inheritDoc}
     */
    public void setDateTime(final long milliseconds) {
        this.milliseconds = milliseconds;

        formatPurchaseDate(milliseconds);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected ContentValues buildContentValues() {
        final String priceValue = ViewUtils.getText(price);
        final String totalValue = ViewUtils.getText(total);

        if (history == null) {
            history = new FuelHistoryDTO();
        }

        history.setFuelAmount(Float.parseFloat(ViewUtils.getText(volume)));
        history.setNotes(ViewUtils.getText(notes));
        history.setOdometerReading(Float.parseFloat(ViewUtils.getText(odometer)));
        history.setPurchaseDate(milliseconds);
        history.setVehicleId(vehicleId);

        if (!TextUtils.isEmpty(priceValue)) {
            history.setCostPerUnit(Float.parseFloat(priceValue));
        }

        if (!TextUtils.isEmpty(totalValue)) {
            history.setTotalCost(Float.parseFloat(totalValue));
        }

        return FuelHistoryDTO.buildContentValues(history);
    }

    /**
     * {@inheritDoc}
     */
    protected void clear() {
        ViewUtils.clearText(odometer, volume, price, total);
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
    protected int getLayoutResourceId() {
        return layout.fragment_fuel_history_addedit;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Integer getMenuResourceId() {
        return menu.menu_fuel_history_edit;
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

    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadForm(final Cursor cursor) {
        history = FuelHistoryDTO.newInstance(cursor);

        setTitle(R.string.fuel_history_edit);
        setSubtitle(history.getName());

        notes.setText(history.getNotes());
        odometer.setText(history.getOdometerReading().toString());
        volume.setText(history.getFuelAmount().toString());

        if (history.getPurchaseDate() > 0) {
            formatPurchaseDate(history.getPurchaseDate());
            milliseconds = history.getPurchaseDate();
        }
        if (history.getCostPerUnit() > 0) {
            price.setText(history.getCostPerUnit().toString());
        }
        if (history.getOdometerReading() > 0) {
            odometer.setText(history.getOdometerReading().toString());
        }
        if (history.getTotalCost() > 0) {
            total.setText(history.getTotalCost().toString());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void setupForm() {
        final int errorResId = R.string.field_required;

        dateTime = finder.find(id.date_time);
        notes = finder.find(id.notes);
        odometer = finder.find(id.odometer);
        price = finder.find(id.price);
        total = finder.find(id.total);
        volume = finder.find(id.volume);

        validator.addValidator("odometer", new RequiredValidator(odometer, errorResId));
        validator.addValidator("volume", new RequiredValidator(volume, errorResId));

        price.addTextChangedListener(priceWatcher);
        total.addTextChangedListener(totalWatcher);
        volume.addTextChangedListener(volumeWatcher);

        formatPurchaseDate(milliseconds);

        dateTime.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(final View v, final boolean hasFocus) {
                if (hasFocus) {
                    final DateTimePickerDialogFragment picker;

                    picker = DateTimePickerDialogFragment.newInstance(milliseconds, null,
                                                                      DateUtils.getCurrentDateTime());
                    picker.show(getActivity().getSupportFragmentManager(), "date_picker");
                }
                odometer.requestFocus();
            }
        });

        finder.onEditorAction(new OnEditorActionListener() {
            @Override
            public boolean onEditorAction(final TextView view, final int actionId, final KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    onEntityEditListener.onEntitySave();

                    return true;
                }

                return false;
            }
        }, total);
    }

    /**
     * Format the passed in milliseconds into a localized date time string.
     *
     * @param milliseconds The milliseconds to format.
     */
    private void formatPurchaseDate(final Long milliseconds) {
        if (milliseconds == null) {
            return;
        }

        final Context context = getActivity();
        final String date = DateUtils.getUserLocaleFormattedDate(context, milliseconds);
        final String time = DateUtils.getUserLocaleFormattedTime(context, milliseconds);

        dateTime.setText(String.format("%s %s", date, time));
    }

    /**
     * Perform auto calculations for price, total, and volume.
     *
     * @param type The field that triggered the calculation.
     */
    private void performCalculations(final CalculationField type) {
        if (ViewUtils.isEmpty(price, total, volume)) {
            return;
        }

        final String priceValue = ViewUtils.getText(price);
        final String totalValue = ViewUtils.getText(total);
        final String volumeValue = ViewUtils.getText(volume);
        Float calculatedPrice;
        Float calculatedTotal;
        Float calculatedVolume;

        calculatedPrice = (TextUtils.isEmpty(priceValue)) ? 0F : Float.parseFloat(priceValue);
        calculatedTotal = (TextUtils.isEmpty(totalValue)) ? 0F : Float.parseFloat(totalValue);
        calculatedVolume = (TextUtils.isEmpty(volumeValue)) ? 0F : Float.parseFloat(volumeValue);

        if (calculatedPrice == 0 && calculatedTotal == 0 && calculatedVolume == 0) {
            return;
        }

        switch (type) {
            case Price: {
                if (calculatedVolume > 0) {
                    calculatedTotal = calculatedPrice * calculatedVolume;

                    if (calculatedTotal > 0) {
                        total.setText(String.format(Constants.DECIMAL_2_FORMAT, calculatedTotal));
                    }
                }
                else if (calculatedTotal > 0) {
                    calculatedVolume = calculatedTotal / calculatedPrice;

                    if (calculatedVolume > 0) {
                        volume.setText(String.format(Constants.DECIMAL_3_FORMAT, calculatedVolume));
                    }
                }
                break;
            }
            case Volume: {
                calculatedTotal = calculatedPrice * calculatedVolume;

                if (calculatedTotal > 0) {
                    total.setText(String.format(Constants.DECIMAL_2_FORMAT, calculatedTotal));
                }
                break;
            }
            case Total: {
                if (calculatedPrice > 0) {
                    calculatedVolume = calculatedTotal / calculatedPrice;

                    if (calculatedVolume > 0) {
                        volume.setText(String.format(Constants.DECIMAL_3_FORMAT, calculatedVolume));
                    }
                }
                else if (calculatedVolume > 0) {
                    calculatedPrice = calculatedTotal / calculatedVolume;

                    if (calculatedPrice > 0) {
                        price.setText(String.format(Constants.DECIMAL_3_FORMAT, calculatedPrice));
                    }
                }
            }
        }
    }

    /**
     *
     */
    private enum CalculationField {
        Price,
        Total,
        Volume
    }
}