package com.stlmissouri.cobalt.gui.form.components;

import com.stlmissouri.cobalt.gui.form.Form;

/**
 * User: Stl
 * Date: 2/5/14
 * Time: 6:49 PM
 * Use:  Basic boolean component
 */
public class BasicBooleanComponent extends FormComponent<Boolean> {

    private boolean value;

    public BasicBooleanComponent(Form parentForm, boolean defaultValue) {
        super(parentForm);
        this.value = defaultValue;
    }

}
