package com.stlmissouri.cobalt.gui.form;

/**
 * User: Stl
 * Date: 2/5/14
 * Time: 7:18 PM
 * Use:  Action which is run when a form is submitted
 */
public interface FormSubmitAction {

    /**
     * Runs the form submit action and returns accordingly
     *
     * @param parentForm The form that submitted this
     * @return return "code" as a string
     */
    public String run(Form parentForm);

}
