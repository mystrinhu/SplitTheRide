package com.splitTheRide.settings;

import java.util.ArrayList;
import java.util.Locale;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.splitTheRide.database.AccountHandler;
import com.splitTheRide.database.PersonHandler;
import com.splitTheRide.database.RouteHandler;
import com.splitTheRide.entities.Route;
import com.splitTheRide.splittheride.R;

public class AddEditPerson extends ActionBarActivity implements OnClickListener{

	private Button ok, cancel;
	private EditText name;
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
		usual_route = (Spinner) findViewById(R.id.spinner_usual_route);

		ok.setOnClickListener(this);
		cancel.setOnClickListener(this);
		
		editPerson = getIntent();
		ActionBar ab = getSupportActionBar();

        routeList = getRoutes();

        routeAdapter = new ArrayAdapter<Route>(this, android.R.layout.simple_list_item_1, routeList);
        usual_route.setAdapter(routeAdapter);


		if (editPerson.getStringExtra("name") != null) {
			
			ab.setTitle(R.string.edit_person);
			ok.setText(R.string.edit);
			
			name.setText(editPerson.getStringExtra("name"));

            RouteHandler routeHandler = new RouteHandler(this);

            routeHandler.open();

            Route route = routeHandler.getRoute(editPerson.getIntExtra("usual_route", 0));

            routeHandler.close();

            usual_route.setSelection(indexOfRoute(route));
			person_id = editPerson.getIntExtra("id", 0);

		}else{
		
			ab.setTitle(R.string.add_person);
			ok.setText(R.string.add);
		}

	}
		

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		switch(v.getId()){
			case R.id.ok:	String getName = name.getText().toString();
                            Route selected_route = (Route) usual_route.getSelectedItem();
							
							handler = new PersonHandler(this);
							handler.open();

							if((ok.getText().toString().equalsIgnoreCase(getResources().getString(R.string.add)) ||
								(ok.getText().toString().equalsIgnoreCase(getResources().getString(R.string.edit)) &&
										!editPerson.getStringExtra("name").equalsIgnoreCase(getName)))
									&& handler.nameExists(getName)) {
								showMessage(getResources().getString(R.string.error), getResources().getString(R.string.initials_in_use));
							}
							else{
								if (getName.length() == 0) {
									showMessage(getResources().getString(R.string.error), getResources().getString(R.string.all_fields_mandatory));
								}else{
								
									if(ok.getText().toString().equalsIgnoreCase(getResources().getString(R.string.add))){
										
										Cursor persons = handler.returnAllPersonsData();

										handler.insertPerson(getName, selected_route.getID());

										Cursor id = handler.getIDfromName(getName);
										
										addAccounts(persons, id);
										
										showMessage(getResources().getString(R.string.person), getResources().getString(R.string.person_added));
									
										name.setText("");
									
									}else{

										if (handler.editPerson(person_id, getName, selected_route.getID())) {
											showMessage(getResources().getString(R.string.person), getName + " "+ getResources().getString(R.string.edit_success));
										
											name.setText("");
										}else 
											showMessage(getResources().getString(R.string.error), getResources().getString(R.string.edit_error) + " "+ getName);
									}
								}
							}
							
							handler.close();
							
							break;
							
			case R.id.cancel:	name.setText("");
				
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
		dialog.setButton(DialogInterface.BUTTON_POSITIVE, getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
			
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

    private int indexOfRoute(Route route){

        int found = -1;

        for(int i=0; i<routeList.size(); i++ ){

            Route r = routeList.get(i);
            if(r.getID()==route.getID())
                found=i;
        }

        return found;
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
