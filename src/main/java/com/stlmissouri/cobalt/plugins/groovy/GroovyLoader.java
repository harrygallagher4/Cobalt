package com.stlmissouri.cobalt.plugins.groovy;

import org.apache.commons.io.FileUtils;
import org.codehaus.groovy.jsr223.GroovyScriptEngineImpl;

import javax.script.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class GroovyLoader {

    private static final GroovyScriptEngineImpl SCRIPT_ENGINE;
    private static final Bindings GLOBAL_BINDINGS = new SimpleBindings();

    private static final Map<String, ScriptContextPair> loadedScripts = new HashMap<>();

    static {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine groovy = manager.getEngineByName("groovy");
        if (!(groovy instanceof GroovyScriptEngineImpl))
            SCRIPT_ENGINE = null;
        else
            SCRIPT_ENGINE = (GroovyScriptEngineImpl) groovy;
    }

    public static ScriptContextPair get(String name) {
        name = name.toLowerCase();
        if(!loadedScripts.containsKey(name))
            return null;
        return loadedScripts.get(name);
    }

    public static Set<String> getAll() {
        return loadedScripts.keySet();
    }

    public static ScriptContextPair load(File file) throws IOException, ScriptException {
        ScriptContextPair script = getCompiledPair(file);
        loadedScripts.put(stripExtension(file.getName().toLowerCase()), script);
        return script;
    }

    public static ScriptContextPair getCompiledPair(File file) throws IOException, ScriptException {
        return new ScriptContextPair(compileGroovyScript(file), buildScriptContext());
    }

    public static CompiledScript compileGroovyScript(File file) throws IOException, ScriptException {
        InputStream stream = FileUtils.openInputStream(file);
        CompiledScript script = SCRIPT_ENGINE.compile(new InputStreamReader(stream));
        return script;
    }

    public static ScriptContext buildScriptContext() {
        ScriptContext context = new SimpleScriptContext();
        Bindings bindings = new SimpleBindings();
        context.setBindings(bindings, 100);
        context.setBindings(GLOBAL_BINDINGS, 200);
        return context;
    }

    public static void registerGlobalBinding(String key, Object val) {
        GLOBAL_BINDINGS.put(key, val);
    }

    public static Object unregisterGlobalBinding(String key) {
        return GLOBAL_BINDINGS.remove(key);
    }

    private static String stripExtension(String input) {
        if(!input.endsWith(".groovy"))
            return input;
        return input.substring(0, input.length() - ".groovy".length());
    }

}
