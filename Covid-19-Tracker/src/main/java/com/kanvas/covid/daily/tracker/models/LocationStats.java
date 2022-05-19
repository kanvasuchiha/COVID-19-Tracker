package com.kanvas.covid.daily.tracker.models;


import lombok.Data;

@Data
public class LocationStats {

    private String state;
    private String country;
    private Long latestTotalCases;
    private Long diffFromPrev10Day;
    private Long lastOneDayChange;

}
