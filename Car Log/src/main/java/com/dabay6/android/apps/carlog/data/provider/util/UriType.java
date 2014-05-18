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

/**
 * UriType
 *
 * @author Remel Pugh
 * @version 1.0
 */
@SuppressWarnings("unused")
public class UriType {
    private final int code;
    private final String idColumnName;
    private final String matchPath;
    private final String tableName;
    private final String type;

    public UriType(final int code, final String matchPath, final String tableName, final String type,
                   final String idColumnName) {
        this.code = code;
        this.idColumnName = idColumnName;
        this.matchPath = matchPath;
        this.tableName = tableName;
        this.type = type;
    }

    /**
     *
     * @return
     */
    public int getCode() {
        return code;
    }

    /**
     *
     * @return
     */
    public String getIdColumnName() {
        return idColumnName;
    }

    /**
     *
     * @return
     */
    public String getMatchPath() {
        return matchPath;
    }

    /**
     *
     * @return
     */
    public String getTableName() {
        return tableName;
    }

    /**
     *
     * @return
     */
    public String getType() {
        return type;
    }
}