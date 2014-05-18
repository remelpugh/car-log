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

package com.dabay6.android.apps.carlog.ui;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import com.dabay6.android.apps.carlog.R.string;
import com.dabay6.android.apps.carlog.R.xml;
import com.dabay6.android.apps.carlog.app.CarLogApplication;
import com.dabay6.libraries.androidshared.logging.Logger;
import com.dabay6.libraries.androidshared.ui.dialogs.changelog.OnChangeLogDialogListener;
import com.dabay6.libraries.androidshared.ui.dialogs.changelog.util.ChangeLogDialogUtils;
import com.dabay6.libraries.androidshared.ui.dialogs.googleservices.util.LegalNoticesUtils;
import com.dabay6.libraries.androidshared.ui.dialogs.opensource.OnOpenSourceDialogListener;
import com.dabay6.libraries.androidshared.ui.dialogs.opensource.OpenSourceItem;
import com.dabay6.libraries.androidshared.ui.dialogs.opensource.util.OpenSourceDialogUtils;
import com.dabay6.libraries.androidshared.util.AndroidUtils;
import com.dabay6.libraries.androidshared.util.AppUtils;
import com.dabay6.libraries.androidshared.util.ListUtils;

import java.util.List;

/**
 * SettingsActivity
 *
 * @author Remel Pugh
 * @version 1.0
 */
public class SettingsActivity extends PreferenceActivity implements OnPreferenceClickListener {
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
    @TargetApi(VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (AndroidUtils.isAtLeastHoneycomb()) {
            @SuppressLint("AppCompatMethod") final android.app.ActionBar actionBar = getActionBar();

            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setSubtitle(getString(string.settings));

                if (AndroidUtils.isAtLeastICS()) {
                    actionBar.setHomeButtonEnabled(true);
                }
            }
        }

        if (AndroidUtils.isVersionBefore(VERSION_CODES.HONEYCOMB)) {
            addPreferencesFromResource(xml.settings);
        }
    }
}