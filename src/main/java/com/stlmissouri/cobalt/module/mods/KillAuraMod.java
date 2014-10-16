package com.stlmissouri.cobalt.module.mods;

import com.darkmagician6.eventapi.EventTarget;
import com.google.gson.reflect.TypeToken;
import com.stlmissouri.cobalt.Cobalt;
import com.stlmissouri.cobalt.command.Command;
import com.stlmissouri.cobalt.events.mod.GameLoopEvent;
import com.stlmissouri.cobalt.events.motion.PostMotionEvent;
import com.stlmissouri.cobalt.events.motion.PreMotionEvent;
import com.stlmissouri.cobalt.module.CobaltModule;
import com.stlmissouri.cobalt.plugins.friends.Friends;
import com.stlmissouri.cobalt.preferences.PreferencesSerializable;
import com.stlmissouri.cobalt.util.pringles.AuraManager;
import com.stlmissouri.cobalt.util.pringles.EntityManager;
import com.stlmissouri.cobalt.util.pringles.TimeManager;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.input.Keyboard;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * User: Stl
 * Date: 3/20/2014
 * Time: 7:27 PM
 * Use:  Killaura module
 */
public class KillAuraMod extends CobaltModule implements PreferencesSerializable<Map<String, Object>> {

    private float yaw = 0F, pitch = 0F, yawHead = 0F;
    private EntityLivingBase target;

    private final TimeManager timeManager = new TimeManager();
    private final AuraManager auraManager = new AuraManager();
    private final EntityManager entityManager;
    private double range = 4.1D;
    private long delay = (1000L / 6);

    private final Minecraft minecraft;

    public KillAuraMod(Cobalt cobalt) {
        super(cobalt, "Killaura");
        this.entityManager = new EntityManager(this.auraManager, this.cobalt.pluginManager.getPlugin(Friends.class));
        this.minecraft = this.cobalt.mc;
        this.updateKeybind(Keyboard.KEY_R);
        this.auraManager.setSilent(true);
        this.registerCommands();
        this.cobalt.preferencesManager.registerPrefs(this.getName(), this);
    }

    @EventTarget
    public void onPreMotion(PreMotionEvent event) {
        if (this.target == null)
            return;
        this.backupAngles(event.getPlayer());
    }

    @EventTarget
    public void onPostMotion(PostMotionEvent event) {
        if (this.target == null)
            return;
        if (auraManager.isSilent())
            this.restoreAngles(event.getPlayer());
    }

    @EventTarget
    public void onUpdate(GameLoopEvent event) {
        if (this.minecraft.thePlayer == null)
            return;
        this.target = entityManager.getEntity(80F, this.range);
        if (this.target == null)
            return;
        if (!this.auraManager.isSilent())
            this.setAngles(this.target);
        this.timeManager.updateTimer();
        if (timeManager.sleep(this.delay)) {
            this.minecraft.thePlayer.swingItem();
            this.minecraft.playerController.attackEntity(minecraft.thePlayer, this.target);
            this.timeManager.updateLast();
            this.auraManager.addToAttackMap(this.target.getEntityId(), this.timeManager.getLast());
        }
    }

    private void setAngles(EntityLivingBase entityLiving) {
        this.backupAngles(minecraft.thePlayer);
        float yaw = entityManager.getAngles(entityLiving)[0];

        mc.thePlayer.rotationPitch = entityManager.getAngles(entityLiving)[1];
        mc.thePlayer.rotationYaw = yaw;
        mc.thePlayer.rotationYawHead = yaw;
    }

    private void backupAngles(EntityPlayer player) {
        this.yaw = player.rotationYaw;
        this.pitch = player.rotationPitch;
        this.yawHead = player.rotationYawHead;
    }

    private void restoreAngles(EntityPlayer player) {
        player.rotationYaw = this.yaw;
        player.rotationPitch = this.pitch;
        player.rotationYawHead = this.yawHead;
    }

    private void registerCommands() {
        this.cobalt.commandManager.registerCommand("aurarange", new Command() {
            @Override
            public void onCommand(String... args) {
                if (args.length < 1) {
                    cobalt.displayChat("Killaura range: " + range);
                    return;
                }
                double newRange;
                try {
                    newRange = Double.parseDouble(args[0]);
                } catch (NumberFormatException nfe) {
                    cobalt.displayChat("Invalid range: " + args[0]);
                    return;
                }
                range = newRange;
                cobalt.displayChat("Killaura range changed to: " + range);
                return;
            }
        });
        this.cobalt.commandManager.registerAlias("aurarange", "ar");

        this.cobalt.commandManager.registerCommand("attackspeed", new Command() {
            @Override
            public void onCommand(String... args) {
                if (args.length < 1)
                    return;
                double speed;
                try {
                    speed = Double.parseDouble(args[0]);
                } catch (NumberFormatException nfe) {
                    cobalt.displayChat("Invalid speed: " + args[0]);
                    return;
                }
                delay = (long) (1000L / speed);
                cobalt.displayChat("Killaura speed changed to: " + speed + " attacks/second. (" + delay + "ms)");
                return;
            }
        });
        this.cobalt.commandManager.registerAlias("attackspeed", "as");
    }

    @Override
    public Map<String, Object> getSaveObject() {
        Map<String, Object> toSave = new HashMap<>();
        toSave.put("range", this.range);
        toSave.put("delay", this.delay);
        return toSave;
    }

    @Override
    public void load(Map<String, Object> savedObject) {
        this.range = (double) savedObject.get("range");
        this.delay = ((Double) savedObject.get("delay")).longValue();
    }

    @Override
    public Type getType() {
        return new TypeToken<Map<String, Object>>() {
        }.getType();
    }
}
