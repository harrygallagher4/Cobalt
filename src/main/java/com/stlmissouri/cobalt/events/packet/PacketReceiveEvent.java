package com.stlmissouri.cobalt.events.packet;

import com.darkmagician6.eventapi.events.callables.EventCancellable;
import net.minecraft.network.Packet;

/**
 * User: Stl
 * Date: 2/11/14
 * Time: 12:40 AM
 * Use:  Incoming packet event
 */
public class PacketReceiveEvent extends EventCancellable {

    private Packet packet;

    public PacketReceiveEvent(Packet packet) {
        super();
        this.packet = packet;
    }

    public Packet getPacket() {
        return this.packet;
    }

    public void setPacket(Packet packet) {
        this.packet = packet;
    }

    public boolean ofType(Class<? extends Packet> type) {
        return this.packet.getClass().equals(type);
    }
}