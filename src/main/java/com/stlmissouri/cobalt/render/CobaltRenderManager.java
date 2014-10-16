package com.stlmissouri.cobalt.render;

import com.stlmissouri.cobalt.info.InfoSet;
import com.stlmissouri.cobalt.util.CobaltManager;
import com.stlmissouri.cobalt.util.collection.HashMultiKeyMap;
import com.stlmissouri.cobalt.util.collection.MultiKeyMap;
import net.minecraft.client.Minecraft;

/**
 * User: Stl
 * Date: 2/1/14
 * Time: 10:17 PM
 * Use:  Allows for creation of "renderers" so we don't have to use an event for that
 */
public class CobaltRenderManager implements CobaltManager {

    private final Minecraft mc;

    private MultiKeyMap<RendererLocation, String, Renderer> rendererMap;

    public CobaltRenderManager(Minecraft minecraft) {
        this.mc = minecraft;
        this.rendererMap = new HashMultiKeyMap<>();
        for (RendererLocation location : RendererLocation.values()) {
            this.rendererMap.safe(location);
        }
    }

    public void registerRenderer(RendererLocation location, String key, Renderer renderer) {
        this.rendererMap.put(location, key, renderer);
    }

    public void unregisterRenderer(RendererLocation location, String key) {
        if (this.rendererMap.containsKey(location, key))
            this.rendererMap.remove(location, key);
    }

    public boolean hasRenderer(RendererLocation location, String key) {
        return this.rendererMap.containsKey(location, key);
    }

    public void render(RendererLocation location) {
        for (Renderer renderer : rendererMap.values(location)) {
            renderer.render();
        }
    }

    @Override
    public void updateInfo(InfoSet infoSet) { }

    public enum RendererLocation {
        GUI, INGAME
    }

}