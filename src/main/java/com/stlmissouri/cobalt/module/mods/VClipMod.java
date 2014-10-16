package com.stlmissouri.cobalt.module.mods;

import net.minecraft.client.entity.EntityPlayerSP;

import org.lwjgl.input.Keyboard;

import com.stlmissouri.cobalt.Cobalt;
import com.stlmissouri.cobalt.command.Command;
import com.stlmissouri.cobalt.keybinds.BasicKeybind;
import com.stlmissouri.cobalt.module.CobaltModule;

public class VClipMod extends CobaltModule {

	private float macroClip = 1;

	public VClipMod(final Cobalt cobalt) {
		super(cobalt, "VClip");
		this.cobalt.commandManager.registerCommand("vclip", new Command() {
			@Override
			public void onCommand(String... args) {
				if (args.length == 1) {
					try {
						float f = Float.parseFloat(args[0]);
						vclip(f);
						return;
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else if (args.length == 2) {
					if (args[0].equalsIgnoreCase("set")) {
						try {
                            macroClip = Float.parseFloat(args[1]);
							return;
						} catch (Exception e) {
						}
					}
				}
				return;
			}
		});
		this.cobalt.keybindManager.registerKeybind(this.getName().toLowerCase() + ".bind", new BasicKeybind(Keyboard.KEY_M) {
			@Override
			public void fire() {
				vclip(macroClip);
			}
		});
	}

	private void vclip(float amount) {
		EntityPlayerSP thePlayer = mc.thePlayer;
		double x = thePlayer.posX;
		double y = thePlayer.posY;
		double z = thePlayer.posZ;

		y += amount;

		thePlayer.setPosition(x, y, z);
	}
}