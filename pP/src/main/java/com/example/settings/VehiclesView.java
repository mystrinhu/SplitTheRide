package com.example.settings;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.example.custom.VehicleListAdapter;
import com.example.database.PersonHandler;
import com.example.database.VehicleHandler;
import com.example.entities.Vehicle;
import com.example.pp.R;

public class VehiclesView extends ActionBarActivity{
	
	private ListView listview;
	private VehicleHandler handler;
	private VehicleListAdapter adapter;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.vehicles_list_layout);
		
		listview = (ListView) findViewById(R.id.vehiclesView);
		
		handler = new VehicleHandler(this);
		handler.open();
		
			
		Cursor c = handler.returnData();
		
		c.moveToFirst();
		
		List<Vehicle> vehicleList = new ArrayList<Vehicle>();		
		
	    while (!c.isAfterLast()) {
	    	
	    	Vehicle vehicle = new Vehicle(c.getInt(0), c.getString(1), c.getDouble(2), c.getInt(3));
	    	vehicleList.add(vehicle);
	    	c.moveToNext();
	    }
		
	    handler.close();
	    	    
	    adapter = new VehicleListAdapter(this, R.layout.custom_line_list_view, vehicleList);
	    
	    listview.setAdapter(adapter); 
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.listview, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.action_addview:	Intent addVehicle = new Intent(this, AddEditVehicle.class);
			
										startActivity(addVehicle);
										finish();
	            						return true;
	        default:	return super.onOptionsItemSelected(item);
	    }
	}
	
	
	public void editOnClickHandler(View v) {
		
		Vehicle vehicle = (Vehicle) v.getTag();
		
		Intent editVehicle = new Intent(this, AddEditVehicle.class);
		
		PersonHandler handler = new PersonHandler(this);
		
		handler.open();
		
		Cursor c = handler.getShortNamefromID(vehicle.getPersonID());
		
		c.moveToFirst();
		String short_name = c.getString(0);
		handler.close();

		editVehicle.putExtra("id", vehicle.getId());
		editVehicle.putExtra("name", vehicle.getName());
		editVehicle.putExtra("consumption", vehicle.getConsumption());
		editVehicle.putExtra("person_sname", short_name);
		
		startActivity(editVehicle);
		finish();
	}
	
	
	public void removeOnClickHandler(View v) {
		
		final Vehicle vehicle = (Vehicle) v.getTag();
		
		AlertDialog dialog = new AlertDialog.Builder(this).create();
		dialog.setTitle("Delete vehicle");
		dialog.setMessage("Are you sure you want to delete this vehicle?");
		dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Yes", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				VehicleHandler handler = new VehicleHandler(getApplicationContext());
				handler.open();
				if(handler.removeVehicle(vehicle.getId()))
					adapter.remove(vehicle);
				handler.close();
			}
		});
		
		dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "No", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
			}
		});
		
		dialog.show();
	}

}
