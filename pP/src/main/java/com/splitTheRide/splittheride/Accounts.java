package com.splitTheRide.splittheride;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.splitTheRide.database.AccountHandler;
import com.splitTheRide.entities.Person;

public class Accounts extends ActionBarActivity implements OnItemClickListener {
	
	private ListView accounts;
	private AccountHandler handler;
	private ArrayAdapter<Person> adapter;
	private List<Person> personList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_accounts);
		
		accounts = (ListView) findViewById(R.id.accounts_list);
		
		handler = new AccountHandler(this);
		handler.open();
		
		Cursor c = handler.selectMovements();
		
		c.moveToFirst();
		
		personList = new ArrayList<Person>();		
		
	    while (!c.isAfterLast()) {
	    	
	    	Person person = new Person(c.getInt(0), c.getString(1), c.getString(2));
	    	personList.add(person);
	    	
	    	c.moveToNext();
	    }
		
	    handler.close();
	    	    
	    adapter = new ArrayAdapter<Person>(this, android.R.layout.simple_list_item_1, personList);
	    		
	    accounts.setAdapter(adapter);    
	    accounts.setOnItemClickListener(this);
	
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		
		Intent intent = new Intent(Accounts.this, AccountValues.class);
		intent.putExtra("id", personList.get(position).getId());
		
		startActivity(intent);
	}
}
