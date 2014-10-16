package com.stlmissouri.cobalt.util;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;

import java.util.UUID;

/**
 * User: Stl
 * Date: 5/24/2014
 * Time: 9:44 PM
 * Use:
 */
public class ItemParser {

    public static ItemStack parseStack(String from) {
        String[] split = from.split(" ");
        String idOrName = split[0].toLowerCase();
        Integer damage = null;
        String data = null;
        if (split.length > 1)
            data = from.substring(idOrName.length() + 1);
        if (idOrName.split(":").length > 1) {
            String[] itemSplit = idOrName.split(":");
            try {
                damage = Integer.parseInt(itemSplit[1]);
            } catch (NumberFormatException ignored) {}
            idOrName = itemSplit[0];
        }
        Item item = null;
        try {
            int id = Integer.parseInt(idOrName);
            if (Item.itemRegistry.containsID(id))
                item = (Item) Item.itemRegistry.getObjectForID(id);
        } catch (NumberFormatException nfe) {
            if (Item.itemRegistry.containsKey(idOrName))
                item = (Item) Item.itemRegistry.getObject(idOrName);
        }
        if (item == null)
            return null;
        ItemStack stack = new ItemStack(item);
        if (damage != null)
            stack.setItemDamage(damage);
        if (data != null) {
            try {
                NBTBase nbt = JsonToNBT.func_150315_a(data);
                if ((nbt instanceof NBTTagCompound))
                    stack.setTagCompound((NBTTagCompound) nbt);
            } catch (NBTException e) {
                e.printStackTrace();
            }
        }
        return stack;
    }

    public static String formatDisplayName (String name) {
        while (name.contains("%randcol%")) {
            name = name.replaceFirst("%randcol%", ChatColor.randomColor().toString());
        }
        while (name.contains("%randform%")) {
            name = name.replaceFirst("%randform%", ChatColor.randomFormat().toString());
        }
        while (name.contains("%randstr%")) {
            name = name.replaceFirst("%randstr%", UUID.randomUUID().toString());
        }
        name = ChatColor.translateColorCodes(name, "&");
        return name;
    }

}
