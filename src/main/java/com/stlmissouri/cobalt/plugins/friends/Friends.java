package com.stlmissouri.cobalt.plugins.friends;

import com.stlmissouri.cobalt.Cobalt;
import com.stlmissouri.cobalt.api.CobaltPlugin;
import com.stlmissouri.cobalt.command.Command;

/**
 * User: Stl
 * Date: 6/13/2014
 * Time: 5:25 PM
 * Use:  "Friend" management plugin
 */
public class Friends extends CobaltPlugin {

    private PlayerManager playerManager = null;

    public Friends(Cobalt cobalt) {
        super(cobalt);
    }

    @Override
    public void load(final Cobalt cobalt) {
        this.playerManager = new PlayerManager(cobalt);
        cobalt.commandManager.registerCommand("user", new Command() {
            @Override
            public void onCommand(String... args) {
                cobalt.displayChat("Invalid arguments, try \"user help\"");
            }
        }.registerSubcommand("add", new Command() {
            @Override
            public void onCommand(String... args) {
                if (args.length < 1) {
                    cobalt.displayChat("Usage: \"user add <username> [alias]\"");
                    return;
                }
                String player = args[0].toLowerCase();
                String alias = args.length > 1 ? args[1] : null;
                PlayerProperties props = playerManager.getProperties(player);
                props.setFriend(true);
                props.setDisplayName(alias);
                props.setColor(-1);
                cobalt.displayChat("User %s added to friends as %s", props.getUsername(), props.getDisplayName());
            }
        }).registerSubcommand("delete", new Command() {
            @Override
            public void onCommand(String... args) {
                if (args.length < 1) {
                    cobalt.displayChat("Usage: \"user delete <username>\"");
                    return;
                }
                String player = args[0].toLowerCase();
                PlayerProperties props = playerManager.getProperties(player);
                props.setFriend(false);
                props.setDisplayName(null);
                props.setColor(-1);
                cobalt.displayChat("User %s removed from friends", props.getUsername());
            }
        }).registerSubcommand("color", new Command() {
            @Override
            public void onCommand(String... args) {
                if (args.length < 1) {
                    cobalt.displayChat("Usage: \"user color <username> [hex color]\"");
                    return;
                }
                PlayerProperties props = playerManager.getProperties(args[0].toLowerCase());
                int color = props.getColor();
                if (args[1].startsWith("#"))
                    args[1] = args[1].substring(1);
                if (args.length > 1) {
                    try {
                        color = args[1].equalsIgnoreCase("reset") ? -1 : Integer.parseInt(args[1], 16);
                    } catch (NumberFormatException nfe) {
                        cobalt.displayChat("Unable to parse color: " + args[1]);
                        return;
                    }
                }
                props.setColor(color);
                cobalt.displayChat("Player: %s color: %s", props.getUsername(), Integer.toHexString(props.getColor()));
            }
        }).registerSubcommand("help", new Command() {
            @Override
            public void onCommand(String... args) {
                cobalt.displayChat("User management help:");
                cobalt.displayChat("Usage: user [sub-command]");
                cobalt.displayChat("Available sub-commands:");
                cobalt.displayChat(" help - displays this help message");
                cobalt.displayChat(" add <username> [alias] - adds a user to friends");
                cobalt.displayChat(" delete <username> - removes a user from friends");
                cobalt.displayChat(" color <#rrggbb [reset]> - sets a user's color");
            }
        }).registerSubcommand("alias", new Command() {
            @Override
            public void onCommand(String... args) {
                if (args.length < 1) {
                    cobalt.displayChat("Usage: \"user alias <username> [alias]\"");
                    return;
                }
                PlayerProperties props = playerManager.getProperties(args[0].toLowerCase());
                if (args.length > 1) {
                    String alias = args[1];
                    props.setDisplayName(alias);
                }
                cobalt.displayChat("Player: %s alias: %s", props.getUsername(), props.getDisplayName());
            }
        }));
    }

    public PlayerManager getPlayerManager() {
        return this.playerManager;
    }

    @Override
    public void unload() {
        this.playerManager = null;
    }

    @Override
    public String getName() {
        return "Friends";
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
