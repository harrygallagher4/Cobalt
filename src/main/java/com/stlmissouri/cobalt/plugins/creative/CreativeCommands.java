package com.stlmissouri.cobalt.plugins.creative;

import com.stlmissouri.cobalt.Cobalt;
import com.stlmissouri.cobalt.api.CobaltPlugin;
import com.stlmissouri.cobalt.command.Command;
import com.stlmissouri.cobalt.util.ItemParser;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C10PacketCreativeInventoryAction;
import org.apache.commons.lang3.StringUtils;

/**
 * User: Stl
 * Date: 5/24/2014
 * Time: 11:59 PM
 * Use:
 */
public class CreativeCommands extends CobaltPlugin {

    private Cobalt cobalt;

    public CreativeCommands(Cobalt cobalt) {
        super(cobalt);
    }

    @Override
    public void load(final Cobalt cobalt) {
        this.cobalt = cobalt;
        cobalt.commandManager.registerCommand("give", new Command() {
            @Override
            public void onCommand(String... args) {
                if (!CreativeCommands.this.cobalt.mc.playerController.isInCreativeMode()) {
                    CreativeCommands.this.cobalt.displayChat("You must be in creative mode to use that command!");
                }
                ItemStack stack = ItemParser.parseStack(ItemParser.formatDisplayName(StringUtils.join(args, " ")));
                CreativeCommands.this.cobalt.mc.getNetHandler().addToSendQueue(new C10PacketCreativeInventoryAction(-999, stack));
            }
        });
        cobalt.commandManager.registerAlias("give", "i");
    }

    @Override
    public void unload() {

    }

    @Override
    public String getName() {
        return "Creative Commands";
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public double getPluginVersion() {
        return 1;
    }

}
