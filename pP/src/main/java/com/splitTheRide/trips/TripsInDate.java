package com.splitTheRide.trips;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ListView;

import com.splitTheRide.custom.DataWrapper;
import com.splitTheRide.custom.TripsListAdapter;
import com.splitTheRide.custom.Utils;
import com.splitTheRide.database.TripHandler;
import com.splitTheRide.database.TripPersonHandler;
import com.splitTheRide.entities.Trip;
import com.splitTheRide.splittheride.R;

import java.util.ArrayList;
import java.util.HashMap;

public class TripsInDate extends ActionBarActivity {

    private TripsListAdapter tripsListAdapter;
    ArrayList<Trip> trips = new ArrayList<Trip>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trips_in_date);

        ListView tripsListView = (ListView) findViewById(R.id.listViewTrips);

        Bundle bundle = getIntent().getExtras();
        String date = bundle.getString("date");


        int trip_id, vehicle_id, driver_id, round_trip;

        TripHandler tripHandler = new TripHandler(this);
        tripHandler.open();

        Cursor c = tripHandler.tripsInDate(date);
        c.moveToFirst();

        while (!c.isAfterLast()) {

            trip_id = c.getInt(0);
            driver_id = c.getInt(1);
            vehicle_id = c.getInt(2);
            round_trip = c.getInt(3);

            Trip trip = new Trip(trip_id, date, driver_id, vehicle_id, round_trip);
            trips.add(trip);
            c.moveToNext();

        }

        tripHandler.close();

        TripPersonHandler tripPersonHandler = new TripPersonHandler(this);


        for (Trip t : trips) {
            ArrayList<HashMap<Integer, Integer>> passengers = new ArrayList<HashMap<Integer, Integer>>();
            tripPersonHandler.open();

            Cursor cursor = tripPersonHandler.passengersInTrip(t.getId());
            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {

                HashMap<Integer, Integer> item = new HashMap<Integer, Integer>();

                item.put(cursor.getInt(0), cursor.getInt(1));
                passengers.add(item);

                cursor.moveToNext();
            }

            tripPersonHandler.close();

            t.setPassengers(passengers);
        }

        tripsListAdapter = new TripsListAdapter(this, R.layout.custom_passengers_layout, trips);

        tripsListView.setAdapter(tripsListAdapter);

    }


    public void editElementOnClickHandler(View v) {

        Utils utils = new Utils();
        Trip trip = (Trip) v.getTag();

        Intent intent = new Intent(TripsInDate.this, Trips.class);


        intent.putExtra("date", trip.getDate());
        intent.putExtra("tripID", trip.getId());
        intent.putExtra("round_trip", trip.getRound_trip());
        intent.putExtra("driverID", trip.getDriver());
        intent.putExtra("vehicleID", trip.getVehicle());

        ArrayList<HashMap<String, String>> passengersList = new ArrayList<HashMap<String, String>>();

        for (HashMap<Integer, Integer> passenger_route : trip.getPassengers()) {

            HashMap<String, String> item = new HashMap<String, String>();

            Object[] route = passenger_route.keySet().toArray();
            Object[] passenger = passenger_route.values().toArray();

            item.put("line1", utils.getPersonNameFromID((Integer) passenger[0], this));
            item.put("line2", utils.getRouteNameFromID((Integer) route[0], this));

            passengersList.add(item);
        }

        intent.putExtra("passengersList", new DataWrapper(passengersList));
        intent.putExtra("operation", "Edit");

        startActivity(intent);
        finish();

    }


    // Remover viagem da lista
    public void removeElementOnClickHandler(final View v) {

        AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.setTitle("Delete trip");
        dialog.setMessage("Are you sure you want to delete this trip?");
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Trip trip = (Trip) v.getTag();

                TripPersonHandler tripPersonHandler = new TripPersonHandler(getApplicationContext());

                tripPersonHandler.open();
                tripPersonHandler.removeTrip(trip.getId());
                tripPersonHandler.close();

                TripHandler tripHandler = new TripHandler(getApplicationContext());

                tripHandler.open();
                tripHandler.removeTrip(trip.getId());
                tripHandler.close();

                tripsListAdapter.remove(trip);

            }
        });

        dialog.show();

    }
}
