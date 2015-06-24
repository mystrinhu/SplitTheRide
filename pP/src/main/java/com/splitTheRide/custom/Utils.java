package com.splitTheRide.custom;

import android.content.Context;
import android.database.Cursor;

import com.splitTheRide.database.PersonHandler;
import com.splitTheRide.database.RouteHandler;
import com.splitTheRide.database.TripHandler;
import com.splitTheRide.database.VehicleHandler;
import com.splitTheRide.entities.Vehicle;

import java.util.ArrayList;

/**
 * Created by Joao on 26-05-2015.
 */
public class Utils {


    // PERSONS

    public int getPersonIDfromName(String driverName, Context ctx) {
        int driverID = -1;

        PersonHandler personHandler = new PersonHandler(ctx);

        personHandler.open();
        Cursor driver_cursor = personHandler.getIDfromName(driverName);

        driver_cursor.moveToFirst();

        while (!driver_cursor.isAfterLast()) {

            driverID = driver_cursor.getInt(0);
            driver_cursor.moveToNext();
        }
        personHandler.close();

        return driverID;
    }

    public ArrayList getCarsDrivenBy(int driver, Context ctx) {

        ArrayList<Vehicle> cars = new ArrayList<Vehicle>();

        VehicleHandler vehicleHandler = new VehicleHandler(ctx);

        vehicleHandler.open();

        Cursor c = vehicleHandler.getVehiclesDrivenBy(driver);

        c.moveToFirst();

        while (!c.isAfterLast()) {

            Vehicle vehicle = new Vehicle(c.getInt(0), c.getString(1), c.getDouble(2), c.getInt(3));

            cars.add(vehicle);
            c.moveToNext();
        }

        vehicleHandler.close();

        return cars;
    }

    public int getRouteIDFromName(String name, Context ctx) {
        int route_id = 0;
        RouteHandler handler = new RouteHandler(ctx);

        handler.open();

        Cursor c = handler.getRouteID(name);

        c.moveToFirst();


        while (!c.isAfterLast()) {

            route_id = c.getInt(0);
            c.moveToNext();
        }

        handler.close();

        return route_id;
    }

}
