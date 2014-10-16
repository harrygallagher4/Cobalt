package com.stlmissouri.cobalt.plugins.chatfilter;

/**
 * User: Stl
 * Date: 3/3/14
 * Time: 5:28 PM
 * Use:
 */
public enum ChatFilterType {

    EQUALS(false),
    MATCHES(true),
    CONTAINS(true);

    public final boolean requiresRegex;

    private ChatFilterType(boolean requiresRegex) {
        this.requiresRegex = requiresRegex;
    }

}
