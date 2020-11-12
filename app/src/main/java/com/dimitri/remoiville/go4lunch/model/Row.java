
package com.dimitri.remoiville.go4lunch.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Row {

    @SerializedName("elements")
    @Expose
    private List<Element> elements = null;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Row() {
    }

    /**
     * 
     * @param elements
     */
    public Row(List<Element> elements) {
        super();
        this.elements = elements;
    }

    public List<Element> getElements() {
        return elements;
    }

    public void setElements(List<Element> elements) {
        this.elements = elements;
    }

}
