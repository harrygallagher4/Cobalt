package com.stlmissouri.cobalt.plugins.tabgui;

import com.stlmissouri.cobalt.plugins.tabgui.gui.TabNode;
import com.stlmissouri.cobalt.plugins.tabgui.gui.TabParentNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: Stl
 * Date: 4/29/2014
 * Time: 6:16 PM
 * Use:
 */
public class TabGui {

    private int depth = 0;

    private Map<Integer, Integer> levelSelections;
    private Map<Integer, TabNode[]> levels;

    private TabNode[] activeSet;
    private int activeIndex = 0;

    private List<TabNode> components;

    public TabGui() {
        this.components = new ArrayList<>();
        this.levelSelections = new HashMap<>();
        this.levels = new HashMap<>();
        this.levelSelections.put(depth, activeIndex);
    }

    public void reset() {
        this.components.clear();
        this.levelSelections.clear();
        this.levels.clear();
        this.activeIndex = 0;
        this.depth = 0;
        this.levelSelections.put(this.depth, this.activeIndex);
    }

    public TabNode[] getComponents() {
        return this.components.toArray(new TabNode[this.components.size()]);
    }

    public void addComponent(TabNode node) {
        this.components.add(node);
        this.levels.put(0, this.components.toArray(new TabNode[this.components.size()]));
        this.activeSet = this.levels.get(0);
        this.depth = 0;
    }

    public void removeComponent(TabNode node) {
        this.components.remove(node);
        this.levels.put(0, this.components.toArray(new TabNode[this.components.size()]));
        this.activeSet = this.levels.get(0);
        this.depth = 0;
    }

    public void up() {
        this.activeIndex--;
        if (this.activeIndex < 0)
            this.activeIndex = this.activeSet.length - 1;
        this.activeIndex %= this.activeSet.length;
        this.updateSelection();
    }

    public void down() {
        this.activeIndex++;
        this.activeIndex %= this.activeSet.length;
        this.updateSelection();
    }

    public void downLevel() {
        TabNode selected = this.activeSet[this.activeIndex];
        if (selected instanceof TabParentNode) {
            this.levels.put(depth, this.activeSet);
            this.levelSelections.put(depth, this.activeIndex);
            this.depth++;
            this.activeIndex = 0;
            this.activeSet = ((TabParentNode) selected).getChildren();
        } else {
            selected.select();
        }
        this.updateSelection();
    }

    public void upLevel() {
        if (this.depth == 0)
            return;
        this.levelSelections.put(this.depth, this.activeIndex);
        this.depth--;
        this.activeIndex = this.levelSelections.get(depth);
        this.activeSet = this.levels.get(depth);
        this.updateSelection();
    }

    public boolean isSelected(int depth, int index) {
        if (!this.levelSelections.containsKey(depth))
            return false;
        return this.levelSelections.get(depth) == index;
    }

    private void updateSelection() {
        this.levelSelections.put(depth, activeIndex);
    }

    public int getDepth() {
        return depth;
    }

}
