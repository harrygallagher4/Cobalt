package com.stlmissouri.cobalt.module.mods;

import com.darkmagician6.eventapi.EventTarget;
import com.stlmissouri.cobalt.Cobalt;
import com.stlmissouri.cobalt.command.Command;
import com.stlmissouri.cobalt.events.mod.TickEvent;
import com.stlmissouri.cobalt.module.CobaltModule;
import com.stlmissouri.cobalt.util.ChatColor;
import com.stlmissouri.cobalt.util.ItemParser;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C10PacketCreativeInventoryAction;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.stlmissouri.cobalt.module.mods.Dropper.DropperType.ID;
import static com.stlmissouri.cobalt.module.mods.Dropper.DropperType.RANDOM;

/**
 * User: Stl
 * Date: 5/21/2014
 * Time: 10:04 PM
 * Use:
 */
public class Dropper extends CobaltModule {

    private final Random rng = new Random();
    private final List<Object> itemKeys = new ArrayList<>();
    private DropperType type = RANDOM;
    private ItemStack idItem;

    public Dropper(Cobalt cobalt) {
        super(cobalt, "Dropper");
        this.idItem = new ItemStack(Blocks.stone);
        this.itemKeys.clear();
        for (Object o : Item.itemRegistry.getKeys()) {
            this.itemKeys.add(o);
        }
        this.cobalt.commandManager.registerCommand("ditem", this.dropperCommand);
    }

    @EventTarget
    public void onTick(TickEvent event) {
        ItemStack item = this.getNextItem();
        if (item == null)
            return;
        item.setStackDisplayName(ItemParser.formatDisplayName(item.getDisplayName()));
        mc.getNetHandler().addToSendQueue(new C10PacketCreativeInventoryAction(36 + this.mc.thePlayer.inventory.currentItem, item));
        mc.thePlayer.dropOneItem(false);
    }

    private String randomColor() {
        return ChatColor.values()[rng.nextInt(ChatColor.values().length - 1)].toString();
    }

    private ItemStack getNextItem() {
        switch (this.type) {
            case RANDOM: {
                return this.randomItem();
            }
            case ID: {
                return ItemStack.copyItemStack(this.idItem);
            }
        }
        return null;
    }

    private ItemStack randomItem() {
        try {
            return new ItemStack((Item) Item.itemRegistry.getObject(this.itemKeys.get(this.rng.nextInt(this.itemKeys.size() - 1))));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private final Command dropperCommand = new Command() {
        @Override
        public void onCommand(String... args) {
            if (args.length < 1) {
                cobalt.displayChat("Missing arguments for command!");
                return;
            }
            switch (args[0].toLowerCase()) {
                case "random" : {
                    Dropper.this.type = RANDOM;
                    break;
                }
                case "id" : {
                    Dropper.this.type = ID;
                    if (args.length < 2 ) {
                        cobalt.displayChat("Dropper will now drop a single item: %s", Dropper.this.idItem.getDisplayName());
                        return;
                    }
                    String[] sub = new String[args.length - 1];
                    System.arraycopy(args, 1, sub, 0, sub.length);
                    String itemString = StringUtils.join(sub, ' ');
                    ItemStack stack = ItemParser.parseStack(itemString);
                    Dropper.this.idItem = stack;
                    break;
                }
            }
        }
    };

    protected enum DropperType {
        RANDOM, ID
    }

}
