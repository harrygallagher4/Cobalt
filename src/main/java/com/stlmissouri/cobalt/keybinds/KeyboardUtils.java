package com.stlmissouri.cobalt.keybinds;

import org.lwjgl.input.Keyboard;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;

public final class KeyboardUtils {

	public static final HashMap<String, Integer> keyMap = new HashMap<>();
	public static final HashMap<Integer, String> keyMap2 = new HashMap<>();
	
	static{
		for(Field field : Keyboard.class.getDeclaredFields()){
			if(!isConstant(field))
				continue;
			if(!field.getType().equals(int.class))
				continue;
			String name = field.getName();
			int value = -69;
			try {
				value = field.getInt(null);
				keyMap.put(name, value);
				keyMap2.put(value, name);
			} catch (IllegalArgumentException | IllegalAccessException e) {
			}
        }
	}
	
	public static boolean isConstant(Field field){
		int mods = field.getModifiers();
		boolean isPublic = Modifier.isPublic(mods);
		boolean isStatic = Modifier.isStatic(mods);
		boolean isFinal = Modifier.isFinal(mods);
		return isPublic && isStatic && isFinal;
	}
	
	public static int resolveKey(String key){
        int index = 0;
        if ((index = Keyboard.getKeyIndex(key.toUpperCase())) != 0)
            return index;
        else
            try {
                return Integer.parseInt(key);
            } catch (NumberFormatException nfe) {
                return 0;
            }
	}
}