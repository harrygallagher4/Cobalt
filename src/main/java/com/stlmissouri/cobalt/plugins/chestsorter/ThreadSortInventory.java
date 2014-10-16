package com.stlmissouri.cobalt.plugins.chestsorter;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * User: Stl
 * Date: 9/18/13
 * Time: 11:02 PM
 * Use:  Sorts the player's inventory
 */
public class ThreadSortInventory implements Runnable {

    private final Minecraft mc;

    private final int windowID;

    private final Container container;

    private List<ItemStack> inventory;
    private List<ItemStack> currentLocations = new ArrayList<ItemStack>();
    private List<ItemStack> finalLocations   = new ArrayList<ItemStack>();
    private List<InventoryAction> clickQueue = new ArrayList<InventoryAction>();


    public ThreadSortInventory(Container container, Minecraft mc) {
        this.inventory = container.getInventory();
        this.windowID = container.windowId;
        this.container = container;
        this.mc = mc;
    }

    @Override
    public void run() {
        for(int i = 0; i < inventory.size() - 36; i++) {
            ItemStack stack = inventory.get(i);
            currentLocations.add(i, stack);
            finalLocations.add(i, stack);
        }
        sortStacks();
        for(ItemStack stack : finalLocations) {
            int i = finalLocations.indexOf(stack);
            if(stack == null)
                continue;
            int currentSlot = currentLocations.indexOf(stack);
            if(currentSlot < 0) {
                System.out.println("Could not find " + stack.getDisplayName());
                continue;
            }
            int finalSlot = i;
            if(currentSlot == finalSlot)
                continue;
            ItemStack currentAtFinal = currentLocations.get(finalSlot);
            ItemStack currentAtCurrent = currentLocations.get(currentSlot);
            InventoryAction select = new InventoryAction(windowID, currentSlot, 0, 0);
            InventoryAction move   = new InventoryAction(windowID, finalSlot, 0, 0);
            InventoryAction replace = new InventoryAction(windowID, currentSlot, 0, 0);
            currentLocations.set(finalSlot, currentAtCurrent);
            currentLocations.set(currentSlot, currentAtFinal);
            clickQueue.add(select);
            clickQueue.add(move);
            clickQueue.add(replace);
        }
        for(InventoryAction action : clickQueue) {
            action.perform(mc.playerController, mc.thePlayer);
        }
    }

    private void sortStacks() {
        Collections.sort(finalLocations, new StackComparator());
    }

    private class StackComparator implements Comparator<ItemStack> {

        @Override
        public int compare(ItemStack o1, ItemStack o2) {
            if(o2 == null && o1 != null) {
                return -1;
            } else if (o1 == null && o2 != null) {
                return 1;
            } else if (o1 == null) {
                return 0;
            } else {
                if(isItem(o1) && !isItem(o2))
                    return 1;
                if(isItem(o2) && !isItem(o1))
                    return -1;
                int i = o1.getDisplayName().compareToIgnoreCase(o2.getDisplayName());
                if(i == 0) {
                    if(o1.getItemDamage() < o2.getItemDamage())
                        return -1;
                    if(o2.getItemDamage() < o1.getItemDamage())
                        return 1;
                }
                return i;
            }
        }

        private boolean isItem(ItemStack stack) {
            return Block.getBlockFromItem(stack.getItem()) == null;
        }

    }

}
