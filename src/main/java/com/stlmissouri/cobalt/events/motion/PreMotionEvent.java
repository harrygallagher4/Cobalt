package com.stlmissouri.cobalt.events.motion;

import com.darkmagician6.eventapi.events.callables.EventCancellable;
import net.minecraft.client.entity.EntityClientPlayerMP;

/**
 * User: Stl
 * Date: 2/22/14
 * Time: 11:44 PM
 * Use:
 */
public class PreMotionEvent extends EventCancellable {

    private final EntityClientPlayerMP player;

    public PreMotionEvent(EntityClientPlayerMP player) {
        this.player = player;
    }

    public EntityClientPlayerMP getPlayer() {
        return player;
    }
}
