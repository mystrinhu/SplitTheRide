package com.splitTheRide.settings;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.splitTheRide.database.SegmentHandler;
import com.splitTheRide.splittheride.R;

public class AddEditSegment extends ActionBarActivity implements OnClickListener{

	private Button ok, cancel;
	private EditText name, distance, cost;
	private SegmentHandler handler;
	private int segment_id;
	private Intent intent, editSegment;
	
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_segment_layout);
		
		ok = (Button) findViewById(R.id.ok);
		cancel = (Button) findViewById(R.id.cancel);
		
		name = (EditText) findViewById(R.id.segment_name);
		distance = (EditText) findViewById(R.id.segment_distance);
		cost = (EditText) findViewById(R.id.segment_cost);
		
		ok.setOnClickListener(this);
		cancel.setOnClickListener(this);
		
		editSegment = getIntent();
		ActionBar ab = getSupportActionBar();
		
		
		if(editSegment.getIntExtra("distance", -1) != -1 || editSegment.getDoubleExtra("cost", -1) != -1){
			
			ab.setTitle(R.string.edit_segment);
			ok.setText(R.string.edit);
			
			name.setText(""+editSegment.getStringExtra("name"));
			distance.setText(""+editSegment.getIntExtra("distance", 0));
			cost.setText(""+editSegment.getDoubleExtra("cost", 0));
			segment_id = editSegment.getIntExtra("id", 0);
		}else{
		
			ab.setTitle(R.string.add_segment);
			ok.setText(R.string.add);
		}
		
		
	}
		

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		switch(v.getId()){
			case R.id.ok:	String getName = name.getText().toString();
							String getDistance = distance.getText().toString();
							String getCost = cost.getText().toString();
							
							handler = new SegmentHandler(this);
							handler.open();
							
							int distance_value = Integer.parseInt(getDistance);
							double cost_value = Double.parseDouble(getCost);
							
							if(getDistance.length() == 0 || getCost.length() == 0 || getName.length() == 0){
								showMessage(getResources().getString(R.string.error), getResources().getString(R.string.all_fields_mandatory));
							}else{

								if(ok.getText().toString().contentEquals(getResources().getString(R.string.add))){
									handler.insertSegment(getName, distance_value, cost_value);
									showMessage(getResources().getString(R.string.segment), getResources().getString(R.string.segment_added));

									name.setText("");
									distance.setText("");
									cost.setText("");

								}else{
								
									if(handler.editSegment(segment_id, getName, distance_value, cost_value)){
										showMessage(getResources().getString(R.string.segment), getResources().getString(R.string.segment_edited));

										name.setText("");
										distance.setText("");
										cost.setText("");
								}else 
									showMessage(getResources().getString(R.string.error), getResources().getString(R.string.segment_error));
								}
							}
							
							
							handler.close();
							
							break;
							
			case R.id.cancel:	distance.setText("");
								cost.setText("");
				
								intent = new Intent(this, SegmentsView.class);
			
								startActivity(intent);
								finish();
				
								break;
		}
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
					Intent intent = new Intent(getApplication(), SegmentsView.class);
					
					startActivity(intent);
					finish();
				}
			}
		});
		
		dialog.show();
	}

}
