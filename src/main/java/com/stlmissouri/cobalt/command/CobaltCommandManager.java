package com.stlmissouri.cobalt.command;

import com.darkmagician6.eventapi.EventManager;
import com.darkmagician6.eventapi.EventTarget;
import com.stlmissouri.cobalt.Cobalt;
import com.stlmissouri.cobalt.events.mod.ChatSendEvent;
import com.stlmissouri.cobalt.info.*;
import com.stlmissouri.cobalt.util.CobaltManager;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: Stl
 * Date: 2/26/14
 * Time: 8:20 PM
 * Use:  Manages commands
 */
public class CobaltCommandManager implements CobaltManager {

    private final Minecraft minecraft;
    private final Cobalt cobalt;

    private final Map<String, Command> commands;
    private final Map<String, String> aliases;

    public CobaltCommandManager(Minecraft minecraft, Cobalt cobalt) {
        this.minecraft = minecraft;
        this.cobalt = cobalt;
        this.commands = new HashMap<String, Command>();
        this.aliases = new HashMap<String, String>();
        EventManager.register(this);
    }

    @EventTarget
    public void onChatSendEvent(ChatSendEvent e) {
        String msg = e.getMessage();
        if (!msg.startsWith("."))
            return;
        e.setCancelled(true);
        runCommand(msg);
    }

    public void runCommand(String msg) {
        try {
            String[] split = msg.substring(1).split(" ");
            String cmd = split[0].toLowerCase();
            String[] args = new String[split.length - 1];
            if (split.length > 1) {
                args = new String[split.length - 1];
                System.arraycopy(split, 1, args, 0, args.length);
            }
            if (!(this.commands.containsKey(cmd) || this.aliases.containsKey(cmd))) {
                cobalt.displayChat("Unknown command: " + cmd);
                return;
            }
            if (!this.commands.containsKey(cmd))
                cmd = aliases.get(cmd);
            Command c = commands.get(cmd);
            c.runCommand(args);
        } catch (Exception e) { //Catching all exceptions because Minecraft already seems to but doesn't report them for some reason
            e.printStackTrace();
            throw e;
        }
    }

    public void registerCommand(String identifier, Command command) {
        this.commands.put(identifier.toLowerCase(), command);
    }

    public void unregisterCommand(String identifier) {
        this.commands.remove(identifier.toLowerCase());
    }

    public boolean hasCommand(String identifier) {
        return this.commands.containsKey(identifier.toLowerCase());
    }

    /**
     * Unregisters a command alias.
     * This may be confusing because the first parameter is the map value and the second is the key
     * @param identifier    The original command identifier
     * @param alias         Alias to add to the identifier
     */
    public void registerAlias(String identifier, String alias) {
        if(!this.commands.containsKey(identifier.toLowerCase())) {
            System.err.println("Command manager: could not register alias \"" + alias + "\" to command \"" + identifier + ".\" The specified identifier does not exist.");
            return;
        }
        this.aliases.put(alias.toLowerCase(), identifier.toLowerCase());
    }

    public void unregisterAlias(String alias) {
        this.aliases.remove(alias.toLowerCase());
    }

    @Override
    public void updateInfo(InfoSet infoSet) {
        infoSet.removeNode("cobalt.commands.manager.info");
        ParentInfoNode parent = new ParentInfoNode("Commands");
        for (String command : this.commands.keySet()) {
            List<String> aliases = new ArrayList<String>();
            for (String alias : this.aliases.keySet()) {
                if(this.aliases.get(alias).equalsIgnoreCase(command)) {
                    aliases.add(alias);
                }
            }
            InfoNode component;
            if(aliases.size() > 0) {
                component = new ParentInfoNode(command);
                for (String alias : aliases) {
                    ((ParentInfoNode) component).addChild(new BasicInfoNode(alias));
                }
            } else {
                component = new ParentInfoNode(command);
            }
            parent.addChild(component);
        }
        infoSet.addNode("cobalt.commands.manager.info", parent);
    }

}