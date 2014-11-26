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

public class HeadInputFragment extends Fragment {
	private static final String ARG_SECTION_NUMBER = "section_number";
	Button nextButton;
	EditText headsPerAcre;
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

	public HeadInputFragment() {
		dataPassThrough = new DataSet();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_head_input,
				container, false);

		headsPerAcre = (EditText) rootView.findViewById(R.id.tvHeadsPerAcre);
		
		nextButton = (Button) rootView.findViewById(R.id.buttonToPictures);
		
		
		nextButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				int headsPer = 0;
				try {
					headsPer = Integer.parseInt(headsPerAcre.getText().toString());
				} catch(Exception E){
					Toast.makeText(getActivity(), "Value must be greater than 0.",
							Toast.LENGTH_LONG).show();
					return;
					
				}
				if( headsPer != 0){
					headsPer = Integer.parseInt(headsPerAcre.getText()
							.toString());
					
					//Set the head per to pass data through to next image processing part
					dataPassThrough.SetHeadsPerAcre(headsPer);
					
		
					newFragment = ConfirmFragment.newInstance(4, dataPassThrough);
					
					
					transaction = getFragmentManager().beginTransaction();

					// Replace whatever is in the fragment_container view with
					// this fragment,
					// and add the transaction to the back stack
					transaction.replace(R.id.container, newFragment);
					transaction.addToBackStack(null);

					// Commit the transaction
					transaction.commit();
				} else {
				
				Toast.makeText(getActivity(), "Please input all items.",
						Toast.LENGTH_LONG).show();
				return;
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

