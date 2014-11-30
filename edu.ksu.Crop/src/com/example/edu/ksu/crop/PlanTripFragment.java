
package com.example.edu.ksu.crop;


import java.util.Set;

import android.app.Activity;
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
	
	Fragment newFragment;
	FragmentTransaction transaction;
	
	public static DataSet dataPassThrough;


	public static InputFragment newInstance(int sectionNumber) {
		InputFragment fragment = new InputFragment();
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

		
		saveButton = (Button) rootView.findViewById(R.id.buttonToPictures);
		
		
		saveButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				// Brent/Hayden this is where you would implement the saving to the 
				// database. I'm not sure which one of you will do this, but after they
				// hit this button, you should probably navigate back to the fragment_main
				
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

