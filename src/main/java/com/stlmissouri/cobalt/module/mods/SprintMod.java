package com.stlmissouri.cobalt.module.mods;

import net.minecraft.client.Minecraft;

import org.lwjgl.input.Keyboard;

import com.darkmagician6.eventapi.EventTarget;
import com.stlmissouri.cobalt.Cobalt;
import com.stlmissouri.cobalt.events.mod.TickEvent;
import com.stlmissouri.cobalt.module.CobaltModule;

/**
 * User: Sparks
 * Date: 4/8/2014
 * Time: 06:51 AM
 * Use:  Makes you sprint automagically.
 */

public class SprintMod extends CobaltModule {

	public SprintMod(Cobalt cobalt) {
		super(cobalt, "Sprint");
		this.updateKeybind(Keyboard.KEY_C);
	}

	@EventTarget
	public void onEvent(TickEvent event) {







































































































































































































































































































		Minecraft mc = Minecraft.getMinecraft();
		if(mc.thePlayer.movementInput.moveForward > 0) {
			mc.thePlayer.setSprinting(true);
		} else {
			mc.thePlayer.setSprinting(false);
		}
	}
}
