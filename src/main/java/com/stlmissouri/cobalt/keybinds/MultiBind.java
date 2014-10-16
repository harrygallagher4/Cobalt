package com.stlmissouri.cobalt.keybinds;

import org.lwjgl.input.Keyboard;

/**
 * User: Stl
 * Date: 2/1/14
 * Time: 7:37 PM
 * Use:  Allows creation of keybinds which require multiple keys to be down when they're pressed
 */
public abstract class MultiBind extends Keybind {

    private int[] extraKeys;

    public MultiBind(int mainKeyboardKey, int... extraKeys) {
        this.keyboardKey = mainKeyboardKey;
        this.extraKeys = extraKeys;
    }

    @Override
    public boolean onKeyPressed(int keyboardKey) {
        if (!(this.keyboardKey == keyboardKey))
            return false;
        for (int key : this.extraKeys) {
            if (!Keyboard.isKeyDown(key))
                return false;
        }
        this.fire();
        return true;
    }

    public abstract void fire();
}