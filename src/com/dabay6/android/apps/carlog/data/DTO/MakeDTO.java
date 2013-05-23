package com.dabay6.android.apps.carlog.data.DTO;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import com.dabay6.android.apps.carlog.data.provider.CarLogContract.Make.Columns;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@SuppressWarnings("unused")
public class MakeDTO implements Parcelable {
    public static final Parcelable.Creator<MakeDTO> CREATOR = new Parcelable.Creator<MakeDTO>() {
        public MakeDTO createFromParcel(final Parcel in) {
            return new MakeDTO(in);
        }

        public MakeDTO[] newArray(final int size) {
            return new MakeDTO[size];
        }
    };
    private Long makeId;
    private String makeName;

    public MakeDTO() {
    }

    public MakeDTO(final Parcel in) {
        makeId = in.readLong();
        makeName = in.readString();
    }

    public Long getMakeId() {
        return makeId;
    }

    public void setMakeId(final Long value) {
        makeId = value;
    }
    public String getMakeName() {
        return makeName;
    }

    public void setMakeName(final String value) {
        makeName = value;
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
        dest.writeString(makeName);
    }

    public static ContentValues buildContentValues(final MakeDTO make) {
        final ContentValues values = new ContentValues();
        String name;

        name = Columns.MAKE_NAME.getName();
        values.put(name, make.getMakeName());

        return values;
    }

    public static MakeDTO newInstance(final Cursor cursor) {
        final MakeDTO make;
        int index;

        if (cursor == null) {
            throw new IllegalStateException("Cursor can not be null.");
        }
        else if (cursor.isBeforeFirst() || cursor.isAfterLast()) {
            if (!cursor.moveToFirst()) {
                throw new IllegalStateException("Cursor can not be empty");
            }
        }

        make = new MakeDTO();

        index = cursor.getColumnIndex(Columns.MAKE_ID.getName());
        if (index != -1) {
            make.setMakeId(cursor.getLong(index));
        }
        index = cursor.getColumnIndex(Columns.MAKE_NAME.getName());
        if (index != -1) {
            make.setMakeName(cursor.getString(index));
        }
        return make;
    }
}