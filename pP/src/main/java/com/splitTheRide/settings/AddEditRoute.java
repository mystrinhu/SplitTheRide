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
			
			ab.setTitle(R.string.edit_route);
			ok.setText(R.string.edit);
			
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
		
			ab.setTitle(R.string.add_route);
			ok.setText(R.string.add);
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
									showMessage(getResources().getString(R.string.error), getResources().getString(R.string.route_name_mandatory));
								}else{
									if((ok.getText().toString().equalsIgnoreCase(getResources().getString(R.string.add)) ||
										(ok.getText().toString().equalsIgnoreCase(getResources().getString(R.string.edit)) && !editRoute.getStringExtra("name").equalsIgnoreCase(getName)))
										&& handler.routeNameExists(getName)){
										showMessage(getResources().getString(R.string.error), getResources().getString(R.string.name_in_use));
									}else if(ok.getText().toString().equalsIgnoreCase(getResources().getString(R.string.add))){
										handler.insertRoute(getName);
										showMessage(getResources().getString(R.string.route), getResources().getString(R.string.route_added));
									
										name.setText("");
									
									}else if(ok.getText().toString().equalsIgnoreCase(getResources().getString(R.string.edit))){
									
										if(handler.editRoute(route_id, getName)){
											showMessage(getResources().getString(R.string.route), getName + " " + getResources().getString(R.string.edit_success));
										
											name.setText("");
										}else 
											showMessage(getResources().getString(R.string.error), getResources().getString(R.string.edit_error)+" "+ getName);
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
        
        builder.setTitle(R.string.select_segments);
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
        .setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
        	@Override
        	public void onClick(DialogInterface dialog, int id) {
        		String text = "";

        		for(int i: selectedItems){
        			text = text.concat(items[i]+"\n");
        		}
        		
        		segments.setText(text);
        	}
        })
        .setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
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
		dialog.setButton(DialogInterface.BUTTON_POSITIVE, getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
			
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
