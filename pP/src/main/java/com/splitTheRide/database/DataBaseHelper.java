package com.splitTheRide.database;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelper extends SQLiteOpenHelper{

	private static final String DATA_BASE_NAME = "database";
	private static final int DATABASE_VERSION = 2;

    // TABLE ROUTE

    public static final String ROUTE_ID = "_id";
    public static final String ROUTE_NAME = "route_name";
    public static final String ROUTE_TABLE_NAME = "Routes";

    public static final String CREATE_TABLE_ROUTE = "create table "+ ROUTE_TABLE_NAME +
                                                    " ("+ ROUTE_ID + " integer primary key autoincrement, "+
                                                    ROUTE_NAME + " text not null);";
	
	// TABLE PERSON
	public static final String PERSON_ID = "_id";
	public static final String PERSON_NAME = "name";
	public static final String PERSON_SNAME = "short_name";
    public static final String PERSON_USUAL_ROUTE = "usual_route";
	public static final String PERSON_TABLE_NAME = "Persons";
	
	public static final String CREATE_TABLE_PERSON = "create table "+ PERSON_TABLE_NAME +
													" ("+PERSON_ID+" integer primary key autoincrement, "+
													PERSON_NAME+" text not null, "+
													PERSON_SNAME+" text not null," +
                                                    PERSON_USUAL_ROUTE+" int," +
                                                    "FOREIGN KEY ("+PERSON_USUAL_ROUTE +") REFERENCES "+ ROUTE_TABLE_NAME +" ("+ROUTE_ID+") );";
	
	// TABLE VEHICLE
	
	public static final String VEHICLE_ID = "_id";
	public static final String VEHICLE_NAME = "name";
	public static final String VEHICLE_CONSUMPTION = "consumption";
	public static final String VEHICLE_PERSON_ID = "person_id";
	public static final String VEHICLE_TABLE_NAME = "Vehicles";
	
	public static final String CREATE_TABLE_VEHICLE = "create table "+ VEHICLE_TABLE_NAME +
													" ("+VEHICLE_ID+" integer primary key autoincrement, "+
													VEHICLE_NAME+" text not null, "+
													VEHICLE_CONSUMPTION+" double not null, "+
													VEHICLE_PERSON_ID+" integer, "+
													"FOREIGN KEY("+ VEHICLE_PERSON_ID +") REFERENCES "+ PERSON_TABLE_NAME +" ("+PERSON_ID+") );";


	// TABLE TRIP_PASSENGER

	/*
		  ESTOU POR AQUI. FALTA CRIAR A TABELA QUE RELACIONA OS PASSAGEIROS COM AS VIAGENS
	 */

	
	// TABLE TRIP
	
	public static final String TRIP_ID = "_id";
	public static final String TRIP_DATE = "trip_date";
	public static final String TRIP_ROUTE_ID = "route_id";
	public static final String TRIP_VEHICLE_ID = "vehicle_id";
	public static final String TRIP_TABLE_NAME = "Trips";
	
	public static final String CREATE_TABLE_TRIP = "create table "+TRIP_TABLE_NAME+
												   " ("+ TRIP_ID + " integer primary key autoincrement, "+
												   TRIP_DATE + " date not null, "+
												   TRIP_ROUTE_ID + " integer, "+
												   TRIP_VEHICLE_ID + " integer, "+
												   "FOREIGN KEY(" + TRIP_ROUTE_ID + ") REFERENCES "+ ROUTE_TABLE_NAME+ " ("+ROUTE_ID+"), "+
												   "FOREIGN KEY(" + TRIP_VEHICLE_ID + ") REFERENCES "+ VEHICLE_TABLE_NAME+ " ("+VEHICLE_ID+") ); ";
	
	// TABLE SEGMENT
	
	public static final String SEGMENT_ID = "_id";
	public static final String SEGMENT_NAME = "name";
	public static final String SEGMENT_DISTANCE = "distance";
	public static final String SEGMENT_COST = "cost";
	public static final String SEGMENT_TABLE_NAME = "Segments";
	
	public static final String CREATE_TABLE_SEGMENT = "create table "+SEGMENT_TABLE_NAME+
			   										  " ("+ SEGMENT_ID + " integer primary key autoincrement, "+
			   										  SEGMENT_NAME + " text not null, "+
			   										  SEGMENT_DISTANCE + " integer not null, "+
			   										  SEGMENT_COST + " double not null); ";
	
	
	// TABLE ACCOUNT
	
	public static final String ACCOUNT_ID = "_id";
	public static final String ACCOUNT_VALUE = "value";
	public static final String ACCOUNT_OWNER_ID = "owner_id";
	public static final String ACCOUNT_PAYER_ID = "payer_id";
	public static final String ACCOUNT_TABLE_NAME = "Accounts";
	
	public static final String CREATE_TABLE_ACCOUNT = "create table "+ACCOUNT_TABLE_NAME+
				  									  " ("+ ACCOUNT_ID + " integer primary key autoincrement, "+
				  									  ACCOUNT_VALUE + " float not null, "+
				  									  ACCOUNT_OWNER_ID + " integer, "+
				  									  ACCOUNT_PAYER_ID + " integer, "+
				  									  "FOREIGN KEY(" + ACCOUNT_OWNER_ID + ") REFERENCES "+ PERSON_TABLE_NAME+ " ("+PERSON_ID+"), "+
													  "FOREIGN KEY(" + ACCOUNT_PAYER_ID + ") REFERENCES "+ PERSON_TABLE_NAME+ " ("+PERSON_ID+") ); ";
	
	
	// TABLE TRANSACTION
	
	public static final String TRANSACTION_ID = "_id";
	public static final String TRANSACTION_DATE = "date";
	public static final String TRANSACTION_VALUE = "value";
	public static final String TRANSACTION_PAYMENT = "payment";
	public static final String TRANSACTION_PAYER_ID = "payer_id";
	public static final String TRANSACTION_RECEIVER_ID = "receiver_id";
	public static final String TRANSACTION_TABLE_NAME = "Transactions";
	
	public static final String CREATE_TABLE_TRANSACTION = "create table "+TRANSACTION_TABLE_NAME+
			  										  	  " ("+ TRANSACTION_ID + " integer primary key autoincrement, "+
			  										  	  TRANSACTION_DATE + " date not null, "+
			  										  	  TRANSACTION_VALUE + " float not null, "+
			  										  	  TRANSACTION_PAYMENT + " boolean not null, "+
			  										  	  TRANSACTION_PAYER_ID + " integer, "+
			  										  	  TRANSACTION_RECEIVER_ID + " integer, "+
			  										  	  "FOREIGN KEY(" + TRANSACTION_PAYER_ID + ") REFERENCES "+ PERSON_TABLE_NAME+ " ("+PERSON_ID+"), "+
			  										  	  "FOREIGN KEY(" + TRANSACTION_RECEIVER_ID + ") REFERENCES "+ PERSON_TABLE_NAME+ " ("+PERSON_ID+") ); ";
	
	// TABLE TRIP_PERSON
	
	public static final String TP_TRIP_ID = "trip_id";
	public static final String TP_PERSON_ID = "person_id";
	public static final String TP_TABLE_NAME = "Trip_Person";
	
	public static final String CREATE_TABLE_TP = "create table "+TP_TABLE_NAME+
			  									 " ("+ TP_TRIP_ID + " integer, "+
			  									 TP_PERSON_ID + " integer, "+
			  									 "PRIMARY KEY(" + TP_TRIP_ID + ", "	+ TP_PERSON_ID + ")"+ 
			  									 "FOREIGN KEY(" + TP_TRIP_ID + ") REFERENCES "+ TRIP_TABLE_NAME+ " ("+TRIP_ID+"), "+
			  									 "FOREIGN KEY(" + TP_PERSON_ID + ") REFERENCES "+ PERSON_TABLE_NAME+ " ("+PERSON_ID+") ); ";
	
	
	// TABLE COMPOSED_ROUTE
	
	public static final String CR_SEGMENT_ID = "segment_id";
	public static final String CR_ROUTE_ID = "route_id";
	public static final String CR_TABLE_NAME = "Composed_Route";
	
	public static final String CREATE_TABLE_CR = "create table "+CR_TABLE_NAME+
			  									 " ("+CR_ROUTE_ID + " integer, "+ 
			  									 CR_SEGMENT_ID + " integer, "+
			  									 "PRIMARY KEY(" + CR_ROUTE_ID + ", "	+ CR_SEGMENT_ID + ")"+
			  									 "FOREIGN KEY(" + CR_ROUTE_ID + ") REFERENCES "+ ROUTE_TABLE_NAME + " ("+ROUTE_ID+"), "+
			  									 "FOREIGN KEY(" + CR_SEGMENT_ID + ") REFERENCES "+ SEGMENT_TABLE_NAME + " ("+SEGMENT_ID+") ); ";



    // TRIGGERS

    public static final String CREATE_TRIGGER_DEBT = "CREATE TRIGGER aft_insert_debt "+
                                                        "AFTER INSERT ON "+ TRANSACTION_TABLE_NAME+
                                                        " WHEN ( NEW.PAYMENT == 0 )"+
                                                        " BEGIN"+
                                                        " UPDATE "+ ACCOUNT_TABLE_NAME+
                                                        " SET "+ ACCOUNT_VALUE +" = "+ ACCOUNT_VALUE +" + new."+TRANSACTION_VALUE+
                                                        " WHERE "+  ACCOUNT_OWNER_ID +" =  new."+TRANSACTION_RECEIVER_ID+
                                                        " AND "+ ACCOUNT_PAYER_ID + " = new."+TRANSACTION_PAYER_ID+";"+
                                                        "END;";

    public static final String CREATE_TRIGGER_PAYMENT = "CREATE TRIGGER aft_insert_payment "+
                                                        "AFTER INSERT ON "+ TRANSACTION_TABLE_NAME+
                                                        " WHEN ( NEW.PAYMENT == 1 )"+
                                                        " BEGIN"+
                                                        " UPDATE "+ ACCOUNT_TABLE_NAME+
                                                        " SET "+ ACCOUNT_VALUE +" = "+ ACCOUNT_VALUE +" - new."+TRANSACTION_VALUE+
                                                        " WHERE "+  ACCOUNT_OWNER_ID +" =  new."+TRANSACTION_RECEIVER_ID+
                                                        " AND "+ ACCOUNT_PAYER_ID + " = new."+TRANSACTION_PAYER_ID+";"+
                                                        "END;";
	
	public DataBaseHelper(Context context) {
		
		super(context, DATA_BASE_NAME, null, DATABASE_VERSION);
	}
	

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		try{
            db.execSQL(CREATE_TABLE_ROUTE);
            db.execSQL(CREATE_TABLE_PERSON);
			db.execSQL(CREATE_TABLE_VEHICLE);
			db.execSQL(CREATE_TABLE_SEGMENT);
			db.execSQL(CREATE_TABLE_TRIP);
			db.execSQL(CREATE_TABLE_ACCOUNT);
			db.execSQL(CREATE_TABLE_TRANSACTION);
			db.execSQL(CREATE_TABLE_TP);
			db.execSQL(CREATE_TABLE_CR);
            db.execSQL(CREATE_TRIGGER_DEBT);
            db.execSQL(CREATE_TRIGGER_PAYMENT);
			
		}catch(SQLException e){
			e.printStackTrace();
		}
		
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
		db.execSQL("DROP TABLE IF EXISTS "+ CR_TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS "+ TP_TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS "+ TRANSACTION_TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS "+ ACCOUNT_TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS "+ TRIP_TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS "+ SEGMENT_TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS "+ VEHICLE_TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS "+ PERSON_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+ ROUTE_TABLE_NAME);
		
		onCreate(db);
		
	}
	
}

