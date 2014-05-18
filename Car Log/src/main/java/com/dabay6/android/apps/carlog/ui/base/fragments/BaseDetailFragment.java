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
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import com.dabay6.android.apps.carlog.data.provider.util.ColumnMetadata;
import com.dabay6.libraries.androidshared.adapters.SimpleTwoLineAdapter;
import com.dabay6.libraries.androidshared.logging.Logger;
import com.dabay6.libraries.androidshared.ui.fragments.BaseListFragment;
import com.dabay6.libraries.androidshared.util.StringUtils;

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
//    protected final static String PARAMS_SUB_TITLE = "PARAMS_SUB_TITLE";
//    protected final static String PARAMS_TITLE = "PARAMS_TITLE";
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

//    /**
//     *
//     * @return
//     */
//    public String getSubtitle() {
//        final Bundle bundle = getArguments();
//
//        if (bundle != null && bundle.containsKey(PARAMS_SUB_TITLE)) {
//            return bundle.getString(PARAMS_SUB_TITLE);
//        }
//
//        return null;
//    }

//    /**
//     *
//     * @return
//     */
//    public String getTitle() {
//        final Bundle bundle = getArguments();
//
//        if (bundle != null && bundle.containsKey(PARAMS_TITLE)) {
//            return bundle.getString(PARAMS_TITLE);
//        }
//
//        return null;
//    }

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

//    /**
//     * {@inheritDoc}
//     */
//    @Override
//    public void onResume() {
//        final String title = getTitle();
//
//        super.onResume();
//
//        if (!TextUtils.isEmpty(title)) {
//            setTitle(title);
//        }
//
//        setSubtitle(getSubtitle());
//    }

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

        adapter = new SimpleTwoLineAdapter(getActivity(), new ArrayList<Map<String, String>>(),
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