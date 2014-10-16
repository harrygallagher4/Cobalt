package com.stlmissouri.cobalt.info;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Stl
 * Date: 4/1/2014
 * Time: 9:58 PM
 * Use:
 */
public class ParentInfoNode implements InfoNode {

    private final List<InfoNode> children;
    private String title;

    public ParentInfoNode(String title) {
        this();
        this.setTitle(title);
    }

    public ParentInfoNode() {
        this.children = new ArrayList<>();
    }

    @Override
    public String getInfoString() {
        return this.title;
    }

    @Override
    public boolean hasChildren() {
        return true;
    }

    public void addChild(InfoNode child) {
        this.children.add(child);
    }

    public void removeChild(int index) {
        this.children.remove(index);
    }

    public void removeChild(InfoNode child) {
        this.removeChild(this.children.indexOf(child));
    }

    public InfoNode[] getChildren() {
        return this.children.toArray(new InfoNode[this.children.size()]);
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
