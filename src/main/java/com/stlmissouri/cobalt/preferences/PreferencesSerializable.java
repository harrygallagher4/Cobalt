package com.stlmissouri.cobalt.preferences;

import java.lang.reflect.Type;

/**
 * User: Stl
 * Date: 7/13/2014
 * Time: 5:21 AM
 * Use:  Allows an object to save and load objects of any type
 */
public interface PreferencesSerializable<T> {

    public T getSaveObject();

    public void load(T savedObject);

    public Type getType();

}
