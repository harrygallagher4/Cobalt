package com.stlmissouri.cobalt.plugins.groovy;

import com.darkmagician6.eventapi.EventManager;
import com.darkmagician6.eventapi.EventTarget;
import com.stlmissouri.cobalt.Cobalt;
import com.stlmissouri.cobalt.api.CobaltPlugin;
import com.stlmissouri.cobalt.events.mod.ChatSendEvent;
import com.stlmissouri.cobalt.keybinds.BasicKeybind;
import net.minecraft.client.gui.GuiChat;
import org.apache.commons.io.FileUtils;
import org.lwjgl.input.Keyboard;

import javax.script.ScriptException;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

/**
 * User: Stl
 * Date: 5/4/2014
 * Time: 4:02 AM
 * Use:  Adds support for Groovy scripting
 */
public class CobaltIsGroovy extends CobaltPlugin {

    private Cobalt cobalt;

    public CobaltIsGroovy(Cobalt cobalt) {
        super(cobalt);
    }

    @Override
    public void load(final Cobalt cobalt) {
        this.cobalt = cobalt;
        File scriptsDir = new File(cobalt.ioManager.COBALT_DIR, "scripts");
        if(!scriptsDir.exists())
            scriptsDir.mkdir();
        this.loadScripts(scriptsDir);
        GroovyLoader.registerGlobalBinding("cobalt", cobalt);
        GroovyLoader.registerGlobalBinding("mc", cobalt.mc);
        cobalt.commandManager.registerCommand("groovy", new GroovyCommand(cobalt));
        cobalt.commandManager.registerAlias("groovy", ">");
        cobalt.commandManager.registerCommand("groovyscript", new ScriptCommand(cobalt));
        cobalt.commandManager.registerAlias("groovyscript", "gs");
        cobalt.commandManager.registerAlias("groovyscript", "s");
        cobalt.keybindManager.registerKeybind("script.run", new BasicKeybind(Keyboard.KEY_Y) {
            @Override
            public void fire() {
                cobalt.mc.displayGuiScreen(new GuiChat(">>"));
            }
        });
        EventManager.register(this, ChatSendEvent.class);
    }

    @Override
    public void unload() {
        EventManager.unregister(this);
    }

    @Override
    public String getName() {
        return "Groovy";
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public double getPluginVersion() {
        return 1;
    }

    private void loadScripts(File dir) {
        System.out.println("Cobalt: loading groovy scripts: " + dir.getAbsolutePath());
        Iterator<File> fileIterator = FileUtils.iterateFiles(dir, new String[]{"groovy"}, false);
        File f;
        while (fileIterator.hasNext()) {
            try {
                f = fileIterator.next();
                System.out.println("-\tLoading: " + f.getName());
                GroovyLoader.load(f);
            } catch (ScriptException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    @EventTarget
    public void onChat(ChatSendEvent event) {
        String msg = event.getMessage();
        if(!msg.startsWith(">>"))
            return;
        event.setCancelled(true);
        String sub = msg.substring(2);
        cobalt.commandManager.runCommand(".groovy " + sub);
    }

}
