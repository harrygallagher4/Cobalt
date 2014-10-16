package com.stlmissouri.cobalt.gui.notifications;

import com.stlmissouri.cobalt.render.Renderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

import static org.lwjgl.opengl.GL11.glTranslatef;

/**
 * User: Stl
 * Date: 2/2/14
 * Time: 6:05 PM
 * Use:  Renders notifications
 */
public class NotificationRenderer implements Renderer {

    private final CobaltNotificationManager manager;

    public NotificationRenderer(CobaltNotificationManager manager) {
        this.manager = manager;
    }

    @Override
    public void render() {
        if (manager.getNotifications().size() < 1)
            return;
        Minecraft mc = Minecraft.getMinecraft();
        ScaledResolution sr = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
        int height = 0;
        for (Notification n : manager.getNotifications()) {
            height += n.getHeight();
            height += 2;
        }
        glTranslatef(sr.getScaledWidth() - 2, sr.getScaledHeight() - height, 0);
        int offset = 0;
        for (Notification n : manager.getNotifications()) {
            glTranslatef(-n.getWidth(), offset, 0);
            n.draw();
            glTranslatef(n.getWidth(), -offset, 0);
            offset += n.getHeight() + 2;
        }
        glTranslatef(-(sr.getScaledWidth() - 2), -(sr.getScaledHeight() - height), 0);
    }

}
