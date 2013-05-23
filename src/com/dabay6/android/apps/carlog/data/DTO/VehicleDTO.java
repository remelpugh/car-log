package com.dabay6.android.apps.carlog.data.DTO;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import com.dabay6.android.apps.carlog.data.provider.CarLogContract.Vehicle.Columns;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@SuppressWarnings("unused")
public class VehicleDTO implements Parcelable {
    public static final Parcelable.Creator<VehicleDTO> CREATOR = new Parcelable.Creator<VehicleDTO>() {
        public VehicleDTO createFromParcel(final Parcel in) {
            return new VehicleDTO(in);
        }

        public VehicleDTO[] newArray(final int size) {
            return new VehicleDTO[size];
        }
    };
    private Boolean isActive;
    private String licensePlate;
    private Long makeId;
    private String makeName;
    private Long modelId;
    private String modelName;
    private String name;
    private String notes;
    private Long vehicleId;
    private String vin;
    private Integer year;

    public VehicleDTO() {
    }

    public VehicleDTO(final Parcel in) {
        isActive = in.readByte() != 0x00;
        licensePlate = in.readString();
        makeId = in.readLong();
        makeName = in.readString();
        modelId = in.readLong();
        modelName = in.readString();
        name = in.readString();
        notes = in.readString();
        vehicleId = in.readLong();
        vin = in.readString();
        year = in.readInt();
    }

    public Boolean isActive() {
        return isActive;
    }

    public void setIsActive(final Boolean value) {
        isActive = value;
    }
    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(final String value) {
        licensePlate = value;
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
    public String getName() {
        return name;
    }

    public void setName(final String value) {
        name = value;
    }
    public String getNotes() {
        return notes;
    }

    public void setNotes(final String value) {
        notes = value;
    }
    public Long getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(final Long value) {
        vehicleId = value;
    }
    public String getVin() {
        return vin;
    }

    public void setVin(final String value) {
        vin = value;
    }
    public Integer getYear() {
        return year;
    }

    public void setYear(final Integer value) {
        year = value;
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
        dest.writeByte((byte) (isActive ? 0x01 : 0x00));
        dest.writeString(licensePlate);
        dest.writeLong(makeId);
        dest.writeString(makeName);
        dest.writeLong(modelId);
        dest.writeString(modelName);
        dest.writeString(name);
        dest.writeString(notes);
        dest.writeLong(vehicleId);
        dest.writeString(vin);
        dest.writeInt(year);
    }

    public static ContentValues buildContentValues(final VehicleDTO vehicle) {
        final ContentValues values = new ContentValues();
        String name;

        name = Columns.ACTIVE.getName();
        if (vehicle.isActive() == null) {
            values.putNull(name);
        }
        else {
            values.put(name, vehicle.isActive() ? 1 : 0);
        }

        name = Columns.LICENSE_PLATE.getName();
        if (TextUtils.isEmpty(vehicle.getLicensePlate())) {
            values.putNull(name);
        }
        else {
            values.put(name, vehicle.getLicensePlate());
        }

        name = Columns.MAKE_ID.getName();
        values.put(name, vehicle.getMakeId());

        name = Columns.MODEL_ID.getName();
        values.put(name, vehicle.getModelId());

        name = Columns.NAME.getName();
        if (TextUtils.isEmpty(vehicle.getName())) {
            values.putNull(name);
        }
        else {
            values.put(name, vehicle.getName());
        }

        name = Columns.NOTES.getName();
        if (TextUtils.isEmpty(vehicle.getNotes())) {
            values.putNull(name);
        }
        else {
            values.put(name, vehicle.getNotes());
        }

        name = Columns.VIN.getName();
        if (TextUtils.isEmpty(vehicle.getVin())) {
            values.putNull(name);
        }
        else {
            values.put(name, vehicle.getVin());
        }

        name = Columns.YEAR.getName();
        if (vehicle.getYear() == null) {
            values.putNull(name);
        }
        else {
            values.put(name, vehicle.getYear());
        }

        return values;
    }

    public static VehicleDTO newInstance(final Cursor cursor) {
        final VehicleDTO vehicle;
        int index;

        if (cursor == null) {
            throw new IllegalStateException("Cursor can not be null.");
        }
        else if (cursor.isBeforeFirst() || cursor.isAfterLast()) {
            if (!cursor.moveToFirst()) {
                throw new IllegalStateException("Cursor can not be empty");
            }
        }

        vehicle = new VehicleDTO();

        index = cursor.getColumnIndex(Columns.ACTIVE.getName());
        if (index != -1) {
            vehicle.setIsActive(cursor.getInt(Columns.ACTIVE.getIndex()) == 1);
        }

        index = cursor.getColumnIndex(Columns.LICENSE_PLATE.getName());
        if (index != -1) {
            vehicle.setLicensePlate(cursor.getString(index));
        }
        index = cursor.getColumnIndex(Columns.MAKE_ID.getName());
        if (index != -1) {
            vehicle.setMakeId(cursor.getLong(index));
        }
        index = cursor.getColumnIndex(Columns.MAKE_NAME.getName());
        if (index != -1) {
            vehicle.setMakeName(cursor.getString(index));
        }
        index = cursor.getColumnIndex(Columns.MODEL_ID.getName());
        if (index != -1) {
            vehicle.setModelId(cursor.getLong(index));
        }
        index = cursor.getColumnIndex(Columns.MODEL_NAME.getName());
        if (index != -1) {
            vehicle.setModelName(cursor.getString(index));
        }
        index = cursor.getColumnIndex(Columns.NAME.getName());
        if (index != -1) {
            vehicle.setName(cursor.getString(index));
        }
        index = cursor.getColumnIndex(Columns.NOTES.getName());
        if (index != -1) {
            vehicle.setNotes(cursor.getString(index));
        }
        index = cursor.getColumnIndex(Columns.VEHICLE_ID.getName());
        if (index != -1) {
            vehicle.setVehicleId(cursor.getLong(index));
        }
        index = cursor.getColumnIndex(Columns.VIN.getName());
        if (index != -1) {
            vehicle.setVin(cursor.getString(index));
        }
        index = cursor.getColumnIndex(Columns.YEAR.getName());
        if (index != -1) {
            vehicle.setYear(cursor.getInt(index));
        }
        return vehicle;
    }
}