package com.stlmissouri.cobalt.gui;

import com.stlmissouri.cobalt.Cobalt;
import com.stlmissouri.cobalt.render.CobaltRenderManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;

/**
 * User: Stl
 * Date: 2/1/14
 * Time: 10:22 PM
 * Use:  Override for GuiIngame class
 */
public class CobaltGuiIngame extends GuiIngame {

    private final Cobalt cobalt;
    private final Minecraft mc;

    public CobaltGuiIngame(Minecraft par1Minecraft, Cobalt cobalt) {
        super(par1Minecraft);
        this.cobalt = cobalt;
        this.mc = Minecraft.getMinecraft();
    }

    @Override
    public void renderGameOverlay(float par1, boolean par2, int par3, int par4) {
        super.renderGameOverlay(par1, par2, par3, par4);
        if (!mc.gameSettings.showDebugInfo)
            this.cobalt.renderManager.render(CobaltRenderManager.RendererLocation.INGAME);
    }
}