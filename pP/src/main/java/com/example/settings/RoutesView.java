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

import com.example.custom.RouteListAdapter;
import com.example.database.ComposedRouteHandler;
import com.example.database.RouteHandler;
import com.example.entities.Route;
import com.example.pp.R;

public class RoutesView extends ActionBarActivity{
	
	private ListView listview;
	private RouteHandler handler;
	private ComposedRouteHandler composedHandler;
	private RouteListAdapter adapter;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.routes_list_layout);
		
		listview = (ListView) findViewById(R.id.routesView);
		
		handler = new RouteHandler(this);
		handler.open();
		
			
		Cursor c = handler.returnRoutes();
		
		c.moveToFirst();
		
		List<Route> routeList = new ArrayList<Route>();		
		
	    while (!c.isAfterLast()) {
	    	
	    	Route route = new Route(c.getInt(0), c.getString(1));
	    	routeList.add(route);
	    	c.moveToNext();
	    }
		
	    handler.close();
	    	    
	    adapter = new RouteListAdapter(this, R.layout.custom_line_list_view, routeList);
	    
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
	        case R.id.action_addview:	Intent addRoute = new Intent(this, AddEditRoute.class);
			
										startActivity(addRoute);
										finish();
	            						return true;
	        default:	return super.onOptionsItemSelected(item);
	    }
	}
	
	
	public void editOnClickHandler(View v) {
		
		Route route = (Route) v.getTag();
		
		Intent editRoute = new Intent(this, AddEditRoute.class);
		
		editRoute.putExtra("id", route.getID());
		editRoute.putExtra("name", route.getName());
		
		composedHandler = new ComposedRouteHandler(this);
		composedHandler.open();
		
		Cursor c = composedHandler.getAllRouteSegments(route.getID());

		String[] segments_names = new String[c.getCount()];
		
		c.moveToFirst();
		int pos=0;
		
		while (!c.isAfterLast()) {
	    	
	    	segments_names[pos]=c.getString(0);
	    	pos++;
	    	c.moveToNext();
	    }
		
		composedHandler.close();
		
		editRoute.putExtra("segments", segments_names);
				
		startActivity(editRoute);
		finish();
	}
	
	
	public void removeOnClickHandler(View v) {
		
		final Route route = (Route) v.getTag();
		
		AlertDialog dialog = new AlertDialog.Builder(this).create();
		dialog.setTitle("Delete route");
		dialog.setMessage("Are you sure you want to delete this route?");
		dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Yes", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				RouteHandler handler = new RouteHandler(getApplicationContext());
				handler.open();
				if(handler.removeRoute(route.getID()))
					adapter.remove(route);
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
