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
import com.dabay6.android.apps.carlog.data.provider.CarLogContract.Make;
import com.dabay6.android.apps.carlog.data.provider.CarLogContract.Make.Columns;
import com.dabay6.libraries.androidshared.logging.Logger;
import com.dabay6.libraries.androidshared.view.ViewsFinder;

/**
 * MakeCursorAdapter
 *
 * @author Remel Pugh
 * @version 1.0
 */
@SuppressWarnings("unused")
public class MakeCursorAdapter extends CursorAdapter {
    private final static String TAG = Logger.makeTag(MakeCursorAdapter.class);
    private final LayoutInflater inflater;
    private ContentResolver resolver;

    /**
     *
     * @param context
     * @param c
     */
    @SuppressWarnings("deprecation")
    public MakeCursorAdapter(final Context context, final Cursor c) {
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
    public MakeCursorAdapter(final Context context, final Cursor c, final boolean autoRequery) {
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
    public MakeCursorAdapter(final Context context, final Cursor c, final int flags) {
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
        return cursor.getString(Columns.MAKE_NAME.getIndex());
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
        final String selection;
        final String[] selectionArg;

        sort = Columns.MAKE_NAME.getName() + " COLLATE NOCASE ASC";
        selection = constraint == null ? null : Columns.MAKE_NAME.getName() + " LIKE ?";
        selectionArg = constraint == null ? null : new String[]{"%" + constraint.toString() + "%"};

        return resolver.query(Make.CONTENT_URI, Make.PROJECTION, selection, selectionArg, sort);
    }
}