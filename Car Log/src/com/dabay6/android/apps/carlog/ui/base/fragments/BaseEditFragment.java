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
import com.utils.android.logging.Logger;
import com.utils.android.ui.dialogs.BaseDialogFragment;
import com.utils.android.validation.FormValidation;
import com.utils.android.widget.ButtonBar;
import com.utils.android.widget.OnButtonBarClickListener;

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

        validator = new FormValidation(getSherlockActivity());

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