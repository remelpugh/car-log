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


import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceFragment;

/**
 * MaintenanceSettingsFragment
 *
 * @author RangeLog, LLC.
 * @version 1.0
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public abstract class BaseSettingsFragment extends PreferenceFragment implements OnPreferenceClickListener {
    /**
     * {@inheritDoc}
     */
    @Override
    public void addPreferencesFromResource(final int preferencesResId) {
        super.addPreferencesFromResource(preferencesResId);

        afterPreferences();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreate(final Bundle savedInstanceState) {
        final int resId;
        final String resource = getArguments().getString("resource");

        super.onCreate(savedInstanceState);

        resId = getActivity().getResources().getIdentifier(resource, "xml", getActivity().getPackageName());

        addPreferencesFromResource(resId);
    }

    /**
     *
     */
    protected abstract void afterPreferences();
}