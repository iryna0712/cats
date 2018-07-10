package com.events;

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
