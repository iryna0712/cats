package com.events.custom;

import com.events.EventJSON;

public class GameOverEvent extends EventJSON {
    private short id;

    //TODO: make this hidden constructors?
    public GameOverEvent(short playerId) {
        super(EventJSONType.GAME_OVER);
        this.id = playerId;
    }

    public short getId() {
        return id;
    }
}
