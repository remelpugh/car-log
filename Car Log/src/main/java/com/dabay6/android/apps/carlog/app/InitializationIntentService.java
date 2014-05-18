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

package com.dabay6.android.apps.carlog.app;

import android.app.IntentService;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import com.dabay6.android.apps.carlog.configuration.Intents;
import com.dabay6.android.apps.carlog.configuration.SharedPreferenceKeys;
import com.dabay6.android.apps.carlog.data.DTO.MakeDTO;
import com.dabay6.android.apps.carlog.data.DTO.ModelDTO;
import com.dabay6.android.apps.carlog.data.provider.CarLogContract.Make;
import com.dabay6.android.apps.carlog.data.provider.CarLogContract.Make.Columns;
import com.dabay6.android.apps.carlog.data.provider.CarLogContract.Model;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.dabay6.libraries.androidshared.helper.SharedPreferencesEditorHelper;
import com.dabay6.libraries.androidshared.logging.Logger;
import com.dabay6.libraries.androidshared.util.AssetUtils;
import com.dabay6.libraries.androidshared.util.DataUtils;
import com.dabay6.libraries.androidshared.util.ListUtils;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * InitializationIntentService
 *
 * @author Remel Pugh
 * @version 1.0
 */
public class InitializationIntentService extends IntentService {
    private final static String TAG = Logger.makeTag(InitializationIntentService.class);

    /**
     * {@inheritDoc}
     */
    public InitializationIntentService() {
        super(TAG);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onHandleIntent(final Intent intent) {
        if (generateMakes() && generateModels()) {
            this.broadcastResponse();
        }
        else {
            this.broadcastResponse(true);
        }
    }

    /**
     *
     */
    private void broadcastResponse() {
        this.broadcastResponse(false);
    }

    /**
     * @param hasError
     */
    private void broadcastResponse(final boolean hasError) {
        final boolean isInitialized;
        final Intent intent = new Intent(Intents.INTENT_INIT_FINISHED);
        final SharedPreferencesEditorHelper helper = new SharedPreferencesEditorHelper(this);

        if (hasError) {
            getContentResolver().delete(Make.CONTENT_URI, null, null);
            getContentResolver().delete(Model.CONTENT_URI, null, null);

            intent.putExtra(Intents.INTENT_INIT_EXTRA_ERROR, hasError);
            isInitialized = false;
        }
        else {
            isInitialized = true;
        }

        helper.putBoolean(SharedPreferenceKeys.PREF_INITIALIZED, isInitialized).commit(true);
        this.sendBroadcast(intent);
    }

    /**
     * @return
     */
    private Gson buildGson() {
        final GsonBuilder builder = new GsonBuilder();

        builder.setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE);

        return builder.create();
    }

    /**
     * @return
     */
    private boolean generateMakes() {
        boolean success = true;

        try {
            final List<MakeDTO> makes;
            final List<ContentValues> values;
            final String json = AssetUtils.read(this, "data/makes.json");
            final Type type = new TypeToken<List<MakeDTO>>() {
            }.getType();

            makes = buildGson().fromJson(json, type);

            Collections.sort(makes, new Comparator<MakeDTO>() {
                @Override
                public int compare(final MakeDTO make1, final MakeDTO make2) {
                    return make1.getMakeName().compareTo(make2.getMakeName());
                }
            });

            values = ListUtils.newList(makes.size());
            for (final MakeDTO make : makes) {
                values.add(MakeDTO.buildContentValues(make));
            }

            getContentResolver().bulkInsert(Make.CONTENT_URI, ListUtils.toArray(values, ContentValues.class));
        }
        catch (final Exception e) {
            success = false;
            Logger.error(TAG, e.getMessage(), e);
        }

        return success;
    }

    /**
     * @return
     */
    private boolean generateModels() {
        boolean success = true;

        try {
            final ContentResolver resolver = getContentResolver();
            final List<ModelData> modelDataList;
            final String json = AssetUtils.read(this, "data/models.json");
            final Type type = new TypeToken<List<ModelData>>() {
            }.getType();

            modelDataList = buildGson().fromJson(json, type);

            for (final ModelData modelData : modelDataList) {
                final Cursor cursor;
                final List<ModelDTO> models = modelData.models;
                final List<ContentValues> values;
                final MakeDTO make;
                final String name = Columns.MAKE_NAME.getName();

                Collections.sort(models, new Comparator<ModelDTO>() {
                    @Override
                    public int compare(final ModelDTO model1, final ModelDTO model2) {
                        return model1.getModelName().compareTo(model2.getModelName());
                    }
                });

                cursor = resolver.query(Make.CONTENT_URI, Make.PROJECTION, name + " = ?",
                                        new String[]{modelData.makeName}, name + " COLLATE " + "LOCALIZED ASC");

                make = MakeDTO.newInstance(cursor);

                DataUtils.close(cursor);

                values = ListUtils.newList(models.size());

                for (final ModelDTO model : models) {
                    model.setMakeId(make.getMakeId());

                    values.add(ModelDTO.buildContentValues(model));
                }

                resolver.bulkInsert(Model.CONTENT_URI, ListUtils.toArray(values, ContentValues.class));
            }
        }
        catch (final Exception e) {
            success = false;
            Logger.error(TAG, e.getMessage(), e);
        }

        return success;
    }

    /**
     *
     */
    private static class ModelData {
        public String makeName;
        public List<ModelDTO> models;
    }
}