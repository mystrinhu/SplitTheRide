package com.splitTheRide.trips;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.splitTheRide.custom.DataWrapper;
import com.splitTheRide.custom.Utils;
import com.splitTheRide.database.ComposedRouteHandler;
import com.splitTheRide.database.PersonHandler;
import com.splitTheRide.database.RouteHandler;
import com.splitTheRide.database.SegmentHandler;
import com.splitTheRide.entities.Person;
import com.splitTheRide.entities.Route;
import com.splitTheRide.settings.AddEditRoute;
import com.splitTheRide.splittheride.R;

import java.util.ArrayList;
import java.util.HashMap;

public class PassengersByTrips extends ActionBarActivity implements View.OnClickListener {

    private ListView passengerListView;
    private SimpleAdapter simpleAdapter;
    private ArrayList<HashMap<String, String>> passengersList = new ArrayList<HashMap<String, String>>();
    private String editingPassenger = "";
    private ArrayList<CharSequence> mSelectedItems = new ArrayList<CharSequence>();
    private ArrayList<Person> personList = new ArrayList<Person>();
    private CharSequence[] passengers;
    private Utils utils;
    private String date, driver, operation;
    private int driverID, vehicleID, round_trip, tripID;
    private Cursor personCursor;
    private ArrayList<Integer> selectedPassengers;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passengers_by_trips);

        passengerListView = (ListView) findViewById(R.id.passengerList);
        Button ok = (Button) findViewById(R.id.passengerOK);

        Bundle bundle = getIntent().getExtras();
        date = bundle.getString("date");
        round_trip = bundle.getInt("round_trip");
        driver = bundle.getString("driver");
        driverID = bundle.getInt("driverID");
        vehicleID = bundle.getInt("vehicleID");
        operation = bundle.getString("operation");
        tripID = bundle.getInt("tripID");


        //Log.d("cenas", driverID + " " + vehicleID);

        DataWrapper dw = (DataWrapper) bundle.getSerializable("passengersList");
        passengersList = dw.getPassengersList();

        utils = new Utils();

        // Gets all te necessary information from the Person table
        PersonHandler personHandler = new PersonHandler(this);
        personHandler.open();
        personCursor = personHandler.returnAllPersonsData();

        personCursor.moveToFirst();

        while (!personCursor.isAfterLast()) {
            Person person = new Person(personCursor.getInt(0), personCursor.getString(1), personCursor.getInt(2));
            personList.add(person);

            personCursor.moveToNext();
        }
        personHandler.close();


        simpleAdapter = new SimpleAdapter(this, passengersList, R.layout.custom_passengers_layout,
                new String[]{"line1", "line2"},
                new int[]{R.id.line_a, R.id.line_b});

        passengerListView.setAdapter(simpleAdapter);

        ok.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_passengers_by_trips, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add_passenger) {
            addNewPassenger();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean isAlreadySelected(String name, ArrayList<HashMap<String, String>> listSelected) {

        boolean selected = false;

        for (HashMap<String, String> pair : listSelected) {

            if (pair.containsValue(name))
                selected = true;
        }

        return selected;
    }

    private void addNewPassenger() {

        selectedPassengers = new ArrayList<Integer>();

        // Lista de passageiros (o tamanho depende do condutor e dos passageiros ja escolhidos)
        int numberOfSelected = passengersList.size();
        passengers = new CharSequence[personCursor.getCount() - 1 - numberOfSelected];

        personCursor.moveToFirst();
        int i = 0;

        while (!personCursor.isAfterLast()) {

            if (!personCursor.getString(1).equalsIgnoreCase(driver) &&
                    !isAlreadySelected(personCursor.getString(1), passengersList)) {
                passengers[i] = personCursor.getString(1);
                i++;
            }

            personCursor.moveToNext();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        if (i != 0) {

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
        } else {
            builder.setTitle("Add Passengers")
                    .setMessage("All the persons have a role in this trip.")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
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

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.passengerOK:
                Intent intent = new Intent(PassengersByTrips.this, Trips.class);

                intent.putExtra("tripID", tripID);
                intent.putExtra("date", date);
                intent.putExtra("round_trip", round_trip);
                intent.putExtra("driverID", driverID);
                intent.putExtra("vehicleID", vehicleID);
                intent.putExtra("fromPassengers", true);
                intent.putExtra("operation", operation);

                intent.putExtra("passengersList", new DataWrapper(passengersList));
                startActivity(intent);
                finish();

                break;
        }

    }

    private void removefromPassengers(String name) {

        HashMap<String, String> item = null;

        for (HashMap<String, String> pair : passengersList) {

            if (pair.containsValue(name))
                item = pair;
        }

        if (item != null)
            passengersList.remove(item);

        simpleAdapter = new SimpleAdapter(this, passengersList, R.layout.custom_passengers_layout,
                new String[]{"line1", "line2"},
                new int[]{R.id.line_a, R.id.line_b});

        passengerListView.setAdapter(simpleAdapter);
    }

    // Remover passageiro da lista
    public void removeElementOnClickHandler(View v) {

        LinearLayout rl = (LinearLayout) v.getParent();
        TextView tv = (TextView) rl.findViewById(R.id.line_a);
        String name = tv.getText().toString();

        removefromPassengers(name);
    }

    // dialogo que informa que os segmentos escolhidos formam uma rota que ainda nao
    // foi definida e pergunta se quer definir
    private void showInexistingRouteDialog() {
        AlertDialog.Builder newDialog = new AlertDialog.Builder(this);

        newDialog.setTitle("Inexistent route")
                .setMessage("This route does not exist. Do you wish to add it?")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(PassengersByTrips.this, AddEditRoute.class);

                        intent.putExtra("segments", mSelectedItems);

                        startActivityForResult(intent, 0);
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

    private boolean segmentsOfExistingRoute(ArrayList<CharSequence> segments) {

        ArrayList<Route> routes = utils.getRoutes(this);

        ComposedRouteHandler composedRouteHandler = new ComposedRouteHandler(getApplicationContext());

        composedRouteHandler.open();

        boolean found = false;

        for (int pos = 0; pos < routes.size() && !found; pos++) {

            Cursor c = composedRouteHandler.getAllRouteSegments(routes.get(pos).getID());

            c.moveToFirst();
            ArrayList segments_in_route = new ArrayList();

            while (!c.isAfterLast()) {

                segments_in_route.add(c.getString(0));
                c.moveToNext();
            }

            boolean equal = true;

            if (segments_in_route.size() == segments.size()) {

                for (int i = 0; i < segments.size() && equal; i++) {

                    if (segments_in_route.contains(segments.get(i)) == false)
                        equal = false;
                }
            } else
                equal = false;

            if (equal)
                found = true;
        }

        composedRouteHandler.close();

        return found;
    }


    // Editar os segmentos/Rotas escolhidas
    public void editElementOnClickHandler(View v) {

        // Determinar quem e o utilizador a ser editado
        LinearLayout line_layout = (LinearLayout) v.getParent();
        TextView text_test = (TextView) line_layout.findViewById(R.id.line_a);

        editingPassenger = text_test.getText().toString();

        mSelectedItems = new ArrayList<CharSequence>();

        // Segments

        SegmentHandler segmentHandler = new SegmentHandler(this);
        segmentHandler.open();

        Cursor c1 = segmentHandler.returnSegments();
        c1.moveToFirst();

        final CharSequence[] segments_list = new CharSequence[c1.getCount()];
        int i = 0;

        while (!c1.isAfterLast()) {
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

                            showInexistingRouteDialog();
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

    // Recebe o nome da nova rota adicionada.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0 && resultCode == Activity.RESULT_OK) {

            String newRoute = data.getStringExtra("name");

            for (HashMap<String, String> lines : passengersList) {

                if (lines.get("line1").equalsIgnoreCase(editingPassenger)) {
                    lines.put("line2", newRoute);
                }
            }

            simpleAdapter = new SimpleAdapter(this, passengersList, R.layout.custom_passengers_layout,
                    new String[]{"line1", "line2"},
                    new int[]{R.id.line_a, R.id.line_b});

            passengerListView.setAdapter(simpleAdapter);

        }
    }

    private Person getSelectedPerson(ArrayList<Person> listOfPersons, String name) {

        Person p = null;

        for (Person person : listOfPersons) {

            if (person.getName().equalsIgnoreCase(name))
                p = person;
        }

        return p;
    }

    // Adiciona elementos a lista
    private void setPassengers(ArrayList<Integer> selected, ArrayList<Person> listOfPersons) {

        HashMap<String, String> item;

        RouteHandler routeHandler = new RouteHandler(this);

        routeHandler.open();

        for (int pos : selected) {
            item = new HashMap<String, String>();

            Person p = getSelectedPerson(listOfPersons, passengers[pos].toString());

            Route route = routeHandler.getRoute(p.getUsual_route());

            item.put("line1", p.getName());
            item.put("line2", route.getName());

            passengersList.add(item);
        }

        routeHandler.close();

        simpleAdapter = new SimpleAdapter(this, passengersList, R.layout.custom_passengers_layout,
                new String[]{"line1", "line2"},
                new int[]{R.id.line_a, R.id.line_b});

        passengerListView.setAdapter(simpleAdapter);
    }


}
