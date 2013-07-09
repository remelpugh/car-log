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

package com.dabay6.android.apps.carlog.ui.fragments;

import android.preference.Preference;
import android.preference.PreferenceActivity;
import com.dabay6.android.apps.carlog.R.string;
import com.dabay6.android.apps.carlog.ui.base.fragments.BaseSettingsFragment;
import com.utils.android.logging.Logger;
import com.utils.android.ui.dialogs.changelog.OnChangeLogDialogListener;
import com.utils.android.ui.dialogs.changelog.util.ChangeLogDialogUtils;
import com.utils.android.ui.dialogs.googleservices.util.LegalNoticesUtils;
import com.utils.android.ui.dialogs.opensource.OnOpenSourceDialogListener;
import com.utils.android.ui.dialogs.opensource.OpenSourceItem;
import com.utils.android.ui.dialogs.opensource.util.OpenSourceDialogUtils;
import com.utils.android.util.AppUtils;
import com.utils.android.util.ListUtils;

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
     * {@inheritDoc}
     */
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