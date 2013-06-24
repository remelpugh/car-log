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
import com.utils.android.view.ViewsFinder;

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