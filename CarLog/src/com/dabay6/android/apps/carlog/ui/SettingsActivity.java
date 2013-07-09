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

package com.dabay6.android.apps.carlog.ui;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.support.v4.app.NavUtils;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockPreferenceActivity;
import com.actionbarsherlock.view.MenuItem;
import com.dabay6.android.apps.carlog.R.string;
import com.dabay6.android.apps.carlog.R.xml;
import com.dabay6.android.apps.carlog.app.CarLogApplication;
import com.utils.android.logging.Logger;
import com.utils.android.ui.dialogs.changelog.OnChangeLogDialogListener;
import com.utils.android.ui.dialogs.changelog.util.ChangeLogDialogUtils;
import com.utils.android.ui.dialogs.googleservices.util.LegalNoticesUtils;
import com.utils.android.ui.dialogs.opensource.OnOpenSourceDialogListener;
import com.utils.android.ui.dialogs.opensource.OpenSourceItem;
import com.utils.android.ui.dialogs.opensource.util.OpenSourceDialogUtils;
import com.utils.android.util.AndroidUtils;
import com.utils.android.util.AppUtils;
import com.utils.android.util.ListUtils;

import java.util.List;

/**
 * SettingsActivity
 *
 * @author Remel Pugh
 * @version 1.0
 */
public class SettingsActivity extends SherlockPreferenceActivity implements OnPreferenceClickListener {
    public static final Object[] dataLock = new Object[0];
    @SuppressWarnings("unused")
    private final static String TAG = Logger.makeTag(SettingsActivity.class);

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("deprecation")
    @Override
    public void addPreferencesFromResource(final int preferencesResId) {
        super.addPreferencesFromResource(preferencesResId);

        afterViews();
    }

    /**
     * {@inheritDoc}
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onBuildHeaders(final List<Header> target) {
        loadHeadersFromResource(xml.settings_headers, target);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        final int id = item.getItemId();

        switch (id) {
            case android.R.id.home: {
                NavUtils.navigateUpFromSameTask(this);
                return true;
            }
            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onPreferenceClick(final Preference preference) {
        final String key = preference.getKey();

        if (key.equalsIgnoreCase("backupapp")) {
            ((CarLogApplication) getApplication()).getBackupManager().dataChanged();
        }
        else if (key.equalsIgnoreCase("changelog")) {
            ChangeLogDialogUtils.displayChangeLog(this, new OnChangeLogDialogListener() {
                @Override
                public void onChangeLogDismissed() {
                }
            });
        }
        else if (key.equalsIgnoreCase("opensource")) {
            List<OpenSourceItem> items = ListUtils.newArrayList();

            items.add(new OpenSourceItem(getString(string.open_source_dropbox_sdk),
                                         getString(string.open_source_dropbox_sdk_license)));

            OpenSourceDialogUtils.displayOpenSourceDialog(this, items, new OnOpenSourceDialogListener() {
                @Override
                public void onOpenSourceDialogDismissed() {
                }
            });
        }

        return true;
    }

    /**
     *
     */
    @SuppressWarnings("deprecation")
    protected void afterViews() {
        Preference preference;

        preference = findPreference("BackupApp");
        if (preference != null) {
            preference.setOnPreferenceClickListener(this);
        }

        preference = findPreference("ChangeLog");
        if (preference != null) {
            preference.setSummary(AppUtils.getApplicationVersion(getBaseContext()));
            preference.setOnPreferenceClickListener(this);
        }

        preference = findPreference("OpenSource");
        if (preference != null) {
            preference.setOnPreferenceClickListener(this);
        }

        preference = LegalNoticesUtils.buildLegalNoticePreference(this);

        getPreferenceScreen().addPreference(preference);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        final ActionBar actionBar = getSupportActionBar();

        super.onCreate(savedInstanceState);

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setSubtitle(getString(string.settings));

        if (AndroidUtils.isVersionBefore(VERSION_CODES.HONEYCOMB)) {
            addPreferencesFromResource(xml.settings);
        }
    }
}