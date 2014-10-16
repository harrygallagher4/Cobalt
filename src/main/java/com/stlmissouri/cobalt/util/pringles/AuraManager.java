package com.stlmissouri.cobalt.util.pringles;

import net.minecraft.entity.Entity;

import java.util.HashMap;
import java.util.Map;

/**
 * Manage kill aura settings.
 *
 * @author Ramisme
 * @since Apr 30, 2013
 */
public final class AuraManager {
    private double range = 3.9;
    private int delay = (1000 / 7);
    private boolean silent = true;
    private boolean mob = true, animal = false, player = true;
    private Map<Integer, Long> attackMap;

    public AuraManager() {
        attackMap = new HashMap<>();
    }

    public void addToAttackMap(final int entityId, final long last) {
        attackMap.put(entityId, last);
    }

    private long getLast(Entity entity) {
        if (attackMap.get(entity.getEntityId()) == null)
            return 0L;
        return attackMap.get(entity.getEntityId());
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(final int delay) {
        this.delay = delay;
    }

    public double getRange() {
        return range;
    }

    public void setRange(double range) {
        this.range = range;
    }

    public boolean isSilent() {
        return silent;
    }

    public void setSilent(boolean silent) {
        this.silent = silent;
    }

    public boolean isMob() {
        return mob;
    }

    public void setMob(boolean mob) {
        this.mob = mob;
    }

    public boolean isAnimal() {
        return animal;
    }

    public void setAnimal(boolean animal) {
        this.animal = animal;
    }

    public boolean isPlayer() {
        return player;
    }

    public void setPlayer(boolean player) {
        this.player = player;
    }

    public Map<Integer, Long> getAttackMap() {
        return attackMap;
    }

    public void setAttackMap(Map<Integer, Long> attackMap) {
        this.attackMap = attackMap;
    }

    public long convertToMilliseconds(double delay) {
        return 1000L / (long) delay;
    }

}
