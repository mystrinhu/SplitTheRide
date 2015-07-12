package com.splitTheRide.trips;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;

import com.splitTheRide.custom.DataWrapper;
import com.splitTheRide.custom.Utils;
import com.splitTheRide.database.TripHandler;
import com.splitTheRide.database.TripPersonHandler;
import com.splitTheRide.entities.Person;
import com.splitTheRide.entities.Vehicle;
import com.splitTheRide.splittheride.R;

import java.util.ArrayList;
import java.util.HashMap;

public class Trips extends ActionBarActivity implements OnClickListener{

	private TextView date;
    private Spinner driverSpinner, vehicleSpinner;
    private CheckBox round_trip;
    private ArrayList<HashMap<String, String>> passengersList = new ArrayList<HashMap<String, String>>();
    private ArrayList<Person> driversList;
    private ArrayList<Vehicle> vehiclesList;
	private ArrayAdapter<Person> driverAdapter;
    private ArrayAdapter<Vehicle> vehicleAdapter;
    private Button save;
    private Bundle bundle;


    private Utils utils;

    protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.trips_layout);

        utils = new Utils();

        date = (TextView) findViewById(R.id.date);
		driverSpinner = (Spinner) findViewById(R.id.spinner2);
        vehicleSpinner = (Spinner) findViewById(R.id.spinner3);
        round_trip = (CheckBox) findViewById(R.id.ida_volta);
		save = (Button) findViewById(R.id.saveTrip);
        Button cancel = (Button) findViewById(R.id.cancelTrip);


        bundle = getIntent().getExtras();
        date.setText(bundle.getString("date"));
        if (bundle.getSerializable("passengersList") != null) {
            DataWrapper dw = (DataWrapper) bundle.getSerializable("passengersList");
            passengersList = dw.getPassengersList();
        }

        if (bundle.getInt("round_trip") != -1) {
            if (bundle.getInt("round_trip") == 1)
                round_trip.setChecked(true);
            else round_trip.setChecked(false);
        }

        // Setting up drivers spinner

        driversList = utils.getDrivers(getApplicationContext());

	    driverAdapter = new ArrayAdapter<Person>(this, R.layout.custom_spinner_layout, driversList);
        driverAdapter.notifyDataSetChanged();
        driverSpinner.setAdapter(driverAdapter);

        driverSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                removefromPassengers(parent.getItemAtPosition(position).toString());

                vehiclesList = utils.getCarsDrivenBy(driversList.get(position).getId(), getApplicationContext());
                vehicleAdapter = new ArrayAdapter<Vehicle>(getApplicationContext(), R.layout.custom_spinner_layout, vehiclesList);
                vehicleSpinner.setAdapter(vehicleAdapter);

                if (bundle.getBoolean("fromPassengers"))
                    vehicleSpinner.setSelection(bundle.getInt("vehicleID"));
                else
                    vehicleSpinner.setSelection(utils.getVehicleWithID(vehiclesList, bundle.getInt("vehicleID")));
                vehicleAdapter.notifyDataSetChanged();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        int selectedDriverID = -1;
        if (bundle.getInt("driverID") != 0) {
            if (bundle.getBoolean("fromPassengers"))
                selectedDriverID = bundle.getInt("driverID");
            else selectedDriverID = utils.getDriverWithID(driversList, bundle.getInt("driverID"));

            driverSpinner.setSelection(selectedDriverID);
        }

        // Setting up vehicle spinner
        vehiclesList = utils.getCarsDrivenBy(driversList.get(0).getId(), this);

        vehicleAdapter = new ArrayAdapter<Vehicle>(this, R.layout.custom_spinner_layout, vehiclesList);
        vehicleAdapter.setNotifyOnChange(true);
        vehicleSpinner.setAdapter(vehicleAdapter);

        save.setText(bundle.getString("operation"));

		save.setOnClickListener(this);
		cancel.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
			case R.id.saveTrip:     boolean roundtrip = round_trip.isChecked();
                Person driver = (Person) driverSpinner.getSelectedItem();
                                    String day = date.getText().toString();
                Vehicle vehicle = (Vehicle) vehicleSpinner.getSelectedItem();


                                    if(passengersList.isEmpty())
                                        showDialog("Error", "You must add at least one passenger.", null);
                                    else {
                                        if (save.getText().toString().equalsIgnoreCase("Save")) {
                                            saveTrip(roundtrip, driver, day, vehicle);


                                            Intent intent = new Intent(Trips.this, CalendarView.class);

                                            showDialog("Save trip", "Trip saved successfully", intent);
                                        } else {
                                            int trip_ID = bundle.getInt("tripID");
                                            editTrip(trip_ID, driver, vehicle, roundtrip);

                                            Intent intent = new Intent(Trips.this, CalendarView.class);

                                            showDialog("Edit trip", "Trip edited successfully", intent);
                                        }

                                    }

                                    break;

			case R.id.cancelTrip:	Intent calendar = new Intent(Trips.this, CalendarView.class);

									startActivity(calendar);
                finish();
        }

	}

    // mostra mensagem
    private void showDialog(String title, String message, final Intent intent) {

        AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (intent != null) {
                    startActivity(intent);
                    finish();
                }

            }
        });

        dialog.show();
    }


    // Guarda a informação das viagens
    private void saveTrip(boolean roundTrip, Person driver, String date, Vehicle vehicle) {

        int round_trip = 0;

        if (roundTrip)
            round_trip = 1;

        TripHandler tripHandler = new TripHandler(this);
        tripHandler.open();
        tripHandler.insertTrip(date, driver.getId(), vehicle.getId(), round_trip);
        tripHandler.close();

        tripHandler.open();
        int trip_id = tripHandler.getLastTripID();
        tripHandler.close();

        TripPersonHandler tripPersonHandler = new TripPersonHandler(this);
        tripPersonHandler.open();

        for(HashMap<String, String> passenger: passengersList){

            Object[] pass = passenger.values().toArray();
            tripPersonHandler.insertTripPerson(trip_id, utils.getPersonIDfromName((String) pass[0], this), utils.getRouteIDFromName((String) pass[1], this));
        }

        tripPersonHandler.close();

    }

    //Edita a informação das viagens
    public void editTrip(int tripID, Person driver, Vehicle vehicle, boolean roundTrip) {

        int round_trip = 0;

        if (roundTrip)
            round_trip = 1;

        TripHandler tripHandler = new TripHandler(this);
        tripHandler.open();

        tripHandler.editTrip(tripID, driver.getId(), vehicle.getId(), round_trip);

        tripHandler.close();

        TripPersonHandler tripPersonHandler = new TripPersonHandler(this);
        tripPersonHandler.open();

        tripPersonHandler.removeTrip(tripID);

        for (HashMap<String, String> passenger : passengersList) {

            Object[] pass = passenger.values().toArray();
            tripPersonHandler.insertTripPerson(tripID, utils.getPersonIDfromName((String) pass[0], this), utils.getRouteIDFromName((String) pass[1], this));
        }

        tripPersonHandler.close();

    }

    public void listOfPassengersClickHandler(View v) {

        Intent intent = new Intent(Trips.this, PassengersByTrips.class);

        intent.putExtra("date", date.getText().toString());
        if (round_trip.isChecked())
            intent.putExtra("round_trip", 1);
        else intent.putExtra("round_trip", 0);
        intent.putExtra("driver", driverSpinner.getSelectedItem().toString());
        intent.putExtra("operation", save.getText().toString());
        intent.putExtra("tripID", bundle.getInt("tripID"));


        intent.putExtra("passengersList", new DataWrapper(passengersList));
        intent.putExtra("driverID", utils.getDriverWithID(driversList, ((Person) driverSpinner.getSelectedItem()).getId()));
        intent.putExtra("vehicleID", utils.getVehicleWithID(vehiclesList, ((Vehicle) vehicleSpinner.getSelectedItem()).getId()));

        startActivity(intent);

    }


    private void removefromPassengers(String name){

        HashMap<String,String> item = null;

        for(HashMap<String, String> pair: passengersList){

            if(pair.containsValue(name))
                item = pair;
        }

        if(item != null)
            passengersList.remove(item);

    }




}
