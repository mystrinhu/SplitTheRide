package com.splitTheRide.splittheride;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

import com.splitTheRide.database.PersonHandler;
import com.splitTheRide.database.TransactionHandler;
import com.splitTheRide.settings.AddTransaction;

public class Transactions extends ActionBarActivity {

    private TableLayout tableLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_transactions);


        tableLayout = (TableLayout) findViewById(R.id.tableLayout);

        TransactionHandler t_handler = new TransactionHandler(this);

        t_handler.open();

        Cursor transactions = t_handler.returnTransactions();

        transactions.moveToFirst();
        int count=0;

        while (!transactions.isAfterLast()) {

            // 1 --> true || 0 --> false
            int isPayment = transactions.getInt(5);

            TableRow tr = new TableRow(this);
            TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            tr.setLayoutParams(layoutParams);

            TextView dateView = new TextView(this);

            dateView.setText(transactions.getString(1));
            if(isPayment == 1)
                dateView.setTextColor(Color.parseColor("#008b00"));
            else dateView.setTextColor(Color.BLACK);
            dateView.setLayoutParams(new LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT, 1));
            tr.addView(dateView);

            TextView valueView = new TextView(this);

            valueView.setText(""+transactions.getDouble(2));
            if(isPayment == 1)
                valueView.setTextColor(Color.parseColor("#008b00"));
            else valueView.setTextColor(Color.BLACK);
            valueView.setLayoutParams(new LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT, 1));
            tr.addView(valueView);

            PersonHandler p_handler = new PersonHandler(this);

            p_handler.open();

            TextView fromView = new TextView(this);

            String from = p_handler.getNamefromID(transactions.getInt(3));
            fromView.setText(from);

            if(isPayment == 1)
                fromView.setTextColor(Color.parseColor("#008b00"));
            else fromView.setTextColor(Color.BLACK);

            fromView.setLayoutParams(new LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT, 1));
            tr.addView(fromView);

            TextView toView = new TextView(this);

            String to = p_handler.getNamefromID(transactions.getInt(4));
            toView.setText(to);
            p_handler.close();

            if(isPayment == 1)
                toView.setTextColor(Color.parseColor("#008b00"));
            else toView.setTextColor(Color.BLACK);

            toView.setLayoutParams(new LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT, 1));
            tr.addView(toView);

            tableLayout.addView(tr, new TableLayout.LayoutParams(
                    LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT));

            transactions.moveToNext();
        }

        t_handler.close();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.listview, menu);
        return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_addview:	Intent addTransaction = new Intent(this, AddTransaction.class);

                                        startActivity(addTransaction);
                                        finish();
                                        return true;

            default:	return super.onOptionsItemSelected(item);
        }
	}
}
