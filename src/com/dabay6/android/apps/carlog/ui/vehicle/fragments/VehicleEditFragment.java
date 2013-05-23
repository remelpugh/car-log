package com.dabay6.android.apps.carlog.ui.vehicle.fragments;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import com.dabay6.android.apps.carlog.R;
import com.dabay6.android.apps.carlog.R.id;
import com.dabay6.android.apps.carlog.R.layout;
import com.dabay6.android.apps.carlog.R.menu;
import com.dabay6.android.apps.carlog.R.string;
import com.dabay6.android.apps.carlog.adapters.MakeCursorAdapter;
import com.dabay6.android.apps.carlog.adapters.ModelCursorAdapter;
import com.dabay6.android.apps.carlog.data.DTO.MakeDTO;
import com.dabay6.android.apps.carlog.data.DTO.ModelDTO;
import com.dabay6.android.apps.carlog.data.DTO.VehicleDTO;
import com.dabay6.android.apps.carlog.data.provider.CarLogContract.Make;
import com.dabay6.android.apps.carlog.data.provider.CarLogContract.Model;
import com.dabay6.android.apps.carlog.data.provider.CarLogContract.Vehicle;
import com.dabay6.android.apps.carlog.ui.base.fragments.BaseEditFragment;
import com.utils.android.logging.Logger;
import com.utils.android.util.AndroidUtils;
import com.utils.android.util.ViewUtils;
import com.utils.android.validation.RequiredValidator;

/**
 * VehicleEditFragment
 *
 * @author Remel Pugh
 * @version 1.0
 */
public class VehicleEditFragment extends BaseEditFragment {
    private static final int MAKE_LOADER_ID = ENTITY_LOADER_ID + 1;
    private static final int MODEL_LOADER_ID = MAKE_LOADER_ID + 1;
    @SuppressWarnings("unused")
    private final static String TAG = Logger.makeTag(VehicleEditFragment.class);
    private EditText licensePlate;
    private AutoCompleteTextView make;
    private MakeCursorAdapter makeAdapter;
    private AutoCompleteTextView model;
    private ModelCursorAdapter modelAdapter;
    private Long modelId;
    private EditText name;
    private VehicleDTO vehicle;
    private EditText vin;
    private EditText year;

    /**
     * Creates a new instance of {@link VehicleEditFragment}.
     *
     * @return An instance of {@link VehicleEditFragment}.
     */
    public static VehicleEditFragment newInstance() {
        return VehicleEditFragment.newInstance(null);
    }

