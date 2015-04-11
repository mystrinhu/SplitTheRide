package com.example.custom;

import java.util.List;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import com.example.entities.Person;
import com.example.pp.R;

public class PersonListAdapter extends ArrayAdapter<Person> {

	private List<Person> items;
	private int layoutResourceId;
	private Context context;
	
	
	public PersonListAdapter(Context context, int layoutResourceId, List<Person> items){
		super(context, layoutResourceId, items);
		
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.items = items;
	}
	
	@SuppressLint("ViewHolder")
	public View getView(int position, View convertView, ViewGroup parent){
		
		View row = convertView;
		PersonHolder holder = null;
		
		LayoutInflater inflater = ((Activity) context).getLayoutInflater();
		row = inflater.inflate(layoutResourceId, parent, false);

		holder = new PersonHolder();
		holder.person = items.get(position);
		holder.edit_person = (ImageButton)row.findViewById(R.id.edit);
		holder.remove_person = (ImageButton)row.findViewById(R.id.remove);
		holder.edit_person.setTag(holder.person);
		holder.remove_person.setTag(holder.person);
		
		holder.name = (TextView)row.findViewById(R.id.line_name);

		row.setTag(holder);

		setupItem(holder);
		
		return row;
	}
	
	private void setupItem(PersonHolder holder) {
		holder.name.setText(holder.person.getName());
	}
	
	public static class PersonHolder{
		Person person;
		TextView name;
		ImageButton edit_person;
		ImageButton remove_person;
	}	
}
