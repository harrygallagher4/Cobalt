package com.stlmissouri.cobalt.plugins.repo;

import com.darkmagician6.eventapi.EventManager;
import com.darkmagician6.eventapi.EventTarget;
import com.stlmissouri.cobalt.Cobalt;
import com.stlmissouri.cobalt.command.Command;
import com.stlmissouri.cobalt.events.system.ShutdownEvent;
import com.stlmissouri.cobalt.info.InfoSet;
import com.stlmissouri.cobalt.util.CobaltManager;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: Stl
 * Date: 4/13/2014
 * Time: 4:00 AM
 * Use:  Allows users to install Plugins from plugin repositories
 */
public class CobaltPackageManager implements CobaltManager {

    private final List<PluginRepository> repositories;
    private final Map<String, PluginRepository> availablePackages;
    private final Map<PluginRepository, Boolean> waiting;
    private final Cobalt cobalt;

    public CobaltPackageManager(final Cobalt cobalt) {
        this.repositories = new ArrayList<>();
        this.waiting = new HashMap<>();
        this.availablePackages = new HashMap<>();
        this.cobalt = cobalt;

        cobalt.commandManager.registerCommand("cobaltpackagemanager", new Command() {
            @Override
            public void onCommand(String... args) {
                if(args.length < 1)
                    return;
                if(args[0].equalsIgnoreCase("list")) {
                    StringBuilder builder = new StringBuilder();
                    for (String s : availablePackages.keySet())
                        builder.append(s).append(", ");
                    cobalt.displayChat(builder.toString());
                }
                if(args[0].equalsIgnoreCase("update")) {
                    update();
                }
                if(args.length < 2)
                    return;
                if(args[0].equalsIgnoreCase("check"))
                    cobalt.displayChat("Package " + args[1] + (hasPackage(args[1]) ? " found!" : " not found!"));
                if(args[0].equalsIgnoreCase("install")) {
                    final String pkg = args[1];
                    if(!hasPackage(pkg))
                        return;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                PackageInfo info = availablePackages.get(pkg).getPackage(pkg);
                                info.downloadLatestVersion(new File(cobalt.ioManager.COBALT_PLUGIN_DIR, pkg + ".jar"));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
                if(args[0].equalsIgnoreCase("add")) {
                    String repo = args[1];
                    createIndexAndAdd(repo);
                }
                return;
            }
        });
        cobalt.commandManager.registerAlias("cobaltpackagemanager", "cpm");
        EventManager.register(this);
    }

    public void addRepository(String repo) {
        this.createIndexAndAdd(repo);
    }

    private void createIndexAndAdd(final String repo) {
        try {
            final PluginRepository repository = new PluginRepository(repo);
            this.waiting.put(repository, true);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        repository.index();
                        synchronized (repositories) {
                            repositories.add(repository);
                        }
                        synchronized (waiting) {
                            waiting.put(repository, false);
                        }
                        System.out.println("Added repository: " + repo);
                    } catch (IOException e) {
                        waiting.put(repository, false);
                        e.printStackTrace();
                    }
                }
            }).start();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public void update() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (PluginRepository repository : repositories)
                    synchronized (waiting) {
                        waiting.put(repository, true);
                    }
                for (PluginRepository repository : repositories)
                    try {
                        repository.index();
                        synchronized (waiting) {
                            waiting.put(repository, false);
                        }
                    } catch (IOException e) {
                        synchronized (waiting) {
                            waiting.put(repository, false);
                        }
                        e.printStackTrace();
                    }
                for (PluginRepository repository : repositories) {
                    synchronized (availablePackages) {
                        for (String s : repository.getRepositoryInfo().getPackages()) {
                            availablePackages.put(s, repository);
                        }
                    }
                }
            }
        }).start();
    }

    public boolean hasPackage(String pkg) {
        return this.availablePackages.containsKey(pkg);
    }

    public boolean isWaiting() {
        return this.waiting.keySet().size() > 0;
    }

    @Override
    public void updateInfo(InfoSet infoSet) {

    }

    @EventTarget
    public void onShutdown(ShutdownEvent event) {

    }

}
