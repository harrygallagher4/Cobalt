package com.stlmissouri.cobalt.keybinds;

/**
 * User: Stl
 * Date: 2/1/14
 * Time: 7:25 PM
 * Use:
 */
public abstract class Keybind {

	public static final int FLAG_BIND_REMOVED = -2;
	
    protected int keyboardKey;

    public abstract boolean onKeyPressed(int keyboardKey);

    public boolean isAssignedTo(int keyboardKey) {
        return this.keyboardKey == keyboardKey;
    }
    
    public int getKeyboardKey(){
    	return keyboardKey;
    }
    

    public void setKeyboardKey(int keyboardKey) {
        this.keyboardKey = keyboardKey;
    }

}