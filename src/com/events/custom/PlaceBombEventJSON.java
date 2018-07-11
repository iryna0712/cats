package com.events.custom;

import com.entities.Deck.Place;
import com.events.EventJSON;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PlaceBombEventJSON extends EventJSON{
    public static final int INVALID_PLACE_INDEX = -1;

//    @JsonCreator
//    public PlaceBombEventJSON(@JsonProperty(value="place", required = true)Place place, @JsonProperty(value="isClosed", required = true) boolean isClosed) {
//        super(EventJSON.EventJSONType.PLACE_BOMB);
//        this.isClosed = isClosed;
//        this.place = place;
//        index = INVALID_PLACE_INDEX;
//    }

    //TODO: maybe make index part of place
    @JsonCreator
    public PlaceBombEventJSON(@JsonProperty(value="place", required = true)Place place, @JsonProperty(value="isClosed", required = true) boolean isClosed, @JsonProperty(value="index") int index ) {
        super(EventJSONType.PLACE_BOMB);
        this.place = place;
        //TODO: set default value
        this.index = index;
        this.isClosed = isClosed;
    }

    @JsonProperty(required = true)
    private boolean isClosed;
    @JsonProperty(required = true)
    private Place place;
    @JsonProperty
    private int index;

    public Place getPlace() {
        return place;
    }

    public int getIndex() {
        return index;
    }

    public boolean isClosed() {
        return isClosed;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void setClosed(boolean closed) {
        isClosed = closed;
    }
}
