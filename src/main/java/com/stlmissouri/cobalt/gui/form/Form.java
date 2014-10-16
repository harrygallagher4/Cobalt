package com.stlmissouri.cobalt.gui.form;

import com.stlmissouri.cobalt.gui.form.components.FormComponent;

import java.util.HashMap;
import java.util.Map;

/**
 * User: Stl
 * Date: 2/5/14
 * Time: 5:31 PM
 * Use:  HTML-like forms but for Minecraft
 */
public abstract class Form {

    private Map<String, FormComponent<?>> components;

    public Form() {
        this.components = new HashMap<>();
    }

    public void addComponent(String id, FormComponent<?> component) {
        this.components.put(id, component);
    }

    public Map<String, FormComponent<?>> getComponents() {
        return components;
    }

    public FormComponent<?> getComponentByID(String id) {
        if (this.components.containsKey(id))
            return this.components.get(id);
        return null;
    }

    public boolean hasComponent(String id) {
        return this.components.containsKey(id);
    }

    public abstract void submit();
}