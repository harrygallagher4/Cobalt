package com.stlmissouri.cobalt.gui.form.components;

import com.stlmissouri.cobalt.gui.form.Form;

/**
 * User: Stl
 * Date: 2/5/14
 * Time: 5:59 PM
 * Use:  Basic text component
 */
public class BasicTextComponent extends FormComponent<String> {

    private String value;

    public BasicTextComponent(Form parentForm, String defaultValue) {
        super(parentForm);
        this.value = defaultValue;
    }

}
