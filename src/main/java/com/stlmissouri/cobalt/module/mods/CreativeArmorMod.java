package com.stlmissouri.cobalt.module.mods;

import com.darkmagician6.eventapi.EventTarget;
import com.google.common.reflect.TypeToken;
import com.stlmissouri.cobalt.Cobalt;
import com.stlmissouri.cobalt.command.Command;
import com.stlmissouri.cobalt.events.motion.PreMotionEvent;
import com.stlmissouri.cobalt.module.CobaltModule;
import com.stlmissouri.cobalt.preferences.PreferencesSerializable;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.client.C10PacketCreativeInventoryAction;

import java.lang.reflect.Type;

/**
 * User: Stl
 * Date: 5/18/2014
 * Time: 11:39 PM
 * Use:  gives you infinite amounts of swag in creative mode
 */

public class CreativeArmorMod extends CobaltModule implements PreferencesSerializable<int[]> {

    private final ItemStack helm, chest, pants, boots;
    private int helmIndex = 0, chestIndex = 1, pantsIndex = 2, bootsIndex = 3;
    private int tick = 0;
    private int delay = 5;
    private int[] colors = new int[]{ 0x02FFC0, 0xFF9200, 0xFF1180, 0x6743FF };

    public CreativeArmorMod(Cobalt cobalt) {
        super(cobalt, "ARMOR $WAG");
        this.cobalt.commandManager.registerAlias("armor$wag", "ac");
        this.helm = createItemWithNBT("leather_helmet");
        this.chest = createItemWithNBT("leather_chestplate");
        this.pants = createItemWithNBT("leather_leggings");
        this.boots = createItemWithNBT("leather_boots");
        this.cobalt.preferencesManager.registerPrefs("armormod", this);

        this.cobalt.commandManager.registerCommand("acset", new Command() {
            @Override
            public void onCommand(String... args) {
                if (args.length < 1)
                    return;
                int[] cols = new int[args.length];
                for (int i = 0; i < args.length; i++) {
                    cols[i] = Integer.parseInt(args[i], 16);
                }
                CreativeArmorMod.this.colors = cols;
                initIndex();
                CreativeArmorMod.this.cobalt.displayChat("You've got a fancy new armor set!");
            }
        });
        this.cobalt.commandManager.registerCommand("acdelay", new Command() {
            @Override
            public void onCommand(String... args) {
                if(args.length < 1)
                    return;
                try {
                    CreativeArmorMod.this.delay = Integer.parseInt(args[0]);
                } catch (NumberFormatException nfe) {
                    CreativeArmorMod.this.cobalt.displayChat("%s ain't no number I ever heard of!", args[0]);
                }
                CreativeArmorMod.this.cobalt.displayChat("Your armor changes swag every %s seconds", args[0]);
            }
        });
    }

    private void initIndex() {
        this.helmIndex = 0;
        this.chestIndex = 1;
        this.pantsIndex = 2;
        this.bootsIndex = 3;
        this.helmIndex %= this.colors.length;
        this.chestIndex %= this.colors.length;
        this.pantsIndex %= this.colors.length;
        this.bootsIndex %= this.colors.length;
    }

    private ItemStack createItemWithNBT(String key) {
        ItemStack stack = new ItemStack((Item) Item.itemRegistry.getObject(key));
        NBTTagCompound compound = new NBTTagCompound();
        compound.setTag("display", new NBTTagCompound());
        stack.setTagCompound(compound);
        return stack;
    }

    private void setColor(ItemStack stack, int color) {
        stack.getTagCompound().getCompoundTag("display").setInteger("color", color);
    }

    @EventTarget
    public void onTick(PreMotionEvent event) {
        if (!this.mc.playerController.isInCreativeMode())
            return;
        this.tick++;
        this.tick %= 5;
        if(this.tick != 0)
            return;
        this.setColor(this.helm, this.colors[this.helmIndex]);
        this.setColor(this.chest, this.colors[this.chestIndex]);
        this.setColor(this.pants, this.colors[this.pantsIndex]);
        this.setColor(this.boots, this.colors[this.bootsIndex]);
        this.mc.getNetHandler().addToSendQueue(new C10PacketCreativeInventoryAction(8, this.boots));
        this.mc.getNetHandler().addToSendQueue(new C10PacketCreativeInventoryAction(7, this.pants));
        this.mc.getNetHandler().addToSendQueue(new C10PacketCreativeInventoryAction(6, this.chest));
        this.mc.getNetHandler().addToSendQueue(new C10PacketCreativeInventoryAction(5, this.helm));
        this.helmIndex++;
        this.helmIndex %= this.colors.length;
        this.chestIndex++;
        this.chestIndex %= this.colors.length;
        this.pantsIndex++;
        this.pantsIndex %= this.colors.length;
        this.bootsIndex++;
        this.bootsIndex %= this.colors.length;
    }

    @Override
    public int[] getSaveObject() {
        return this.colors;
    }

    @Override
    public void load(int[] savedObject) {
        this.colors = savedObject;
    }

    @Override
    public Type getType() {
        return new TypeToken<int[]>(){}.getType();
    }
}
