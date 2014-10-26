package com.example.edu.ksu.crop;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.example.edu.ksu.crop.MainActivity.PlaceholderFragment;
import com.jjoe64.graphview.*;
import com.jjoe64.*;
import com.jjoe64.graphview.GraphViewDataInterface;
import com.jjoe64.graphview.LineGraphView;

public class GraphFragment extends Fragment{
	private static final String ARG_SECTION_NUMBER = "section_number";
	
	
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
		View rootView = inflater.inflate(R.layout.fragment_graph, container,
				false);
		
		
		GraphViewSeries exampleSeries = new GraphViewSeries(new GraphViewData[] {
			    new GraphViewData(1, 2.0d)
			    , new GraphViewData(2, 1.5d)
			    , new GraphViewData(3, 2.5d)
			    , new GraphViewData(4, 1.0d)
			});
			 
			GraphView graphView = new LineGraphView(getActivity(), "Example");
			graphView.addSeries(exampleSeries); // data
			 
			LinearLayout layout = (LinearLayout) rootView.findViewById(R.id.widget43);
			layout.addView(graphView);
		
		return rootView;
	}
}
