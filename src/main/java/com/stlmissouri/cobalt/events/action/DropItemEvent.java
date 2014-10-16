package com.stlmissouri.cobalt.events.action;

import com.darkmagician6.eventapi.events.callables.EventCancellable;

public class DropItemEvent extends EventCancellable {

    private ItemDropType actionType;

    public DropItemEvent(int type) {
        if (type == 3) {
            this.actionType = ItemDropType.DROP_STACK;
        } else if (type == 4) {
            this.actionType = ItemDropType.DROP_ONE_ITEM;
        }
    }

    public ItemDropType getActionType() {
        return actionType;
    }

    public enum ItemDropType {

        DROP_STACK(3), DROP_ONE_ITEM(4);

        public int packetAction = 1;

        private ItemDropType(int packetAction) {
            this.packetAction = packetAction;
        }
    }
}