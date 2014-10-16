package com.stlmissouri.cobalt.gui.notifications;

import java.util.List;

/**
 * User: Stl
 * Date: 2/2/14
 * Time: 5:55 PM
 * Use:  Notification parent class
 */
public abstract class Notification {

    protected final String title;
    protected final List<String> message;

    protected int time;

    public Notification(String title, List<String> message) {
        this.title = title;
        this.message = message;
        this.time = 60;
    }

    public abstract void draw();

    public abstract int getWidth();

    public abstract int getHeight();

    public void updateTime() {
        this.time--;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getTime() {
        return time;
    }

}
