package com.example.edu.ksu.crop;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseException;
import com.parse.ParseUser;

public class HistoryFragment extends Fragment {
	private static final String ARG_SECTION_NUMBER = "";

	ListView list;
	ArrayList<Trip> matchesList;

	public static HistoryFragment newInstance(int sectionNumber) {
		HistoryFragment fragment = new HistoryFragment();
		Bundle args = new Bundle();
		args.putInt(ARG_SECTION_NUMBER, sectionNumber);
		fragment.setArguments(args);
		return fragment;
	}

	public HistoryFragment() {

	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View rootView = inflater.inflate(R.layout.fragment_history,
				container, false);

		list = (ListView) rootView
				.findViewById(R.id.listView_HistoryFragment_list);
		LoadHistoryAsync lha = new LoadHistoryAsync(getActivity(), list, this);
		lha.execute();
		
		 list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			 public void onItemClick(AdapterView<?> adapterView, View view, int i,
				long l) {
				 Toast.makeText(getActivity(), "thisisatest", Toast.LENGTH_LONG).show();
			 }
		 });
		
		
		// ArrayList<String> builderOptions = new ArrayList<String>();
		// builderOptions.add("Revisit their profile");
		// builderOptions.add("Send them an email");
		//
		// CharSequence[] cM = new CharSequence[builderOptions.size()];
		// cM = builderOptions.toArray(cM);
		// AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		// builder.setTitle("Get In Touch!");
		// builder.setItems(cM, new DialogInterface.OnClickListener() {
		// @Override
		// public void onClick(DialogInterface dialog, int which) {
		// if(which == 0) {
		// // switch fragment to their profile
		// } else if( which == 1 ) {
		// sendAppInviteEmail(matchesList.get(0).getNumber());
		// }
		// }
		// });
		// builder.show();
		// }
		// });

		return rootView;
	}

	public void asyncResult(ArrayList<Trip> trips) {
		// TODO Auto-generated method stub
		
	}

}

class LoadHistoryAsync extends AsyncTask<Void, Void, ArrayList<Trip>> {
	ProgressDialog progressDialog;
	Activity mActivity;
	ListView historyList;
	HistoryFragment hf;
	ArrayList<Trip> trips;

	public LoadHistoryAsync(Activity activity, ListView historyList,
			HistoryFragment hf) {
		mActivity = activity;
		this.historyList = historyList;
		progressDialog = new ProgressDialog(mActivity);
		this.hf = hf;
		trips = new ArrayList<Trip>();
	}

	protected void onPreExecute() {
		super.onPreExecute();
		progressDialog.setCancelable(true);
		progressDialog.setMessage("Loading Matches");
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.show();
	}

	protected ArrayList<Trip> doInBackground(Void... params) {

        ParseUser currentUser = ParseUser.getCurrentUser();

        ParseQuery<ParseObject> query1 = ParseQuery.getQuery("Trip");

        query1.whereEqualTo("user", currentUser);

        try{
        	List<ParseObject> tripsList = query1.find();
        	for(int i = 0; i < tripsList.size(); i++) {
        		Trip t = new Trip(
        				tripsList.get(i).getString("fieldName"),
        				String.valueOf(tripsList.get(i).getNumber("calculatedYield")),
        				String.valueOf(tripsList.get(i).getNumber("headsPerAcre")),
        				tripsList.get(i).getParseGeoPoint("location").toString(),
        				String.valueOf(tripsList.get(i).getNumber("headsPerAcre")),
        				String.valueOf(tripsList.get(i).getNumber("rowSize")),
        				String.valueOf(tripsList.get(i).getNumber("sizeOfField")),
        				tripsList.get(i).getUpdatedAt().toString()
        				);
        		trips.add(t);
        	}  
        } catch(Exception ex) {
        	Log.d("HistoryFragment", "ParseQuery for trips list failed");
        }

        Collections.sort(trips, new Comparator<Trip>() {
            public int compare(Trip t1, Trip t2) {
            	DateFormat format = new SimpleDateFormat("MMM dd, yyyy, HH:mm", Locale.ENGLISH);
            	Date d1 = null, d2 = null;
            	try{
                	d1 = format.parse(t1.getDate());
                	d2 = format.parse(t2.getDate());
                	return d1.compareTo(d2);
            	} catch(Exception ex) {
            		Log.d("HistoryFragment", "A date failed to parse");
            	}
            	return 0;
            }
        });

        return trips;
    }

	protected void onPostExecute(ArrayList<Trip> trips) {
		super.onPostExecute(trips);
		hf.asyncResult(trips);
		Trip[] tripsArray = new Trip[trips.size()];
		tripsArray = trips.toArray(tripsArray);
		TripAdapter customAdapter = new TripAdapter(mActivity,
				R.layout.item_history, tripsArray);
		historyList.setAdapter(customAdapter);
		progressDialog.cancel();
	}
}
