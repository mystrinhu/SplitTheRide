package com.splitTheRide.custom;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.splitTheRide.entities.Person;
import com.splitTheRide.entities.Trip;
import com.splitTheRide.splittheride.R;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Joao on 06-07-2015.
 */
public class TripsListAdapter extends ArrayAdapter<Trip> {

    private List<Trip> items;
    private int layoutResourceId;
    private Context context;

    public TripsListAdapter(Context context, int layoutResourceId, List<Trip> items) {
        super(context, layoutResourceId, items);

        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.items = items;
    }

    @SuppressLint("ViewHolder")
    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;
        TripHolder holder = null;

        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        row = inflater.inflate(layoutResourceId, parent, false);

        holder = new TripHolder();
        holder.trip = items.get(position);
        holder.edit = (ImageButton) row.findViewById(R.id.editElement);
        holder.remove = (ImageButton) row.findViewById(R.id.removeElement);
        holder.edit.setTag(holder.trip);
        holder.remove.setTag(holder.trip);

        holder.layout1 = (LinearLayout) row.findViewById(R.id.textViewlayout);

        row.setTag(holder);

        setupItem(holder);

        return row;
    }

    private void setupItem(TripHolder holder) {

        Utils utils = new Utils();

        TextView textView1 = (TextView) holder.layout1.findViewById(R.id.line_a);
        textView1.setText(utils.getPersonNameFromID(holder.trip.getDriver(), context));

        TextView textView2 = (TextView) holder.layout1.findViewById(R.id.line_b);
        String passengerString = "";
        for (HashMap<Integer, Integer> passenger : holder.trip.getPassengers()) {
            Collection<Integer> a_passenger = passenger.values();
            Object[] pass_array = a_passenger.toArray();
            passengerString += utils.getPersonNameFromID((Integer) pass_array[0], context) + " ";
        }
        textView2.setText(passengerString);
    }

    public static class TripHolder {
        Trip trip;
        LinearLayout layout1;
        ImageButton edit;
        ImageButton remove;
    }
}
