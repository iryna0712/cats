package com.events.custom;

import com.events.EventJSON;

public class NextTurnEventJSON extends EventJSON {

    private short id;

    public NextTurnEventJSON(short id) {
        super(EventJSONType.TURN);
        this.id = id;
    }

    public short getId() {
        return id;
    }
}
