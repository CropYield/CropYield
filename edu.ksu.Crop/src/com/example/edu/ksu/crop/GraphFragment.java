package com.example.edu.ksu.crop;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.example.edu.ksu.crop.MainActivity.PlaceholderFragment;
import com.jjoe64.graphview.*;
import com.jjoe64.*;
import com.jjoe64.graphview.GraphViewDataInterface;
import com.jjoe64.graphview.LineGraphView;

public class GraphFragment extends Fragment implements OnSeekBarChangeListener{
	private static final String ARG_SECTION_NUMBER = "section_number";
	GraphViewSeries exampleSeries;
	SeekBar seekBarHeads;
	SeekBar seekBarSPH;
	SeekBar seekBarSPP;
	
	TextView tvHeads;
	TextView tvSPH;
	TextView tvSPP;
	
	int headsPer = 45;
	int seedsPerHead = 2500;
	int seedsPerPound = 15500;
	
	double averageBUA, lowBUA, highBUA = 0;
	
	public static GraphFragment newInstance(int sectionNumber) {
		GraphFragment fragment = new GraphFragment();
		Bundle args = new Bundle();
		args.putInt(ARG_SECTION_NUMBER, sectionNumber);
		fragment.setArguments(args);
		return fragment;
	}

	public GraphFragment() {
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View rootView = inflater.inflate(R.layout.fragment_graph, container,
				false);
		
		seekBarHeads = (SeekBar) rootView.findViewById(R.id.seekBarHeads);
		seekBarSPH = (SeekBar) rootView.findViewById(R.id.seekBarSPH);
		seekBarSPP = (SeekBar) rootView.findViewById(R.id.seekBarSPP);
		
		tvHeads = (TextView) rootView.findViewById(R.id.textViewHPA);
		tvSPH = (TextView) rootView.findViewById(R.id.textViewSPHV);
		tvSPP = (TextView) rootView.findViewById(R.id.textViewSPPV);
		
		exampleSeries = new GraphViewSeries(CalculateValues(headsPer, seedsPerHead, seedsPerPound));
		 
		GraphView graphView = new LineGraphView(getActivity(), "Projeced Bushels Per Acre");
		graphView.addSeries(exampleSeries); // data
		graphView.setHorizontalLabels(new String[] {"Low Yield", "Average Yield", "High Yield"});
		LinearLayout layout = (LinearLayout) rootView.findViewById(R.id.widget43);
		layout.addView(graphView);
		
		
		seekBarHeads.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            	//Graph stuff
            	exampleSeries.resetData(CalculateValues(headsPer, seedsPerHead, seedsPerPound));

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                    boolean fromUser) {
                // TODO Auto-generated method stub
        		//tvHeads = (TextView) rootView.findViewById(R.id.textViewHPA);

            	headsPer = UpdateGraphView(progress, 90, 30);
            	exampleSeries.resetData(CalculateValues(headsPer, seedsPerHead, seedsPerPound));

                //tvHeads.setText(progress);

            }
        });
		
		seekBarSPH.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            	//Graph stuff
            	exampleSeries.resetData(CalculateValues(headsPer, seedsPerHead, seedsPerPound));

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                    boolean fromUser) {
                // TODO Auto-generated method stub
            	seedsPerHead = UpdateGraphView(progress, 3500, 1500);
            	exampleSeries.resetData(CalculateValues(headsPer, seedsPerHead, seedsPerPound));

                //tvSPH.setText(progress);

            }
        });
		
		
		seekBarSPP.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            	//Graph stuff
            	exampleSeries.resetData(CalculateValues(headsPer, seedsPerHead, seedsPerPound));
            	

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
            	exampleSeries.resetData(CalculateValues(headsPer, seedsPerHead, seedsPerPound));

               // tvHeads.setText(progress);

            }
        });
		
		
		return rootView;
	}
	
	
	private GraphViewData[] CalculateValues(int HPA, int SPH, int SPP){
		
		averageBUA = (double)((((HPA * SPH) * 1000) / SPP) / 56);
		lowBUA = averageBUA * .85;
		highBUA = averageBUA * 1.15;
		
		return new GraphViewData[] {
			    new GraphViewData(1, lowBUA)
			    , new GraphViewData(2, averageBUA)
			    , new GraphViewData(3, highBUA)
		};
		
	}
	
	private int UpdateGraphView(int x, int d, int c){
		return (int)((((double)(x - 1) / (double)(99)) * (d - c)) + c);
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		// TODO Auto-generated method stub
		if(seekBar == seekBarHeads){
			headsPer = UpdateGraphView(progress, 90, 30);
           // tvHeads.setText(progress);

		} else if (seekBar == seekBarSPH){
			seedsPerPound = UpdateGraphView(progress, 22000, 9000);
           // tvSPH.setText(progress);
		} else {
			seedsPerPound = UpdateGraphView(progress, 90, 30);
            //tvHeads.setText(progress);
		}
		
	}

	@Override
	public void onStartTrackingTouch(SeekBar arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStopTrackingTouch(SeekBar arg0) {
		// TODO Auto-generated method stub
		exampleSeries.resetData(CalculateValues(headsPer, seedsPerHead, seedsPerPound));
		
	}
}
