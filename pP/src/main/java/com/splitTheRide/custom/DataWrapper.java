package com.splitTheRide.custom;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Joao on 29-06-2015.
 */
public class DataWrapper implements Serializable {

    private ArrayList<HashMap<String, String>> passengersList;

    public DataWrapper(ArrayList<HashMap<String, String>> data) {

        this.passengersList = data;
    }

    public ArrayList<HashMap<String, String>> getPassengersList() {

        return this.passengersList;
    }
}
