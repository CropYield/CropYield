package com.example.edu.ksu.crop;

import java.util.ArrayList;
import java.util.List;

import com.example.edu.ksu.crop.MainActivity.WeatherFragment;

public class DataSet {
	
	private int headsPerPartAcre = 0;
	private List<Double> areas = new ArrayList<Double>();
	//we might need to use a dictionary so that we can add and remove image areas as we go.
	//I will have to look a little deeper into the photo code, but for now this will work.
	
	private int imageAreaCounter = 0;
	
	public DataSet(){
		
	}
	
	public void SetHeadsPerAcre(int value){
		headsPerPartAcre = value;
	}
	
	//Returns the value for heads in the current acre set
	public int ReturnHeadsPerAcre(){
		return headsPerPartAcre;
	}
	
	//Add a calculated area to the total
	public void AddAreas(double value){
		areas.add(value);
		imageAreaCounter++;
	}
	
	//This is incomplete
	public double AverageArea(){
		double averageArea = 0;
		
		for(double area : areas){
			averageArea += area;
			
		}
		
		averageArea = averageArea/areas.size();
		
		return averageArea;
	}
	
	//Equation supplied as of 10/06/14 This is using inches for the equation.
	public double ReturnGrainNumber(){
		return 120 * AverageArea() - 400;
	}
	
	
	
	

}
