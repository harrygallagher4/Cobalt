package com.stlmissouri.cobalt.events.block;

import com.darkmagician6.eventapi.events.Event;

/**
 * User: Stl
 * Date: 2/22/14
 * Time: 11:37 PM
 * Use:  Used to pass block damaging to listeners
 */
public class BlockDamageEvent implements Event {

    private float damage;
    private int delay;
    private final int x, y, z;

    public BlockDamageEvent(float damage, int delay, int x, int y, int z) {
        this.damage = damage;
        this.delay = delay;
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public int getX(){
    	return x;
    }
    
    public int getY(){
    	return y;
    }
    
    public int getZ(){
    	return z;
    }

    public float getDamage() {
        return damage;
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }
}
