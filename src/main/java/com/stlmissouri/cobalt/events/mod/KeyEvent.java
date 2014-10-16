package com.stlmissouri.cobalt.events.mod;

import com.darkmagician6.eventapi.events.Event;

/**
 * User: Stl
 * Date: 2/1/14
 * Time: 9:58 PM
 * Use:  Event to represent a key being pressed
 */
public class KeyEvent implements Event {

    public final int key;

    public KeyEvent(int key) {
        this.key = key;
    }
}