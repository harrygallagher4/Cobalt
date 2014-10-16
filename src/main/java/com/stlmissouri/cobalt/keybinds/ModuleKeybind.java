package com.stlmissouri.cobalt.keybinds;

import com.stlmissouri.cobalt.module.CobaltModule;

/**
 * User: Stl Date: 2/22/14 Time: 10:46 PM Use: Keybind associated with a module
 */
public class ModuleKeybind extends BasicKeybind {

	public static String STORE_KEY = "cobalt.keybind.module.";
	
	private CobaltModule module;
	private String saveKey;

	public ModuleKeybind(CobaltModule module, String saveKey, int keyboardKey) {
		super(keyboardKey);
		this.module = module;
		this.saveKey = saveKey;
	}

	public CobaltModule getModule() {
		return module;
	}

	@Override
	public void fire() {
		this.module.toggle();
	}
	
	public static String createKeybindModuleStoreKey(CobaltModule module){
		return module.getName().toLowerCase();
	}
	
	public String getKeybindStoreKey(){
		return saveKey;
	}

	public void setModule(CobaltModule module) {
		this.module = module;
	}
}