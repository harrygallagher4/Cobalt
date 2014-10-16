package com.stlmissouri.cobalt.info;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * User: Stl
 * Date: 4/5/2014
 * Time: 1:40 AM
 * Use:
 */
public class InfoSet {

    private final Map<String, InfoNode> components;

    public InfoSet() {
        this.components = new LinkedHashMap<>();
    }

    public void addNode(String key, InfoNode component) {
        this.components.put(key, component);
    }

    public void removeNode(String key) {
        this.components.remove(key);
    }

    public Map<String, InfoNode> getNodes() {
        return this.components;
    }

    public Collection<InfoNode> getNodeSet() {
        return this.components.values();
    }

}
