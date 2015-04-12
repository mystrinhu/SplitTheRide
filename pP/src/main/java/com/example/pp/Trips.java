package com.example.pp;

import java.util.ArrayList;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.database.PersonHandler;
import com.example.database.RouteHandler;
import com.example.entities.Person;
import com.example.entities.Route;

public class Trips extends ActionBarActivity implements OnClickListener{

	private EditText date;
	private Spinner driverSpinner, routeSpinner;
	private Button save, cancel;
	//private PersonHandler personHandler;
	private ArrayList<Person> personList;
	private ArrayAdapter<Person> driverAdapter;
	
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.trips_layout);
		
		date = (EditText) findViewById(R.id.date);
		driverSpinner = (Spinner) findViewById(R.id.spinner2);
		save = (Button) findViewById(R.id.saveTrip);
		cancel = (Button) findViewById(R.id.cancelTrip);
		
		Bundle bundle = getIntent().getExtras();
		date.setText(bundle.getString("date"));
				
		personList = getDrivers();
		
	    driverAdapter = new ArrayAdapter<Person>(this, android.R.layout.simple_list_item_1, personList);
		driverSpinner.setAdapter(driverAdapter);
		
		save.setOnClickListener(this);
		cancel.setOnClickListener(this);
		
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
			case R.id.saveTrip:	break;
			
			case R.id.cancelTrip:	Intent calendar = new Intent(Trips.this, CalendarView.class);
									
									startActivity(calendar);
									this.finish();
		}
		
	}

    public void addNewPassengerHandler(View v){

        Toast.makeText(getApplicationContext(), "Quero adicionar um novo passageiro", Toast.LENGTH_LONG).show();
    }
	
	
	private ArrayList<Person> getDrivers(){
		
		ArrayList<Person> drivers = new ArrayList<Person>();
		
		PersonHandler personHandler = new PersonHandler(this);
		personHandler.open();
		
		Cursor c = personHandler.getAllDrivers();
		
		c.moveToFirst();
		
		personList = new ArrayList<Person>();		
		
	    while (!c.isAfterLast()) {
	    	
	    	Person person = new Person(c.getInt(0), c.getString(1), c.getString(2), c.getInt(3));
	    	drivers.add(person);
	    	
	    	c.moveToNext();
	    }
		
	    personHandler.close();
	    
	    return drivers;
	}
	
	private ArrayList<Route> getRoutes(){
		
		ArrayList<Route> routes = new ArrayList<Route>();
		
		RouteHandler routeHandler = new RouteHandler(this);
		routeHandler.open();
		
		Cursor c = routeHandler.returnRoutes();
		
		c.moveToFirst();
		
		personList = new ArrayList<Person>();		
		
	    while (!c.isAfterLast()) {
	    	
	    	Route route = new Route(c.getInt(0), c.getString(1));
	    	routes.add(route);
	    	
	    	c.moveToNext();
	    }
		
		routeHandler.close();
		
		return routes;
		
	}
	
}
