package com.stlmissouri.cobalt.module.mods;

import com.stlmissouri.cobalt.Cobalt;
import com.stlmissouri.cobalt.module.CobaltModule;
import com.stlmissouri.cobalt.render.CobaltRenderManager;
import com.stlmissouri.cobalt.render.Renderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;


public class TextRadarMod extends CobaltModule {

    public TextRadarMod(Cobalt cobalt) {
        super(cobalt, "TextRadar");
    }

    public void onEnable() {
        super.onEnable();
        cobalt.renderManager.registerRenderer(CobaltRenderManager.RendererLocation.INGAME, "cobalt.renderer.textradar", new TextRadarRenderer());
    }

    public void onDisable() {
        super.onDisable();
        cobalt.renderManager.unregisterRenderer(CobaltRenderManager.RendererLocation.INGAME, "cobalt.renderer.textradar");
    }

    public class TextRadarRenderer implements Renderer {

        public void render() {
            int yPos = -8;
            for(CobaltModule module: cobalt.moduleManager.getAllModules()) {
                if(!module.isEnabled()) continue;
                yPos+=10;
            }

            for(Object ent: mc.theWorld.playerEntities) {
                if(ent instanceof EntityPlayer) {
                    EntityPlayer player = (EntityPlayer) ent;
                    if (player.equals(mc.thePlayer)) continue;
                    String bc = "\2477";
                    String nc = "\2478";
                    String d = "\247";
                    String dc = "a";

                    int dist = (int) mc.thePlayer.getDistanceToEntity(player);

                    if(dist <= 16) {
                        dc = "c";
                    }else
                    if(dist <= 24) {
                        dc = "6";
                    }

                    String text = String.format(bc + "[" + d + dc + "%s" + bc + "]" + nc + " %s", dist, player.getGameProfile().getName());
                    int x = getScaledResolution().getScaledWidth() - mc.fontRenderer.getStringWidth(text) - 2;
                    int y = yPos += 12;

                    mc.fontRenderer.drawStringWithShadow(text, x, y, (int) 0xffffff);
                }
            }
        }

        public ScaledResolution getScaledResolution() {
            return new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
        }
    }
}
