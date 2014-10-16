package com.stlmissouri.cobalt.module.mods;

import net.minecraft.network.play.client.C03PacketPlayer;

import org.lwjgl.input.Keyboard;

import com.darkmagician6.eventapi.EventTarget;
import com.stlmissouri.cobalt.Cobalt;
import com.stlmissouri.cobalt.events.mod.TickEvent;
import com.stlmissouri.cobalt.module.CobaltModule;

/**
 * User: Sparks
 * Date: 4/9/2014
 * Time: 05:21 AM
 * Use:  Makes you jump up blocks automagically.
 * (Will be improved in a few days. Need to add checks.)
 */

public class StepMod extends CobaltModule {

	public StepMod(Cobalt cobalt) {
		super(cobalt, "Step");
		this.updateKeybind(Keyboard.KEY_O);
	}
	
	@EventTarget
	public void onEvent(TickEvent e) {
		boolean check = !mc.thePlayer.isOnLadder() && !mc.thePlayer.isInWater()
				&& mc.thePlayer.isCollidedHorizontally && this.isAir() && mc.thePlayer.onGround && !Keyboard.isKeyDown(Keyboard.KEY_SPACE);
		if(check) {
			mc.thePlayer.boundingBox.offset(0, 1F, 0);
			mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY - 0.5, mc.thePlayer.posY + 0.5, mc.thePlayer.posZ, mc.thePlayer.onGround));
	    	if (mc.thePlayer.onGround){
				mc.thePlayer.motionY = -999;
	    	}
		}
	}
	
	private boolean isAir() {
        return mc.theWorld.isAirBlock((int) mc.thePlayer.posX, (int) mc.thePlayer.posY, (int) mc.thePlayer.posZ);
    }
}
