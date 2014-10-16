package com.stlmissouri.cobalt.keybinds;

import com.darkmagician6.eventapi.EventManager;
import com.darkmagician6.eventapi.EventTarget;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.stlmissouri.cobalt.Cobalt;
import com.stlmissouri.cobalt.command.Command;
import com.stlmissouri.cobalt.events.mod.KeyEvent;
import com.stlmissouri.cobalt.events.system.ShutdownEvent;
import com.stlmissouri.cobalt.info.InfoSet;
import com.stlmissouri.cobalt.module.CobaltModule;
import com.stlmissouri.cobalt.util.CobaltManager;
import com.stlmissouri.cobalt.util.Debugger;
import org.lwjgl.input.Keyboard;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import static com.stlmissouri.cobalt.keybinds.Keybind.FLAG_BIND_REMOVED;

/**
 * User: Stl
 * Date: 2/1/14
 * Time: 7:24 PM
 * Use: Used to manage keybinds
 * <p/>
 * Okay, before you guys flame me for doing this "wrong," just know some
 * things are managed weird so I can create binds which require multiple
 * keys to be pressed.
 */
public class CobaltKeybindManager implements CobaltManager {

    private Cobalt cobalt;
    private Map<String, Keybind> bindMap;
    private Map<String, Integer> overrides;

    public CobaltKeybindManager(Cobalt cobalt) {
        this.cobalt = cobalt;
        bindMap = new HashMap<>();
        overrides = new HashMap<>();
        initCommand();
        EventManager.register(this);
    }

