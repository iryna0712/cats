package com.events;

import com.game.Event;
import com.game.EventJSON;

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
