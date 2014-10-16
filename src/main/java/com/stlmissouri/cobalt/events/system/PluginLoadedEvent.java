package com.stlmissouri.cobalt.events.system;

import com.darkmagician6.eventapi.events.Event;
import com.stlmissouri.cobalt.api.CobaltPlugin;

/**
 * User: Stl
 * Date: 7/8/2014
 * Time: 6:47 PM
 * Use:
 */
public class PluginLoadedEvent implements Event {

    private final CobaltPlugin plugin;

    public PluginLoadedEvent(CobaltPlugin plugin) {
        this.plugin = plugin;
    }

    public CobaltPlugin getPlugin() {
        return this.plugin;
    }

    public Class getPluginClass() {
        return this.plugin.getClass();
    }

}
