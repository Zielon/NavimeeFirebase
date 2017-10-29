package com.navimee.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Flight {
    public int flightId;
    public int flightNumber;
    public String departureAirportFsCode;
    public String arrivalAirportFsCode;
    public Date departureDate;
    public Date arrivalDate;
    public Date publishedDeparture;
    public Date publishedArrival;
    public Date scheduledGateDeparture;
    public Date scheduledGateArrival;
    public int flightDurations;
    public String departureTerminal;
    public String departureGate;
    public String baggage;
}
