package com.splitTheRide.settings;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.splitTheRide.database.PersonHandler;
import com.splitTheRide.database.TransactionHandler;
import com.splitTheRide.splittheride.R;
import com.splitTheRide.splittheride.Transactions;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddTransaction extends ActionBarActivity implements View.OnClickListener {

    private Button ok, cancel;
    private EditText date, value;
    private Spinner payer, receiver;
    private PersonHandler p_handler;
    private ArrayAdapter<String> adapter;
    private List<Integer> personIDs = new ArrayList<Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);

        date = (EditText) findViewById(R.id.date);
        value = (EditText) findViewById(R.id.value);
        payer = (Spinner) findViewById(R.id.spinner_payer);
        receiver = (Spinner) findViewById(R.id.spinner_receiver);
        ok = (Button) findViewById(R.id.ok_button);
        cancel = (Button) findViewById(R.id.cancel_button);

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Calendar cal = Calendar.getInstance();

        date.setText(dateFormat.format(cal.getTime()));

        p_handler = new PersonHandler(this);

        p_handler.open();
        Cursor person_cursor = p_handler.returnAllPersonsData();

        person_cursor.moveToFirst();

        List<String> personList = new ArrayList<String>();

        personList.add("");

        while (!person_cursor.isAfterLast()) {

            personList.add(person_cursor.getString(1));
            personIDs.add(person_cursor.getInt(0));
            person_cursor.moveToNext();
        }

        p_handler.close();

        adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, personList);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

        payer.setAdapter(adapter);
        receiver.setAdapter(adapter);

        ok.setOnClickListener(this);
        cancel.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.ok_button:    TransactionHandler t_handler = new TransactionHandler(this);

                                    if(value.getText().toString().equals("")||
                                            payer.getSelectedItem().equals("") ||
                                            receiver.getSelectedItem().equals(""))
                                        showMessage("Error", "All fields are mandatory");
                                    else {
                                        String current_date = date.getText().toString();
                                        double payed_value = Double.parseDouble(value.getText().toString());
                                        int payerID = personIDs.get(payer.getSelectedItemPosition() - 1);
                                        int receiverID = personIDs.get(receiver.getSelectedItemPosition() - 1);

                                        if (payerID == receiverID) {
                                            showMessage("Error", "The payer user and the receiver user are the same.");
                                        } else {

                                            t_handler.open();
                                            t_handler.insertTransaction(current_date, payed_value, true, payerID, receiverID);
                                            t_handler.close();

                                            showMessage("Success", "Payment inserted successfully.");
                                        }
                                    }
                                    break;

            case R.id.cancel_button:    Intent back = new Intent(this, Transactions.class);
                                        startActivity(back);

                                        finish();
                                        break;
        }
    }

    private void showMessage(final String title, String message){

        AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(!title.equalsIgnoreCase("error")){
                    Intent intent = new Intent(getApplication(), Transactions.class);

                    startActivity(intent);
                    finish();
                }
            }
        });

        dialog.show();
    }

}
