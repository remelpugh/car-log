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
import com.utils.android.helper.SharedPreferencesEditorHelper;
import com.utils.android.logging.Logger;
import com.utils.android.util.AssetUtils;
import com.utils.android.util.DataUtils;
import com.utils.android.util.ListUtils;

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