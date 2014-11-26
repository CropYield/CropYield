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

public class ConfirmFragment extends Fragment {
	private static final String ARG_SECTION_NUMBER = "section_number";
	Button yesButton;
	Button noButton;
	static DataSet dataSet;

	Fragment newFragment;
	FragmentTransaction transaction;

	public static DataSet dataPassThrough;

	public static InputFragment newInstance(int sectionNumber, DataSet data) {
		InputFragment fragment = new InputFragment();
		Bundle args = new Bundle();
		args.putInt(ARG_SECTION_NUMBER, sectionNumber);
		fragment.setArguments(args);
		dataSet = data;
		return fragment;
	}

	public ConfirmFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_head_input,
				container, false);

		yesButton = (Button) rootView.findViewById(R.id.buttonYes);
		noButton = (Button) rootView.findViewById(R.id.buttonNo); 

		yesButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				// Hayden Change this to the Picture Fragment Page
				// *************************************************************************************
				newFragment = PictureFragment.newInstance(4, dataPassThrough);

				transaction = getFragmentManager().beginTransaction();

				// Replace whatever is in the fragment_container view with
				// this fragment,
				// and add the transaction to the back stack
				transaction.replace(R.id.container, newFragment);
				transaction.addToBackStack(null);

				// Commit the transaction
				transaction.commit();

			}
		});
		
		noButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//Implement the web view
				//Show what they need to continue
				
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
