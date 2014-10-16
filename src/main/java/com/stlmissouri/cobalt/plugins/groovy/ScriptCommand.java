package com.stlmissouri.cobalt.plugins.groovy;

import com.stlmissouri.cobalt.Cobalt;
import com.stlmissouri.cobalt.command.Command;

import javax.script.ScriptException;
import java.io.File;
import java.io.IOException;

/**
 * User: Stl
 * Date: 5/4/2014
 * Time: 1:32 PM
 * Use:
 */
public class ScriptCommand extends Command {

    private final Cobalt cobalt;
    private final File scriptsDir;

    public ScriptCommand(Cobalt cobalt) {
        this.cobalt = cobalt;
        scriptsDir = new File(cobalt.ioManager.COBALT_DIR, "scripts");
    }

    @Override
    public void onCommand(String... args) {
        if(args.length < 1) {
            return;
        }
        if(args[0].equalsIgnoreCase("load") && args[1] != null) {
            try {
                ScriptContextPair script = GroovyLoader.load(new File(scriptsDir, args[1] + ".groovy"));
                if(script != null)
                    cobalt.displayChat("Loaded script: " + args[1]);
            } catch (IOException | ScriptException e) {
                cobalt.displayChat(e.getMessage());
                e.printStackTrace();
            }
        }
        return;
    }
}
