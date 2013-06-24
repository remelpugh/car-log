package com.dabay6.android.apps.carlog.data.provider.util;

public interface ColumnMetadata {
    public int getIndex();

    public Boolean isForeign();

    public String getName();

    public String getType();
}