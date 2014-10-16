package com.stlmissouri.cobalt.module;

import com.stlmissouri.cobalt.Cobalt;
import com.stlmissouri.cobalt.command.Command;
import com.stlmissouri.cobalt.info.BasicInfoNode;
import com.stlmissouri.cobalt.info.InfoSet;
import com.stlmissouri.cobalt.info.ParentInfoNode;
import com.stlmissouri.cobalt.util.CobaltManager;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * User: Stl
 * Date: 2/11/14
 * Time: 6:47 PM
 * Use:  what do you think
 */
public class CobaltModuleManager implements CobaltManager {

    private final Map<Class<? extends CobaltModule>, CobaltModule> modules;

    private final Cobalt cobalt;

    public CobaltModuleManager(Cobalt cobalt) {
        this.cobalt = cobalt;
        this.modules = new HashMap<>();
        this.init();
    }

    private void init() {
        this.cobalt.commandManager.registerCommand("modules", new Command() {
            @Override
            public void onCommand(String... args) {
                boolean show = args.length > 0 && args[0].equalsIgnoreCase("list");
                cobalt.displayChat(modules.size() + " modules currently loaded." + (show ? "" : " \".mods list\" to list modules"));
                CobaltModule[] cobaltModules = modules.values().toArray(new CobaltModule[modules.size()]);
                String[] mods = new String[cobaltModules.length];
                if(show) {
                    for (int i = 0; i < cobaltModules.length; i++) {
                        mods[i] = cobaltModules[i].getName();
                    }
                    cobalt.displayChat(StringUtils.join(mods, ", "));
                }

            }
        });
        this.cobalt.commandManager.registerAlias("modules", "mods");
    }

    public void registerModule(CobaltModule module) {
        this.registerModule(module.getClass(), module);
    }

    private void registerModule(Class<? extends CobaltModule> key, CobaltModule module) {
        this.modules.put(key, module);
    }

    public void unregisterModule(String key) {
        this.modules.remove(key);
    }

    public Collection<CobaltModule> getAllModules() {
        return this.modules.values();
    }
    public Map<Class<? extends CobaltModule>, CobaltModule> getAllModulesAsMap() {
        return this.modules;
    }

    public <T extends CobaltModule> T getModule(Class<T> clazz) {
        return (T) this.modules.get(clazz);
    }
    
    public CobaltModule getModuleByName(String name){
    	for(CobaltModule mod : modules.values()){
    		if(mod.getName().equalsIgnoreCase(name))
    			return mod;
    	}
    	return null;
    }

    @Override
    public void updateInfo(InfoSet infoSet) {
        infoSet.removeNode("cobalt.manager.modules.info");
        ParentInfoNode parentInfoComponent = new ParentInfoNode("Modules");
        for (Class clazz : this.modules.keySet()) {
            CobaltModule assocModule = this.modules.get(clazz);
            BasicInfoNode component = new BasicInfoNode(assocModule.getName());
            parentInfoComponent.addChild(component);
        }
        infoSet.addNode("cobalt.manager.modules.info", parentInfoComponent);
    }
}