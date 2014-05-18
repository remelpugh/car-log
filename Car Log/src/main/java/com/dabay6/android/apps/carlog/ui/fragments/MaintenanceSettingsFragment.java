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
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.preference.Preference;
import android.text.TextUtils;
import com.dabay6.android.apps.carlog.R;
import com.dabay6.android.apps.carlog.app.CarLogApplication;
import com.dabay6.android.apps.carlog.data.provider.CarLogProviderExtension;
import com.dabay6.android.apps.carlog.ui.base.fragments.BaseSettingsFragment;
import com.dropbox.sync.android.DbxAccountManager;
import com.dropbox.sync.android.DbxFile;
import com.dropbox.sync.android.DbxFileSystem;
import com.dropbox.sync.android.DbxPath;
import com.dabay6.libraries.androidshared.logging.Logger;
import com.dabay6.libraries.androidshared.util.StringUtils;
import com.dabay6.libraries.androidshared.util.ToastUtils;

import java.io.IOException;

/**
 * MaintenanceSettingsFragment
 *
 * @author RangeLog, LLC.
 * @version 1.0
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MaintenanceSettingsFragment extends BaseSettingsFragment {
    private final static int REQUEST_LINK_TO_DBX = 0x01;
    @SuppressWarnings("unused")
    private final static String TAG = Logger.makeTag(MaintenanceSettingsFragment.class);
    private DbxAccountManager dbxAccountManager;

    /**
     * Default constructor.
     */
    public MaintenanceSettingsFragment() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        switch (requestCode) {
            case REQUEST_LINK_TO_DBX: {
                if (resultCode == Activity.RESULT_OK) {
                    backupToDropbox();
                }

                ToastUtils.show(getActivity(), R.string.dropbox_link_failed);
                break;
            }
            default: {
                super.onActivityResult(requestCode, resultCode, data);
                break;
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onAttach(final Activity activity) {
        super.onAttach(activity);

        dbxAccountManager = CarLogApplication.getDbxAccountManager(activity.getApplicationContext());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onPreferenceClick(final Preference preference) {
        final String key = preference.getKey();

        if (key.equalsIgnoreCase("backupapp")) {
            final CarLogApplication application = (CarLogApplication) getActivity().getApplication();

            application.getBackupManager().dataChanged();
        }
        else if (key.equalsIgnoreCase("dropbox")) {
            if (!dbxAccountManager.hasLinkedAccount()) {
                dbxAccountManager.startLink(getActivity(), REQUEST_LINK_TO_DBX);

                return true;
            }

            backupToDropbox();
        }

        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void afterPreferences() {
        final int count = getPreferenceScreen().getPreferenceCount();

        for (int index = 0; index < count; index++) {
            final Preference preference = getPreferenceScreen().getPreference(index);
            final String key = preference.getKey();
            Drawable icon = null;

            preference.setOnPreferenceClickListener(this);
            try {
                String packageName = StringUtils.empty();

                if (key.equalsIgnoreCase("drive")) {
                    packageName = "com.google.android.apps.docs";
                }
                else if (key.equalsIgnoreCase("dropbox")) {
                    packageName = "com.dropbox.android";
                }

                if (!TextUtils.isEmpty(packageName)) {
                    icon = getActivity().getPackageManager().getApplicationIcon(packageName);
                }
            }
            catch (NameNotFoundException ignored) {
                // do nothing
            }

            if (icon != null) {
                preference.setIcon(icon);
            }
        }
    }

    private void backupToDropbox() {
        try {
            final java.io.File dbPath = getActivity().getDatabasePath(CarLogProviderExtension.DATABASE_NAME);
            final DbxFileSystem dbxFileSystem = DbxFileSystem.forAccount(dbxAccountManager.getLinkedAccount());
            final DbxPath dbxPath = new DbxPath(DbxPath.ROOT, dbPath.getName());
            final DbxFile file = dbxFileSystem.create(dbxPath);

            file.writeFromExistingFile(dbPath, true);
        }
        catch (final IOException ignored) {
        }
    }
}