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

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.support.v7.view.ActionMode;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import com.dabay6.android.apps.carlog.R.string;
import com.dabay6.libraries.androidshared.adapters.BaseCheckableCursorAdapter;
import com.dabay6.libraries.androidshared.adapters.OnActionModeCallbackListener;
import com.dabay6.libraries.androidshared.logging.Logger;
import com.dabay6.libraries.androidshared.ui.fragments.BaseListFragment;
import com.dabay6.libraries.androidshared.util.ListUtils;

import java.util.List;
import java.util.Set;

/**
 * BaseDeleteListFragment
 *
 * @author Remel Pugh
 * @version 1.0
 */
@SuppressWarnings("unused")
public abstract class BaseDeleteListFragment extends BaseListFragment
        implements LoaderCallbacks<Cursor>, OnActionModeCallbackListener {
    private static final int DATA_LIST_ID = 0x01;
    private final static String TAG = Logger.makeTag(BaseDeleteListFragment.class);
    protected static String KEY_IS_DATA_QUERY_ALLOWED = "KEY_IS_DATA_QUERY_ALLOWED";
    protected OnEntityListListener onEntityListListener;
    private BaseCheckableCursorAdapter adapter;

    /**
     * {@inheritDoc}
     */
    @Override
    public void onActionDelete(final ActionMode mode) {
        final DeleteDialogFragment dialog = new DeleteDialogFragment();

        dialog.show(getFragmentManager(), "delete_dialog");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onActionModeCreated(final ActionMode mode) {
        final Integer resId = getTitleResourceId();

        if (resId != null) {
            mode.setSubtitle(getTitleResourceId());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onActionModeDestroyed(final ActionMode mode) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onAttach(final Activity activity) {
        super.onAttach(activity);

        try {
            onEntityListListener = (OnEntityListListener) activity;
        }
        catch (final ClassCastException ex) {
            throw new ClassCastException(activity.toString() + " must implement OnEntityListListener");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDestroyView() {
        getLoaderManager().destroyLoader(DATA_LIST_ID);

        super.onDestroyView();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onLoadFinished(final Loader<Cursor> cursorLoader, final Cursor cursor) {
        adapter.swapCursor(cursor);

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
    public void onLoaderReset(final Loader<Cursor> cursorLoader) {
        adapter.swapCursor(null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);

        if (adapter != null) {
            adapter.onSaveInstanceState(outState);
        }
    }

    /**
     *
     */
    public void refresh() {
        setListShown(false);
        getLoaderManager().restartLoader(DATA_LIST_ID, null, this);
    }

    /**
     *
     */
    public void startLoader() {
        getLoaderManager().initLoader(DATA_LIST_ID, null, this);
    }

    /**
     * @param savedInstanceState
     * @return
     */
    protected abstract BaseCheckableCursorAdapter createListAdapter(final Bundle savedInstanceState);

    /**
     *
     * @return
     */
    protected abstract int getDeleteMessageResourceId();

    /**
     *
     * @return
     */
    protected abstract int getEmptyTextResourceId();

    /**
     *
     * @return
     */
    protected abstract Integer getTitleResourceId();

    /**
     * {@inheritDoc}
     */
    @Override
    protected void setupListView(final Bundle savedInstanceState) {
        final Bundle arguments = getArguments();
        final Resources resources = getResources();
        boolean isDataQueryAllowed = true;

        setEmptyText(getString(getEmptyTextResourceId()));

        if (adapter == null) {
            adapter = createListAdapter(savedInstanceState);
        }

        adapter.setAdapterView(getListView());
        adapter.setDualPane(isDualPane());
        adapter.setOnActionModeCallbackListener(this);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
                if (onEntityListListener != null) {
                    onEntityListListener.onEntitySelected(position, id);
                }
            }
        });

        setListAdapter(adapter);

        if (arguments != null && arguments.containsKey(KEY_IS_DATA_QUERY_ALLOWED)) {
            isDataQueryAllowed = arguments.getBoolean(KEY_IS_DATA_QUERY_ALLOWED);
        }

        if (isDataQueryAllowed) {
            startLoader();
        }

        setListShown(false);
    }

    /**
     *
     */
    public interface OnEntityListListener {
        /**
         *
         */
        void onEntityAdd();

        /**
         *
         * @param ids
         */
        void onEntityDelete(final List<Long> ids);

        /**
         *
         * @param position
         * @param id
         */
        void onEntitySelected(final int position, final long id);
    }

    /**
     *
     */
    @SuppressLint("ValidFragment")
    private class DeleteDialogFragment extends DialogFragment {
        /**
         * Default constructor.
         */
        public DeleteDialogFragment() {
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Dialog onCreateDialog(final Bundle savedInstanceState) {
            final Builder builder = new Builder(getActivity());
            final int count = adapter.getSelectedCount();
            final List<Long> ids = ListUtils.newList(count);
            final Set<Long> selectedItems = adapter.getSelectedItems();

            for (final Long id : selectedItems) {
                ids.add(id);
            }

            return builder.setTitle(string.app_name)
                          .setMessage(getResources().getQuantityString(getDeleteMessageResourceId(), count, count))
                          .setNegativeButton(android.R.string.no, new OnClickListener() {
                              @Override
                              public void onClick(final DialogInterface dialog, final int which) {
                                  dialog.dismiss();
                              }
                          })
                          .setPositiveButton(android.R.string.yes, new OnClickListener() {
                              @Override
                              public void onClick(final DialogInterface dialog, final int which) {
                                  onEntityListListener.onEntityDelete(ids);

                                  adapter.finishActionMode();

                                  dialog.dismiss();
                              }
                          })
                          .create();
        }
    }
}