package com.example.settings;

import java.util.ArrayList;
import java.util.Locale;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.custom.RouteListAdapter;
import com.example.database.AccountHandler;
import com.example.database.PersonHandler;
import com.example.database.RouteHandler;
import com.example.entities.Person;
import com.example.entities.Route;
import com.example.pp.R;

public class AddEditPerson extends ActionBarActivity implements OnClickListener{

	private Button ok, cancel;
	private EditText name, short_name;
    private Spinner usual_route;
	private PersonHandler handler;
	private int person_id;
	private Intent intent, editPerson;
    private ArrayAdapter<Route> routeAdapter;
    private ArrayList<Route> routeList;
	
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_person_layout);
		
		ok = (Button) findViewById(R.id.ok);
		cancel = (Button) findViewById(R.id.cancel);
		
		name = (EditText) findViewById(R.id.name);
		short_name = (EditText) findViewById(R.id.short_name);
		usual_route = (Spinner) findViewById(R.id.spinner_usual_route);

		ok.setOnClickListener(this);
		cancel.setOnClickListener(this);
		
		editPerson = getIntent();
		ActionBar ab = getSupportActionBar();

        routeList = getRoutes();

        routeList = getRoutes();
        routeAdapter = new ArrayAdapter<Route>(this, android.R.layout.simple_list_item_1, routeList);
        usual_route.setAdapter(routeAdapter);

		
		if(editPerson.getStringExtra("name") != null || editPerson.getStringExtra("short_name") != null){
			
			ab.setTitle("Edit Person");
			ok.setText("Edit");
			
			name.setText(editPerson.getStringExtra("name"));
			short_name.setText(editPerson.getStringExtra("short_name"));

			person_id = editPerson.getIntExtra("id", 0);

		}else{
		
			ab.setTitle("Add Person");
			ok.setText("Add");
		}
		
		
	}
		

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		switch(v.getId()){
			case R.id.ok:	String getName = name.getText().toString();
							String getSName = short_name.getText().toString();
                            Log.d("route", "" + usual_route.getSelectedItem());

                            // Ando por aqui
							
							handler = new PersonHandler(this);
							handler.open();
							
							getSName = getSName.toUpperCase(Locale.US);
							
							if((ok.getText().toString().equalsIgnoreCase("Add") || 
								(ok.getText().toString().equalsIgnoreCase("Edit") &&
								 !editPerson.getStringExtra("short_name").equalsIgnoreCase(getSName)))
								&& handler.shortNameExists(getSName)){
								showMessage("Error", "These initials are already in use. Choose other initials.");
							}
							else{
								if(getName.length() == 0 || getSName.length() == 0){
									showMessage("Error", "All fields are mandatory");
								}else{
								
									if(ok.getText() == "Add"){
										
										Cursor persons = handler.returnAllPersonsData();
										
										//handler.insertPerson(getName, getSName, Integer.parseInt(getUsualRoute));
										
										Cursor id = handler.getIDfromShortName(getSName);
										
										addAccounts(persons, id);
										
										showMessage("Person", "Person inserted");
									
										name.setText("");
										short_name.setText("");
									
									}else{
									
										/*if(handler.editPerson(person_id, getName, getSName, Integer.parseInt(getUsualRoute))){
											showMessage("Person", getName + " edited successfully");
										
											name.setText("");
											short_name.setText("");
										}else 
											showMessage("Error", "It was not possible to edit "+ getName);*/
									}
								}
							}
							
							handler.close();
							
							break;
							
			case R.id.cancel:	name.setText("");
								short_name.setText("");
				
								intent = new Intent(this, PersonsView.class);
			
								startActivity(intent);
								finish();
				
								break;
		}
	}
	
	
	private void addAccounts(Cursor data, Cursor id){
		
		int new_person_id = 0;
		
		id.moveToFirst();
		
		while(!id.isAfterLast()){
			new_person_id = id.getInt(0);
			id.moveToNext();
		}
		
		AccountHandler account_handler = new AccountHandler(this);
		account_handler.open();
		
		data.moveToFirst();		
		
	    while (!data.isAfterLast()) {
	    	
	    	if(new_person_id != data.getInt(0)){
	    		account_handler.insertAccount(data.getInt(0), new_person_id);
		    	account_handler.insertAccount(new_person_id, data.getInt(0));
	    	}
	    	
	    	data.moveToNext();
	    }
	    
	    account_handler.close();
	    
	}
	
	
	private void showMessage(final String title, String message){
		
		AlertDialog dialog = new AlertDialog.Builder(this).create();
		dialog.setTitle(title);
		dialog.setMessage(message);
		dialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				if(!title.equalsIgnoreCase("error")){
					Intent intent = new Intent(getApplication(), PersonsView.class);
					
					startActivity(intent);
					finish();
				}
			}
		});
		
		dialog.show();
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
	
}
