package com.stlmissouri.cobalt.plugins.modlist.properties;

/**
 * User: Stl
 * Date: 6/13/2014
 * Time: 5:45 PM
 * Use:  Stores properties for displaying a module in the module list
 */
public class ModListProperties {

    private boolean visible = true;
    private int color = 0xFFFFFF;
    private String tag = null;

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
