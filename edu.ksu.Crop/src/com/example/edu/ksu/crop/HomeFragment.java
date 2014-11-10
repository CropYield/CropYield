package com.example.edu.ksu.crop;

import com.example.edu.ksu.crop.MainActivity.WeatherFragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class HomeFragment extends Fragment {

	private static final String ARG_SECTION_NUMBER = "section_number";
	Button homeButton;
	Button weatherButton;
	Button tripButton;
	Button photoButton;
	Button calcButton;
	Button projectionButton;

	public static android.support.v4.app.Fragment newInstance(int sectionNumber) {
		HomeFragment fragment = new HomeFragment();
		Bundle args = new Bundle();
		args.putInt(ARG_SECTION_NUMBER, sectionNumber);
		fragment.setArguments(args);
		return fragment;
	}

	public HomeFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_calculate, container,
				false);
		final FragmentManager fragmentManager = getFragmentManager();
		
		homeButton = (Button) rootView.findViewById(R.id.home_button);
		homeButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Toast.makeText(getActivity(), "Already Home", Toast.LENGTH_SHORT).show();
			}
		});
		
		weatherButton = (Button) rootView.findViewById(R.id.weather_button);
		weatherButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				fragmentManager.beginTransaction().replace(R.id.container, WeatherFragment.newInstance(2))
			.commit();
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
