package com.stlmissouri.cobalt.plugins.groovy;import javax.script.*;
import java.io.Reader;
import java.io.Writer;
import java.util.List;

public class ScriptContextPair extends CompiledScript implements ScriptContext {

    private final CompiledScript script;
    private final ScriptContext context;

    public ScriptContextPair(CompiledScript script, ScriptContext context) {
        this.script = script;
        this.context = context;
    }

    @Override
    public Object eval() throws ScriptException {
        return this.script.eval(this.context);
    }

    @Override
    public Object eval(Bindings bindings) throws ScriptException {
        return this.script.eval(bindings);
    }

    @Override
    public Object eval(ScriptContext context) throws ScriptException {
        return this.script.eval(context);
    }

    @Override
    public ScriptEngine getEngine() {
        return this.script.getEngine();
    }

    @Override
    public void setBindings(Bindings bindings, int scope) {
        this.context.setBindings(bindings, scope);
    }

    @Override
    public Bindings getBindings(int scope) {
        return this.context.getBindings(scope);
    }

    @Override
    public void setAttribute(String name, Object value, int scope) {
        this.context.setAttribute(name, value, scope);
    }

    @Override
    public Object getAttribute(String name, int scope) {
        return this.context.getAttribute(name, scope);
    }

    @Override
    public Object removeAttribute(String name, int scope) {
        return this.context.removeAttribute(name, scope);
    }

    @Override
    public Object getAttribute(String name) {
        return this.context.getAttribute(name);
    }

    @Override
    public int getAttributesScope(String name) {
        return this.context.getAttributesScope(name);
    }

    @Override
    public Writer getWriter() {
        return this.context.getWriter();
    }

    @Override
    public Writer getErrorWriter() {
        return this.context.getErrorWriter();
    }

    @Override
    public void setWriter(Writer writer) {
        this.context.setWriter(writer);
    }

    @Override
    public void setErrorWriter(Writer writer) {
        this.context.setErrorWriter(writer);
    }

    @Override
    public Reader getReader() {
        return this.context.getReader();
    }

    @Override
    public void setReader(Reader reader) {
        this.context.setReader(reader);
    }

    @Override
    public List<Integer> getScopes() {
        return this.context.getScopes();
    }
}
