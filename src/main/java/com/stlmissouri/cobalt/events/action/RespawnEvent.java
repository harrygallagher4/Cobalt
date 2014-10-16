package com.stlmissouri.cobalt.events.action;

import com.darkmagician6.eventapi.events.callables.EventCancellable;
import net.minecraft.client.entity.EntityClientPlayerMP;

public class RespawnEvent extends EventCancellable {

    private EntityClientPlayerMP player;

    public RespawnEvent(EntityClientPlayerMP player) {
        this.player = player;
    }

    public EntityClientPlayerMP getPlayer() {
        return player;
    }
}