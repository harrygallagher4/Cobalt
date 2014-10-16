package com.stlmissouri.cobalt.gui.form.components;

import java.util.HashMap;
import java.util.Map;

/**
 * User: Stl
 * Date: 2/5/14
 * Time: 5:57 PM
 * Use:  Enum for FormComponent types
 */
public enum FormComponentType {

    TEXT, BOOLEAN, SUBMIT, LABEL, NUMBER;

    public static FormComponentType getDefaultTypeForObject(Class c) {
        if (!(classFormComponentTypeMap.containsKey(c)))
            return LABEL;
        return classFormComponentTypeMap.get(c);
    }

    private static Map<Class, FormComponentType> classFormComponentTypeMap;

    static {
        classFormComponentTypeMap = new HashMap<>();
        classFormComponentTypeMap.put(String.class, TEXT);
        classFormComponentTypeMap.put(Boolean.class, BOOLEAN);
        classFormComponentTypeMap.put(Float.class, NUMBER);
        classFormComponentTypeMap.put(Double.class, NUMBER);
        classFormComponentTypeMap.put(Integer.class, NUMBER);
    }

}
