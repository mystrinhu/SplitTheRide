package com.splitTheRide.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


public class TripPersonHandler {

    DataBaseHelper dbhelper;
    Context ctx;
    SQLiteDatabase db;

    public TripPersonHandler(Context ctx) {

        this.ctx = ctx;

        dbhelper = new DataBaseHelper(ctx);
    }

    public TripPersonHandler open() {

        db = dbhelper.getWritableDatabase();

        return this;
    }

    public void close() {

        dbhelper.close();
    }

    public long insertTripPerson(int trip_id, int passenger_id, int route_id) {

        ContentValues content = new ContentValues();
        content.put(DataBaseHelper.TP_TRIP_ID, trip_id);
        content.put(DataBaseHelper.TP_PASSENGER_ID, passenger_id);
        content.put(DataBaseHelper.TP_ROUTE_ID, route_id);

        return db.insertOrThrow(DataBaseHelper.TP_TABLE_NAME, null, content);

    }

    public Cursor passengersInTrip(int trip_id) {

        return db.query(DataBaseHelper.TP_TABLE_NAME,
                new String[]{DataBaseHelper.TP_ROUTE_ID, DataBaseHelper.TP_PASSENGER_ID},
                DataBaseHelper.TP_TRIP_ID + " =?",
                new String[]{"" + trip_id},
                null, null, null);

    }


    public void removeTrip(int trip_id) {

        db.delete(DataBaseHelper.TP_TABLE_NAME,
                DataBaseHelper.TP_TRIP_ID + "=?",
                new String[]{"" + trip_id});
    }

}
