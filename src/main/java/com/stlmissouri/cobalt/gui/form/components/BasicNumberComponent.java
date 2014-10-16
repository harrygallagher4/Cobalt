package com.stlmissouri.cobalt.gui.form.components;

import com.stlmissouri.cobalt.gui.form.Form;

/**
 * User: Stl
 * Date: 2/5/14
 * Time: 7:31 PM
 * Use:  Basic number component
 */
public class BasicNumberComponent extends FormComponent<Float> {

    public BasicNumberComponent(Form parentForm, Float defaultValue) {
        super(parentForm);
        this.value = defaultValue;
    }

}
