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

package com.dabay6.android.apps.carlog.ui.fragments;

import android.annotation.TargetApi;
import android.os.Build.VERSION_CODES;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import com.dabay6.android.apps.carlog.R.string;
import com.dabay6.android.apps.carlog.ui.base.fragments.BaseSettingsFragment;
import com.dabay6.libraries.androidshared.logging.Logger;
import com.dabay6.libraries.androidshared.ui.dialogs.changelog.OnChangeLogDialogListener;
import com.dabay6.libraries.androidshared.ui.dialogs.changelog.util.ChangeLogDialogUtils;
import com.dabay6.libraries.androidshared.ui.dialogs.googleservices.util.LegalNoticesUtils;
import com.dabay6.libraries.androidshared.ui.dialogs.opensource.OnOpenSourceDialogListener;
import com.dabay6.libraries.androidshared.ui.dialogs.opensource.OpenSourceItem;
import com.dabay6.libraries.androidshared.ui.dialogs.opensource.util.OpenSourceDialogUtils;
import com.dabay6.libraries.androidshared.util.AppUtils;
import com.dabay6.libraries.androidshared.util.ListUtils;

import java.util.List;

/**
 * AboutSettingsFragment
 *
 * @author Remel Pugh
 * @version 1.0
 */
public class AboutSettingsFragment extends BaseSettingsFragment {
    @SuppressWarnings("unused")
    private final static String TAG = Logger.makeTag(AboutSettingsFragment.class);

    /**
     * Default constructor.
     */
    public AboutSettingsFragment() {
    }

    /**
     * {@inheritDoc}
     */
    @TargetApi(VERSION_CODES.HONEYCOMB)
    @Override
    public boolean onPreferenceClick(final Preference preference) {
        final String key = preference.getKey();

        if (key.equalsIgnoreCase("changelog")) {
            ChangeLogDialogUtils.displayChangeLog(getActivity(), new OnChangeLogDialogListener() {
                @Override
                public void onChangeLogDismissed() {
                }
            });
        }
        else if (key.equalsIgnoreCase("opensource")) {
            List<OpenSourceItem> items = ListUtils.newArrayList();

            items.add(new OpenSourceItem(getString(string.open_source_dropbox_sdk),
                                         getString(string.open_source_dropbox_sdk_license)));

            OpenSourceDialogUtils.displayOpenSourceDialog(getActivity(), items, new OnOpenSourceDialogListener() {
                @Override
                public void onOpenSourceDialogDismissed() {
                }
            });
        }

        return true;
    }

    /**
     * {@inheritDoc}
     */
    @TargetApi(VERSION_CODES.HONEYCOMB)
    @Override
    protected void afterPreferences() {
        Preference preference;

        preference = findPreference("ChangeLog");
        if (preference != null) {
            preference.setSummary(AppUtils.getApplicationVersion(getActivity().getApplicationContext()));
            preference.setOnPreferenceClickListener(this);
        }

        preference = findPreference("OpenSource");
        if (preference != null) {
            preference.setOnPreferenceClickListener(this);
        }

        preference = LegalNoticesUtils.buildLegalNoticePreference((PreferenceActivity) getActivity());

        getPreferenceScreen().addPreference(preference);
    }
}