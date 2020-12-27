package com.dimitri.remoiville.go4lunch.event;

import com.google.android.libraries.places.api.model.Place;

public class AutocompleteEvent {

    public final Place place;

    public AutocompleteEvent(Place place) {
        this.place = place;
    }

}
