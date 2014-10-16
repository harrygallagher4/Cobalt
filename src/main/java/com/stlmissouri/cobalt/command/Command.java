package com.stlmissouri.cobalt.command;

import java.util.HashMap;
import java.util.Map;

/**
 * User: Stl
 * Date: 2/26/14
 * Time: 8:22 PM
 * Use:
 */
public abstract class Command {

    private Map<String, Command> subcommands = new HashMap<>();

    public void runCommand(String... args) {
        if (args.length < 1) {
            this.onCommand(args);
            return;
        }
        if (this.subcommands.containsKey(args[0].toLowerCase())) {
            String[] newArgs = new String[args.length - 1];
            if (newArgs.length > 0)
                System.arraycopy(args, 1, newArgs, 0, newArgs.length);
            this.subcommands.get(args[0].toLowerCase()).runCommand(newArgs);
        } else {
            this.onCommand(args);
        }
    }

    public abstract void onCommand(String... args);

    public Command registerSubcommand(String identifier, Command command) {
        identifier = identifier.toLowerCase();
        subcommands.put(identifier, command);
        return this;
    }

    public void clearSubcommand(String identifier) {
        this.subcommands.remove(identifier);
    }

}