package com.example.edu.ksu.crop;

/**
 * Created by HaydenKinney on 2/25/15.
 */

import java.util.List;
import java.util.Locale;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.parse.ParseObject;


public class DetailedHistoryFragment extends Fragment {
    private static final String ARG_SECTION_NUMBER = "6";
    public static Trip trip;
    public String[] coords = {"39.193516", "-96.585031"};

    private LinearLayout netLinearLayout;
    private TextView fieldNameTxtView, calculatedYieldTxtView, dateTxtView, coordinatesTxtView, 
    	zipCodeTxtView, headsPerAcreTxtView, rowSizeTxtView, sizeOfFieldTxtView, photosAnalyzedTxtView;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static DetailedHistoryFragment newInstance(int sectionNumber, Trip t) {
    	DetailedHistoryFragment fragment = new DetailedHistoryFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        trip = t;
        return fragment;
    }

    public DetailedHistoryFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_history_detailed, container, false);

        fieldNameTxtView = (TextView)rootView.findViewById(R.id.txtView_HistoryDetails_FieldName);
        calculatedYieldTxtView = (TextView)rootView.findViewById(R.id.txtView_HistoryDetails_YieldVal);
        dateTxtView = (TextView)rootView.findViewById(R.id.txtView_HistoryDetails_Date);
        coordinatesTxtView = (TextView)rootView.findViewById(R.id.txtView_HistoryDetails_Coordinates);
        zipCodeTxtView = (TextView)rootView.findViewById(R.id.txtView_HistoryDetails_Zip);
        headsPerAcreTxtView = (TextView)rootView.findViewById(R.id.txtView_HistoryDetails_HeadsPerAcre);
        rowSizeTxtView = (TextView)rootView.findViewById(R.id.txtView_HistoryDetails_RowSize);
        sizeOfFieldTxtView = (TextView)rootView.findViewById(R.id.txtView_HistoryDetails_SizeOfField);
        photosAnalyzedTxtView = (TextView)rootView.findViewById(R.id.txtView_HistoryDetails_PhotosAnalyzed);

        fieldNameTxtView.setText(trip.getFieldName());
        calculatedYieldTxtView.setText(trip.getCalculatedYield());
        dateTxtView.setText(trip.getDate());
        zipCodeTxtView.setText(trip.getLocation());
        coordinatesTxtView.setText(trip.getLocation());

        String loc = trip.getLocation();
        loc = (String)loc.subSequence(14, loc.length()-1);
        coords = loc.split(",");
        coordinatesTxtView.setText(coords[0] + ", " + coords[1]);
        String description = "";
        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
        try {
        	List<Address> addresses = geocoder.getFromLocation(Double.parseDouble(coords[0]), Double.parseDouble(coords[1]), 1);	
        	zipCodeTxtView.setText(addresses.get(0).getPostalCode());
        } catch(Exception ex) {
        	Log.d("TripAdapter", " error thrown Geocoding");
        } 
        
        
        headsPerAcreTxtView.setText(trip.getHeadsPerAcre());
        rowSizeTxtView.setText(trip.getRowSize());
        sizeOfFieldTxtView.setText(trip.getSizeOfField());
        photosAnalyzedTxtView.setText(trip.getPhotosAnalyzed());
        
        coordinatesTxtView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String uri = String.format(Locale.ENGLISH, "geo:%f,%f", Float.parseFloat(coords[ 0 ]), Float.parseFloat(coords[ 1 ]));
				Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
				getActivity().startActivity(intent);
			}
		});

        return rootView;
    }
}
