package com.stlmissouri.cobalt.plugins.tabgui.gui;

/**
 * User: Stl
 * Date: 4/29/2014
 * Time: 6:19 PM
 * Use:
 */
public class TabActionNode extends BasicTabNode {

    private Runnable action = null;

    public TabActionNode(String name) {
        super(name);
    }

    @Override
    public void select() {
        if (action != null)
            action.run();
    }

    public void setAction(Runnable action) {
        this.action = action;
    }

    public void removeAction() {
        this.action = null;
    }

}
