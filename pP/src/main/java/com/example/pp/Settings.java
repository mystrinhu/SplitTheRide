package com.example.pp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.settings.PersonsView;
import com.example.settings.RoutesView;
import com.example.settings.SegmentsView;
import com.example.settings.VehiclesView;

public class Settings extends ActionBarActivity implements OnItemClickListener{

	private ListView listview;
	private String[] settingOptions = {"Persons", "Vehicles", "Routes", "Segments"}; 
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		
		
		listview = (ListView) findViewById(R.id.settingsList);
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
	              android.R.layout.simple_list_item_1, android.R.id.text1, settingOptions);
		
		listview.setAdapter(adapter); 
		
		listview.setOnItemClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.settings, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		switch(position){
			case 0:	Intent persons = new Intent(Settings.this, PersonsView.class);
					startActivity(persons);
			
					break;
					
			case 1: Intent vehicles = new Intent(Settings.this, VehiclesView.class);
					startActivity(vehicles);
			
					break;
					
			case 2: Intent routes = new Intent(Settings.this, RoutesView.class);
					startActivity(routes);
	
					break;
					
			case 3: Intent segments = new Intent(Settings.this, SegmentsView.class);
					startActivity(segments);

					break;
		}
	}
}
