package com.example.edu.ksu.crop;

import java.util.HashMap;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.example.edu.ksu.crop.MainActivity.SoilFragment;
import com.example.edu.ksu.crop.MainActivity.WeatherFragment;
import com.jjoe64.graphview.*;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseUser;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;

public class FinalFragment extends Fragment implements OnSeekBarChangeListener {

	private static final String ARG_SECTION_NUMBER = "section_number";
	GraphViewSeries exampleSeries;
	SeekBar seekBarHeads;
	static DataSet data;
	static int seedsPerPound = 15500;
	static int headsPerAcreInt;
	static double grainNum;
	static double averageBUA;
	TextView bpaTV;
	Button weather;
	Button weather, soilBtn;
	Button saveButton;

	/*
	 * newInstance
	 * 
	 * @params: sectionNumber : int : not necessary for input since this
	 * fragment isn't accessible from the menu dataSet : DataSet : Used to pass
	 * through data about the recent fields like area
	 * 
	 * Return : FinalFragment : returns an instance of this class.
	 */
	public static FinalFragment newInstance(int sectionNumber, DataSet dataSet) {
		FinalFragment fragment = new FinalFragment();
		data = dataSet;
		headsPerAcreInt = data.getHeadsPerAcre();
		grainNum = data.ReturnGrainNumber();
		CalculateValues(headsPerAcreInt, grainNum, seedsPerPound);
		Bundle args = new Bundle();
		args.putInt(ARG_SECTION_NUMBER, sectionNumber);
		fragment.setArguments(args);
		return fragment;
	}

	/*
	 * Constructor for override
	 */
	public FinalFragment() {

	}

	/*
	 * Implemented by default. FinalFragment extends Fragment. Please see
	 * documentation below
	 * 
	 * This also creates the view and set all the listeners for the View
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater,
	 * android.view.ViewGroup, android.os.Bundle)
	 */
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View rootView = inflater.inflate(R.layout.fragment_final_page,
				container, false);

		seekBarHeads = (SeekBar) rootView.findViewById(R.id.seekBarHeadsPer);
		bpaTV = (TextView) rootView.findViewById(R.id.textViewValueBPAF);
		String tempVal4 = String.format("%.2f", averageBUA) + " bu/acre";
		bpaTV.setText(tempVal4);

		exampleSeries = new GraphViewSeries(CalculateValues(headsPerAcreInt,
				grainNum, seedsPerPound));

		GraphView graphView = new BarGraphView(getActivity(),
				"Projeced Bushels Per Acre");
		graphView.addSeries(exampleSeries); // data
		graphView.setHorizontalLabels(new String[] { "Low Yield",
				"Average Yield", "High Yield" });
		LinearLayout layout = (LinearLayout) rootView
				.findViewById(R.id.widget44);
		layout.addView(graphView);

		seekBarHeads.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				// Graph stuff
				exampleSeries.resetData(CalculateValues(headsPerAcreInt,
						grainNum, seedsPerPound));
				String tempVal = String.format("%.2f", averageBUA) + " bu/acre";
				bpaTV.setText(tempVal);

			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				seedsPerPound = UpdateGraphView(progress, 22500, 9000);
				exampleSeries.resetData(CalculateValues(headsPerAcreInt,
						grainNum, seedsPerPound));

