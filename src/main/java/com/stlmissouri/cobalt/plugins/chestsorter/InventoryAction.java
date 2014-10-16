package com.stlmissouri.cobalt.plugins.chestsorter;

import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.entity.player.EntityPlayer;

/**
 * User: Stl
 * Date: 9/18/13
 * Time: 10:30 PM
 * Use:  dick nuggets
 * Help: http://wiki.vg/Protocol#Click_Window_.280x66.29 | http://wiki.vg/Inventory#Windows
 */
public class InventoryAction {

    private final int
    windowID,
    slot,
    button,
    mode;

    /**
     * Creates a new window click which can be executed later
     * @param windowID  the window id
     * @param slot      slot to click
     * @param button    button used
     * @param mode      action mode
     */
    public InventoryAction(int windowID, int slot, int button, int mode) {
        this.windowID = windowID;
        this.slot = slot;
        this.button = button;
        this.mode = mode;
    }

    /**
     * Performs the inventory action
     * @param playerController  A PlayerControllerMP object to use for the action
     * @param player            A player object
     */
    public void perform(PlayerControllerMP playerController, EntityPlayer player) {
        playerController.windowClick(windowID, slot, button, mode, player);
    }

}
