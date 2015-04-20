package com.splitTheRide.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class AccountHandler {
	
	DataBaseHelper dbhelper;
	Context ctx;
	SQLiteDatabase db;
		
	public AccountHandler(Context ctx){
		
		this.ctx = ctx;
		
		dbhelper = new DataBaseHelper(ctx);
	}
	

	public AccountHandler open(){
		
		db = dbhelper.getWritableDatabase();
		
		return this;
	}
	
	public void close(){
		
		dbhelper.close();
	}
	
	public long insertAccount(int owner_id, int payer_id){
		
		ContentValues content = new ContentValues();
		content.put(DataBaseHelper.ACCOUNT_OWNER_ID, owner_id);
		content.put(DataBaseHelper.ACCOUNT_PAYER_ID, payer_id);
		content.put(DataBaseHelper.ACCOUNT_VALUE, 0.0);
		
		return db.insertOrThrow(DataBaseHelper.ACCOUNT_TABLE_NAME, null, content);
		
	}
	
	/*public boolean editAccount(int owner_id, int payer_id, double value, String operation){

		String query = "";

        if(operation.equalsIgnoreCase("payment"))
            query = "UPDATE "+DataBaseHelper.ACCOUNT_TABLE_NAME+
                    " SET "+DataBaseHelper.ACCOUNT_VALUE+" = "+ DataBaseHelper.ACCOUNT_VALUE+ " - "+ value+
                    " WHERE "+DataBaseHelper.ACCOUNT_OWNER_ID+" = "+owner_id+
                    " AND "+DataBaseHelper.ACCOUNT_PAYER_ID+" = "+payer_id;
        else query = "UPDATE "+DataBaseHelper.ACCOUNT_TABLE_NAME+
                     " SET "+DataBaseHelper.ACCOUNT_VALUE+" = "+ value + " + "+ DataBaseHelper.ACCOUNT_VALUE+
                     " WHERE "+DataBaseHelper.ACCOUNT_OWNER_ID+" = "+owner_id+
                     " AND "+DataBaseHelper.ACCOUNT_PAYER_ID+" = "+payer_id;

        ContentValues content = new ContentValues();
        if(operation.equalsIgnoreCase("payment"))
            content.put(DataBaseHelper.ACCOUNT_VALUE, DataBaseHelper.ACCOUNT_VALUE +" - "+ value);
        else content.put(DataBaseHelper.ACCOUNT_VALUE, value + " + "+ DataBaseHelper.ACCOUNT_VALUE);

        int myvalue = db.update(DataBaseHelper.ACCOUNT_TABLE_NAME, content,
                DataBaseHelper.ACCOUNT_OWNER_ID+"=? AND "+DataBaseHelper.ACCOUNT_PAYER_ID+"=?",
                new String[]{""+owner_id, ""+payer_id});

        Log.d("cenas", ""+myvalue);

        return myvalue>0;


*//*        Log.d("cenas", query);

        return db.rawQuery(query, null);*//*
	}*/
	
	public Cursor selectMovements(){
		
		return db.rawQuery("SELECT DISTINCT p."+DataBaseHelper.PERSON_ID+", p."+DataBaseHelper.PERSON_NAME+", p."+DataBaseHelper.PERSON_SNAME+
							" FROM "+DataBaseHelper.PERSON_TABLE_NAME+" p, "+DataBaseHelper.ACCOUNT_TABLE_NAME+" ac "+
							"WHERE p."+DataBaseHelper.PERSON_ID+" = "+DataBaseHelper.ACCOUNT_OWNER_ID, null);
	}
	
	public boolean removeAccounts(int id){
		
		return db.delete(DataBaseHelper.ACCOUNT_TABLE_NAME, DataBaseHelper.ACCOUNT_OWNER_ID + " =? or "+ DataBaseHelper.ACCOUNT_PAYER_ID +" =?", new String[]{""+id, ""+id}) > 0;
		
	}

	public Cursor returnAccountsForID(int id){
		
		return db.rawQuery("SELECT p."+DataBaseHelper.PERSON_NAME+", ac."+DataBaseHelper.ACCOUNT_VALUE+
							" FROM "+DataBaseHelper.PERSON_TABLE_NAME+" p, "+DataBaseHelper.ACCOUNT_TABLE_NAME+" ac "+
							"WHERE p."+DataBaseHelper.PERSON_ID+" = ac."+DataBaseHelper.ACCOUNT_PAYER_ID + " AND ac."+DataBaseHelper.ACCOUNT_OWNER_ID+" = "+id,
							null);
	}
	
	public Cursor returnAllAccounts(){
		
		return db.query(DataBaseHelper.ACCOUNT_TABLE_NAME, new String[]{DataBaseHelper.ACCOUNT_ID, DataBaseHelper.ACCOUNT_OWNER_ID, DataBaseHelper.ACCOUNT_PAYER_ID, DataBaseHelper.ACCOUNT_VALUE}, null, null, null, null, null);
	}
	
	
		
	
}
