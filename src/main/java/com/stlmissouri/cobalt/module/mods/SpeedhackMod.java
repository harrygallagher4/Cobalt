package com.stlmissouri.cobalt.module.mods;

import com.darkmagician6.eventapi.EventTarget;
import com.stlmissouri.cobalt.Cobalt;
import com.stlmissouri.cobalt.command.Command;
import com.stlmissouri.cobalt.events.motion.PreMotionEvent;
import com.stlmissouri.cobalt.module.CobaltModule;
import net.minecraft.entity.player.EntityPlayer;

/**
 * User: Stl
 * Date: 7/9/2014
 * Time: 5:40 PM
 * Use:
 */
public class SpeedhackMod extends CobaltModule {

    private int tick = 0;
    private int skip = 0;
    private double speed = 3.8D;
    private double[] skippedProps = new double[4];

    public SpeedhackMod(Cobalt cobalt) {
        super(cobalt, "Speedhack");
        this.cobalt.commandManager.registerCommand("sf", new Command() {
            @Override
            public void onCommand(String... args) {
                if (args.length < 1)
                    return;
                try {
                    speed = Double.parseDouble(args[0]);
                } catch (NumberFormatException nfe) {
                    SpeedhackMod.this.cobalt.displayChat("Unable to parse value: " + args[0]);
                    return;
                }
                SpeedhackMod.this.cobalt.displayChat("Speedhack speed set to: " + Double.toString(speed));
            }
        });
    }

    @EventTarget
    public void onUpdate(PreMotionEvent event) {
        if (this.skip > 0) {
            this.skip--;
            return;
        }
        if (event.getPlayer().isInWater() || !event.getPlayer().onGround) {
            this.skip = 2;
            return;
        }
        if (isStandingStill(event.getPlayer()))
            return;
        tick++;
        tick %= 2;
        if (tick != 0)
            return;
        movePlayer(event.getPlayer());
    }

    private boolean isStandingStill(EntityPlayer entity) {
        return Math.abs(entity.motionX) <= 0.01D && Math.abs(entity.motionZ) <= 0.01D;
    }

    private void movePlayer(EntityPlayer player) {
        double dX = player.motionX * this.speed;
        double dZ = player.motionZ * this.speed;
        player.posX += dX;
        player.posZ += dZ;
        player.posY += 0.01D;
        player.boundingBox.minY += 0.01D;
        player.boundingBox.offset(dX, 0.01D, dZ);
    }

}
