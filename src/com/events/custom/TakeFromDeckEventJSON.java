package com.events.custom;

import com.events.EventJSON;

public class TakeFromDeckEventJSON extends EventJSON {
    public TakeFromDeckEventJSON() {
        super(EventJSONType.TAKE_FROM_DECK);
    }
}
