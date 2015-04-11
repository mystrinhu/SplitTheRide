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

import com.example.custom.PersonListAdapter;
import com.example.database.AccountHandler;
import com.example.database.PersonHandler;
import com.example.entities.Person;
import com.example.pp.R;

public class PersonsView extends ActionBarActivity{
	
	private ListView listview;
	private PersonHandler handler;
	private PersonListAdapter adapter;
	
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.person_list_layout);
		
		listview = (ListView) findViewById(R.id.listView1);
		
		handler = new PersonHandler(this);
		handler.open();
		
			
		Cursor c = handler.returnData();
		
		
		c.moveToFirst();
		
		List<Person> personList = new ArrayList<Person>();		
		
	    while (!c.isAfterLast()) {
	    	
	    	Person person = new Person(c.getInt(0), c.getString(1), c.getString(2));
	    	personList.add(person);
	    	c.moveToNext();
	    }
		
	    handler.close();
	    	    
	    adapter = new PersonListAdapter(this, R.layout.custom_line_list_view, personList);
	    		
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
	        case R.id.action_addview:	Intent addPerson = new Intent(this, AddEditPerson.class);
			
										startActivity(addPerson);
										finish();
	            						return true;
	            						
	        default:	return super.onOptionsItemSelected(item);
	    }
	}
		
	public void editOnClickHandler(View v) {
		
		Person p = (Person) v.getTag();
		
		Intent editPerson = new Intent(this, AddEditPerson.class);

		editPerson.putExtra("id", p.getId());
		editPerson.putExtra("name", p.getName());
		editPerson.putExtra("short_name", p.getShort());
		
		startActivity(editPerson);
		finish();
	}
	
	public void removeOnClickHandler(View v) {
		
		final Person person = (Person) v.getTag();
		
		AlertDialog dialog = new AlertDialog.Builder(this).create();
		dialog.setTitle("Delete person");
		dialog.setMessage("Are you sure you want to delete this person?");
		dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Yes", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				PersonHandler handler = new PersonHandler(getApplicationContext());
				AccountHandler acc_handler = new AccountHandler(getApplicationContext());
				
				handler.open();
				
				// Remover as entradas na tabela das contas
				acc_handler.open();
				acc_handler.removeAccounts(person.getId());
				acc_handler.close();
				
				if(handler.removePerson(person.getId())){
					adapter.remove(person);
					
				}
				
				acc_handler.close();
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
