package com.example.edu.ksu.crop;

import com.jjoe64.graphview.*;
//Class to implement GraphViewDataInterface
//Please see documentation at: android-graphview.org(2014)
public class GraphViewData implements GraphViewDataInterface{

	private double X;
	private double Y;
	
	public GraphViewData(double x, double y){
		this.X = x;
		this.Y = y;
	}
	@Override
	public double getX() {
		// TODO Auto-generated method stub
		return X;
	}

	@Override
	public double getY() {
		// TODO Auto-generated method stub
		return Y;
	}

}
