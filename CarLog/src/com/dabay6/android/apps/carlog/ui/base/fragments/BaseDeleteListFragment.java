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
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import com.actionbarsherlock.view.ActionMode;
import com.dabay6.android.apps.carlog.R.string;
import com.utils.android.adapters.BaseCheckableCursorAdapter;
import com.utils.android.adapters.OnActionModeCallbackListener;
import com.utils.android.logging.Logger;
import com.utils.android.ui.fragments.BaseListFragment;
import com.utils.android.util.ListUtils;

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
    private class DeleteDialogFragment extends DialogFragment {
        /**
         * {@inheritDoc}
         */
        @Override
        public Dialog onCreateDialog(final Bundle savedInstanceState) {
            final Builder builder = new Builder(getSherlockActivity());
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