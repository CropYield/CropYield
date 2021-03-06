package com.example.edu.ksu.crop;

import java.util.Set;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class InputFragment extends Fragment {
	private static final String ARG_SECTION_NUMBER = "section_number";
	Button nextButton;
	Button helpbutton;
	
	EditText fieldName;
	EditText headsPerAcre;
	EditText editFieldSize;
	
	RadioGroup hasSheet;
	RadioGroup rowSize;
	
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

	public InputFragment() {
		dataPassThrough = new DataSet();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_input_field,
				container, false);

		headsPerAcre = (EditText) rootView.findViewById(R.id.editHPA);
		nextButton = (Button) rootView.findViewById(R.id.buttonNext);
		fieldName = (EditText) rootView.findViewById(R.id.editFieldName);
		hasSheet = (RadioGroup) rootView.findViewById(R.id.radioHasSheetGroup);
		rowSize = (RadioGroup) rootView.findViewById(R.id.radioRowSizeGroup);
		helpbutton = (Button) rootView.findViewById(R.id.buttonHelpInput);
		editFieldSize = (EditText) rootView.findViewById(R.id.editFieldSize);
		
		
		
		helpbutton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				 Intent intent = new Intent();
			        intent.setAction(Intent.ACTION_VIEW);
			        intent.addCategory(Intent.CATEGORY_BROWSABLE);
			        intent.setData(Uri.parse("http://www.sorghumyield.cis.ksu.edu"));//This needs to be changed
			        startActivity(intent);
				
			}
		});
		
		
		hasSheet.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				hideKeyboard();
				if(checkedId == R.id.radioYes){
					nextButton.setEnabled(true);
				} else {
					nextButton.setEnabled(false);
				}
				
			}
		});

//		rowSize.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//			
//			@Override
//			public void onCheckedChanged(RadioGroup group, int checkedId) {
//				if(checkedId == R.id.radioYes){
//					nextButton.setEnabled(true);
//				} else {
//					nextButton.setEnabled(false);
//				}
//				
//			}
//		});
		
		nextButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				int headsPer = 0;
				String fieldNameString = "";
				double fieldSize = 0.0;
				try {
					headsPer = Integer.parseInt(headsPerAcre.getText()
							.toString());
				} catch (Exception E) {
					Toast.makeText(getActivity(),
							"Value must be greater than 0.", Toast.LENGTH_LONG)
							.show();
					return;
				}
				
				try {
					fieldNameString = fieldName.getText().toString();
				} catch (Exception E) {
					Toast.makeText(getActivity(),
							"Please enter a field name.", Toast.LENGTH_LONG)
							.show();
					return;
				}
				
				try {
					fieldSize = Double.parseDouble(editFieldSize.getText().toString());
				} catch (Exception E) {
					Toast.makeText(getActivity(),
							"Please enter size of field.", Toast.LENGTH_LONG)
							.show();
					return;
				}

				int rowSizeButtonSelected = rowSize.getCheckedRadioButtonId();
				double rowSizeVal = 7.5;
				
				if(rowSizeButtonSelected == 2) {
					rowSizeVal = 15.0;
				} else if(rowSizeButtonSelected == 3) {
					rowSizeVal = 30.0;
				}
				
				
				
				
				if (headsPer != 0 && !fieldNameString.isEmpty()) {
					headsPer = Integer.parseInt(headsPerAcre.getText()
							.toString());
					fieldNameString = fieldName.getText().toString();

					
					dataPassThrough.setFieldName(fieldNameString);
					dataPassThrough.SetHeadsPerAcre(headsPer);
					dataPassThrough.SetRowSize(rowSizeVal);
					dataPassThrough.setFieldSize(fieldSize);// Set the head
																// per to pass
																// data through
																// to next image
																// processing
																// part
					newFragment = PictureFragment.newInstance(4,
							dataPassThrough);
					
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

	private void hideKeyboard() {    
	    // Check if no view has focus: 
	    View view = getActivity().getCurrentFocus();
	    if (view != null) {
	        InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
	        inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	    } 
	} 
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		((MainActivity) activity).onSectionAttached(getArguments().getInt(
				ARG_SECTION_NUMBER));
	}

}
