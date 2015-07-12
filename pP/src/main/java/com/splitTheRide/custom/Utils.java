package com.splitTheRide.custom;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.splitTheRide.database.PersonHandler;
import com.splitTheRide.database.RouteHandler;
import com.splitTheRide.database.TripHandler;
import com.splitTheRide.database.VehicleHandler;
import com.splitTheRide.entities.Person;
import com.splitTheRide.entities.Route;
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

    public String getPersonNameFromID(int id, Context ctx) {

        String name = "";

        PersonHandler personHandler = new PersonHandler(ctx);

        personHandler.open();
        name = personHandler.getNamefromID(id);

        personHandler.close();

        return name;
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

    // ROUTES

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

    public String getRouteNameFromID(int route_id, Context ctx) {
        String route_name = "";
        RouteHandler handler = new RouteHandler(ctx);

        handler.open();

        Cursor c = handler.getRouteName(route_id);

        c.moveToFirst();


        while (!c.isAfterLast()) {

            route_name = c.getString(0);
            c.moveToNext();
        }

        handler.close();

        return route_name;
    }

    public ArrayList<Route> getRoutes(Context ctx) {

        ArrayList<Route> routes = new ArrayList<Route>();

        RouteHandler routeHandler = new RouteHandler(ctx);
        routeHandler.open();

        Cursor c = routeHandler.returnRoutes();

        c.moveToFirst();

        while (!c.isAfterLast()) {

            Route route = new Route(c.getInt(0), c.getString(1));
            routes.add(route);

            c.moveToNext();
        }

        routeHandler.close();

        return routes;

    }

    public ArrayList<Person> getDrivers(Context ctx) {

        ArrayList<Person> drivers = new ArrayList<Person>();

        PersonHandler personHandler = new PersonHandler(ctx);
        personHandler.open();

        Cursor c = personHandler.getAllDrivers();

        c.moveToFirst();

        while (!c.isAfterLast()) {

            Person person = new Person(c.getInt(0), c.getString(1), c.getInt(2));
            drivers.add(person);

            c.moveToNext();
        }

        personHandler.close();

        return drivers;
    }


    public int getDriverWithID(ArrayList<Person> drivers, int driverID) {

        int arrayPosition = -1;

        for (int i = 0; i < drivers.size() && arrayPosition == -1; i++) {

            if (drivers.get(i).getId() == driverID)
                arrayPosition = i;

        }

        return arrayPosition;
    }

    public int getVehicleWithID(ArrayList<Vehicle> vehicles, int vehicleID) {

        int arrayPosition = -1;

        for (int i = 0; i < vehicles.size() && arrayPosition == -1; i++) {

            if (vehicles.get(i).getId() == vehicleID)
                arrayPosition = i;

        }

        return arrayPosition;
    }

}
