package com.stlmissouri.cobalt.plugins.pluginlist;

import com.darkmagician6.eventapi.EventTarget;
import com.stlmissouri.cobalt.Cobalt;
import com.stlmissouri.cobalt.command.Command;
import com.stlmissouri.cobalt.events.packet.PacketReceiveEvent;
import com.stlmissouri.cobalt.util.ChatColor;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.network.play.client.C14PacketTabComplete;
import net.minecraft.network.play.server.S3APacketTabComplete;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Stl
 * Date: 5/17/2014
 * Time: 2:15 AM
 * Use:
 */
public class PluginLister {

    private final Cobalt cobalt;

    public PluginLister(Cobalt cobalt) {
        this.cobalt = cobalt;
        this.cobalt.commandManager.registerCommand("pluginlist", new Command() {
            @Override
            public void onCommand(String... args) {
                PluginLister.this.cobalt.mc.getNetHandler().addToSendQueue(new C14PacketTabComplete("/"));
                PluginLister.this.cobalt.displayChat("Attempting to list plugins...");
            }
        });
        this.cobalt.commandManager.registerAlias("pluginlist", "plist");
        this.cobalt.commandManager.registerAlias("pluginlist", "listpl");
    }

    @EventTarget
    public void onPacket(PacketReceiveEvent event) {
        if (!event.ofType(S3APacketTabComplete.class))
            return;
        if (this.cobalt.mc.currentScreen instanceof GuiChat)
            return;
        S3APacketTabComplete packet = (S3APacketTabComplete) event.getPacket();
        List<String> plugins = new ArrayList<>();
        for (String s : packet.func_149630_c()) {
            if (s.startsWith("/")) {
                s = s.substring(1);
                String[] explode = s.split(":");
                if (explode.length < 2)
                    continue;
                String pl = ChatColor.GREEN + WordUtils.capitalize(explode[0]);
                if (!plugins.contains(pl))
                    plugins.add(pl);
            }
        }
        if (plugins.size() < 1)
            return;
        this.cobalt.displayChat("Plugins (%d): %s", plugins.size(), StringUtils.join(plugins, ChatColor.RESET + ", "));
        event.setCancelled(true);
    }

}
