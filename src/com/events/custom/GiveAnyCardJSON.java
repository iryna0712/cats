package com.events.custom;

import com.events.EventJSON;

public class GiveAnyCardJSON extends EventJSON {

    private short id;

    public GiveAnyCardJSON(short id) {
        super(EventJSONType.GIVE_ANY_CARD);
        this.id = id;
    }

    public short getId() {
        return id;
    }
}
