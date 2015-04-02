package com.example.edu.ksu.crop;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class PlanTripFragment extends Fragment {
	private static final String ARG_SECTION_NUMBER = "section_number";

	Button saveButton;
	EditText zipcode;
	// Initial and final half for querying using the weather api
	private static final String INITAL_HALF = "https://api.aerisapi.com/forecasts/closest?p=";
	private static final String FINAL_HALF = "&limit=7&client_id=3697JMRSKj6ncjcGrRIZt&client_secret=hyOUsu3HU4TG4kXAGEmwsL50ZKKRhoaYMFM8pti5";

	String zipcodeString;

	Fragment newFragment;
	FragmentTransaction transaction;
	private ArrayList<String> listOfWeatherData = new ArrayList<String>();

	public static DataSet dataPassThrough;

	public static PlanTripFragment newInstance(int sectionNumber) {
		PlanTripFragment fragment = new PlanTripFragment();
		Bundle args = new Bundle();
		args.putInt(ARG_SECTION_NUMBER, sectionNumber);
		fragment.setArguments(args);
		return fragment;
	}

	public PlanTripFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_plan_trip,
				container, false);

		saveButton = (Button) rootView.findViewById(R.id.buttonSaveInfo);
		zipcode = (EditText) rootView.findViewById(R.id.etZipcode);

		

		saveButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					zipcodeString = zipcode.getText().toString();

				} catch (Exception e) {
					Toast.makeText(getActivity(), "Must be valid Zip Code",
							Toast.LENGTH_SHORT).show();
				}
			}
		});

		return rootView;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		((MainActivity) activity).onSectionAttached(getArguments().getInt(
				ARG_SECTION_NUMBER));
	}

	

}
