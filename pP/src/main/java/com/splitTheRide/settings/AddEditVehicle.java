package com.splitTheRide.settings;

import java.util.ArrayList;
import java.util.List;

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

import com.splitTheRide.database.PersonHandler;
import com.splitTheRide.database.VehicleHandler;
import com.splitTheRide.splittheride.R;

public class AddEditVehicle extends ActionBarActivity implements OnClickListener{

	private Button ok, cancel;
	private EditText name, consumption;
	private Spinner person;
	private PersonHandler p_handler;
	private VehicleHandler handler;
	private int vehicle_id;
	private Intent intent;
	private ArrayAdapter<String> adapter;
	
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_vehicle_layout);
		
		ok = (Button) findViewById(R.id.ok);
		cancel = (Button) findViewById(R.id.cancel);
		person = (Spinner) findViewById(R.id.spinner1);
		name = (EditText) findViewById(R.id.name);
		consumption = (EditText) findViewById(R.id.consumption);
		
		ok.setOnClickListener(this);
		cancel.setOnClickListener(this);
		
		Intent editVehicle = getIntent();
		ActionBar ab = getSupportActionBar();
		
		
		p_handler = new PersonHandler(this);
		
		p_handler.open();
		Cursor person_cursor = p_handler.returnAllPersonsShortName();
		
		person_cursor.moveToFirst();
		
		List<String> personList = new ArrayList<String>();		
		
		personList.add("");
		
	    while (!person_cursor.isAfterLast()) {
	    	
	    	personList.add(person_cursor.getString(0));
	    	person_cursor.moveToNext();
	    }
		
	    p_handler.close();
	    	    
	    adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, personList);
	    adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
         
	    person.setAdapter(adapter);
		
		
		if(editVehicle.getStringExtra("name") != null || editVehicle.getStringExtra("consumption") != null){
			
			ab.setTitle("Edit Vehicle");
			ok.setText("Edit");
			
			name.setText(editVehicle.getStringExtra("name"));
			consumption.setText(""+editVehicle.getDoubleExtra("consumption", 0));
			
			
			person.setSelection(personList.indexOf(editVehicle.getStringExtra("person_sname")));
			
			vehicle_id = editVehicle.getIntExtra("id", 0);
		}else{
		
			ab.setTitle("Add Vehicle");
			ok.setText("Add");
		}
		
		
	}
		

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		switch(v.getId()){
			case R.id.ok:	String getName = name.getText().toString();
							String getConsump = consumption.getText().toString();
							String getOwner = person.getSelectedItem().toString();
							
							double cons = Double.parseDouble(getConsump);
							
							handler = new VehicleHandler(this);
							handler.open();
												
							
							if(getName.length() == 0 || getConsump.length() == 0 || getOwner == ""){
								showMessage("Error", "All fields are mandatory");
							}else{
								
								p_handler.open();
								
								Cursor cursor = p_handler.getIDfromShortName(getOwner);
								cursor.moveToFirst();
								
								int person_id = cursor.getInt(0);
								
								p_handler.close();
								
								if(ok.getText().toString().equalsIgnoreCase(getResources().getString(R.string.add))){
									
									handler.insertVehicle(getName, cons, person_id);
									showMessage("Vehicle", "Vehicle inserted");
										
									name.setText("");
									consumption.setText("");
									person.setSelection(0);
									
								}else{
									
									if(handler.editVehicle(vehicle_id, getName, cons, person_id)){
										showMessage("Vehicle", getName + " edited successfully");
										
										name.setText("");
										consumption.setText("");
										person.setSelection(0);
									}else 
										showMessage("Error", "It was not possible to edit "+ getName);
								}
							}
								
							handler.close();
							
							break;
							
			case R.id.cancel:	name.setText("");
								consumption.setText("");
								person.setSelection(0);
								
								intent = new Intent(this, VehiclesView.class);
			
								startActivity(intent);
								finish();
				
								break;
		}
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
					Intent intent = new Intent(getApplication(), VehiclesView.class);
					
					startActivity(intent);
					finish();
				}
			}
		});
		
		dialog.show();
	}

	
}
