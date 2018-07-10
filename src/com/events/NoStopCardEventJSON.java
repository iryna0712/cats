package com.events;

public class NoStopCardEventJSON extends EventJSON{
    public NoStopCardEventJSON() {
        super(EventJSON.EventJSONType.COOLDOWN_FINISHED);
    }
}
