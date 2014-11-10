package com.example.edu.ksu.crop;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.jjoe64.graphview.*;
import com.jjoe64.graphview.LineGraphView;

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

	public FinalFragment() {

	}

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

		GraphView graphView = new LineGraphView(getActivity(),
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

	private static GraphViewData[] CalculateValues(int HPA, double d, int SPP) {
		double  lowBUA, highBUA;
		averageBUA = (double) ((((HPA * d) * 1000) / SPP) / 56);
		lowBUA = averageBUA * .85;
		highBUA = averageBUA * 1.15;

		return new GraphViewData[] { new GraphViewData(1, lowBUA),
				new GraphViewData(2, averageBUA), new GraphViewData(3, highBUA) };

	}

	private int UpdateGraphView(int x, int d, int c) {
		return (int) ((((double) (x - 1) / (double) (99)) * (d - c)) + c);
	}

}
