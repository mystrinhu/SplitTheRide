package com.splitTheRide.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Joao on 05-07-2015.
 */
public class Trip implements Serializable {

    private int id;
    private String date;
    private int driver;
    private int vehicle;
    private int round_trip;
    private ArrayList<HashMap<Integer, Integer>> passengers;

    public Trip(int id, String date, int driver, int vehicle, int round_trip) {

        this.id = id;
        this.date = date;
        this.driver = driver;
        this.vehicle = vehicle;
        this.round_trip = round_trip;
        this.passengers = new ArrayList<HashMap<Integer, Integer>>();
    }

    public void setPassengers(ArrayList<HashMap<Integer, Integer>> passengers) {

        this.passengers = passengers;
    }

    public int getId() {

        return id;
    }

    public String getDate() {

        return date;
    }

    public int getDriver() {

        return driver;
    }

    public int getVehicle() {

        return vehicle;
    }

    public int getRound_trip() {

        return this.round_trip;
    }

    public ArrayList<HashMap<Integer, Integer>> getPassengers() {

        return passengers;
    }


}
