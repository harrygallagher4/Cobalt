package com.stlmissouri.cobalt;

import com.darkmagician6.eventapi.EventManager;
import com.darkmagician6.eventapi.EventTarget;
import com.stlmissouri.cobalt.api.CobaltPluginManager;
import com.stlmissouri.cobalt.api.CobaltShutdownThread;
import com.stlmissouri.cobalt.command.CobaltCommandManager;
import com.stlmissouri.cobalt.events.system.DisplayCreateEvent;
import com.stlmissouri.cobalt.gui.notifications.CobaltNotificationManager;
import com.stlmissouri.cobalt.gui.notifications.Notification;
import com.stlmissouri.cobalt.info.InfoSet;
import com.stlmissouri.cobalt.io.CobaltIOManager;
import com.stlmissouri.cobalt.keybinds.BasicKeybind;
import com.stlmissouri.cobalt.keybinds.CobaltKeybindManager;
import com.stlmissouri.cobalt.module.CobaltModuleManager;
import com.stlmissouri.cobalt.plugins.chatfilter.ChatFilter;
import com.stlmissouri.cobalt.plugins.creative.CreativeCommands;
import com.stlmissouri.cobalt.plugins.friends.Friends;
import com.stlmissouri.cobalt.plugins.groovy.CobaltIsGroovy;
import com.stlmissouri.cobalt.plugins.modlist.ModList;
import com.stlmissouri.cobalt.plugins.modpack.ModPack;
import com.stlmissouri.cobalt.plugins.pluginlist.PluginList;
import com.stlmissouri.cobalt.plugins.repo.CobaltPackageManager;
import com.stlmissouri.cobalt.plugins.tabgui.TabGuiPlugin;
import com.stlmissouri.cobalt.preferences.CobaltPreferencesManager;
import com.stlmissouri.cobalt.render.CobaltRenderManager;
import com.stlmissouri.cobalt.render.Renderer;
import com.stlmissouri.cobalt.util.ChatColor;
import com.stlmissouri.cobalt.util.CobaltManager;
import com.stlmissouri.cobalt.util.Debugger;
import com.stlmissouri.cobalt.util.Elements;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.util.ChatComponentText;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Stl
 * Date: 1/20/14
 * Time: 1:43 PM
 * Use:  Cobalt main class
 */
public class Cobalt {

    public static String CLIENT_NAME = Elements.randomElement();
    public static final String CLIENT_VERSION = "";

    public final Minecraft mc;
    //Yeah, look at that public stuff, whatchu gonna do about it
    public final CobaltIOManager ioManager;
    public final CobaltCommandManager commandManager;
    public final CobaltKeybindManager keybindManager;
    public final CobaltRenderManager renderManager;
    public final CobaltNotificationManager notificationManager;
    public final CobaltModuleManager moduleManager;
    public final CobaltPluginManager pluginManager;
    public final CobaltPackageManager packageManager;
    public final CobaltPreferencesManager preferencesManager;

    public final InfoSet cobaltInfo;

    private List<CobaltManager> thingsThatManageOtherThings = new ArrayList<>();

    public Cobalt(Minecraft minecraft) {
        System.out.println("Creating Cobalt objects...");
        this.mc = minecraft;
        this.ioManager = new CobaltIOManager(this, mc.mcDataDir);
        this.commandManager = new CobaltCommandManager(this.mc, this);
        this.keybindManager = new CobaltKeybindManager(this);
        this.renderManager = new CobaltRenderManager(this.mc);
        this.notificationManager = new CobaltNotificationManager(this.mc, this);
        this.moduleManager = new CobaltModuleManager(this);
        this.pluginManager = new CobaltPluginManager(this);
        this.packageManager = new CobaltPackageManager(this);
        this.preferencesManager = new CobaltPreferencesManager(this);
        this.thingsThatManageOtherThings.add(this.ioManager);
        this.thingsThatManageOtherThings.add(this.commandManager);
        this.thingsThatManageOtherThings.add(this.keybindManager);
        this.thingsThatManageOtherThings.add(this.renderManager);
        this.thingsThatManageOtherThings.add(this.notificationManager);
        this.thingsThatManageOtherThings.add(this.moduleManager);
        this.thingsThatManageOtherThings.add(this.pluginManager);
        this.thingsThatManageOtherThings.add(this.packageManager);
        this.thingsThatManageOtherThings.add(this.preferencesManager);
        this.cobaltInfo = new InfoSet();
        System.out.println("Done. Initializing...");
        init();
        System.out.println("Done.");
    }

    private void init() {
        Runtime.getRuntime().addShutdownHook(new Thread(new CobaltShutdownThread()));
        Debugger.debugging = true;
        this.keybindManager.registerKeybind("gui.command", new BasicKeybind(Keyboard.KEY_PERIOD) {
            @Override
            public void fire() {
                mc.displayGuiScreen(new GuiChat("."));
            }
        });
        this.keybindManager.registerKeybind("rename", new BasicKeybind(Keyboard.KEY_F4) {
            @Override
            public void fire() {
                Cobalt.CLIENT_NAME = ChatColor.randomColor() + Elements.randomElement();
            }
        });
        this.keybindManager.registerKeybind("notification.clear", new BasicKeybind(Keyboard.KEY_O) {
            @Override
            public void fire() {
                int reverseIndex = 1;
                do {
                    if (!notificationManager.getNotifications().isEmpty() && notificationManager.getNotifications().size() - reverseIndex >= 0) {
                        Notification notification = notificationManager.getNotifications().get(notificationManager.getNotifications().size() - reverseIndex);
                        if (notification.getTime() > 20) {
                            notification.setTime(20);
                            break;
                        } else {
                            reverseIndex++;
                        }
                    } else {
                        break;
                    }
                } while (true);
            }
        });
        this.renderManager.registerRenderer(CobaltRenderManager.RendererLocation.INGAME, "title", new Renderer() {
            @Override
            public void render() {
                mc.fontRenderer.drawStringWithShadow(String.format("%s %s", CLIENT_NAME, CLIENT_VERSION), 2, 2, 0xFFFFFFFF);
            }
        });
        EventManager.register(this);
    }

    @EventTarget
    public void onDisplayCreate(DisplayCreateEvent event) {
        this.pluginManager.loadJars(ioManager.COBALT_PLUGIN_DIR);
        this.keybindManager.loadAsJson();
        try {
            this.pluginManager.registerPlugin(new ModPack(this));
            this.pluginManager.registerPlugin(new Friends(this));
            this.pluginManager.registerPlugin(new ModList(this));
            this.pluginManager.registerPlugin(new ChatFilter(this));
            this.pluginManager.registerPlugin(new TabGuiPlugin(this));
            this.pluginManager.registerPlugin(new CobaltIsGroovy(this));
            this.pluginManager.registerPlugin(new PluginList(this));
            this.pluginManager.registerPlugin(new CreativeCommands(this));
        } catch (Exception e) {
            e.printStackTrace();
        }
        EventManager.unregister(this);
    }

    public void displayChat(String message, Object... args) {
        this.displayChat(String.format(message, args));
    }

    public void displayChat(String message) {
        if (mc.ingameGUI == null || mc.ingameGUI.getChatGUI() == null)
            return;
        mc.ingameGUI.getChatGUI().func_146234_a(new ChatComponentText(String.format("[\2479%s\247r] ", CLIENT_NAME) + message), 0);
    }

}