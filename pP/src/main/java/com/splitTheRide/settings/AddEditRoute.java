package com.splitTheRide.settings;

import java.util.ArrayList;
import java.util.Arrays;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.splitTheRide.database.ComposedRouteHandler;
import com.splitTheRide.database.RouteHandler;
import com.splitTheRide.database.SegmentHandler;
import com.splitTheRide.splittheride.R;

public class AddEditRoute extends ActionBarActivity implements OnClickListener{

	private Button ok, cancel;
	private EditText name; 
	private TextView segments;
	private RouteHandler handler;
	private ComposedRouteHandler composedHandler;
	private SegmentHandler segmentHandler;
	private int route_id = -1;
	private Intent intent, editRoute;
	
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_route_layout);
		
		ok = (Button) findViewById(R.id.ok);
		cancel = (Button) findViewById(R.id.cancel);
		
		name = (EditText) findViewById(R.id.route_name);
		segments = (TextView) findViewById(R.id.route_segments);
		
		ok.setOnClickListener(this);
		cancel.setOnClickListener(this);
		
		editRoute = getIntent();
		ActionBar ab = getSupportActionBar();
		
		
		if(editRoute.getStringExtra("name") != null){
			
			ab.setTitle("Edit Route");
			ok.setText("Edit");
			
			name.setText(editRoute.getStringExtra("name"));
			route_id = editRoute.getIntExtra("id", 0);
			
			String text = "";
			if(editRoute.getStringArrayExtra("segments").length != 0){
				for(String s: editRoute.getStringArrayExtra("segments")){
					text = text.concat(s+"\n");
				}
    		
    			segments.setText(text);
			}
			
		}else{
		
			ab.setTitle("Add Route");
			ok.setText("Add");
		}
	}
		

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		switch(v.getId()){
			case R.id.ok:	String getName = name.getText().toString();
							
							handler = new RouteHandler(this);
							handler.open();
							
							if(getName.length() == 0){
									showMessage("Error", "Route name is mandatory");
								}else{
									if((ok.getText().toString().equalsIgnoreCase("Add") || 
										(ok.getText().toString().equalsIgnoreCase("Edit") && !editRoute.getStringExtra("name").equalsIgnoreCase(getName)))
										&& handler.routeNameExists(getName)){
										showMessage("Error", "This name is already in use. Choose another name.");
									}else if(ok.getText() == "Add"){
										handler.insertRoute(getName);
										showMessage("Route", "Route inserted");
									
										name.setText("");
									
									}else if(ok.getText() == "Edit"){
									
										if(handler.editRoute(route_id, getName)){
											showMessage("Route", getName + " edited successfully");
										
											name.setText("");
										}else 
											showMessage("Error", "It was not possible to edit "+ getName);
									}
								}

							
							handler.close();
							
							handler.open();
							
							Cursor c = handler.getRouteID(getName);
							
							c.moveToFirst();
							
							
							while (!c.isAfterLast()) {
						    	
						    	route_id = c.getInt(0);
						    	c.moveToNext();
						    }
							
							handler.close();
							
							if(route_id != -1){
								
								segmentHandler = new SegmentHandler(this);
								segmentHandler.open();
								
								ArrayList<Integer> idsToAdd = new ArrayList<Integer>();
															
								for(String s : segments.getText().toString().split("[\\r\\n]+")){
									
									c = segmentHandler.getSegmentID(s);
									
									c.moveToFirst();
									
									while (!c.isAfterLast()) {
								    	
								    	idsToAdd.add(c.getInt(0));
								    	c.moveToNext();
								    }
									
								}
								
								segmentHandler.close();
								
								composedHandler = new ComposedRouteHandler(this);
								composedHandler.open();
								
								composedHandler.removeComposedRoute(route_id);
								
								for(int id: idsToAdd){
									
									composedHandler.insertComposedRoute(route_id, id);
								}
								
								composedHandler.close();
							}
							
							
							break;
							
			case R.id.cancel:	name.setText("");
				
								intent = new Intent(this, RoutesView.class);
			
								startActivity(intent);
								finish();
				
								break;
		}
	}
	
	public void addEditSegments(View v){
		
		segmentHandler = new SegmentHandler(this);
		segmentHandler.open();
		
		Cursor c = segmentHandler.returnSegments();
		
		c.moveToFirst();
		
		final String[] items = new String[c.getCount()];
		int i=0;
		
	    while (!c.isAfterLast()) {
	    	
	    	items[i] = c.getString(1);
	    	i++;
	    	c.moveToNext();
	    }
		
		segmentHandler.close();

        // arraylist to keep the selected items
        final ArrayList<Integer> selectedItems=new ArrayList<Integer>();
        
        boolean[] checkeditems = new boolean[items.length];
        Arrays.fill(checkeditems, false);
        
        for(String s: segments.getText().toString().split("[\\r\\n]+")){
        	
        	
        	if(Arrays.asList(items).indexOf(s)>=0){
        		selectedItems.add(Arrays.asList(items).indexOf(s));
            	checkeditems[Arrays.asList(items).indexOf(s)] = true;
        	}
        }
         
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        AlertDialog dialog;
        
        builder.setTitle("Select the desired segments");
        builder.setMultiChoiceItems(items, checkeditems, new DialogInterface.OnMultiChoiceClickListener() {
       
        	@Override
        	public void onClick(DialogInterface dialog, int indexSelected, boolean isChecked) {
        		if (isChecked) {
        			// If the user checked the item, add it to the selected items
        			// write your code when user checked the checkbox 
        			selectedItems.add(indexSelected);
        		} else if (selectedItems.contains(indexSelected)) {
        			// Else, if the item is already in the array, remove it 
        			// write your code when user Uchecked the checkbox 
        			selectedItems.remove(Integer.valueOf(indexSelected));
        		}
        	}
        })
        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
        	@Override
        	public void onClick(DialogInterface dialog, int id) {
        		String text = "";

        		for(int i: selectedItems){
        			text = text.concat(items[i]+"\n");
        		}
        		
        		segments.setText(text);
        	}
        })
        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
        	@Override
        	public void onClick(DialogInterface dialog, int id) {
        		//  Your code when user clicked on Cancel
           
        	}
        });

        dialog = builder.create();
        dialog.show();
		
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
					Intent intent = new Intent(getApplication(), RoutesView.class);
					
					startActivity(intent);
					finish();
				}
			}
		});
		
		dialog.show();
	}

	
}
