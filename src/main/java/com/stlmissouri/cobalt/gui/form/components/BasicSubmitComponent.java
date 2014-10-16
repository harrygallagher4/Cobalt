package com.stlmissouri.cobalt.gui.form.components;

import com.stlmissouri.cobalt.gui.form.Form;
import com.stlmissouri.cobalt.gui.form.FormSubmitAction;

/**
 * User: Stl
 * Date: 2/5/14
 * Time: 6:57 PM
 * Use:  Basic submit component (a button)
 */
public class BasicSubmitComponent extends FormComponent<String> {

    private FormSubmitAction action;

    public BasicSubmitComponent(Form parentForm, FormSubmitAction action) {
        super(parentForm);
        this.action = action;
    }

    /**
     * @return submit return value
     */
    @Override
    public String getValue() {
        return this.action.run(this.parentForm);
    }

    //We won't be using this
    @Override
    public void setValue(String value) {
    }

    @Override
    public FormComponentType getType() {
        return FormComponentType.SUBMIT;
    }

}
