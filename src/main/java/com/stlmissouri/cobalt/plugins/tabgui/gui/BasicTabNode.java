package com.stlmissouri.cobalt.plugins.tabgui.gui;

/**
 * User: Stl
 * Date: 4/29/2014
 * Time: 6:19 PM
 * Use:
 */
public abstract class BasicTabNode implements TabNode {

    private String name;

    protected BasicTabNode(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
