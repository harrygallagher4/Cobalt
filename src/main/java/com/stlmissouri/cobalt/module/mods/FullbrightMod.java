package com.stlmissouri.cobalt.module.mods;

import com.darkmagician6.eventapi.EventManager;
import com.darkmagician6.eventapi.EventTarget;
import com.stlmissouri.cobalt.Cobalt;
import com.stlmissouri.cobalt.events.mod.TickEvent;
import com.stlmissouri.cobalt.events.system.ShutdownEvent;
import com.stlmissouri.cobalt.module.CobaltModule;
import org.lwjgl.input.Keyboard;

public class FullbrightMod extends CobaltModule {

	private boolean flag;
	private float startBrightness = -69;

	public FullbrightMod(Cobalt cobalt) {
		super(cobalt, "Fullbright");
        this.updateKeybind(Keyboard.KEY_B);
		new FullbrightModShutdownHook(this);
	}

	@Override
	public void toggle() {
		isEnabled = !isEnabled;
		if (isEnabled) {
			this.onEnable();
			flag = true;
		} else {
			flag = false;
		}
	}

	@EventTarget
	public void onTick(TickEvent e) {
		if (flag) {
			if (mc.gameSettings.gammaSetting < 10)
				mc.gameSettings.gammaSetting += 0.5F;
		} else {
			if (mc.gameSettings.gammaSetting > startBrightness)
				mc.gameSettings.gammaSetting -= 0.5F;
			else
				onDisable();
		}
	}

	@Override
	public void onEnable() {
		super.onEnable();
		startBrightness = mc.gameSettings.gammaSetting;
	}

	class FullbrightModShutdownHook {

		private FullbrightMod mod;
		
		private FullbrightModShutdownHook(FullbrightMod mod) {
			this.mod = mod;
			EventManager.register(this);
		}

		@EventTarget
		public void onShutdown(ShutdownEvent event){
			if(mod.startBrightness != -69){
				mc.gameSettings.gammaSetting = mod.startBrightness;
			}
		}
	}
}