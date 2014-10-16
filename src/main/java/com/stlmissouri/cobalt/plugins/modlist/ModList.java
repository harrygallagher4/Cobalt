package com.stlmissouri.cobalt.plugins.modlist;

import com.darkmagician6.eventapi.EventManager;
import com.stlmissouri.cobalt.Cobalt;
import com.stlmissouri.cobalt.api.CobaltPlugin;
import com.stlmissouri.cobalt.command.Command;
import com.stlmissouri.cobalt.render.CobaltRenderManager;
import net.minecraft.client.Minecraft;

/**
 * User: Stl
 * Date: 3/1/14
 * Time: 10:15 PM
 * Use:  Displays modules on the screen
 */
public class ModList extends CobaltPlugin {

    private ModListRenderer listRenderer;
    private Command toggleCommand;
    private Minecraft mc;
    private Cobalt cobalt;

    private static String RENDERER_KEY = "cobalt.renderer.modlist";
    private static String COMMAND_ID = "modlist";

    public ModList(Cobalt cobalt) {
        super(cobalt);
        this.mc = Minecraft.getMinecraft();
    }

    @Override
    public void load(Cobalt cb) {
        this.cobalt = cb;
        EventManager.register(this);
        this.listRenderer = new com.stlmissouri.cobalt.plugins.modlist.ModListRenderer(this.cobalt);
        this.toggleCommand = new Command() {
            @Override
            public void onCommand(String... args) {
                boolean show = !cobalt.renderManager.hasRenderer(CobaltRenderManager.RendererLocation.INGAME, RENDERER_KEY);
                if (args.length > 0) {
                    if (args[0].equalsIgnoreCase("show"))
                        show = true;
                    else if (args[0].equalsIgnoreCase("hide"))
                        show = false;
                    else {
                        cobalt.displayChat("Invalid argument: " + args[0]);
                        return;
                    }
                }
                if (show)
                    cobalt.renderManager.registerRenderer(CobaltRenderManager.RendererLocation.INGAME, RENDERER_KEY, listRenderer);
                else
                    cobalt.renderManager.unregisterRenderer(CobaltRenderManager.RendererLocation.INGAME, RENDERER_KEY);
                return;
            }
        };
        cobalt.renderManager.registerRenderer(CobaltRenderManager.RendererLocation.INGAME, RENDERER_KEY, this.listRenderer);
        cobalt.commandManager.registerCommand(COMMAND_ID, this.toggleCommand);
    }

    public ModListRenderer getRenderer() {
        return this.listRenderer;
    }

    @Override
    public void unload() {
        EventManager.register(this);
        cobalt.renderManager.unregisterRenderer(CobaltRenderManager.RendererLocation.INGAME, RENDERER_KEY);
        cobalt.commandManager.unregisterCommand(COMMAND_ID);
    }

    @Override
    public String getName() {
        return "Mod List";
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public double getPluginVersion() {
        return 1;
    }
}
