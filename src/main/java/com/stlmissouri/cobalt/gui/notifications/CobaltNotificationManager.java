package com.stlmissouri.cobalt.gui.notifications;

import com.darkmagician6.eventapi.EventManager;
import com.darkmagician6.eventapi.EventTarget;
import com.stlmissouri.cobalt.Cobalt;
import com.stlmissouri.cobalt.events.mod.TickEvent;
import com.stlmissouri.cobalt.info.InfoSet;
import com.stlmissouri.cobalt.render.CobaltRenderManager;
import com.stlmissouri.cobalt.util.CobaltManager;
import com.stlmissouri.cobalt.util.Debugger;

import net.minecraft.client.Minecraft;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Stl
 * Date: 2/2/14
 * Time: 6:06 PM
 * Use:  Manages notifications
 */
public class CobaltNotificationManager implements CobaltManager {

    private final List<Notification> notifications;

    public CobaltNotificationManager(Minecraft minecraft, Cobalt cobalt) {
        EventManager.register(this);
        cobalt.renderManager.registerRenderer(CobaltRenderManager.RendererLocation.GUI, "cobalt.renderer.notifications", new NotificationRenderer(this));
        notifications = new ArrayList<>();
    }

    public void addNotification(Notification notification) {
        notifications.add(notification);
        Debugger.debug("Notification list size: " + notifications.size());
    }

    public void clear() {
        notifications.clear();
        Debugger.debug("Notifications cleared");
    }

    @EventTarget
    public void notify(NotificationEvent event) {
        addNotification(new BasicNotification(event.title, event.message));
    }

    @EventTarget
    public void tick(TickEvent event) {
        update();
    }

    public List<Notification> getNotifications() {
        return notifications;
    }

    public void update() {
        if (!(notifications.size() > 0))
            return;
        Notification n;
        List<Integer> remove = new ArrayList<>();
        for (int index = 0; index < notifications.size(); index++) {
            n = notifications.get(index);
            n.updateTime();
            if (n.time <= 0) {
                remove.add(index);
            }
        }
        int removed = 0;
        for (int index : remove) {
            notifications.remove(index - removed);
            removed++;
        }
    }

    @Override
    public void updateInfo(InfoSet infoSet) { }
}
