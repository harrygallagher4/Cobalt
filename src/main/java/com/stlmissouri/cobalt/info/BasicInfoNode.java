package com.stlmissouri.cobalt.info;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * User: Stl
 * Date: 4/1/2014
 * Time: 9:58 PM
 * Use:
 */
public class BasicInfoNode implements InfoNode {

    private String infoString;
    private final List<Object> variables;

    public BasicInfoNode(Object... vars) {
        this("", vars);
    }

    public BasicInfoNode(String infoString, Object... vars) {
        this.infoString = infoString;
        this.variables = new ArrayList<>();
        Collections.addAll(this.variables, vars);
    }

    public String getInfoString() {
        return String.format(this.infoString, this.variables.toArray());
    }

    public String getRawString() {
        return this.infoString;
    }

    public void setInfoString(String infoString) {
        this.infoString = infoString;
    }

    public boolean hasChildren() {
        return false;
    }

}
