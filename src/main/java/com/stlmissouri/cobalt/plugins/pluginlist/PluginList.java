package com.stlmissouri.cobalt.plugins.pluginlist;

import com.darkmagician6.eventapi.EventManager;
import com.stlmissouri.cobalt.Cobalt;
import com.stlmissouri.cobalt.api.CobaltPlugin;

/**
 * User: Stl
 * Date: 5/17/2014
 * Time: 2:15 AM
 * Use:
 */
public class PluginList extends CobaltPlugin {

    public PluginList(Cobalt cobalt) {
        super(cobalt);
    }

    @Override
    public void load(Cobalt cobalt) {
        EventManager.register(new PluginLister(cobalt));
    }

    @Override
    public void unload() {

    }

    @Override
    public String getName() {
        return "Plugin List";
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
