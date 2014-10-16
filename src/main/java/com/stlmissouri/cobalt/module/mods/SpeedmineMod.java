package com.stlmissouri.cobalt.module.mods;

import com.darkmagician6.eventapi.EventTarget;
import com.google.gson.reflect.TypeToken;
import com.stlmissouri.cobalt.Cobalt;
import com.stlmissouri.cobalt.command.Command;
import com.stlmissouri.cobalt.events.block.BlockDamageEvent;
import com.stlmissouri.cobalt.module.CobaltModule;
import com.stlmissouri.cobalt.preferences.PreferencesSerializable;
import org.lwjgl.input.Keyboard;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * User: Stl
 * Date: 2/22/14
 * Time: 11:33 PM
 * Use:  Increases mining speed
 */
public class SpeedmineMod extends CobaltModule implements PreferencesSerializable<Map<String, Object>> {

    private float factor = 1.05F;
    private int delay = 3;

    public SpeedmineMod(final Cobalt cobalt) {
        super(cobalt, "Speedmine");
        this.updateKeybind(Keyboard.KEY_V);
        this.cobalt.preferencesManager.registerPrefs(this.getName(), this);

        this.cobalt.commandManager.registerCommand("speedminefactor", new Command() {
            @Override
            public void onCommand(String... args) {
                if (args.length < 1) {
                    SpeedmineMod.this.cobalt.displayChat("Speedmine factor: %.2f", SpeedmineMod.this.factor);
                    return;
                }
                try {
                    SpeedmineMod.this.factor = Float.parseFloat(args[0]);
                } catch (NumberFormatException e) {
                    SpeedmineMod.this.cobalt.displayChat("Invalid factor: %s", args[0]);
                    return;
                }
                SpeedmineMod.this.cobalt.displayChat("Speedmine factor set to: %.2f", SpeedmineMod.this.factor);
            }
        });
        this.cobalt.commandManager.registerCommand("speedminedelay", new Command() {
            @Override
            public void onCommand(String... args) {
                if (args.length < 1) {
                    SpeedmineMod.this.cobalt.displayChat("Speedmine delay: %d", SpeedmineMod.this.delay);
                    return;
                }
                try {
                    SpeedmineMod.this.delay = Integer.parseInt(args[0]);
                } catch (NumberFormatException e) {
                    SpeedmineMod.this.cobalt.displayChat("Invalid delay: %s", args[0]);
                    return;
                }
                SpeedmineMod.this.cobalt.displayChat("Speedmine delay set to: %d", SpeedmineMod.this.delay);
            }
        });
        this.cobalt.commandManager.registerAlias("speedminefactor", "sf");
        this.cobalt.commandManager.registerAlias("speedminedelay", "sd");
    }

    @EventTarget
    public void speedBreak(BlockDamageEvent event) {
        if (!this.mc.thePlayer.canHarvestBlock(this.mc.theWorld.getBlock(event.getX(), event.getY(), event.getZ())))
            return;
        event.setDamage(event.getDamage() * this.factor);
        if (event.getDelay() > this.delay)
            event.setDelay(this.delay);
    }

    @Override
    public Map<String, Object> getSaveObject() {
        Map<String, Object> toSave = new HashMap<>();
        toSave.put("factor", this.factor);
        toSave.put("delay", this.delay);
        return toSave;
    }

    @Override
    public void load(Map<String, Object> savedObject) {
        this.factor = ((Double)savedObject.get("factor")).floatValue();
        this.delay  = ((Double)savedObject.get("delay")).intValue();
    }

    @Override
    public Type getType() {
        return new TypeToken<Map<String, Object>>(){}.getType();
    }
}