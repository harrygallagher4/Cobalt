package com.stlmissouri.cobalt.gui.notifications;

import com.darkmagician6.eventapi.events.Event;

/**
 * User: Stl
 * Date: 2/2/14
 * Time: 5:53 PM
 * Use:  Event called for a notification
 */
public class NotificationEvent implements Event {

    public final String title, message;

    public NotificationEvent(String title, String message) {
        this.title = title;
        this.message = message;
    }

}
