package com.stlmissouri.cobalt.gui;

import com.stlmissouri.cobalt.gui.components.GuiLabelMC;
import com.stlmissouri.cobalt.gui.components.MCComponent;
import com.stlmissouri.cobalt.gui.form.Form;
import com.stlmissouri.cobalt.gui.form.components.FormComponent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

import java.util.HashMap;
import java.util.Map;

/**
 * User: Stl
 * Date: 2/5/14
 * Time: 7:55 PM
 * Use:  Renders the form as a GuiScreen
 */
public class GuiForm extends GuiScreen {

    private final Form parentForm;

    private Map<FormComponent<?>, MCComponent> components;

    private Minecraft minecraft;

    public GuiForm(Minecraft minecraft, Form form) {
        this.parentForm = form;
        this.components = new HashMap<>();
        this.minecraft = minecraft;
    }

    public void drawScreen(int par1, int par2, float par3) {

    }

    private void addComponents() {
        MCComponent mcComponent = null;
        for (FormComponent component : parentForm.getComponents().values()) {
            switch (component.getType()) {
                case LABEL: {
                    mcComponent = new GuiLabelMC(minecraft, this, (String) component.getValue());
                }
            }
        }
    }
}