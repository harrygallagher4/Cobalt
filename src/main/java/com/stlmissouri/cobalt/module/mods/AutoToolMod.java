package com.stlmissouri.cobalt.module.mods;

import com.darkmagician6.eventapi.EventTarget;
import com.stlmissouri.cobalt.Cobalt;
import com.stlmissouri.cobalt.events.block.BlockClickEvent;
import com.stlmissouri.cobalt.module.CobaltModule;
import com.stlmissouri.cobalt.plugins.modlist.ModList;
import com.stlmissouri.cobalt.plugins.modlist.properties.ModListProperties;
import net.minecraft.block.Block;

/**
 * User: Stl
 * Date: 5/12/2014
 * Time: 2:34 PM
 * Use:  Automatically finds the best tool when a block is clicked
 */
public class AutoToolMod extends CobaltModule {

    public AutoToolMod(Cobalt cobalt) {
        super(cobalt, "AutoTool");
        this.cobalt.commandManager.registerAlias("autotool", "at");
        if (this.cobalt.pluginManager.hasPlugin(ModList.class)) {
            ModList modList = this.cobalt.pluginManager.getPlugin(ModList.class);
            ModListProperties props = modList.getRenderer().getProperties(this);
            props.setTag("Auto Tool");
            props.setColor(0xFF0000);
        }
    }

    @EventTarget
    public void onBlockClicked(BlockClickEvent event) {
        if (event.getGameType().isCreative() || event.getGameType().isAdventure())
            return;
        Block block = this.mc.theWorld.getBlock(event.getX(), event.getY(), event.getZ());
        float curDmg = block.getPlayerRelativeBlockHardness(this.mc.thePlayer, this.mc.theWorld, event.getX(), event.getY(), event.getZ());
        int curSlot = this.mc.thePlayer.inventory.currentItem;
        for (int i = 0; i < 9; i++) {
            this.mc.thePlayer.inventory.currentItem = i;
            if (this.mc.thePlayer.getHeldItem() == null)
                continue;
            float dmg = block.getPlayerRelativeBlockHardness(this.mc.thePlayer, this.mc.theWorld, event.getX(), event.getY(), event.getZ());
            if (dmg > curDmg) {
                curSlot = i;
                curDmg = dmg;
            }
        }
        this.mc.thePlayer.inventory.currentItem = curSlot;
        this.mc.playerController.updateController();
    }

}
