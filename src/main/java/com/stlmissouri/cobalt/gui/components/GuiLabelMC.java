package com.stlmissouri.cobalt.gui.components;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

/**
 * User: Stl
 * Date: 2/5/14
 * Time: 11:19 PM
 * Use:  Label class for Minecraft GUIs because I don't understand theirs
 */
public class GuiLabelMC extends MCComponent {

    private String displayText;

    private int color = 0xFFFFFFFF;

    public GuiLabelMC(Minecraft mc, GuiScreen parent, String displayText) {
        super(mc, parent);
        this.displayText = displayText;
    }

    @Override
    public void draw() {
        this.mc.fontRenderer.drawString(this.displayText, this.posX, this.posY, this.color);
    }

    public void setColor(int color) {
        this.color = color;
    }

}
