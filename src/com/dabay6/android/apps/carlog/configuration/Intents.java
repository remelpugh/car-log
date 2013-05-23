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