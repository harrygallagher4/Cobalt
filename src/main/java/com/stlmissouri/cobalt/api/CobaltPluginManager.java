package com.stlmissouri.cobalt.api;

import com.darkmagician6.eventapi.EventManager;
import com.darkmagician6.eventapi.EventTarget;
import com.stlmissouri.cobalt.Cobalt;
import com.stlmissouri.cobalt.command.Command;
import com.stlmissouri.cobalt.events.system.PluginLoadedEvent;
import com.stlmissouri.cobalt.events.system.ShutdownEvent;
import com.stlmissouri.cobalt.info.BasicInfoNode;
import com.stlmissouri.cobalt.info.InfoSet;
import com.stlmissouri.cobalt.info.ParentInfoNode;
import com.stlmissouri.cobalt.util.CobaltManager;
import eu.bibl.bytetools.jar.ASMClassLoader;
import eu.bibl.bytetools.jar.ClassParser;
import eu.bibl.bytetools.jar.JarDownloader;
import eu.bibl.bytetools.jar.JarType;
import org.apache.commons.io.FileUtils;
import org.objectweb.asm.tree.ClassNode;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class CobaltPluginManager implements CobaltManager {

    static {
        ClassParser.addToClassLoader = true;
        ClassParser.verbose = false;
        ClassParser.verboseWarnings = false;
        ASMClassLoader.verbose = false;
    }

    private final Cobalt cobalt;

    private final Map<Class<? extends CobaltPlugin>, CobaltPlugin> plugins;

    public CobaltPluginManager(final Cobalt cobalt) {
        this.cobalt = cobalt;
        this.plugins = new HashMap<>();
        this.init();
        EventManager.register(this);
    }

    private void init() {
        this.cobalt.commandManager.registerCommand("reload", new Command() {
            @Override
            public void onCommand(String... args) {
                if (args.length > 0)
                    cobalt.displayChat("Invalid argument: " + args[0]);
                int reloaded = 0;
                for (Class<? extends CobaltPlugin> clazz : plugins.keySet()) {
                    System.out.println("Reloading: " + clazz);
                    CobaltPlugin plugin = plugins.get(clazz);
                    try{
                    plugin.unload();
                    plugin.load(cobalt);
                    } catch (Exception e) {
                        System.err.println("Error reloading: " + clazz);
                    }
                    reloaded++;
                }
                cobalt.displayChat("\247A" + reloaded + " plugins reloaded!");
            }
        });
    }

    public void registerPlugin(CobaltPlugin plugin) {
        this.registerPlugin(plugin.getClass(), plugin);
    }

    private void registerPlugin(Class<? extends CobaltPlugin> key, CobaltPlugin plugin) {
        this.plugins.put(key, plugin);
        plugin.load(this.cobalt);
        EventManager.call(new PluginLoadedEvent(plugin));
    }

    public void unregisterPlugin(Class<? extends CobaltPlugin> key) {
        if (this.plugins.containsKey(key))
            this.plugins.get(key).unload();
        this.plugins.remove(key);
    }

    public boolean hasPlugin(Class<? extends CobaltPlugin> key) {
        return this.plugins.containsKey(key);
    }

    public <T extends CobaltPlugin> T getPlugin(Class<T> clazz) {
        if (!this.hasPlugin(clazz))
            return null;
        //noinspection unchecked
        return (T) this.plugins.get(clazz);
    }

    public Collection<CobaltPlugin> getAllPlugins() {
        return this.plugins.values();
    }

    public void loadJars(File dir) {
        System.out.println("Cobalt: loading jars: " + dir.getAbsolutePath());
        Iterator<File> fileIterator = FileUtils.iterateFiles(dir, new String[]{"jar"}, false);
        File f;
        while (fileIterator.hasNext()) {
            try {
                f = fileIterator.next();
                System.out.println("-\tLoading: " + f.getName());
                JarDownloader downloader = new JarDownloader(f.getAbsolutePath(), JarType.FILE);
                ASMClassLoader cl = downloader.getParser().classLoader;
                for (ClassNode cn : downloader.getParser().getResultantClasses().values()) {
                    Class<?> clazz = cl.nodeToClass(cn);
                    if (clazz != null) {
                        if (!clazz.getSuperclass().equals(CobaltPlugin.class))
                            continue;
                        CobaltPlugin plugin = (CobaltPlugin) clazz.newInstance();
                        //noinspection unchecked
                        this.registerPlugin((Class<? extends CobaltPlugin>) clazz, plugin);
                    }
                }
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void updateInfo(InfoSet infoSet) {
        infoSet.removeNode("cobalt.manager.plugins.info");
        ParentInfoNode parentInfoComponent = new ParentInfoNode("Plugins");
        for (Class<? extends CobaltPlugin> clazz : this.plugins.keySet()) {
            BasicInfoNode pluginComponent = new BasicInfoNode(clazz);
            parentInfoComponent.addChild(pluginComponent);
        }
        infoSet.addNode("cobalt.manager.plugins.info", parentInfoComponent);
    }

    @EventTarget
    public void onShutdown(ShutdownEvent event) {
        for (CobaltPlugin plugin : this.plugins.values()) {
            plugin.unload();
        }
    }

}