package com.stlmissouri.cobalt.events.block;

import com.darkmagician6.eventapi.events.Event;

/**
 * Project: cobalt
 * Package: com.stlmissouri.cobalt.events.block
 * Created by SabourQ on 10/03/14.
 * Use: Called on the destruction of a block.
 */
public class BlockDestroyEvent implements Event {

    private final int x, y, z;


    public BlockDestroyEvent(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     *
     * @return The x co-ordinate of the block destroyed.
     */
    public int getX() {
        return x;
    }

    /**
     *
     * @return The y co-ordinate of the block destroyed.
     */
    public int getY() {
        return y;
    }

    /**
     *
     * @return The z co-ordinate of the block destroyed.
     */
    public int getZ() {
        return z;
    }
}
