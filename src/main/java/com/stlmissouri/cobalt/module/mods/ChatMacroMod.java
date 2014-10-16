package com.stlmissouri.cobalt.module.mods;

import com.stlmissouri.cobalt.Cobalt;
import com.stlmissouri.cobalt.command.Command;
import com.stlmissouri.cobalt.keybinds.BasicKeybind;
import com.stlmissouri.cobalt.keybinds.KeyboardUtils;
import com.stlmissouri.cobalt.module.CobaltModule;

import java.util.ArrayList;
import java.util.HashMap;

public class ChatMacroMod extends CobaltModule {

	private HashMap<String, MacroMessageList> macros;

	public ChatMacroMod(final Cobalt cobalt) {
		super(cobalt, "ChatMacro");
		macros = new HashMap<>();

		Command theCommand = new Command() {
			@Override
			public void onCommand(String... args) {
				if (args.length == 2) {
					if (args[0].equalsIgnoreCase("create") || args[0].equalsIgnoreCase("make")) {
						String name = args[1];
						if (forName(name, false) != null) {
							cobalt.displayChat("Macro already exists: " + name);
						} else {
							MacroMessageList macroMessages = new MacroMessageList();
							macros.put(name, macroMessages);
							cobalt.displayChat("Created macro: " + name);
						}
						return;
					} else if (args[0].equalsIgnoreCase("del") || args[0].equalsIgnoreCase("delete")) {
						String name = args[1];
						if (forName(name) != null) {
							macros.remove(name);
							cobalt.displayChat("Removed macro: " + name);
						}
						return;
					} else if (args[0].equalsIgnoreCase("unbind")) {
						String name = args[1];
						MacroMessageList list = null;
						if((list = forName(name)) != null){
							list.unbind();
							cobalt.displayChat("Unbound macro: " + name);
						}
						return;
					}else if(args[0].equalsIgnoreCase("execute") || args[0].equalsIgnoreCase("exe") || args[0].equalsIgnoreCase("do")){
						String name = args[1];
						MacroMessageList list = null;
						if((list = forName(name)) != null){
							list.executeMessages();
						}
						return;
					}
				} else if (args.length == 3) {
					if (args[0].equalsIgnoreCase("bind")) {
						String name = args[1];
						MacroMessageList list = null;
						if((list = forName(name)) == null){
							return;
						}
						String key = args[2];
						int keyValue = 0;
						if (key.toLowerCase().startsWith("key")) {
							Integer val = KeyboardUtils.keyMap.get(key.toUpperCase());
							if (val != null) {
								keyValue = val;
								list.updateBind(keyValue);
								cobalt.displayChat("Bound " + KeyboardUtils.keyMap2.get(keyValue) + " to " + name);
								return;
							} else {
								cobalt.displayChat("Couldn't bind " + key + " to " + name);
								return;
							}
						} else {
							try {
                                keyValue = Integer.parseInt(key);
								list.updateBind(keyValue);
								cobalt.displayChat("Bound " + KeyboardUtils.keyMap2.get(keyValue) + " to " + name);
								return;
							} catch (Exception e) {
								String newKey = "KEY_" + key.toUpperCase();
								Integer val = KeyboardUtils.keyMap.get(newKey.toUpperCase());
								if (val != null) {
									keyValue = val;
									list.updateBind(keyValue);
									cobalt.displayChat("Bound " + KeyboardUtils.keyMap2.get(keyValue) + " to " + name);
									return;
								} else {
									cobalt.displayChat("Couldn't bind " + key + " to " + name);
									return;
								}
							}
						}
					}
				}else{
					if(args[0].equalsIgnoreCase("add")){
						String name = args[1];
						MacroMessageList list = null;
						if((list = forName(name)) != null){
							String toAdd = concat(args, 2, args.length);
							MacroMessage msg = new MacroMessage();
							msg.macroName = name;
							msg.msg = toAdd;
							list.add(msg);
							cobalt.displayChat("Added message \"" + toAdd + "\" to macro " + name);
						}
					}
				}
				return;
			}
		};
		cobalt.commandManager.registerCommand("macro", theCommand);
	}
	
	public static String concat(String[] args){
		return concat(args, 0, args.length);
	}
	
	public static String concat(String[] args, int start, int end){
		String concatted = "";
		for(int i=start; i < end; i++){
			concatted += args[i];
			concatted += " ";
		}
		return concatted.trim();
	}
	
	private MacroMessageList forName(String name){
		return forName(name, true);
	}
	
	private MacroMessageList forName(String name, boolean display){
		if (macros.containsKey(name)) {
			return macros.get(name);
		} else if(display){
			cobalt.displayChat("Macro does not exist: " + name);
		}
		return null;
	}

	class MacroMessage {

		String macroName;
		String msg;
	}

	class MacroMessageList extends ArrayList<MacroMessage> {

		private static final long serialVersionUID = -8115623984019785664L;

		static final String KEY_BASE = "com.cobalt.chatmacro.messagelist.";

		int keyBind;

		void unbind() {
			cobalt.keybindManager.unregisterKeybind(KEY_BASE + keyBind);
		}

		void updateBind(int newBind) {
			unbind();
			cobalt.keybindManager.registerKeybind(KEY_BASE + newBind, new BasicKeybind(newBind) {
				@Override
				public void fire() {
					executeMessages();
				}
			});
		}

		void executeMessages() {
			for (MacroMessage msg : this) {
				mc.thePlayer.sendChatMessage(msg.msg);
			}
		}
		
		MacroMessage byMsg(String msg){
			for(MacroMessage mmsg : this)
				if(mmsg.msg.equals(msg))
					return mmsg;
			return null;
		}
	}
}