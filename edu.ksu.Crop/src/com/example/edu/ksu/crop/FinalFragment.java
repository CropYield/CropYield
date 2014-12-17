package com.example.edu.ksu.crop;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.example.edu.ksu.crop.MainActivity.WeatherFragment;
import com.jjoe64.graphview.*;

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
	
	/* newInstance
	 * @params:
	 *	sectionNumber : int : not necessary for input since this fragment isn't accessible from the menu
	 *	dataSet : DataSet : Used to pass through data about the recent fields like area
	 * 
	 * Return : FinalFragment : returns an instance of this class.
	 */
	public static FinalFragment newInstance(int sectionNumber, DataSet dataSet) {
		FinalFragment fragment = new FinalFragment();
		data = dataSet;
		headsPerAcreInt = data.ReturnHeadsPerAcre();
		grainNum = data.ReturnGrainNumber();
		CalculateValues(headsPerAcreInt,
				grainNum, seedsPerPound);
		Bundle args = new Bundle();
		args.putInt(ARG_SECTION_NUMBER, sectionNumber);
		fragment.setArguments(args);
		return fragment;
	}

	/*
	 * Constructor for override
	 * 
	 */
	public FinalFragment() {

	}

	/*
	 * Implemented by default. FinalFragment extends Fragment. Please see documentation below
	 * 
	 * This also creates the view and set all the listeners for the View
	 * (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View rootView = inflater.inflate(R.layout.fragment_final_page,
				container, false);

		seekBarHeads = (SeekBar) rootView.findViewById(R.id.seekBarHeadsPer);
		bpaTV = (TextView) rootView.findViewById(R.id.textViewValueBPAF);
		String tempVal4 = String.format("%.2f", averageBUA) +
				" bu/acre";
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
				String tempVal = String.format("%.2f", averageBUA) +
				" bu/acre";
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
		
		weather = (Button) rootView.findViewById(R.id.weatherButtonFinal);
		weather.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Fragment newFragment = WeatherFragment.newInstance(10);//Instance has something to do with title, will work on this later during clearn up
				FragmentTransaction transaction = getFragmentManager().beginTransaction();
				transaction.replace(R.id.container, newFragment);
				transaction.addToBackStack(null);
				transaction.commit();
			}
		});
		
		return rootView;
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
	 * @params:
	 * 	HPA : int : Heads per Acre
	 *  d : double : calculated value from pictures
	 *  SPP : int : SeedsPerPound
	 *  
	 * Return: 
	 * 	GraphViewData[] : array of GraphViewData from GraphViewData.java
	 *  Please see the GraphViewData.java file to see how this is implemented. It
	 *  is used to implement the interface from android-graphview.
	 * 	See android-graphview.org for documentation.
	 * 
	 */
	private static GraphViewData[] CalculateValues(int HPA, double d, int SPP) {
		double  lowBUA, highBUA;
		averageBUA = (double) ((((HPA * d) * 1000) / SPP) / 56);
		lowBUA = averageBUA * .85;
		highBUA = averageBUA * 1.15;

		return new GraphViewData[] { new GraphViewData(1, lowBUA),
				new GraphViewData(2, averageBUA), new GraphViewData(3, highBUA) };

	}

	/*
	 * This is used to convert ranges of 0 - 100 to different values(sliders)
	 * @params:
	 * 	x : int : The value of the slider(0 - 100)
	 * 	d : int : Maximum value of range
	 *  c : int : Minimum value of range
	 *  
	 * Return:
	 * 	int : valuse converted from 100 to number within range
	 */
	private int UpdateGraphView(int x, int d, int c) {
		return (int) ((((double) (x - 1) / (double) (99)) * (d - c)) + c);
	}

}
