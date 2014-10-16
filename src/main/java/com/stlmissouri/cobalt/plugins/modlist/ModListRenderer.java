package com.stlmissouri.cobalt.plugins.modlist;

import com.darkmagician6.eventapi.EventManager;
import com.darkmagician6.eventapi.EventTarget;
import com.stlmissouri.cobalt.Cobalt;
import com.stlmissouri.cobalt.events.mod.TickEvent;
import com.stlmissouri.cobalt.module.CobaltModule;
import com.stlmissouri.cobalt.plugins.modlist.properties.ModListProperties;
import com.stlmissouri.cobalt.render.Renderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * User: Stl
 * Date: 6/13/2014
 * Time: 5:38 PM
 * Use:
 */
public class ModListRenderer implements Renderer {

    private Cobalt cobalt;
    private Minecraft mc;

    private ScaledResolution sr;

    private int lastWidth, lastHeight, lastScale;

    private Map<CobaltModule, ModListProperties> moduleProperties;

    public ModListRenderer(Cobalt cobalt) {
        this.cobalt = cobalt;
        this.mc = cobalt.mc;
        this.moduleProperties = new HashMap<>();
        this.sr = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
        this.lastWidth = mc.displayWidth;
        this.lastHeight = mc.displayHeight;
        this.lastScale = mc.gameSettings.guiScale;
        EventManager.register(this);
    }

    @Override
    public void render() {
        Collection<CobaltModule> modules = cobalt.moduleManager.getAllModules();
        int yPos = 2;
        for (CobaltModule module : modules) {
            if (!module.isEnabled())
                continue;
            String name = module.getName();
            ModListProperties properties = getProperties(module);
            if (!properties.isVisible())
                continue;
            if (properties.getTag() != null)
                name = properties.getTag();
            mc.fontRenderer.drawStringWithShadow(name, sr.getScaledWidth() - mc.fontRenderer.getStringWidth(name) - 2, yPos, properties.getColor());
            yPos += 10;
        }
    }

    @EventTarget
    public void onTick(TickEvent event) {
        if (lastWidth == mc.displayWidth && lastHeight == mc.displayHeight && lastScale == mc.gameSettings.guiScale)
            return;
        this.sr = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
        this.lastWidth = mc.displayWidth;
        this.lastHeight = mc.displayHeight;
        this.lastScale = mc.gameSettings.guiScale;
    }

    public ModListProperties getProperties(CobaltModule module) {
        if (!this.moduleProperties.containsKey(module)) {
            ModListProperties properties = new ModListProperties();
            this.moduleProperties.put(module, properties);
            return properties;
        }
        return this.moduleProperties.get(module);
    }

    public void setModuleProperties(CobaltModule module, ModListProperties properties) {
        this.moduleProperties.put(module, properties);
    }

}
