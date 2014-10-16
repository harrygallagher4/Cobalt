package com.stlmissouri.cobalt.gui.components;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;

/**
 * User: Stl
 * Date: 2/5/14
 * Time: 11:25 PM
 * Use:  Base class for additional Minecraft components
 */
public abstract class MCComponent extends Gui {

    protected int posX, posY;

    protected Minecraft mc;
    protected GuiScreen parent;

    public MCComponent(Minecraft mc, GuiScreen parent) {
        this.mc = mc;
        this.parent = parent;
        this.posX = 0;
        this.posY = 0;
    }

    public void keyTyped(char c, int i) {

    }

    public void onUpdate() {

    }

    public void setPosition(int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
    }

    public abstract void draw();

}
