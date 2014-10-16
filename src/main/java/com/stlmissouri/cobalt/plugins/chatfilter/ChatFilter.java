package com.stlmissouri.cobalt.plugins.chatfilter;

import com.darkmagician6.eventapi.EventManager;
import com.stlmissouri.cobalt.Cobalt;
import com.stlmissouri.cobalt.api.CobaltPlugin;

import java.io.File;
import java.io.IOException;

/**
 * User: Stl
 * Date: 3/3/14
 * Time: 5:02 PM
 * Use:
 */
public class ChatFilter extends CobaltPlugin {

    private ChatFilterListener listener;
    private File configFile;

    public ChatFilter(Cobalt cobalt) {
        super(cobalt);
    }

    public void load(Cobalt cobalt) {
        this.configFile = new File(cobalt.ioManager.COBALT_DIR, "filters.json");
        if(!this.configFile.exists())
            try {
                this.configFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        this.listener = new ChatFilterListener(cobalt);
        EventManager.register(this.listener);
        this.listener.loadConfig(this.configFile);
    }

    /**
     * Called when the plugin is unloaded, most likely by a client shutdown
     */
    @Override
    public void unload() {
        EventManager.unregister(this.listener);
        this.listener.saveConfig(this.configFile);
    }

    @Override
    public String getName() {
        return "Chat Filter";
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
