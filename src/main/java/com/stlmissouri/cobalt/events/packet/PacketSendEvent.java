package com.stlmissouri.cobalt.events.packet;

import com.darkmagician6.eventapi.events.callables.EventCancellable;
import net.minecraft.network.Packet;

/**
 * User: Stl
 * Date: 2/11/14
 * Time: 12:42 AM
 * Use:  Outgoing packet event
 */
public class PacketSendEvent extends EventCancellable {

    private Packet packet;

    public PacketSendEvent(Packet packet) {
        super();
        this.packet = packet;
    }

    public Packet getPacket() {
        return this.packet;
    }

    public void setPacket(Packet packet) {
        this.packet = packet;
    }
}