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

package com.dabay6.android.apps.carlog.adapters;

import android.R.id;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.dabay6.android.apps.carlog.data.provider.CarLogContract.Model;
import com.utils.android.logging.Logger;
import com.utils.android.view.ViewsFinder;

/**
 * ModelCursorAdapter
 *
 * @author Remel Pugh
 * @version 1.0
 */
@SuppressWarnings("unused")
public class ModelCursorAdapter extends CursorAdapter {
    private final static String TAG = Logger.makeTag(ModelCursorAdapter.class);
    private final LayoutInflater inflater;
    private Long makeId;
    private ContentResolver resolver;

    /**
     *
     * @param context
     * @param c
     */
    @SuppressWarnings("deprecation")
    public ModelCursorAdapter(final Context context, final Cursor c) {
        super(context, c);

        inflater = LayoutInflater.from(context);
        resolver = context.getContentResolver();
    }

    /**
     *
     * @param context
     * @param c
     * @param autoRequery
     */
    public ModelCursorAdapter(final Context context, final Cursor c, final boolean autoRequery) {
        super(context, c, autoRequery);

        inflater = LayoutInflater.from(context);
        resolver = context.getContentResolver();
    }

    /**
     *
     * @param context
     * @param c
     * @param flags
     */
    public ModelCursorAdapter(final Context context, final Cursor c, final int flags) {
        super(context, c, flags);

        inflater = LayoutInflater.from(context);
        resolver = context.getContentResolver();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void bindView(final View view, final Context context, final Cursor cursor) {
        final ViewsFinder finder = new ViewsFinder(view);
        final TextView text = finder.find(id.text1);

        text.setText(convertToString(cursor));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CharSequence convertToString(final Cursor cursor) {
        final int columnIndex = cursor.getColumnIndexOrThrow(Model.Columns.MODEL_NAME.getName());

        return cursor.getString(columnIndex);
    }

    /**
     *
     * @return
     */
    public Long getMakeId() {
        return makeId;
    }

    /**
     *
     * @param makeId
     */
    public void setMakeId(Long makeId) {
        this.makeId = makeId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public View newView(final Context context, final Cursor cursor, final ViewGroup parent) {
        return inflater.inflate(android.R.layout.simple_dropdown_item_1line, parent, false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Cursor runQueryOnBackgroundThread(final CharSequence constraint) {
        if (getFilterQueryProvider() != null) {
            return getFilterQueryProvider().runQuery(constraint);
        }

        final String sort;
        String selection = null;
        String[] selectionArg = null;

        sort = Model.Columns.MODEL_NAME.getName() + " COLLATE NOCASE ASC";

        if (constraint != null) {
            selection = Model.Columns.MODEL_NAME.getName() + " LIKE ? AND MakeId = ?";
            selectionArg = new String[]{"%" + constraint.toString() + "%", makeId.toString()};
        }

        return resolver.query(Model.CONTENT_URI, Model.PROJECTION, selection, selectionArg, sort);
    }
}