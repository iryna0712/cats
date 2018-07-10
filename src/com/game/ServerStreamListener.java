package com.game;

import com.events.EventJSON;

public interface ServerStreamListener {
    void send(Client client, EventJSON event);
}
