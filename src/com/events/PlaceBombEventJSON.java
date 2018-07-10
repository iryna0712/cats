package com.events;

import com.entities.Deck.Place;

public class PlaceBombEventJSON extends EventJSON{
    public static final int INVALID_PLACE_INDEX = -1;

    public PlaceBombEventJSON(Place place, boolean isClosed) {
        super(EventJSONType.PLACE_BOMB);
        this.isClosed = isClosed;
        index = INVALID_PLACE_INDEX;
    }

    //TODO: maybe make index part of place
    public PlaceBombEventJSON(Place place, int index, boolean isClosed) {
        super(EventJSONType.PLACE_BOMB);
        this.place = place;
        this.index = index;
        this.isClosed = isClosed;
    }

    private Place place;
    private int index;
    private boolean isClosed;

    public Place getPlace() {
        return place;
    }

    public int getIndex() {
        return index;
    }

    public boolean isClosed() {
        return isClosed;
    }
}
