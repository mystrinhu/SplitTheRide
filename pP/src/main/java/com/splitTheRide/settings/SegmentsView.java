package com.splitTheRide.settings;

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

import com.splitTheRide.custom.SegmentListAdapter;
import com.splitTheRide.database.SegmentHandler;
import com.splitTheRide.entities.Segment;
import com.splitTheRide.splittheride.R;

public class SegmentsView extends ActionBarActivity{
	
	private ListView listview;
	private SegmentHandler handler;
	private SegmentListAdapter adapter;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.segments_list_layout);
		
		listview = (ListView) findViewById(R.id.segmentsView);
		
		handler = new SegmentHandler(this);
		handler.open();
		
			
		Cursor c = handler.returnSegments();
		
		c.moveToFirst();
		
		List<Segment> segmentList = new ArrayList<Segment>();		
		
	    while (!c.isAfterLast()) {
	    	
	    	Segment segment = new Segment(c.getInt(0), c.getString(1), c.getInt(2), c.getDouble(3));
	    	segmentList.add(segment);
	    	c.moveToNext();
	    }
		
	    handler.close();
	    	    
	    adapter = new SegmentListAdapter(this, R.layout.custom_line_list_view, segmentList);
	    
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
	        case R.id.action_addview:	Intent addSegment = new Intent(this, AddEditSegment.class);
			
										startActivity(addSegment);
										finish();
	            						return true;
	        default:	return super.onOptionsItemSelected(item);
	    }
	}
	
	
	public void editOnClickHandler(View v) {
		
		Segment segment = (Segment) v.getTag();
		
		Intent editSegment = new Intent(this, AddEditSegment.class);
		
		editSegment.putExtra("id", segment.getId());
		editSegment.putExtra("name", segment.getName());
		editSegment.putExtra("distance", segment.getDistance());
		editSegment.putExtra("cost", segment.getCost());
				
		startActivity(editSegment);
		finish();
	}
	
	
	public void removeOnClickHandler(View v) {
		
		final Segment segment = (Segment) v.getTag();
		
		AlertDialog dialog = new AlertDialog.Builder(this).create();
		dialog.setTitle("Delete Segment");
		dialog.setMessage("Are you sure you want to delete this segment?");
		dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Yes", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				SegmentHandler handler = new SegmentHandler(getApplicationContext());
				handler.open();
				if(handler.removeSegment(segment.getId()))
					adapter.remove(segment);
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
