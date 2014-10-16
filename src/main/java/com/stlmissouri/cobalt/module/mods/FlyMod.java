package com.stlmissouri.cobalt.module.mods;

import net.minecraft.client.entity.EntityPlayerSP;

import org.lwjgl.input.Keyboard;

import com.darkmagician6.eventapi.EventTarget;
import com.stlmissouri.cobalt.Cobalt;
import com.stlmissouri.cobalt.events.mod.TickEvent;
import com.stlmissouri.cobalt.module.CobaltModule;

public class FlyMod extends CobaltModule {

//    private IDebugger debugger;

    public FlyMod(Cobalt cobalt) {
        super(cobalt, "Fly");
        this.updateKeybind(Keyboard.KEY_F);
    }

    @EventTarget
    public void onEvent(TickEvent event) {
    	EntityPlayerSP thePlayer = mc.thePlayer;
        thePlayer.motionX = 0;
        thePlayer.motionY = 0;
        thePlayer.motionZ = 0;

        boolean up = this.keyDown(mc.gameSettings.keyBindJump.getKeyCode());
        boolean down = this.keyDown(mc.gameSettings.keyBindSneak.getKeyCode());

        if (up)
            thePlayer.motionY++;
        if (down)
            thePlayer.motionY--;

        double rotYaw = thePlayer.rotationYaw + 90F;
        boolean forward = this.keyDown(mc.gameSettings.keyBindForward.getKeyCode());
        boolean back = this.keyDown(mc.gameSettings.keyBindBack.getKeyCode());
        boolean left = this.keyDown(mc.gameSettings.keyBindLeft.getKeyCode());
        boolean right = this.keyDown(mc.gameSettings.keyBindRight.getKeyCode());
        if (forward) {
            if (left) {
                rotYaw -= 45D;
            } else if (right) {
                rotYaw += 45D;
            }
        } else if (back) {
            rotYaw += 180D;
            if (left) {
                rotYaw += 45D;
            } else if (right) {
                rotYaw -= 45D;
            }
        } else if (left) {
            rotYaw -= 90D;
        } else if (right) {
            rotYaw += 90D;
        }
        if (forward || left || back || right) {
            thePlayer.motionX = Math.cos(Math.toRadians(rotYaw));
            thePlayer.motionZ = Math.sin(Math.toRadians(rotYaw));
        }
    }

    private boolean keyDown(int key) {
        return mc.inGameHasFocus && Keyboard.isKeyDown(key);
    }

    @Override
    public void onEnable() {
        super.onEnable();
//        thePlayer.onGround = false;
//        cobalt.debugManager.attachDebugger(debugger.getKey());
    }

    @Override
    public void onDisable() {
        super.onDisable();
//        cobalt.debugManager.detachDebugger(debugger.getKey());
//        thePlayer.fallDistance = 0;
//        thePlayer.onGround = true;
    }
}