package com.example.edu.ksu.crop;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;
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
		final View rootView = inflater.inflate(R.layout.fragment_graph, container,
				false);
		
        list = (ListView)rootView.findViewById(R.id.listView_HistoryFragment_list);
        LoadHistoryAsync lha = new LoadHistoryAsync(getActivity(), list, this);
        lha.execute();

//        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                ArrayList<String> builderOptions = new ArrayList<String>();
//                builderOptions.add("Revisit their profile");
//                builderOptions.add("Send them an email");
//
//                CharSequence[] cM = new CharSequence[builderOptions.size()];
//                cM = builderOptions.toArray(cM);
//                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//                builder.setTitle("Get In Touch!");
//                builder.setItems(cM, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        if(which == 0) {
//                            // switch fragment to their profile
//                        } else if( which == 1 ) {
//                            sendAppInviteEmail(matchesList.get(0).getNumber());
//                        }
//                    }
//                });
//                builder.show();
//            }
//        });
        
		
		return rootView;
	}
	
}

class LoadHistoryAsync extends AsyncTask<Void, Void, ArrayList<Trip>> {
    ProgressDialog progressDialog;
    Activity mActivity;
    ListView historyList;
    HistoryFragment hf;
    ArrayList<Trip> trips;

    public LoadHistoryAsync(Activity activity, ListView historyList, HistoryFragment hf ) {
        mActivity = activity;
        this.historyList = historyList;
        progressDialog = new ProgressDialog( mActivity );
        this.hf = hf;
        trips  = new ArrayList<Trip>();
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
            List<ParseObject> tripList = query1.findInBackground(new FindCallback<ParseObject> {
            	
            });
            for(int i = 0; i < tripList.size(); i++) {
                ParseUser p = (ParseUser)tripList.get(i).get("userTwo");
                p = p.fetchIfNeeded();
                Match m = new Match((p.getString("firstName") + " " + p.getString("lastName")), p.getEmail(), p.getParseFile("userProfileImage"));
                matches.add(m);
            }
        } catch(Exception ex) {
            Log.d("MATCHESFRAG:", "There was an error! : " + ex.toString());
        }
        try{
            List<ParseObject> matchesList = query2.find();
            for(int i = 0; i < matchesList.size(); i++) {
                ParseUser p = (ParseUser)matchesList.get(i).get("userOne");
                p = p.fetchIfNeeded();
                Match m = new Match((p.getString("firstName") + " " + p.getString("lastName")), p.getEmail(), p.getParseFile("userProfileImage"));
                matches.add(m);
            }
        } catch(Exception ex) {
            Log.d("MATCHESFRAG:", "There was an error! : " + ex.toString());
        }

        Collections.sort(matches, new Comparator<Match>() {
            public int compare(Match m1, Match m2) {
                return m1.getName().compareTo(m2.getName());
            }
        });

        return matches;
    }

    protected void onPostExecute(ArrayList<Match> matches) {
        super.onPostExecute(matches);
        mf.asyncResult(matches);
        Match[] matchesArray = new Match[matches.size()];
        matchesArray = matches.toArray(matchesArray);
        MatchesAdapter customAdapter = new MatchesAdapter( mActivity, R.layout.item_matched_user, matchesArray);
        historyList.setAdapter(customAdapter);
        progressDialog.cancel();
    }
}
