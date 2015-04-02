package com.example.edu.ksu.crop;

/**
 * Created by HaydenKinney on 2/25/15.
 */

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.support.v4.app.Fragment;
import android.text.Html;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseAnalytics;
import com.parse.ParseObject;
import com.parse.ParseUser;


public class DetailedHistoryFragment extends Fragment {
    private static final String ARG_SECTION_NUMBER = "6";
    public static Trip trip;
    public String[] coords = {"39.193516", "-96.585031"};
    public String zipCode = "00000";
    private Button emailReportBtn;
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
        emailReportBtn = (Button)rootView.findViewById(R.id.button_HistoryDetails_email);
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
        	zipCode = addresses.get(0).getPostalCode();
        	if(zipCode == null) { zipCode = "No Zipcode"; }
        	zipCodeTxtView.setText(zipCode);
        } catch(Exception ex) {
        	Log.d("TripAdapter", " error thrown Geocoding");
        } 
        
		emailReportBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				emailReport();
			}
		});
        
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
    
    private void emailReport() {
    	AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());

    	alert.setTitle("Enter Email Address");

    	// Set an EditText view to get user input 
    	final EditText input = new EditText(getActivity());
    	alert.setView(input);

    	alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
    	public void onClick(DialogInterface dialog, int whichButton) {
    	  String value = input.getText().toString();
    	  value = value.replace(" ", "");
    	  Pattern p = Pattern.compile(".+@.+\\.[a-z]+");
    	  Matcher m = p.matcher(value);
    	  boolean matchFound = m.matches();
    	  if (matchFound) {
    	      createEmail(value);
    	  } else {
    		  Toast.makeText(getActivity(), "Value is not an email",  Toast.LENGTH_LONG).show();
    	  }
    	  }
    	});

    	alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
    	  public void onClick(DialogInterface dialog, int whichButton) {

    	  }
    	});
    	
    	alert.show();
    }
    
    private void createEmail(String email) {
        final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);

        emailIntent.setType("text/HTML");
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{email});
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Sorghum Yield Report for Field " + trip.getFieldName());
        if(zipCode.contains("No Zipcode") ) {
            emailIntent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml("<h1><small>Here's Your Report!</small></h1>\n" +
                    "\n" +
                    "<p>Calculated Yield: " + trip.getCalculatedYield() + "<br>" +
                    "Date: " + trip.getDate() + "<br>" +
                    "Coordinates: " + trip.getLocation().subSequence(14,  trip.getLocation().length()-1) + "<br>" +
                    "Zipcode: " + "Not available for this location" + "<br>" +
                    "Heads per Acre: " + trip.getHeadsPerAcre() + "<br>" +
                    "Row Size: " + trip.getRowSize() + "<br>" +
                    "Size of Field: " + trip.getSizeOfField() + "<br>" +
                    "Photos Analyzed: " + trip.getPhotosAnalyzed() + "<br></p>"
                    ));
        } else {
           emailIntent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml("<h1><small>Here's Your Report!</small></h1>\n" +
                "\n" +
                "Calculated Yield: " + trip.getCalculatedYield() + "<br>" +
                "Date: " + trip.getDate() + "<br>" +
                "Coordinates: " + trip.getLocation().subSequence(14,  trip.getLocation().length()-1) + "<br>" +
                "Zipcode: " + zipCode + "<br>" +
                "Heads per Acre: " + trip.getHeadsPerAcre() + "<br>" +
                "Row Size: " + trip.getRowSize() + "<br>" +
                "Size of Field: " + trip.getSizeOfField() + "<br>" +
                "Photos Analyzed: " + trip.getPhotosAnalyzed() + "<br></p>"
                ));
        }
        Map<String, String> parseAnalytics = new HashMap<String, String>();
		parseAnalytics.put("category", "Emailed Data");
		ParseAnalytics.trackEvent("Emailed Data", parseAnalytics);
		
        startActivity(Intent.createChooser(emailIntent, "Send mail..."));
    }
    
    public void onResume() { 
        super.onResume(); 
        getActivity().getActionBar().setTitle(trip.getFieldName());
    } 
    
}
