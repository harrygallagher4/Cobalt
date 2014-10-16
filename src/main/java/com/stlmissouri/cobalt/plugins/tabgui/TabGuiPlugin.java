package com.stlmissouri.cobalt.plugins.tabgui;

import com.stlmissouri.cobalt.Cobalt;
import com.stlmissouri.cobalt.api.CobaltPlugin;
import com.stlmissouri.cobalt.command.Command;
import com.stlmissouri.cobalt.info.InfoNode;
import com.stlmissouri.cobalt.info.ParentInfoNode;
import com.stlmissouri.cobalt.keybinds.BasicKeybind;
import com.stlmissouri.cobalt.plugins.tabgui.gui.TabActionNode;
import com.stlmissouri.cobalt.plugins.tabgui.gui.TabGuiRenderer;
import com.stlmissouri.cobalt.plugins.tabgui.gui.TabNode;
import com.stlmissouri.cobalt.plugins.tabgui.gui.TabParentNode;
import com.stlmissouri.cobalt.render.CobaltRenderManager;
import org.lwjgl.input.Keyboard;

/**
 * User: Stl
 * Date: 4/29/2014
 * Time: 6:13 PM
 * Use:
 */
public class TabGuiPlugin extends CobaltPlugin {

    public static final String RENDERER_KEY = "renderer.tabgui";
    public static final String KEYBIND_UP = "tabgui.up";
    public static final String KEYBIND_DOWN = "tabgui.down";
    public static final String KEYBIND_UP_LEVEL = "tabgui.uplevel";
    public static final String KEYBIND_DOWN_LEVEL = "tabgui.downlevel";

    private TabGui gui;
    private TabGuiRenderer renderer;

    private Cobalt cobalt;

    public TabGuiPlugin(Cobalt cobalt) {
        super(cobalt);
        this.gui = new TabGui();
    }

    @Override
    public void load(final Cobalt cobalt) {
        this.cobalt = cobalt;
        this.renderer = new TabGuiRenderer(this.gui, cobalt.mc);
        this.cobalt.keybindManager.registerKeybind(KEYBIND_UP, new BasicKeybind(Keyboard.KEY_UP) {
            @Override
            public void fire() {
                gui.up();
            }
        });
        this.cobalt.keybindManager.registerKeybind(KEYBIND_DOWN, new BasicKeybind(Keyboard.KEY_DOWN) {
            @Override
            public void fire() {
                gui.down();
            }
        });
        this.cobalt.keybindManager.registerKeybind(KEYBIND_UP_LEVEL, new BasicKeybind(Keyboard.KEY_LEFT) {
            @Override
            public void fire() {
                gui.upLevel();
            }
        });
        this.cobalt.keybindManager.registerKeybind(KEYBIND_DOWN_LEVEL, new BasicKeybind(Keyboard.KEY_RIGHT) {
            @Override
            public void fire() {
                gui.downLevel();
            }
        });
        this.cobalt.renderManager.registerRenderer(CobaltRenderManager.RendererLocation.INGAME, RENDERER_KEY, this.renderer);
        this.cobalt.commandManager.registerCommand("build", new Command() {
            @Override
            public void onCommand(String... args) {
                TabGuiPlugin.this.build(cobalt);
                return;
            }
        });
    }

    @Override
    public void unload() {
        this.cobalt.renderManager.unregisterRenderer(CobaltRenderManager.RendererLocation.INGAME, RENDERER_KEY);
        this.cobalt.keybindManager.unregisterKeybind(KEYBIND_UP);
        this.cobalt.keybindManager.unregisterKeybind(KEYBIND_DOWN);
        this.cobalt.keybindManager.unregisterKeybind(KEYBIND_UP_LEVEL);
        this.cobalt.keybindManager.unregisterKeybind(KEYBIND_DOWN_LEVEL);
        this.gui.reset();
    }

    @Override
    public String getName() {
        return "Tab GUI";
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public double getPluginVersion() {
        return 1;
    }

    private void build(Cobalt cobalt) {
        this.gui.reset();
        for (InfoNode node : cobalt.cobaltInfo.getNodeSet()) {
            TabNode tabNode;
            if (node instanceof ParentInfoNode && ((ParentInfoNode) node).getChildren().length > 0) {
                tabNode = buildNode((ParentInfoNode) node);
            } else {
                tabNode = new TabActionNode(node.getInfoString());
            }
            this.gui.addComponent(tabNode);
        }
    }

    private TabNode buildNode(ParentInfoNode node) {
        TabParentNode tabNode = new TabParentNode(node.getInfoString());
        for (InfoNode child : node.getChildren()) {
            TabNode childTabNode;
            if (child instanceof ParentInfoNode && ((ParentInfoNode) child).getChildren().length > 0) {
                childTabNode = buildNode((ParentInfoNode) child);
            } else {
                childTabNode = new TabActionNode(child.getInfoString());
            }
            tabNode.addChild(childTabNode);
        }
        return tabNode;
    }

}
