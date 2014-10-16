package com.stlmissouri.cobalt.module.mods;

import com.darkmagician6.eventapi.EventManager;
import com.darkmagician6.eventapi.EventTarget;
import com.stlmissouri.cobalt.Cobalt;
import com.stlmissouri.cobalt.events.render.WorldRenderEvent;
import com.stlmissouri.cobalt.events.system.PluginLoadedEvent;
import com.stlmissouri.cobalt.module.CobaltModule;
import com.stlmissouri.cobalt.plugins.friends.Friends;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.input.Keyboard;

import java.util.HashMap;
import java.util.Map;

import static com.stlmissouri.cobalt.render.Render2D.setColor;
import static com.stlmissouri.cobalt.render.Render3D.*;

/**
 * User: Stl
 * Date: 2/17/14
 * Time: 10:56 PM
 * Use:  Draws "tracers" to players and mobs
 */
public class TracersMod extends CobaltModule {

    private Friends friends;
    private TracerManager manager;

    public TracersMod(Cobalt cobalt) {
        super(cobalt, "Tracers");
        this.updateKeybind(Keyboard.KEY_G);
        manager = new TracerManager();
    }

    private double interpolate(double now, double then, double percent) {
        return then + ((now - then) * percent);
    }

    private void drawTracer(double posX, double posY, double posZ) {
        setup3DRendering(true);
        drawLine(0, 0, 0, posX, posY, posZ, 1.5F);
        end3DRendering();
    }

    @EventTarget
    public void onWorldRender(WorldRenderEvent event) {
        float pt = event.getPartialTicks();
        render(pt);
    }

    private void render(float pt) {
        try {
            for (Object entity : mc.theWorld.loadedEntityList) {
                if (!(entity instanceof EntityPlayer))
                    continue;
                if (entity.equals(mc.thePlayer))
                    continue;
                EntityPlayer player = (EntityPlayer) entity;
                double posX = interpolate(player.posX, player.lastTickPosX, pt) - RenderManager.renderPosX;
                double posY = interpolate(player.posY, player.lastTickPosY, pt) - RenderManager.renderPosY;
                double posZ = interpolate(player.posZ, player.lastTickPosZ, pt) - RenderManager.renderPosZ;
                this.colorize(player, 1.0F);
                this.drawTracer(posX, posY, posZ);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void colorize(Entity entity, float opacity) {
        setColor(this.manager.getColor(entity), opacity);
    }

    private class TracerManager {

        private Friends friends;
        private Map<Class<? extends Entity>, Integer> colorMap = new HashMap<>();

        private int DEFAULT = 0xFA6600;

        public TracerManager() {
            this.friends = cobalt.pluginManager.getPlugin(Friends.class);
            //todo: add entities to the color map
            EventManager.register(this);
        }

        @EventTarget
        public void onFriends(PluginLoadedEvent event) {
            if (!event.getPluginClass().equals(Friends.class))
                return;
            this.friends = TracersMod.this.cobalt.pluginManager.getPlugin(Friends.class); //not using event.getPlugin because casting
        }

        public int getColor(Entity entity) {
            if (entity instanceof EntityPlayer)
                return this.getPlayerColor((EntityPlayer) entity);
            if (this.colorMap.containsKey(entity.getClass()))
                return this.colorMap.get(entity.getClass());
            else
                return DEFAULT;
        }

        private int getPlayerColor(EntityPlayer player) {
            return this.friends.getPlayerManager().getProperties(player).getColor();
        }

    }

}
