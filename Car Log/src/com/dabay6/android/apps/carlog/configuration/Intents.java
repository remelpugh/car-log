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

package com.dabay6.android.apps.carlog.configuration;

/**
 * Intents
 *
 * @author Remel Pugh
 * @version 1.0
 */
public final class Intents {
    /**
     *
     */
    public static final String INTENT_EXTRA_PARENT = Intents.INTENT_EXTRA_PREFIX + "parent";
    /**
     * Prefix for all extra data added to intents
     */
    public static final String INTENT_EXTRA_PREFIX = Intents.INTENT_PREFIX + "extra.";
    /**
     *
     */
    public static final String INTENT_INIT_EXTRA_ERROR = Intents.INTENT_EXTRA_PREFIX + "init.error";
    /**
     *
     */
    public static final String INTENT_INIT_FINISHED = Intents.INTENT_PREFIX + "init.finished";
    /**
     * Prefix for all intents created
     */
    public static final String INTENT_PREFIX = "com.dabay6.android.apps.carlog.";
}