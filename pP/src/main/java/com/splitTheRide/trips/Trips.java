package com.splitTheRide.trips;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.splitTheRide.database.ComposedRouteHandler;
import com.splitTheRide.database.PersonHandler;
import com.splitTheRide.database.RouteHandler;
import com.splitTheRide.database.SegmentHandler;
import com.splitTheRide.entities.Person;
import com.splitTheRide.entities.Route;
import com.splitTheRide.settings.AddEditRoute;
import com.splitTheRide.splittheride.R;

public class Trips extends ActionBarActivity implements OnClickListener{

	private TextView date;
	private Spinner driverSpinner;
    private ListView passengerListView;
	private Button save, cancel;
    private ArrayList<HashMap<String,String>> list;
	private ArrayList<Person> personList = new ArrayList<Person>(), driversList;
    private Cursor personCursor;
    private CharSequence[] passengers;
	private ArrayAdapter<Person> driverAdapter;
    private SimpleAdapter simpleAdapter;
    private ArrayList<Integer> selectedPassengers;
    private final ArrayList<CharSequence> mSelectedItems = new ArrayList<CharSequence>();
	
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.trips_layout);
		
		date = (TextView) findViewById(R.id.date);
		driverSpinner = (Spinner) findViewById(R.id.spinner2);
		save = (Button) findViewById(R.id.saveTrip);
		cancel = (Button) findViewById(R.id.cancelTrip);
        passengerListView = (ListView) findViewById(R.id.passengerList);
		
		Bundle bundle = getIntent().getExtras();
		date.setText(bundle.getString("date"));

        // Gets all te necessary information from the Person table
        PersonHandler personHandler = new PersonHandler(this);
        personHandler.open();

        personCursor = personHandler.returnAllPersonsData();

        personCursor.moveToFirst();

        while(!personCursor.isAfterLast()){
            Person person = new Person(personCursor.getInt(0), personCursor.getString(1), personCursor.getString(2), personCursor.getInt(3));
            personList.add(person);

            personCursor.moveToNext();
        }
        personHandler.close();

				
		driversList = getDrivers();
		
	    driverAdapter = new ArrayAdapter<Person>(this, R.layout.custom_spinner_layout, driversList);
		driverSpinner.setAdapter(driverAdapter);

        driverSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                removefromPassengers(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        list = new ArrayList<HashMap<String,String>>();

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


    // Remover passageiro da lista
    public void removePassengerOnClickHandler(View v){

        LinearLayout rl = (LinearLayout)v.getParent();
        TextView tv = (TextView)rl.findViewById(R.id.line_a);
        String name = tv.getText().toString();

        removefromPassengers(name);
    }


    private void showDialog(){
        AlertDialog.Builder newDialog = new AlertDialog.Builder(this);

        newDialog.setTitle("Inexistent route")
                .setMessage("This route does not exist. Do you wish to add it?")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Trips.this, AddEditRoute.class);

                        intent.putExtra("segments", mSelectedItems );

                        startActivity(intent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        newDialog.create();
        newDialog.show();
    }

    // Editar os segmentos/Rotas escolhidas

    public void editPassengerOnClickHandler(View v){

        // Segments

        SegmentHandler segmentHandler = new SegmentHandler(this);
        segmentHandler.open();

        Cursor c1 = segmentHandler.returnSegments();
        c1.moveToFirst();

        final CharSequence[] segments_list = new CharSequence[c1.getCount()];
        int i=0;

        while(!c1.isAfterLast()){
            segments_list[i] = c1.getString(1);
            i++;

            c1.moveToNext();
        }

        segmentHandler.close();



        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Set the dialog title
        builder.setTitle("Select the segments")
                // Specify the list array, the items to be selected by default (null for none),
                // and the listener through which to receive callbacks when items are selected
                .setMultiChoiceItems(segments_list, null,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which,
                                                boolean isChecked) {
                                if (isChecked) {
                                    // If the user checked the item, add it to the selected items
                                    mSelectedItems.add(segments_list[which]);
                                } else if (mSelectedItems.contains(which)) {
                                    // Else, if the item is already in the array, remove it
                                    mSelectedItems.remove(segments_list[Integer.valueOf(which)]);
                                }
                            }
                        })
                        // Set the action buttons
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        if (segmentsOfExistingRoute(mSelectedItems) == false) {

                            showDialog();
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

        builder.create();
        builder.show();

    }


    public void addNewPassengerHandler(View v){

        String driver = driverSpinner.getSelectedItem().toString();
        ArrayList<Person> persons = new ArrayList<Person>();

        selectedPassengers = new ArrayList<Integer>();

        // Lista de passageiros (o tamanho depende do condutor e dos passageiros já escolhidos)
        int numberOfSelected = list.size();
        passengers = new CharSequence[personCursor.getCount()-1-numberOfSelected];

        personCursor.moveToFirst();
        int i = 0;

        while (!personCursor.isAfterLast()){

            if(!personCursor.getString(1).equalsIgnoreCase(driver) &&
                    !isAlreadySelected(personCursor.getString(1), list)) {
                passengers[i] = personCursor.getString(1);
                i++;
            }

            personCursor.moveToNext();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        if(i!=0) {

            builder.setTitle("Add passengers")
                    // Specify the list array, the items to be selected by default (null for none),
                    // and the listener through which to receive callbacks when items are selected
                    .setMultiChoiceItems(passengers, null,
                            new DialogInterface.OnMultiChoiceClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which,
                                                    boolean isChecked) {
                                    if (isChecked) {
                                        // If the user checked the item, add it to the selected items
                                        selectedPassengers.add(which);
                                    } else if (selectedPassengers.contains(which)) {
                                        // Else, if the item is already in the array, remove it
                                        selectedPassengers.remove(Integer.valueOf(which));
                                    }
                                }
                            })
                            // Set the action buttons
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            // User clicked OK, so save the mSelectedItems results somewhere
                            // or return them to the component that opened the dialog
                            setPassengers(selectedPassengers, personList);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });
        }else{
            builder.setTitle("Add Passengers")
                   .setMessage("All the persons have a role in this trip.")
                   .setPositiveButton("OK", new DialogInterface.OnClickListener(){
                       @Override
                       public void onClick(DialogInterface dialog, int id) {
                           // User clicked OK, so save the mSelectedItems results somewhere
                           // or return them to the component that opened the dialog
                           setPassengers(selectedPassengers, personList);
                       }
                   });
        }

        builder.create();

        builder.show();
    }


    // Adiciona elementos à lista
	private void setPassengers(ArrayList<Integer> selected, ArrayList<Person> listOfPersons){

        HashMap<String,String> item;

        RouteHandler routeHandler = new RouteHandler(this);

        routeHandler.open();

        for(int pos: selected){
            item = new HashMap<String,String>();

            Person p = getSelectedPerson(listOfPersons, passengers[pos].toString());

            Route route = routeHandler.getRoute(p.getUsual_route());

            item.put( "line1", p.getName());
            item.put( "line2", route.getName());

            list.add(item);
        }

        routeHandler.close();

        simpleAdapter = new SimpleAdapter(this, list, R.layout.custom_passengers_layout ,
                                new String[] { "line1","line2" },
                                new int[] {R.id.line_a, R.id.line_b});

        passengerListView.setAdapter(simpleAdapter);
    }

    private void removefromPassengers(String name){

        HashMap<String,String> item = null;

        for(HashMap<String, String> pair: list){

            if(pair.containsValue(name))
                item = pair;
        }

        if(item != null)
            list.remove(item);

        simpleAdapter = new SimpleAdapter(this, list, R.layout.custom_passengers_layout ,
                new String[] { "line1","line2" },
                new int[] {R.id.line_a, R.id.line_b});

        passengerListView.setAdapter(simpleAdapter);
    }

    private Person getSelectedPerson(ArrayList<Person> listOfPersons, String name){

        Person p = null;

        for(Person person: listOfPersons){

            if(person.getName().equalsIgnoreCase(name))
                p = person;
        }

        return p;
    }

    private boolean isAlreadySelected(String name, ArrayList<HashMap<String, String>> listSelected){

        boolean selected = false;

        for(HashMap<String, String> pair: listSelected){

            if(pair.containsValue(name))
                selected = true;
        }

        return selected;
    }

	private ArrayList<Person> getDrivers(){
		
		ArrayList<Person> drivers = new ArrayList<Person>();
		
		PersonHandler personHandler = new PersonHandler(this);
		personHandler.open();
		
		Cursor c = personHandler.getAllDrivers();
		
		c.moveToFirst();
		
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
		
	    while (!c.isAfterLast()) {
	    	
	    	Route route = new Route(c.getInt(0), c.getString(1));
	    	routes.add(route);
	    	
	    	c.moveToNext();
	    }
		
		routeHandler.close();
		
		return routes;
		
	}

    private boolean segmentsOfExistingRoute(ArrayList<CharSequence> segments){

        ArrayList<Route> routes = getRoutes();

        ComposedRouteHandler composedRouteHandler = new ComposedRouteHandler(getApplicationContext());

        composedRouteHandler.open();

        boolean found = false;

        for(int pos=0; pos<routes.size() && !found; pos++){

            Cursor c = composedRouteHandler.getAllRouteSegments(routes.get(pos).getID());

            c.moveToFirst();
            ArrayList segments_in_route = new ArrayList();

            while(!c.isAfterLast()){

                segments_in_route.add(c.getString(0));
                c.moveToNext();
            }

            boolean equal = true;

            if(segments_in_route.size() == segments.size()){

                for(int i=0; i < segments.size() && equal ; i++){

                    if(segments_in_route.contains(segments.get(i))==false)
                        equal = false;
                }
            }else
                equal = false;

            if(equal)
                found = true;
        }

        composedRouteHandler.close();

        return found;
    }
}
