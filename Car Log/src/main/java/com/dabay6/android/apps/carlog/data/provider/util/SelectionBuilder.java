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

package com.dabay6.android.apps.carlog.data.provider.util;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import java.util.*;

/**
 * SelectionBuilder
 */
@SuppressWarnings("unused")
public class SelectionBuilder {
    private final Map<String, String> projectionMap = new HashMap<String, String>();
    private final StringBuilder selection = new StringBuilder();
    private final ArrayList<String> selectionArgs = new ArrayList<String>();
    private String table = null;

    /**
     * Execute delete using the current internal state as {@code WHERE} clause.
     */
    public int delete(final SQLiteDatabase db) {
        assertTable();

        return db.delete(table, getSelection(), getSelectionArgs());
    }

    /**
     * Return selection string for current internal state.
     *
     * @see #getSelectionArgs()
     */
    public String getSelection() {
        return selection.toString();
    }

    /**
     * Return selection arguments for current internal state.
     *
     * @see #getSelection()
     */
    public String[] getSelectionArgs() {
        return selectionArgs.toArray(new String[selectionArgs.size()]);
    }

    public SelectionBuilder map(final String fromColumn, final String toClause) {
        projectionMap.put(fromColumn, toClause + " AS " + fromColumn);

        return this;
    }

    public SelectionBuilder mapToTable(final String column, final String table) {
        projectionMap.put(column, table + "." + column);

        return this;
    }

    /**
     * Execute query using the current internal state as {@code WHERE} clause.
     */
    public Cursor query(final SQLiteDatabase db, final String[] columns, final String orderBy) {
        return query(db, columns, null, null, orderBy, null);
    }

    /**
     * Execute query using the current internal state as {@code WHERE} clause.
     */
    public Cursor query(final SQLiteDatabase db, final String[] columns, final String groupBy, final String having,
                        final String orderBy, final String limit) {
        assertTable();
        if (columns != null) {
            mapColumns(columns);
        }

        return db.query(table, columns, getSelection(), getSelectionArgs(), groupBy, having, orderBy, limit);
    }

    /**
     * Reset any internal state, allowing this builder to be recycled.
     */
    public SelectionBuilder reset() {
        table = null;
        selection.setLength(0);
        selectionArgs.clear();

        return this;
    }

    public SelectionBuilder table(final String table) {
        this.table = table;

        return this;
    }

    @Override
    public String toString() {
        return "SelectionBuilder[table=" + table + ", selection=" + getSelection() + ", " +
                "selectionArgs=" + Arrays.toString(getSelectionArgs()) + "]";
    }

    /**
     * Execute update using the current internal state as {@code WHERE} clause.
     */
    public int update(final SQLiteDatabase db, final ContentValues values) {
        assertTable();

        return db.update(table, values, getSelection(), getSelectionArgs());
    }

    /**
     * Append the given selection clause to the internal state. Each clause is
     * surrounded with parenthesis and combined using {@code AND}.
     */
    public SelectionBuilder where(final String selection, final String... selectionArgs) {
        if (TextUtils.isEmpty(selection)) {
            if (selectionArgs != null && selectionArgs.length > 0) {
                throw new IllegalArgumentException("Valid selection required when including arguments=");
            }

            return this;
        }

        if (this.selection.length() > 0) {
            this.selection.append(" AND ");
        }

        this.selection.append("(").append(selection).append(")");

        if (selectionArgs != null) {
            Collections.addAll(this.selectionArgs, selectionArgs);
        }

        return this;
    }

    private void assertTable() {
        if (table == null) {
            throw new IllegalStateException("Table not specified");
        }
    }

    private void mapColumns(final String[] columns) {
        for (int i = 0; i < columns.length; i++) {
            final String target = projectionMap.get(columns[i]);

            if (target != null) {
                columns[i] = target;
            }
        }
    }
}