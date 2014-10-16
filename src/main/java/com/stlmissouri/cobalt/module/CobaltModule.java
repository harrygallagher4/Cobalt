package com.stlmissouri.cobalt.module;

import com.darkmagician6.eventapi.EventManager;
import com.stlmissouri.cobalt.Cobalt;
import com.stlmissouri.cobalt.command.Command;
import com.stlmissouri.cobalt.keybinds.Keybind;
import com.stlmissouri.cobalt.keybinds.ModuleKeybind;
import com.stlmissouri.cobalt.util.Debugger;
import net.minecraft.client.Minecraft;

/**
 * User: Stl
 * Date: 2/2/14
 * Time: 4:25 PM
 * Use:  Module base class
 */
public abstract class CobaltModule {

    private static final String MODULE_KEY_BASE = "cobalt.module.";
    private final String name;
    protected final Cobalt cobalt;
    protected final Minecraft mc;

    protected boolean isEnabled = false;

    public CobaltModule(Cobalt cobalt, String name) {
        this.name = name;
        this.cobalt = cobalt;
        this.mc = Minecraft.getMinecraft();
        this.cobalt.commandManager.registerCommand(this.name.replaceAll(" ", ""), new Command() {
            @Override
            public void onCommand(String... args) {
                CobaltModule.this.toggle();
            }
        });
        this.createAndRegisterKeybind(0);
    }

    /**
     * Called to toggle the module
     * Be sure to include a call to super if you override this
     */
    public void toggle() {
        this.isEnabled = !this.isEnabled;
        if (this.isEnabled)
            this.onEnable();
        else
            this.onDisable();
    }

    /**
     * @return The module's name in simple format
     */
    public String getName() {
        return this.name;
    }

    /**
     * @return The object key in "com.cobalt.xxx" format
     */
    public String getKey() {
        return MODULE_KEY_BASE + this.name;
    }

	/*
     * We're going to register this regardless. This shouldn't have an impact on performance because
	 * DarkMagician's API only registers methods with the annotation present. Therefore if no methods
	 * have the annotation it won't take up any space in memory.
	 */

    /**
     * Fired when the module is enabled
     * Be sure to include a call to super if you override this
     */
    protected void onEnable() {
        Debugger.debug("Cobalt: " + getName() + " enabled");
        EventManager.register(this);
    }

    /**
     * Fired when the module is disabled
     * Be sure to include a call to super if you override this
     */
    protected void onDisable() {
        Debugger.debug("Cobalt: " + getName() + " disabled");
        EventManager.unregister(this);
    }

    protected void updateKeybind(int keyboardKey) {
        String key = ModuleKeybind.createKeybindModuleStoreKey(this);
        if (!cobalt.keybindManager.containsKeybindKey(key))
            return;
        Keybind bind = cobalt.keybindManager.getKeybind(key);
        bind.setKeyboardKey(keyboardKey);
    }

    /**
     * Creates an associated ModuleKeybind object
     *
     * @param keyboardKey Keyboard key value
     * @return ModuleKeybind object for this module
     */
    protected Keybind makeKeybind(int keyboardKey) {
        String bindKey = ModuleKeybind.createKeybindModuleStoreKey(this);
        return new ModuleKeybind(this, bindKey, keyboardKey);
    }

    /**
     * Registers a keybind with this module's name as the key in "cobalt.keybind.module.xxx" format
     *
     * @param keybind keybind object to be registered
     */
    private void registerKeybind(Keybind keybind) {
        this.cobalt.keybindManager.registerKeybind(ModuleKeybind.createKeybindModuleStoreKey(this), keybind);
    }

    /**
     * Creates and registers a keybind
     *
     * @param keyboardKey Keyboard key value
     */
    private void createAndRegisterKeybind(int keyboardKey) {
        this.registerKeybind(this.makeKeybind(keyboardKey));
    }

    public boolean isEnabled() {
        return isEnabled;
    }
}