    private void saveAsJson() {
        Map<String, String> toSave = new HashMap<>();
        for (String s : this.bindMap.keySet()) {
            Keybind k = this.bindMap.get(s);
            if (k instanceof MultiBind)
                continue; //Can't think of a way to save these yet
            int key = k.getKeyboardKey();
            toSave.put(s, Keyboard.getKeyName(key));
        }
        for (String s : this.overrides.keySet()) {
            int key = this.overrides.get(s);
            toSave.put(s, Keyboard.getKeyName(key));
        }
        Map<String, String> save = new TreeMap<>();
        save.putAll(toSave);
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            BufferedWriter writer = cobalt.ioManager.genBindFileWriter();
            gson.toJson(save, writer);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadAsJson() {
        Type type = new TypeToken<Map<String, String>>() {
        }.getType();
        try {
            BufferedReader reader = cobalt.ioManager.genBindFileReader();
            Map<String, String> loaded = new Gson().fromJson(reader, type);
            reader.close();
            if (loaded == null)
                return;
            Map<String, Integer> overrides = new HashMap<>();
            for (String mapKey : loaded.keySet()) {
                String key = loaded.get(mapKey);
                int keyboardKey = Keyboard.getKeyIndex(key);
                overrides.put(mapKey.toLowerCase(), keyboardKey);
            }
            this.overrides = overrides;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void registerKeybind(String key, Keybind bind) {
        key = key.toLowerCase();
        if (this.overrides.containsKey(key)) {
            bind.setKeyboardKey(this.overrides.get(key));
            this.overrides.remove(key);
        }
        if (this.bindMap.containsKey(key))
            this.bindMap.remove(key);
        this.bindMap.put(key, bind);
    }

    public void unregisterKeybind(String key) {
        if (bindMap.containsKey(key.toLowerCase()))
            bindMap.remove(key.toLowerCase());
    }

    public boolean containsKeybindKey(String key) {
        return bindMap.containsKey(key.toLowerCase());
    }

    public Keybind getKeybind(String key) {
        return bindMap.get(key.toLowerCase());
    }

    @EventTarget
    public void onKeyPressed(KeyEvent keyEvent) {
        if (keyEvent.key == Keyboard.KEY_NONE)
            return;
        Keybind keybind = null;
        for (String key : bindMap.keySet()) {
            keybind = bindMap.get(key);
            if (keybind.onKeyPressed(keyEvent.key))
                Debugger.debug("Keybind fired: " + key);
        }
    }

    @EventTarget
    public void onShutdownEvent(ShutdownEvent event) {
        this.saveAsJson();
    }

    private boolean isHelpCommand(String type) {
        return type.equalsIgnoreCase("help");
    }

    private boolean handleHelpCommand(String[] args, String type) {
        if (!isHelpCommand(type))
            return false;
        displayHelpMessage();
        return true;
    }

    private void displayHelpMessage() {
        cobalt.displayChat("[BindManager]: Showing CobaltBindManager help.");
        cobalt.displayChat("[BindManager]: .bind help");
        cobalt.displayChat("[BindManager]: .bind [rem/remove/clear] [mod/key]");
        cobalt.displayChat("[BindManager]: .bind set [mod/key] <key>");
        cobalt.displayChat("[BindManager]: .bind add [mod/key] <key>");
        cobalt.displayChat("[BindManager]: .bind reset <mod>");
        cobalt.displayChat("[BindManager]:  NOTE: key may represent the internal key of a bind, NOT A MOD.");
        saveAsJson();
    }

    private boolean isRemoveCommand(String type) {
        return type.toLowerCase().startsWith("rem") || type.equalsIgnoreCase("clear");
    }

    private boolean handleRemoveCommand(String[] args, String type) {
        if (!isRemoveCommand(type))
            return false;
        if (!(args.length >= 2))
            return false;
        // true for name, false for key
        boolean isName = !args[1].contains(".");
        if (isName) {
            CobaltModule theMod = cobalt.moduleManager.getModuleByName(args[1]);
            if (theMod == null) {
                displayError("Module " + args[1] + " doesn't exist.");
                // return true to stop the default help message showing.
                return true;
            }
            String bindKey = ModuleKeybind.createKeybindModuleStoreKey(theMod);
            Keybind theBind = cobalt.keybindManager.getKeybind(bindKey);
            if (theBind == null || theBind.keyboardKey == FLAG_BIND_REMOVED) {
                displayError("Module " + args[1] + " exists but keybind " + bindKey + " has not been registered yet so cannot be removed.");
                // return true to stop the default help message showing.
                return true;
            }
            theBind.keyboardKey = FLAG_BIND_REMOVED;
            display("", "Successfully removed keybind for " + bindKey);
            return true;
        } else {
            String bindKey = args[1];
            Keybind theBind = cobalt.keybindManager.getKeybind(bindKey);
            if (theBind == null || theBind.keyboardKey == FLAG_BIND_REMOVED) {
                displayError("Keybind " + bindKey + " has not been registered yet so cannot be removed.");
                // return true to stop the default help message showing.
                return true;
            }
            theBind.keyboardKey = FLAG_BIND_REMOVED;
            display("", "Successfully removed keybind for " + bindKey);
            return true;
        }
    }

    private boolean isSetCommand(String type) {
        return type.equalsIgnoreCase("set");
    }

    private boolean handleSetCommand(String[] args, String type) {
        if (!isSetCommand(type))
            return false;
        if (!(args.length >= 3))
            return false;
        int newKey = KeyboardUtils.resolveKey(args[2]);
        if (newKey == 0) {
            displayError("Must use a valid keyboard key for setting a bind.");
            // return true to stop the default help message showing.
            return true;
        }
        // true for name, false for key
        boolean isName = !args[1].contains(".");
        if (isName) {
            CobaltModule theMod = cobalt.moduleManager.getModuleByName(args[1]);
            if (theMod == null) {
                displayError("Module " + args[1] + " doesn't exist.");
                // return true to stop the default help message showing.
                return true;
            }
            String bindKey = ModuleKeybind.createKeybindModuleStoreKey(theMod);
            Keybind theBind = cobalt.keybindManager.getKeybind(bindKey);
            if (theBind == null || theBind.keyboardKey == FLAG_BIND_REMOVED) {
                displayError("Module " + args[1] + " exists but keybind " + bindKey + " has not been registered yet so cannot be set.");
                // return true to stop the default help message showing.
                return true;
            }
            theBind.keyboardKey = newKey;
            display("", "Successfully set keybind for " + bindKey + " to " + newKey);
            return true;
        } else {
            String bindKey = args[1];
            Keybind theBind = cobalt.keybindManager.getKeybind(bindKey);
            if (theBind == null || theBind.keyboardKey == FLAG_BIND_REMOVED) {
                displayError("Keybind " + bindKey + " has not been registered yet so cannot be set.");
                // return true to stop the default help message showing.
                return true;
            }
            theBind.keyboardKey = newKey;
            display("", "Successfully set keybind for " + bindKey + " to " + newKey);
            return true;
        }
    }

    private boolean isAddCommand(String type) {
        return type.equalsIgnoreCase("add");
    }

    private boolean handleAddCommand(String[] args, String type) {
        if (!isAddCommand(type))
            return false;
        if (!(args.length >= 3))
            return false;
        int newKey = KeyboardUtils.resolveKey(args[2]);
        if (newKey == -69) {
            displayError("Must use a valid keyboard key for setting a bind.");
            // return true to stop the default help message showing.
            return true;
        }
        // true for name, false for key
        CobaltModule theMod = cobalt.moduleManager.getModuleByName(args[1]);
        if (theMod == null) {
            displayError("Module " + args[1] + " doesn't exist.");
            // return true to stop the default help message showing.
            return true;
        }
        String bindKey = ModuleKeybind.createKeybindModuleStoreKey(theMod);
        Keybind theBind = cobalt.keybindManager.getKeybind(bindKey);
        if (theBind == null) {
            theBind = new ModuleKeybind(theMod, bindKey, newKey);
            registerKeybind(bindKey, theBind);
            display("", "Successfully added keybind " + bindKey + " to key " + newKey);
            return true;
        } else if (theBind.keyboardKey == FLAG_BIND_REMOVED) {
            theBind.keyboardKey = newKey;
            display("", "Successfully added keybind " + bindKey + " to key " + newKey);
            return true;
        }
        displayError("Keybind for " + bindKey + " already exists.");
        return true;
    }

    private boolean isResetCommand(String type) {
        return type.equalsIgnoreCase("reset");
    }

    private boolean handleResetCommand(String[] args, String type) {
        if (!isResetCommand(type))
            return false;
        if (!(args.length >= 2))
            return false;
        // true for name, false for key
        boolean isName = !args[1].contains(".");
        if (isName) {
            CobaltModule theMod = cobalt.moduleManager.getModuleByName(args[1]);
            if (theMod == null) {
                displayError("Module " + args[1] + " doesn't exist.");
                // return true to stop the default help message showing.
                return true;
            }
            String bindKey = ModuleKeybind.createKeybindModuleStoreKey(theMod);
            Keybind theBind = cobalt.keybindManager.getKeybind(bindKey);
            if (theBind == null) {
                displayError("Module " + args[1] + " exists but keybind " + bindKey + " has not been registered yet so cannot be reset.");
                // return true to stop the default help message showing.
                return true;
            }
//            theBind.keyboardKey = theBind.defaultKey;
//            display("", "Successfully reset keybind for " + bindKey + " to " + theBind.defaultKey);
            return true;
        } else {
            String bindKey = args[1];
            Keybind theBind = cobalt.keybindManager.getKeybind(bindKey);
            if (theBind == null) {
                displayError("Keybind " + bindKey + " has not been registered yet so cannot be reset.");
                // return true to stop the default help message showing.
                return true;
            }
//            theBind.keyboardKey = theBind.defaultKey;
//            display("", "Successfully reset keybind for " + bindKey + " to " + theBind.defaultKey);
            return true;
        }
    }

    private void initCommand() {
        Command changeKeyCommand = new Command() {
            @Override
            public void onCommand(String... args) {
                if (args.length > 0) {
                    String type = args[0];
                    if (handleHelpCommand(args, type)) {
                        return;
                    } else if (handleRemoveCommand(args, type)) {
                        return;
                    } else if (handleSetCommand(args, type)) {
                        return;
                    } else if (handleAddCommand(args, type)) {
                        return;
                    } else if (handleResetCommand(args, type)) {
                        return;
                    } else {
                        displayError("Usage: .bind help");
                        return;
                    }
                } else {
                    displayError("Usage: .bind help");
                    return;
                }
            }
        };
        cobalt.commandManager.registerCommand("bind", changeKeyCommand);
    }

    private void displayError(String msg) {
        display("Error", msg);
    }

    private void display(String type, String msg) {
        cobalt.displayChat("[BindManager" + type + "]: " + msg);
    }

    @Override
    public void updateInfo(InfoSet infoSet) {
    }
}