package com.stlmissouri.cobalt.events.mod;

import com.darkmagician6.eventapi.events.callables.EventCancellable;

public class ChatSendEvent extends EventCancellable {

    private String msg;

    public ChatSendEvent(String msg) {
        this.msg = msg;
    }

    public void setMessage(String msg) {
        this.msg = msg;
    }

    public String getMessage() {
        return msg;
    }
}