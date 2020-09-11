package com.dimitri.remoiville.go4lunch.model;

import java.time.DayOfWeek;
import java.time.LocalTime;

public class OpeningHours {

    private DayOfWeek dayOfWeek;
    private LocalTime from;
    private LocalTime to;

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public LocalTime getFrom() {
        return from;
    }

    public void setFrom(LocalTime from) {
        this.from = from;
    }

    public LocalTime getTo() {
        return to;
    }

    public void setTo(LocalTime to) {
        this.to = to;
    }
}
