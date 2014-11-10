package com.example.edu.ksu.crop;

import java.util.ArrayList;
import java.util.List;

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
		int counter = 0;
		for(double area : areas){
			averageArea += area;
			counter++; 
		}
		return (averageArea/counter);
	}
	
	
	
	

}
