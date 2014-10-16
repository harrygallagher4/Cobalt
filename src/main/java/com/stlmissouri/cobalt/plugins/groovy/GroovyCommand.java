package com.stlmissouri.cobalt.plugins.groovy;

import com.stlmissouri.cobalt.Cobalt;
import com.stlmissouri.cobalt.command.Command;

import javax.script.Bindings;
import javax.script.ScriptException;

/**
 * User: Stl
 * Date: 5/4/2014
 * Time: 1:11 PM
 * Use:
 */
public class GroovyCommand extends Command {

    private final Cobalt cobalt;

    public GroovyCommand(Cobalt cobalt) {
        this.cobalt = cobalt;
    }

    @Override
    public void onCommand(String... args) {
        if (args.length < 0)
            return;
        ScriptContextPair script = GroovyLoader.get(args[0]);
        if(script == null) {
            cobalt.displayChat("Script not found: " + args[0]);
            return;
        }
        Object[] $args = new Object[args.length - 1];
        System.arraycopy(args, 1, $args, 0, $args.length);
        Bindings bindings = script.getBindings(100);
        bindings.put("me", cobalt.mc.thePlayer);
        bindings.put("world", cobalt.mc.theWorld);
        bindings.put("args", $args);
        try {
            Object result = script.eval();
            if(result != null)
                cobalt.displayChat(result.toString());
        } catch (ScriptException e) {
            cobalt.displayChat(e.getMessage());
        }
        return;
    }
}
