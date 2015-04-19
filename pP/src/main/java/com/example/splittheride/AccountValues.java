package com.example.splittheride;

import com.example.database.AccountHandler;
import com.example.pp.R;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;


public class AccountValues extends ActionBarActivity {

	private TableLayout tablelayout;
	private AccountHandler accountHandler;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.accounts_values);
		
		tablelayout = (TableLayout) findViewById(R.id.values_table);
		
		Bundle bundle = getIntent().getExtras();
		
		accountHandler = new AccountHandler(this);
		accountHandler.open();
		
		Cursor c = accountHandler.returnAccountsForID(bundle.getInt("id"));
		
		c.moveToFirst();
		
		while (!c.isAfterLast()) {

            double debt_value = c.getDouble(1);

			TableRow tr = new TableRow(this);
			LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			layoutParams.setMargins(0, 15, 0, 0);
	        tr.setLayoutParams(layoutParams);   

	        // Create a TextView to house the name of the province
	        TextView user = new TextView(this);
	        user.setText(""+c.getString(0));
            user.setTextColor(Color.BLACK);
	        user.setLayoutParams(new LayoutParams(
	        		LayoutParams.MATCH_PARENT,
	                LayoutParams.WRAP_CONTENT, 1));
	        tr.addView(user);
	        
	        TextView value = new TextView(this);
	        value.setText(""+debt_value);

            if(debt_value>0.0)
                value.setTextColor(Color.parseColor("#008b00"));
            else if(debt_value<0.0)
                value.setTextColor(Color.RED);
            else value.setTextColor(Color.BLACK);

            value.setLayoutParams(new LayoutParams(
	                LayoutParams.MATCH_PARENT,
	                LayoutParams.WRAP_CONTENT, 1));
	        tr.addView(value);
	        
	        // Add the TableRow to the TableLayout
	        tablelayout.addView(tr, new TableLayout.LayoutParams(
	                	LayoutParams.MATCH_PARENT,
	                	LayoutParams.WRAP_CONTENT));
	        
	        c.moveToNext();
		}
		
		accountHandler.close();
		
	}
}
