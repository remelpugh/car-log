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
import android.annotation.TargetApi;
import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.dabay6.android.apps.carlog.R;
import com.dabay6.android.apps.carlog.R.string;
import com.dabay6.android.apps.carlog.data.DTO.VehicleDTO;
import com.dabay6.libraries.androidshared.view.ViewsFinder;

/**
 * DualLineCursorAdapter
 *
 * @author Remel Pugh
 * @version 1.0
 */
public class DualLineCursorAdapter extends CursorAdapter {
    private final LayoutInflater inflater;
    private int dropDownLayout;
    private String title;

    /**
     * @param context
     * @param c
     */
    @SuppressWarnings("deprecation")
    public DualLineCursorAdapter(final Context context, final Cursor c) {
        super(context, c);

        inflater = LayoutInflater.from(context);
        title = context.getString(string.app_name);
    }

    /**
     * @param context
     * @param c
     * @param flags
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public DualLineCursorAdapter(final Context context, final Cursor c, final int flags) {
        super(context, c, flags);

        inflater = LayoutInflater.from(context);
        title = context.getString(string.app_name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void bindView(final View view, final Context context, final Cursor cursor) {
        final TextView text1;
        final TextView text2;
        final VehicleDTO vehicle = VehicleDTO.newInstance(cursor);
        final ViewsFinder finder = new ViewsFinder(view);

        text1 = finder.find(id.text1);
        text1.setText(this.title);

        text2 = finder.find(id.text2);
        if (text2 != null) {
            text2.setText(vehicle.getName());
        }
        else {
            text1.setText(vehicle.getName());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public View newDropDownView(final Context context, final Cursor cursor, final ViewGroup parent) {
        return inflater.inflate(dropDownLayout, parent, false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public View newView(final Context context, final Cursor cursor, final ViewGroup parent) {
        return inflater.inflate(R.layout.spinner_item_dual_line, parent, false);
    }

    /**
     * <p>Sets the layout resource of the drop down views.</p>
     *
     * @param dropDownLayout the layout resources used to createItems drop down views
     */
    public void setDropDownViewResource(final int dropDownLayout) {
        this.dropDownLayout = dropDownLayout;
    }

    /**
     * @param title
     */
    public void setTitle(final String title) {
        this.title = title;
    }

    public void setTitle(final int resId) {
        setTitle(mContext.getString(resId));
    }
}