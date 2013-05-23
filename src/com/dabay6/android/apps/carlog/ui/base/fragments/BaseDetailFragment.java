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
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import com.dabay6.android.apps.carlog.data.provider.util.ColumnMetadata;
import com.utils.android.adapters.SimpleTwoLineAdapter;
import com.utils.android.logging.Logger;
import com.utils.android.ui.fragments.BaseListFragment;
import com.utils.android.util.StringUtils;

import java.util.ArrayList;
import java.util.Map;

/**
 * BaseDetailFragment
 *
 * @author Remel Pugh
 * @version 1.0
 */
@SuppressWarnings("unused")
public abstract class BaseDetailFragment extends BaseListFragment implements LoaderCallbacks<Cursor> {
    protected final static int ENTITY_LOADER_ID = 0x01;
    protected final static String PARAMS_ENTITY_ID = "PARAMS_ENTITY_ID";
    protected final static String PARAMS_SUB_TITLE = "PARAMS_SUB_TITLE";
    protected final static String PARAMS_TITLE = "PARAMS_TITLE";
    private final static String KEY_ENTITY_ID = "KEY_ENTITY_ID";
    private final static String TAG = Logger.makeTag(BaseDetailFragment.class);
    protected SimpleTwoLineAdapter adapter;
    protected Long entityId;
    protected OnEntityDetailListener onEntityDetailListener;

    /**
     *
     * @return
     */
    public Long getEntityId() {
        return entityId;
    }

    /**
     *
     * @return
     */
    public String getSubtitle() {
        final Bundle bundle = getArguments();

        if (bundle != null && bundle.containsKey(PARAMS_SUB_TITLE)) {
            return bundle.getString(PARAMS_SUB_TITLE);
        }

        return null;
    }

    /**
     *
     * @return
     */
    public String getTitle() {
        final Bundle bundle = getArguments();

        if (bundle != null && bundle.containsKey(PARAMS_TITLE)) {
            return bundle.getString(PARAMS_TITLE);
        }

        return null;
    }

    /**
     *
     * @param id
     */
    public void loadDetails(final Long id) {
        entityId = id;

        if (entityId != null) {
            refresh();
        }
        else {
            adapter.clear();
            setListShownNoAnimation(true);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onAttach(final Activity activity) {
        super.onAttach(activity);

        try {
            onEntityDetailListener = (OnEntityDetailListener) activity;
        }
        catch (final ClassCastException ex) {
            throw new ClassCastException(activity.toString() + " must implement OnEntityDetailListener");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Loader<Cursor> onCreateLoader(final int i, final Bundle bundle) {
        final Uri uri = getUri().buildUpon().appendPath(entityId.toString()).build();

        return new CursorLoader(getActivity(), uri, getProjection(), null, null, null);
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
        createListItems(cursor);

        if (isResumed()) {
            setListShown(true);
        }
        else {
            setListShownNoAnimation(true);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onResume() {
        final String title = getTitle();

        super.onResume();

        if (!TextUtils.isEmpty(title)) {
            setTitle(title);
        }

        setSubtitle(getSubtitle());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);

        if (entityId != null) {
            outState.putLong(KEY_ENTITY_ID, entityId);
        }
    }

    /**
     *
     */
    public void refresh() {
        getLoaderManager().restartLoader(ENTITY_LOADER_ID, null, this);
    }

    /**
     *
     * @param column
     * @param data
     * @return
     */
    protected Map<String, String> createDetailItem(final ColumnMetadata column, final String data) {
        if (TextUtils.isEmpty(data)) {
            return null;
        }

        return SimpleTwoLineAdapter.createItem(StringUtils.splitIntoWords(column.getName()), data);
    }

    /**
     *
     * @param cursor
     */
    protected abstract void createListItems(final Cursor cursor);

    /**
     *
     * @return
     */
    protected abstract int getEmptyTextResourceId();

    /**
     * @return
     */
    protected abstract String getIdentityColumnName();

    /**
     *
     * @return
     */
    protected abstract int getListItemResourceId();

    /**
     * @return
     */
    protected abstract String[] getProjection();

    /**
     * @return
     */
    protected abstract Uri getUri();

    /**
     * {@inheritDoc}
     */
    @Override
    protected void setupListView(final Bundle savedInstanceState) {
        final Bundle bundle = getArguments();

        setEmptyText(getString(getEmptyTextResourceId()));

        adapter = new SimpleTwoLineAdapter(getSherlockActivity(), new ArrayList<Map<String, String>>(),
                                           getListItemResourceId());
        setListAdapter(adapter);

        if (bundle != null && bundle.containsKey(PARAMS_ENTITY_ID)) {
            entityId = bundle.getLong(PARAMS_ENTITY_ID);
        }

        if (savedInstanceState != null && savedInstanceState.containsKey(KEY_ENTITY_ID)) {
            entityId = savedInstanceState.getLong(KEY_ENTITY_ID);
        }

        if (entityId != null) {
            getLoaderManager().initLoader(ENTITY_LOADER_ID, null, this);

            setListShown(false);
        }
        else {
            setListShownNoAnimation(true);
        }
    }

    /**
     *
     */
    public interface OnEntityDetailListener {
        /**
         *
         * @param entityId
         */
        void onEntityEdit(final Long entityId);
    }
}