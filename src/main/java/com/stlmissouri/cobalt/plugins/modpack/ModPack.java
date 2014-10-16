package com.stlmissouri.cobalt.plugins.modpack;

import com.google.common.reflect.ClassPath;
import com.stlmissouri.cobalt.Cobalt;
import com.stlmissouri.cobalt.api.CobaltPlugin;
import com.stlmissouri.cobalt.module.CobaltModule;
import com.stlmissouri.cobalt.module.mods.*;
import com.stlmissouri.cobalt.plugins.friends.Friends;

public class ModPack extends CobaltPlugin {

    private static LoadMethod loadMethod = LoadMethod.STATIC;

    public ModPack(Cobalt cobalt) {
        super(cobalt);
        depend(Friends.class, 0);
    }

    public static void setLoadMethod(LoadMethod method) {
        loadMethod = method;
    }

    @Override
    public void load(Cobalt cobalt) {
        switch (loadMethod) {
            case STATIC:
                this.loadStatic(cobalt);
                break;
            case DYNAMIC:
                this.loadPackage(cobalt);
                break;
        }
    }

    @Override
    public void unload() {

    }

    @Override
    public String getName() {
        return "Mod Pack";
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public double getPluginVersion() {
        return 1;
    }

    private void loadPackage(Cobalt cobalt) {
        try {
            for(ClassPath.ClassInfo classInfo : ClassPath.from(this.getClass().getClassLoader())
                    .getTopLevelClassesRecursive("com.stlmissouri.cobalt.module.mods")) {
                Class<?> c = classInfo.load();
                if(CobaltModule.class.isAssignableFrom(c))
                    cobalt.moduleManager.registerModule((CobaltModule) c.getDeclaredConstructor(Cobalt.class).newInstance(cobalt));
            }
        }catch (Exception e) {}
    }

    private void loadStatic(Cobalt cobalt) {
        try {
            cobalt.moduleManager.registerModule(new FlyMod(cobalt));
            cobalt.moduleManager.registerModule(new SpeedmineMod(cobalt));
            cobalt.moduleManager.registerModule(new FullbrightMod(cobalt));
            cobalt.moduleManager.registerModule(new ChestESPMod(cobalt));
            cobalt.moduleManager.registerModule(new VClipMod(cobalt));
            cobalt.moduleManager.registerModule(new TeleportMod(cobalt));
            cobalt.moduleManager.registerModule(new ChatMacroMod(cobalt));
            cobalt.moduleManager.registerModule(new TracersMod(cobalt));
            cobalt.moduleManager.registerModule(new BlockOverlayMod(cobalt));
            cobalt.moduleManager.registerModule(new KillAuraMod(cobalt));
            cobalt.moduleManager.registerModule(new BowAimbotMod(cobalt));
            cobalt.moduleManager.registerModule(new PacketSendMonitorMod(cobalt));
            cobalt.moduleManager.registerModule(new PacketReceiveMonitorMod(cobalt));
            cobalt.moduleManager.registerModule(new StepMod(cobalt));
            cobalt.moduleManager.registerModule(new AutoToolMod(cobalt));
            cobalt.moduleManager.registerModule(new TextRadarMod(cobalt));
            cobalt.moduleManager.registerModule(new CreativeArmorMod(cobalt));
            cobalt.moduleManager.registerModule(new Dropper(cobalt));
            cobalt.moduleManager.registerModule(new SpeedhackMod(cobalt));
            cobalt.moduleManager.registerModule(new SprintMod(cobalt));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public enum LoadMethod {
        STATIC, DYNAMIC
    }

}