    /**
     * Creates a new instance of {@link VehicleEditFragment}.
     *
     * @param id The vehicle id to be loaded.
     *
     * @return An instance of {@link VehicleEditFragment}.
     */
    public static VehicleEditFragment newInstance(final Long id) {
        final Bundle bundle = new Bundle();
        final VehicleEditFragment fragment = new VehicleEditFragment();

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
        setTitle(string.vehicle_add);

        super.onActivityCreated(savedInstanceState);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Loader<Cursor> onCreateLoader(final int id, final Bundle bundle) {
        switch (id) {
            case MAKE_LOADER_ID: {
                return new CursorLoader(applicationContext, Make.CONTENT_URI, Make.PROJECTION, null, null, null);
            }
            case MODEL_LOADER_ID: {
                return new CursorLoader(applicationContext, Model.CONTENT_URI, Model.PROJECTION, null, null, null);
            }
            default: {
                return super.onCreateLoader(id, bundle);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDestroyView() {
        final LoaderManager manager = getLoaderManager();

        makeAdapter = null;
        modelAdapter = null;

        manager.destroyLoader(MAKE_LOADER_ID);
        manager.destroyLoader(MODEL_LOADER_ID);

        super.onDestroyView();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onLoadFinished(final Loader<Cursor> cursorLoader, final Cursor cursor) {
        final int id = cursorLoader.getId();

        switch (id) {
            case MAKE_LOADER_ID: {
                if (cursor != null) {
                    makeAdapter.swapCursor(cursor);
                }
                break;
            }
            case MODEL_LOADER_ID: {
                if (cursor != null) {
                    modelAdapter.swapCursor(cursor);
                }
                break;
            }
            default: {
                super.onLoadFinished(cursorLoader, cursor);
                break;
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onLoaderReset(final Loader<Cursor> cursorLoader) {
        final int id = cursorLoader.getId();

        switch (id) {
            case MAKE_LOADER_ID: {
                if (makeAdapter != null) {
                    makeAdapter.swapCursor(null);
                }
                break;
            }
            case MODEL_LOADER_ID: {
                if (modelAdapter != null) {
                    modelAdapter.swapCursor(null);
                }
                break;
            }
        }
    }

    /**
     *
     */
    @Override
    protected ContentValues buildContentValues() {
        final ContentResolver resolver = getSherlockActivity().getContentResolver();
        final StringBuilder vehicleName = new StringBuilder();

        vehicleName.append(ViewUtils.getText(name));

        if (vehicle == null) {
            vehicle = new VehicleDTO();
        }
        if (TextUtils.isEmpty(vehicleName)) {
            if (!ViewUtils.isEmpty(year)) {
                vehicleName.insert(0, " ");
                vehicleName.insert(0, ViewUtils.getText(year));
            }
            vehicleName.append(ViewUtils.getText(make)).append(" ").append(ViewUtils.getText(model));
        }

        vehicle.setIsActive(true);
        vehicle.setLicensePlate(ViewUtils.getText(licensePlate));
        vehicle.setName(vehicleName.toString());
        vehicle.setVin(ViewUtils.getText(vin));

        if (modelAdapter.getMakeId() == null) {
            final MakeDTO makeDTO = new MakeDTO();

            makeDTO.setMakeName(ViewUtils.getText(make));

            modelAdapter.setMakeId(
                    ContentUris.parseId(resolver.insert(Make.CONTENT_URI, MakeDTO.buildContentValues(makeDTO))));
        }

        vehicle.setMakeId(modelAdapter.getMakeId());

        if (modelId == null) {
            final ModelDTO modelDTO = new ModelDTO();

            modelDTO.setMakeId(modelAdapter.getMakeId());
            modelDTO.setModelName(ViewUtils.getText(model));

            modelId = ContentUris.parseId(resolver.insert(Model.CONTENT_URI, ModelDTO.buildContentValues(modelDTO)));
        }
        vehicle.setModelId(modelId);

        if (!ViewUtils.isEmpty(year)) {
            vehicle.setYear(Integer.valueOf(ViewUtils.getText(year)));
        }

        return VehicleDTO.buildContentValues(vehicle);
    }

    /**
     *
     */
    @TargetApi(VERSION_CODES.JELLY_BEAN_MR1)
    protected void clear() {
        if (AndroidUtils.isAtLeastJellyBeanMR1()) {
            ViewUtils.clearText(licensePlate, name, vin, year);
            make.setText(null, false);
            model.setText(null, false);
        }
        else {
            ViewUtils.clearText(licensePlate, name, make, model, vin, year);
        }

        name.requestFocus();
    }

    /**
     * @return
     */
    @Override
    protected String getIdentityColumnName() {
        return Vehicle.Columns.VEHICLE_ID.getName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected int getLayoutResourceId() {
        return layout.fragment_vehicle_addedit;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Integer getMenuResourceId() {
        return menu.menu_vehicle_edit;
    }

    /**
     * @return
     */
    @Override
    protected String[] getProjection() {
        return Vehicle.PROJECTION;
    }

    /**
     * @return
     */
    @Override
    protected Uri getUri() {
        return Vehicle.CONTENT_URI;
    }

    /**
     * @param cursor
     */
    @Override
    protected void loadForm(final Cursor cursor) {
        vehicle = VehicleDTO.newInstance(cursor);

        setTitle(vehicle.getName());

        licensePlate.setText(vehicle.getLicensePlate());
        make.setText(vehicle.getMakeName());
        model.setText(vehicle.getModelName());
        model.setEnabled(true);
        name.setText(vehicle.getName());
        vin.setText(vehicle.getVin());

        if (vehicle.getYear() > 0) {
            final String text = vehicle.getYear().toString();

            year.setText(text);
        }

        modelAdapter.setMakeId(vehicle.getMakeId());
        modelId = vehicle.getModelId();
    }

    /**
     *
     */
    @SuppressWarnings("deprecation")
    @Override
    protected void setupForm() {
        final int errorResId = R.string.field_required;
        final LoaderManager manager = getLoaderManager();
        final String[] fromModel = new String[]{Model.Columns.MODEL_NAME.getName()};
        final Context context = getActivity();

        if (AndroidUtils.isAtLeastHoneycomb()) {
            makeAdapter = new MakeCursorAdapter(context, null, 0);
            modelAdapter = new ModelCursorAdapter(context, null, 0);
        }
        else {
            makeAdapter = new MakeCursorAdapter(context, null);
            modelAdapter = new ModelCursorAdapter(context, null);
        }

        licensePlate = finder.find(id.license_plate);
        make = finder.find(R.id.make);
        model = finder.find(R.id.model);
        name = finder.find(R.id.name);
        vin = finder.find(R.id.vin);
        year = finder.find(R.id.year);

        validator.addValidator("make", new RequiredValidator(make, errorResId));
        validator.addValidator("model", new RequiredValidator(model, errorResId));

        make.setAdapter(makeAdapter);
        make.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
                modelAdapter.setMakeId(id);
            }
        });

        finder.addTextWatcher(new TextWatcher() {
            @Override
            public void afterTextChanged(final Editable s) {
            }

            @Override
            public void beforeTextChanged(final CharSequence s, final int start, final int count, final int after) {
            }

            @Override
            public void onTextChanged(final CharSequence s, final int start, final int before, final int count) {
                model.setEnabled(s.length() > 0);
            }
        }, make);

        finder.onEditorAction(new OnEditorActionListener() {
            @Override
            public boolean onEditorAction(final TextView view, final int actionId, final KeyEvent event) {
                onPositiveButtonClick();

                return true;
            }
        }, vin);

        model.setAdapter(modelAdapter);
        model.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
                modelId = id;
            }
        });

        manager.initLoader(MAKE_LOADER_ID, null, this);
        manager.initLoader(MODEL_LOADER_ID, null, this);
    }
}