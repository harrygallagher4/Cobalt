package com.stlmissouri.cobalt.events.block;

import com.darkmagician6.eventapi.events.callables.EventCancellable;
import net.minecraft.world.WorldSettings;

/**
 * User: Stl
 * Date: 5/12/2014
 * Time: 2:36 PM
 * Use:
 */
public class BlockClickEvent extends EventCancellable {

    private int x, y, z, face;
    private WorldSettings.GameType gameType;

    public BlockClickEvent(int x, int y, int z, int face, WorldSettings.GameType gameType) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.face = face;
        this.gameType = gameType;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public int getFace() {
        return face;
    }

    public WorldSettings.GameType getGameType() {
        return gameType;
    }
}
