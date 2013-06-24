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

package com.dabay6.android.apps.carlog.data.DTO;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import com.dabay6.android.apps.carlog.data.provider.CarLogContract.Model.Columns;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@SuppressWarnings("unused")
public class ModelDTO implements Parcelable {
    public static final Parcelable.Creator<ModelDTO> CREATOR = new Parcelable.Creator<ModelDTO>() {
        public ModelDTO createFromParcel(final Parcel in) {
            return new ModelDTO(in);
        }

        public ModelDTO[] newArray(final int size) {
            return new ModelDTO[size];
        }
    };
    private Long makeId;
    private Long modelId;
    private String modelName;

    public ModelDTO() {
    }

    public ModelDTO(final Parcel in) {
        makeId = in.readLong();
        modelId = in.readLong();
        modelName = in.readString();
    }

    public Long getMakeId() {
        return makeId;
    }

    public void setMakeId(final Long value) {
        makeId = value;
    }
    public Long getModelId() {
        return modelId;
    }

    public void setModelId(final Long value) {
        modelId = value;
    }
    public String getModelName() {
        return modelName;
    }

    public void setModelName(final String value) {
        modelName = value;
    }
    public int describeContents() {
        return 0;
    }

    @Override
    public String toString() {
        final GsonBuilder builder = new GsonBuilder();
        final Gson gson;

        gson = builder.create();

        return gson.toJson(this);
    }

    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeLong(makeId);
        dest.writeLong(modelId);
        dest.writeString(modelName);
    }

    public static ContentValues buildContentValues(final ModelDTO model) {
        final ContentValues values = new ContentValues();
        String name;

        name = Columns.MAKE_ID.getName();
        values.put(name, model.getMakeId());

        name = Columns.MODEL_NAME.getName();
        values.put(name, model.getModelName());

        return values;
    }

    public static ModelDTO newInstance(final Cursor cursor) {
        final ModelDTO model;
        int index;

        if (cursor == null) {
            throw new IllegalStateException("Cursor can not be null.");
        }
        else if (cursor.isBeforeFirst() || cursor.isAfterLast()) {
            if (!cursor.moveToFirst()) {
                throw new IllegalStateException("Cursor can not be empty");
            }
        }

        model = new ModelDTO();

        index = cursor.getColumnIndex(Columns.MAKE_ID.getName());
        if (index != -1) {
            model.setMakeId(cursor.getLong(index));
        }
        index = cursor.getColumnIndex(Columns.MODEL_ID.getName());
        if (index != -1) {
            model.setModelId(cursor.getLong(index));
        }
        index = cursor.getColumnIndex(Columns.MODEL_NAME.getName());
        if (index != -1) {
            model.setModelName(cursor.getString(index));
        }
        return model;
    }
}