package com.stlmissouri.cobalt.keybinds;

/**
 * User: Stl
 * Date: 2/1/14
 * Time: 7:32 PM
 * Use:  Standard single-key keybind
 */
public abstract class BasicKeybind extends Keybind {
	
    public BasicKeybind(int keyboardKey) {
        this.keyboardKey = keyboardKey;
    }

    @Override
    public boolean onKeyPressed(int keyboardKey) {
        if (this.keyboardKey != keyboardKey)
            return false;
        this.fire();
        return true;
    }

    public abstract void fire();
}
