package com.stlmissouri.cobalt.events.render;

import com.darkmagician6.eventapi.events.Event;

/**
 * User: Stl
 * Date: 3/3/14
 * Time: 3:23 PM
 * Use:  Event called when the world is rendered
 *      We're using an event and not a rendermanager for this so we can pass partial-ticks
 */
public class WorldRenderEvent implements Event {

    private final static WorldRenderEvent instance = new WorldRenderEvent();

    private float partialTicks = 0.0F;

    private WorldRenderEvent() {

    }

    public static WorldRenderEvent create(float partialTicks) {
        instance.partialTicks = partialTicks;
        return instance;
    }

    public float getPartialTicks() {
        return partialTicks;
    }
}