				// tvHeads.setText(progress);

			}
		});
		soilBtn = (Button) rootView.findViewById(R.id.soilButtonFinal);
		
		soilBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				try {					
					FragmentTransaction transaction = getFragmentManager()
							.beginTransaction();
					transaction.replace(R.id.container, SoilFragment.newInstance(5));
					transaction.addToBackStack(null);
					transaction.commit();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		weather = (Button) rootView.findViewById(R.id.weatherButtonFinal);

		weather.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Fragment newFragment = WeatherFragment.newInstance(10);// Instance
																		// has
																		// something
																		// to do
																		// with
																		// title,
																		// will
																		// work
																		// on
																		// this
																		// later
																		// during
																		// clearn
																		// up
				FragmentTransaction transaction = getFragmentManager()
						.beginTransaction();
				transaction.replace(R.id.container, newFragment);
				transaction.addToBackStack(null);
				transaction.commit();
			}
		});

		saveButton = (Button) rootView.findViewById(R.id.buttonSave);

		saveButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// Save data from dataset
				
				data.setLocation(getCurrentLocation(true));
				ParseObject dataToPush = new ParseObject("Trip");
				dataToPush.put("fieldName", data.getFieldName());
				ParseGeoPoint point = new ParseGeoPoint(data.getLocation().getLatitude(), data.getLocation().getLongitude());
				dataToPush.put("location", point);
				dataToPush.put("sizeOfField", data.getFieldSize());
				dataToPush.put("headsPerAcre", data.getHeadsPerAcre());
				dataToPush.put("photosAnalyzed", data.getPhotoAnalyzed());
				dataToPush.put("rowSize",  data.getRowSize());
				dataToPush.put("calculatedYield", averageBUA);
				dataToPush.put("user", ParseUser.getCurrentUser());
				dataToPush.saveEventually();
				
				if(netCheckin()){
					Toast.makeText(getActivity(), "Saving Data.", Toast.LENGTH_LONG).show();
				} else
				{
					Toast.makeText(getActivity(), "Data will be saved when data connection is available.", Toast.LENGTH_LONG).show();
				}
				
				//track user saving data
				Map<String, String> parseAnalytics = new HashMap<String, String>();
				parseAnalytics.put("category", "saved");
				ParseAnalytics.trackEvent("SavedData", parseAnalytics);
				finishAppOptions();
				
				
				

			}
		});

		return rootView;
	}

	private void finishAppOptions() {
		CharSequence options[] = new CharSequence[] { "Start New Field", "Email a Report", "Close App"};
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("Choose Photo Selection Method");
		builder.setItems(options,  new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int picked) {
				switch (picked) {
				case 0:
					switchToStart();
					break;
				case 1:
					emailReport();
					break;
				case 2:
					System.exit(0);
					break;
				}
			}
		});
		builder.show();
	}

	private void switchToStart() {
		FragmentManager fm = getActivity().getSupportFragmentManager();
		for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {    
		    fm.popBackStack();
		}
		
		FragmentTransaction transaction = getFragmentManager()
				.beginTransaction();
		transaction.replace(R.id.container, InputFragment.newInstance(5));
		transaction.addToBackStack(null);
		transaction.commit();
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
	
    @SuppressLint("SimpleDateFormat") private void createEmail(String email) {
        final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
        
		DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm"); 
		
        emailIntent.setType("text/HTML");
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{email});
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Sorghum Yield Report for Field " + data.getFieldName());
           emailIntent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml("<h1><small>Here's Your Report!</small></h1>\n" +
                "\n" +
                "Calculated Yield: " + averageBUA + "<br>" +
                "Date: " + df.format(Calendar.getInstance().getTime()) + "<br>" +
                "Coordinates: " + data.getLocation().getLatitude() + ", " + data.getLocation().getLongitude() + "<br>" +
                "Heads per Acre: " + data.getHeadsPerAcre() + "<br>" +
                "Row Size: " + data.getRowSize() + "<br>" +
                "Size of Field: " + data.getFieldSize() + "<br>" +
                "Photos Analyzed: " + data.getPhotoAnalyzed() + "<br></p>"
                ));
        
        startActivity(Intent.createChooser(emailIntent, "Send mail..."));
    }
    
	@Override
	public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStartTrackingTouch(SeekBar arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStopTrackingTouch(SeekBar arg0) {
		// TODO Auto-generated method stub

	}

	/*
	 * This method will return a GraphViewData[] array of the 3 values
	 * 
	 * @params: HPA : int : Heads per Acre d : double : calculated value from
	 * pictures SPP : int : SeedsPerPound
	 * 
	 * Return: GraphViewData[] : array of GraphViewData from GraphViewData.java
	 * Please see the GraphViewData.java file to see how this is implemented. It
	 * is used to implement the interface from android-graphview. See
	 * android-graphview.org for documentation.
	 */
	private static GraphViewData[] CalculateValues(int HPA, double d, int SPP) {
		double lowBUA, highBUA;
		averageBUA = (double) ((((HPA * d) * 1000) / SPP) / 56);
		lowBUA = averageBUA * .85;
		highBUA = averageBUA * 1.15;

		return new GraphViewData[] { new GraphViewData(1, lowBUA),
				new GraphViewData(2, averageBUA), new GraphViewData(3, highBUA) };

	}

	/*
	 * This is used to convert ranges of 0 - 100 to different values(sliders)
	 * 
	 * @params: x : int : The value of the slider(0 - 100) d : int : Maximum
	 * value of range c : int : Minimum value of range
	 * 
	 * Return: int : valuse converted from 100 to number within range
	 */
	private int UpdateGraphView(int x, int d, int c) {
		return (int) ((((double) (x - 1) / (double) (99)) * (d - c)) + c);
	}

	// Obtains the current location from the phone
	private Location getCurrentLocation(Boolean showToast) {

		LocationManager mLocationManager = (LocationManager) getActivity()
				.getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
		List<String> providers = mLocationManager.getProviders(true);
		Location bestLocation = null;
		for (String provider : providers) {
			Location l = mLocationManager.getLastKnownLocation(provider);
			if (l == null) {
				continue;
			}
			if (bestLocation == null
					|| l.getAccuracy() < bestLocation.getAccuracy()) {
				// Found best last known location: %s", l);
				bestLocation = l;
			}
		}

		if (bestLocation == null) {
			sendToast("Couldn't get your location, defaulting to Manhattan, KS.");
			bestLocation = new Location("null");
			bestLocation.setLatitude(39.1917);
			bestLocation.setLongitude(-96.5917);
		}

		if (showToast)
			sendToast("Location is " + bestLocation.getLatitude() + ", "
					+ bestLocation.getLongitude() + ".");
		return bestLocation;

	}
	
	private void sendToast(String message) {
        CharSequence text = (CharSequence) message;
        Toast toast = Toast.makeText(getActivity(), text, Toast.LENGTH_LONG);
        toast.show();
    }
	
	private boolean netCheckin() {
		Context context = getActivity();
	    try {
	        ConnectivityManager nInfo = (ConnectivityManager) context.getSystemService(
	            Context.CONNECTIVITY_SERVICE);
	        nInfo.getActiveNetworkInfo().isConnectedOrConnecting();
	        Log.d("Internet", "Net avail:"
	            + nInfo.getActiveNetworkInfo().isConnectedOrConnecting());
	        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(
	            Context.CONNECTIVITY_SERVICE);
	        NetworkInfo netInfo = cm.getActiveNetworkInfo();
	        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
	            Log.d("Internet", "Network available:true");
	            return true;
	        } else {
	            Log.d("Internet", "Network available:false");
	            return false;
	        }
	    } catch (Exception e) {
	        return false;
	    }
	}

}
