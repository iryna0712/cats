package com.events.custom;

import com.events.EventJSON;

public class ConnectEventJSON extends EventJSON {

    private String name;

    //constructor for deserialization
    public ConnectEventJSON() {}

    public ConnectEventJSON(String name) {
        super(EventJSONType.HANDSHAKE);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
