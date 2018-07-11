package com.events.custom;

import com.events.EventJSON;

public class NoStopCardEventJSON extends EventJSON {
    public NoStopCardEventJSON() {
        super(EventJSON.EventJSONType.COOLDOWN_FINISHED);
    }
}
