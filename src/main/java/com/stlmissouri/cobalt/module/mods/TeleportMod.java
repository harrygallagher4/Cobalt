package com.stlmissouri.cobalt.module.mods;

import com.stlmissouri.cobalt.Cobalt;
import com.stlmissouri.cobalt.command.Command;
import com.stlmissouri.cobalt.module.CobaltModule;

public class TeleportMod extends CobaltModule {

	public TeleportMod(Cobalt cobalt) {
		super(cobalt, "Teleport");
		Command theCommand = new Command() {
			@Override
			public void onCommand(String... args) {
				if (args.length == 3) {
					try {
						double x = Double.parseDouble(args[0]);
						double y = Double.parseDouble(args[1]) + 2;
						double z = Double.parseDouble(args[2]);
						mc.thePlayer.setPosition(x, y, z);
						return;
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				return;
			}
		};

		cobalt.commandManager.registerCommand("tp", theCommand);
	}
}