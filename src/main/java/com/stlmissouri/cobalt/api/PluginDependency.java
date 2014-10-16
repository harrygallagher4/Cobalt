package com.stlmissouri.cobalt.api;

import java.util.Collection;

/**
 * User: Stl
 * Date: 7/6/2014
 * Time: 12:38 AM
 * Use:
 */
public class PluginDependency {

    private final Class<? extends CobaltPlugin> pluginClass;
    private final double version;

    public PluginDependency(Class<? extends CobaltPlugin> pluginClass, double version) {
        this.pluginClass = pluginClass;
        this.version = version;
    }

    public boolean test(Collection<CobaltPlugin> plugins) {
        for (CobaltPlugin plugin : plugins) {
            if (plugin.getClass().equals(this.pluginClass) && plugin.getPluginVersion() >= this.version)
                return true;
        }
        return false;
    }

}
