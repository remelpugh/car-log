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
import com.dabay6.android.apps.carlog.data.provider.CarLogContract.FuelHistory.Columns;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@SuppressWarnings("unused")
public class FuelHistoryDTO implements Parcelable {
    public static final Parcelable.Creator<FuelHistoryDTO> CREATOR = new Parcelable.Creator<FuelHistoryDTO>() {
        public FuelHistoryDTO createFromParcel(final Parcel in) {
            return new FuelHistoryDTO(in);
        }

        public FuelHistoryDTO[] newArray(final int size) {
            return new FuelHistoryDTO[size];
        }
    };
    private Float costPerUnit;
    private Float fuelAmount;
    private Long historyId;
    private Float latitude;
    private String location;
    private Float longitude;
    private String name;
    private String notes;
    private Float odometerReading;
    private Long purchaseDate;
    private Float totalCost;
    private Long vehicleId;

    public FuelHistoryDTO() {
    }

    public FuelHistoryDTO(final Parcel in) {
        costPerUnit = in.readFloat();
        fuelAmount = in.readFloat();
        historyId = in.readLong();
        latitude = in.readFloat();
        location = in.readString();
        longitude = in.readFloat();
        name = in.readString();
        notes = in.readString();
        odometerReading = in.readFloat();
        purchaseDate = in.readLong();
        totalCost = in.readFloat();
        vehicleId = in.readLong();
    }

    public Float getCostPerUnit() {
        return costPerUnit;
    }

    public void setCostPerUnit(final Float value) {
        costPerUnit = value;
    }
    public Float getFuelAmount() {
        return fuelAmount;
    }

    public void setFuelAmount(final Float value) {
        fuelAmount = value;
    }
    public Long getHistoryId() {
        return historyId;
    }

    public void setHistoryId(final Long value) {
        historyId = value;
    }
    public Float getLatitude() {
        return latitude;
    }

    public void setLatitude(final Float value) {
        latitude = value;
    }
    public String getLocation() {
        return location;
    }

    public void setLocation(final String value) {
        location = value;
    }
    public Float getLongitude() {
        return longitude;
    }

    public void setLongitude(final Float value) {
        longitude = value;
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
    public Float getOdometerReading() {
        return odometerReading;
    }

    public void setOdometerReading(final Float value) {
        odometerReading = value;
    }
    public Long getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(final Long value) {
        purchaseDate = value;
    }
    public Float getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(final Float value) {
        totalCost = value;
    }
    public Long getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(final Long value) {
        vehicleId = value;
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
        dest.writeFloat(costPerUnit);
        dest.writeFloat(fuelAmount);
        dest.writeLong(historyId);
        dest.writeFloat(latitude);
        dest.writeString(location);
        dest.writeFloat(longitude);
        dest.writeString(name);
        dest.writeString(notes);
        dest.writeFloat(odometerReading);
        dest.writeLong(purchaseDate);
        dest.writeFloat(totalCost);
        dest.writeLong(vehicleId);
    }

    public static ContentValues buildContentValues(final FuelHistoryDTO fuelhistory) {
        final ContentValues values = new ContentValues();
        String name;

        name = Columns.COST_PER_UNIT.getName();
        if (fuelhistory.getCostPerUnit() == null) {
            values.putNull(name);
        }
        else {
            values.put(name, fuelhistory.getCostPerUnit());
        }

        name = Columns.FUEL_AMOUNT.getName();
        values.put(name, fuelhistory.getFuelAmount());

        name = Columns.LATITUDE.getName();
        if (fuelhistory.getLatitude() == null) {
            values.putNull(name);
        }
        else {
            values.put(name, fuelhistory.getLatitude());
        }

        name = Columns.LOCATION.getName();
        if (TextUtils.isEmpty(fuelhistory.getLocation())) {
            values.putNull(name);
        }
        else {
            values.put(name, fuelhistory.getLocation());
        }

        name = Columns.LONGITUDE.getName();
        if (fuelhistory.getLongitude() == null) {
            values.putNull(name);
        }
        else {
            values.put(name, fuelhistory.getLongitude());
        }

        name = Columns.NOTES.getName();
        if (TextUtils.isEmpty(fuelhistory.getNotes())) {
            values.putNull(name);
        }
        else {
            values.put(name, fuelhistory.getNotes());
        }

        name = Columns.ODOMETER_READING.getName();
        values.put(name, fuelhistory.getOdometerReading());

        name = Columns.PURCHASE_DATE.getName();
        if (fuelhistory.getPurchaseDate() == null) {
            values.putNull(name);
        }
        else {
            values.put(name, fuelhistory.getPurchaseDate());
        }

        name = Columns.TOTAL_COST.getName();
        if (fuelhistory.getTotalCost() == null) {
            values.putNull(name);
        }
        else {
            values.put(name, fuelhistory.getTotalCost());
        }

        name = Columns.VEHICLE_ID.getName();
        values.put(name, fuelhistory.getVehicleId());

        return values;
    }

    public static FuelHistoryDTO newInstance(final Cursor cursor) {
        final FuelHistoryDTO fuelhistory;
        int index;

        if (cursor == null) {
            throw new IllegalStateException("Cursor can not be null.");
        }
        else if (cursor.isBeforeFirst() || cursor.isAfterLast()) {
            if (!cursor.moveToFirst()) {
                throw new IllegalStateException("Cursor can not be empty");
            }
        }

        fuelhistory = new FuelHistoryDTO();

        index = cursor.getColumnIndex(Columns.COST_PER_UNIT.getName());
        if (index != -1) {
            fuelhistory.setCostPerUnit(cursor.getFloat(index));
        }
        index = cursor.getColumnIndex(Columns.FUEL_AMOUNT.getName());
        if (index != -1) {
            fuelhistory.setFuelAmount(cursor.getFloat(index));
        }
        index = cursor.getColumnIndex(Columns.HISTORY_ID.getName());
        if (index != -1) {
            fuelhistory.setHistoryId(cursor.getLong(index));
        }
        index = cursor.getColumnIndex(Columns.LATITUDE.getName());
        if (index != -1) {
            fuelhistory.setLatitude(cursor.getFloat(index));
        }
        index = cursor.getColumnIndex(Columns.LOCATION.getName());
        if (index != -1) {
            fuelhistory.setLocation(cursor.getString(index));
        }
        index = cursor.getColumnIndex(Columns.LONGITUDE.getName());
        if (index != -1) {
            fuelhistory.setLongitude(cursor.getFloat(index));
        }
        index = cursor.getColumnIndex(Columns.NAME.getName());
        if (index != -1) {
            fuelhistory.setName(cursor.getString(index));
        }
        index = cursor.getColumnIndex(Columns.NOTES.getName());
        if (index != -1) {
            fuelhistory.setNotes(cursor.getString(index));
        }
        index = cursor.getColumnIndex(Columns.ODOMETER_READING.getName());
        if (index != -1) {
            fuelhistory.setOdometerReading(cursor.getFloat(index));
        }
        index = cursor.getColumnIndex(Columns.PURCHASE_DATE.getName());
        if (index != -1) {
            fuelhistory.setPurchaseDate(cursor.getLong(index));
        }
        index = cursor.getColumnIndex(Columns.TOTAL_COST.getName());
        if (index != -1) {
            fuelhistory.setTotalCost(cursor.getFloat(index));
        }
        index = cursor.getColumnIndex(Columns.VEHICLE_ID.getName());
        if (index != -1) {
            fuelhistory.setVehicleId(cursor.getLong(index));
        }
        return fuelhistory;
    }
}