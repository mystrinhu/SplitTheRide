package com.splitTheRide.trips;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.splitTheRide.database.TripHandler;
import com.splitTheRide.splittheride.R;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

@SuppressLint("SimpleDateFormat")
public class CalendarView extends ActionBarActivity {

	private CaldroidFragment caldroidFragment;
	private CaldroidFragment dialogCaldroidFragment;
	private TripHandler handler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calendar);
		
		final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

		// Setup caldroid fragment
		caldroidFragment = new CaldroidFragment();
		
		// If Activity is created after rotation
		if (savedInstanceState != null) {
			caldroidFragment.restoreStatesFromKey(savedInstanceState,
					"CALDROID_SAVED_STATE");
		}
		// If activity is created from fresh
		else {
			Bundle args = new Bundle();
			Calendar cal = Calendar.getInstance();
			args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
			args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
			args.putBoolean(CaldroidFragment.ENABLE_SWIPE, true);
			args.putBoolean(CaldroidFragment.SIX_WEEKS_IN_CALENDAR, true);

			// Uncomment this to customize startDayOfWeek
			// args.putInt(CaldroidFragment.START_DAY_OF_WEEK,
			// CaldroidFragment.TUESDAY); // Tuesday
			caldroidFragment.setArguments(args);
		}

		//setCustomResourceForDates();

		// Attach to the activity
		FragmentTransaction t = getSupportFragmentManager().beginTransaction();
		t.replace(R.id.calendar, caldroidFragment);
		t.commit();
		
		// Setup listener
		final CaldroidListener listener = new CaldroidListener() {

			@Override
			public void onSelectDate(final Date date, View view) {

				final Intent trips = new Intent(CalendarView.this, Trips.class);
				
				trips.putExtra("date", formatter.format(date));
				
				handler = new TripHandler(getApplicationContext());
				handler.open();

				final Cursor tripsInDate = handler.tripsInDate(formatter.format(date));


				if (tripsInDate.getCount() > 0) {

					final CharSequence options[] = new CharSequence[]{"Edit existing trip", "Add new trip"};

					AlertDialog.Builder builder = new AlertDialog.Builder(CalendarView.this);
					builder.setTitle("Select an option");
					builder.setItems(options, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
					        // the user clicked on colors[which]
							switch (which) {

								case 0:
									Intent tripsDate = new Intent(CalendarView.this, TripsInDate.class);

									tripsDate.putExtra("date", formatter.format(date));
									startActivity(tripsDate);

									break;

								case 1:
									trips.putExtra("operation", "Save");
									startActivity(trips);
									break;
							}

						}
					});
					builder.show();
					
				}else{
					trips.putExtra("operation", "Save");
					startActivity(trips);
				}

				handler.close();
			}

			@Override
			public void onChangeMonth(int month, int year) {
				//String text = "month: " + month + " year: " + year;
				//Toast.makeText(getApplicationContext(), text,
				//		Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onLongClickDate(Date date, View view) {
				
			}

			@Override
			public void onCaldroidViewCreated() {
//				if (caldroidFragment.getLeftArrowButton() != null) {
//					Toast.makeText(getApplicationContext(),
//							"Caldroid view is created", Toast.LENGTH_SHORT)
//							.show();
//				}
			}
		};

		// Setup Caldroid
		caldroidFragment.setCaldroidListener(listener);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.calendar, menu);
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
	
	private void setCustomResourceForDates() {
		Calendar cal = Calendar.getInstance();

		// Min date is last 7 days
		cal.add(Calendar.DATE, -5);
		Date blueDate = cal.getTime();

		// Max date is next 7 days
		cal = Calendar.getInstance();
		cal.add(Calendar.DATE, 16);
		Date greenDate = cal.getTime();

		if (caldroidFragment != null) {
			caldroidFragment.setBackgroundResourceForDate(R.color.blue,
					blueDate);
			caldroidFragment.setBackgroundResourceForDate(R.color.green,
					greenDate);
			caldroidFragment.setTextColorForDate(R.color.white, blueDate);
			caldroidFragment.setTextColorForDate(R.color.white, greenDate);
		}
	}
	
	/**
	 * Save current states of the Caldroid here
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);

		if (caldroidFragment != null) {
			caldroidFragment.saveStatesToKey(outState, "CALDROID_SAVED_STATE");
		}

		if (dialogCaldroidFragment != null) {
			dialogCaldroidFragment.saveStatesToKey(outState,
					"DIALOG_CALDROID_SAVED_STATE");
		}
	}
}
