package com.stlmissouri.cobalt.plugins.tabgui.gui;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Stl
 * Date: 4/29/2014
 * Time: 6:18 PM
 * Use:
 */
public class TabParentNode extends BasicTabNode {

    private List<TabNode> children;

    public TabParentNode(String name) {
        super(name);
        this.children = new ArrayList<>();
    }

    @Override
    public void select() {
    }

    public TabNode[] getChildren() {
        return this.children.toArray(new TabNode[this.children.size()]);
    }

    public void addChild(TabNode node) {
        this.children.add(node);
    }

}
