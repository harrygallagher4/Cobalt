package com.stlmissouri.cobalt.gui.form.components;

import com.stlmissouri.cobalt.gui.form.Form;

/**
 * User: Stl
 * Date: 2/5/14
 * Time: 5:34 PM
 * Use:  Basic form component
 */
public abstract class FormComponent<E> {

    protected final Form parentForm;

    protected E value;

    public FormComponent(Form parentForm) {
        this.parentForm = parentForm;
    }

    public E getValue() {
        return this.value;
    }

    public void setValue(E value) {
        this.value = value;
    }

    public FormComponentType getType() {
        return FormComponentType.getDefaultTypeForObject(value.getClass());
    }
}