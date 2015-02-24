package com.example.edu.ksu.crop;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import com.parse.ParseGeoPoint;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class TripAdapter extends ArrayAdapter<Trip> {
    Context context;
    int layoutResourceId;
    Trip[] data;
    
    public TripAdapter(Context context, int layoutResourceId, Trip[] data) {
        super(context, layoutResourceId, data);
//        Log.d("DOGR:MATCHESADAPTER: ", "Entered constructor of MatchesAdapter");


        
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.data = data;

//        Log.d("DOGR:MATCHESADAPTER: ", "Exited constructor of MatchesAdapter");

    }

    
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        TripHolder holder = null;

//        Log.d("DOGR:MATCHESADAPTER: ", "Entered if block of getView of MatchesAdapter");

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new TripHolder();
            holder.txtFieldName = (TextView) row.findViewById(R.id.txtView_HistoryItem_FieldName);
            holder.txtFieldDescription = (TextView) row.findViewById(R.id.txtView_HistoryItem_DateZip);
            holder.imgView = (ImageView) row.findViewById(R.id.imgView_HistoryItem_FieldIcon);

            row.setTag(holder);
        } else {
            holder = (TripHolder) row.getTag();
        }

//        Log.d("DOGR:MATCHESADAPTER: ", "Exited if block of getView of MatchesAdapter");

        Trip trip = data[position];
        String name = trip.getFieldName();
        String[] coords = new String[2];
        String loc = trip.getLocation();
        loc = (String)loc.subSequence(14, loc.length()-1);
        coords = loc.split(",");
        String description = "";
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
        	List<Address> addresses = geocoder.getFromLocation(Double.parseDouble(coords[0]), Double.parseDouble(coords[1]), 1);	
        	String zipCode = addresses.get(0).getPostalCode();
        	description = zipCode + " - " + trip.getDate();
        } catch(Exception ex) {
        	Log.d("TripAdapter", " error thrown Geocoding");
            description = trip.getLocation() + " - " +  trip.getDate();
        } 
        
        

//        holder.imgView.setImageResource(R.drawable.ic_action_person);
        holder.txtFieldName.setText(name);
        holder.txtFieldDescription.setText(description);
        holder.imgView.setImageResource(R.drawable.ic_next_picture);
        
        return row;
    }

    static class TripHolder {
        ImageView imgView;
        TextView txtFieldName;
        TextView txtFieldDescription;

    }

}





