package com.stlmissouri.cobalt.events.motion;

import com.darkmagician6.eventapi.events.callables.EventCancellable;
import net.minecraft.client.entity.EntityClientPlayerMP;

public class PostMotionEvent extends EventCancellable {

    private final EntityClientPlayerMP player;

    public PostMotionEvent(EntityClientPlayerMP player) {
        this.player = player;
    }

    public EntityClientPlayerMP getPlayer() {
        return player;
    }
}