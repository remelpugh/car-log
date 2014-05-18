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

package com.dabay6.android.apps.carlog.ui.base.fragments;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import com.dabay6.android.apps.carlog.R.id;
import com.dabay6.libraries.androidshared.logging.Logger;
import com.dabay6.libraries.androidshared.ui.dialogs.BaseDialogFragment;
import com.dabay6.libraries.androidshared.validation.FormValidation;
import com.dabay6.libraries.androidshared.widget.ButtonBar;
import com.dabay6.libraries.androidshared.widget.OnButtonBarClickListener;

/**
 * BaseEditFragment
 *
 * @author Remel Pugh
 * @version 1.0
 */
@SuppressWarnings("unused")
public abstract class BaseEditFragment extends BaseDialogFragment
        implements LoaderCallbacks<Cursor>, OnButtonBarClickListener {
    protected static final int ENTITY_LOADER_ID = 0x01;
    protected static final String PARAMS_ENTITY_ID = "PARAMS_ENTITY_ID";
    private final static String TAG = Logger.makeTag(BaseEditFragment.class);
    protected Long entityId;
    protected boolean isInsert = true;
    protected OnEntityEditListener onEntityEditListener;
    protected FormValidation validator;

    /**
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        final Bundle bundle = getArguments();

        super.onActivityCreated(savedInstanceState);

        if (bundle == null) {
            return;
        }

        if (bundle.containsKey(PARAMS_ENTITY_ID)) {
            final LoaderManager manager = getLoaderManager();

            isInsert = false;
            entityId = bundle.getLong(PARAMS_ENTITY_ID);

            manager.restartLoader(ENTITY_LOADER_ID, null, this);
            //            if (manager.getLoader(ENTITY_LOADER_ID) == null) {
            //                manager.initLoader(ENTITY_LOADER_ID, null, this);
            //            }
            //            else {
            //                manager.restartLoader(ENTITY_LOADER_ID, null, this);
            //            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onAttach(final Activity activity) {
        super.onAttach(activity);

        try {
            onEntityEditListener = (OnEntityEditListener) activity;
        }
        catch (final ClassCastException ex) {
            throw new ClassCastException(activity.toString() + " must implement OnEntityEditListener");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Loader<Cursor> onCreateLoader(final int i, final Bundle bundle) {
        if (i == ENTITY_LOADER_ID) {
            final Uri uri = getUri().buildUpon().appendPath(entityId.toString()).build();

            return new CursorLoader(applicationContext, uri, getProjection(), null, null, null);
        }

        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDestroyView() {
        getLoaderManager().destroyLoader(ENTITY_LOADER_ID);

        super.onDestroyView();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onLoadFinished(final Loader<Cursor> cursorLoader, final Cursor cursor) {
        loadForm(cursor);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onNegativeButtonClick() {
        onEntityEditListener.onEntityCancel();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onPositiveButtonClick() {
        try {
            if (validator.validate()) {
                final ContentValues values = buildContentValues();
                final ContentResolver resolver = applicationContext.getContentResolver();

                if (isInsert) {
                    resolver.insert(getUri(), values);
                }
                else {
                    final String selection = getIdentityColumnName() + " = ?";
                    final String[] selectionArgs = new String[]{entityId.toString()};

                    resolver.update(getUri(), values, selection, selectionArgs);
                }

                clear();

                onEntityEditListener.onEntitySave();
            }
        }
        catch (Exception e) {
            Logger.error(TAG, e.getMessage(), e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void afterViews(final Bundle savedInstanceState) {
        final ButtonBar buttonBar;

        buttonBar = finder.find(id.buttonBar);
        buttonBar.setOnButtonBarClickListener(this);
        buttonBar.setPositiveButtonEnabled(true);

        validator = new FormValidation(getActivity());

        setupForm();
    }

    /**
     *
     */
    protected abstract ContentValues buildContentValues();

    /**
     * Reset the all form views to empty.
     */
    protected abstract void clear();

    /**
     * @return
     */
    protected abstract String getIdentityColumnName();

    /**
     * @return
     */
    protected abstract String[] getProjection();

    /**
     * @return
     */
    protected abstract Uri getUri();

    /**
     * @param cursor
     */
    protected abstract void loadForm(final Cursor cursor);

    /**
     *
     */
    protected abstract void setupForm();

    /**
     *
     */
    public interface OnEntityEditListener {
        /**
         *
         */
        void onEntityCancel();

        /**
         *
         */
        void onEntitySave();
    }
}