package com.stlmissouri.cobalt.api;

import com.stlmissouri.cobalt.Cobalt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * User: Stl
 * Date: 2/8/14
 * Time: 2:59 PM
 * Use:  Plugin base
 */
public abstract class CobaltPlugin {

    protected List<PluginDependency> dependencies = new ArrayList<>();

    public CobaltPlugin(Cobalt cobalt) {

    }

    public List<PluginDependency> getDependencies() {
        return this.dependencies;
    }

    /**
     * Adds a plugin to this plugin's dependencies
     * @param pluginClass the plugin depended on
     * @param version minimum version
     */
    protected void depend(Class<? extends CobaltPlugin> pluginClass, double version) {
        this.dependencies.add(new PluginDependency(pluginClass, version));
    }

    public boolean testDependencies(Collection<CobaltPlugin> plugins) {
        for (PluginDependency dependency : this.dependencies) {
            if (!dependency.test(plugins))
                return false;
        }
        return true;
    }

    /**
     * Called when the plugin is loaded by the client
     */
    public abstract void load(Cobalt cobalt);

    /**
     * Called when the plugin is unloaded, most likely by a client shutdown
     */
    public abstract void unload();

    /**
     * @return plugin name
     */
    public abstract String getName();

    /**
     * @return short description of the plugin
     */
    public abstract String getDescription();

    /**
     * @return plugin version
     */
    public abstract double getPluginVersion();

}