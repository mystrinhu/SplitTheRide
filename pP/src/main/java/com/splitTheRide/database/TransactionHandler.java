package com.splitTheRide.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class TransactionHandler {

	DataBaseHelper dbhelper;
	Context ctx;
	SQLiteDatabase db;

	public TransactionHandler(Context ctx){
		
		this.ctx = ctx;
		
		dbhelper = new DataBaseHelper(ctx);
	}
	

	public TransactionHandler open(){
		
		db = dbhelper.getWritableDatabase();
		
		return this;
	}
	
	public void close(){
		
		dbhelper.close();
	}
	
	public long insertTransaction(String date, double value, boolean payment, int from, int to){
		
		ContentValues content = new ContentValues();
		content.put(DataBaseHelper.TRANSACTION_DATE, date);
		content.put(DataBaseHelper.TRANSACTION_VALUE, value);
		content.put(DataBaseHelper.TRANSACTION_PAYMENT, payment);
        content.put(DataBaseHelper.TRANSACTION_PAYER_ID, from);
        content.put(DataBaseHelper.TRANSACTION_RECEIVER_ID, to);
				
		return db.insertOrThrow(DataBaseHelper.TRANSACTION_TABLE_NAME, null, content);
		
	}
	
/*
public boolean editSegment(int id, String name, int distance, double cost){

ContentValues content = new ContentValues();
content.put(DataBaseHelper.SEGMENT_NAME, name);
content.put(DataBaseHelper.SEGMENT_DISTANCE, distance);
content.put(DataBaseHelper.SEGMENT_COST, cost);

return db.update(DataBaseHelper.SEGMENT_TABLE_NAME, content, DataBaseHelper.SEGMENT_ID+"=?", new String[]{""+id})>0;
}
*/

    public boolean removeTransaction(int id){
		
		return db.delete(DataBaseHelper.TRANSACTION_TABLE_NAME, DataBaseHelper.TRANSACTION_ID + " =?", new String[]{""+id}) > 0;
		
	}
	
	public Cursor returnTransactions(){
		
		return db.query(DataBaseHelper.TRANSACTION_TABLE_NAME,
						new String[]{DataBaseHelper.TRANSACTION_ID, DataBaseHelper.TRANSACTION_DATE,
                                     DataBaseHelper.TRANSACTION_VALUE, DataBaseHelper.TRANSACTION_PAYER_ID,
                                     DataBaseHelper.TRANSACTION_RECEIVER_ID, DataBaseHelper.TRANSACTION_PAYMENT},
						null, null, null, null, null);
	}	
	
}
