package com.example.edu.ksu.crop;

import java.util.ArrayList;
import java.util.List;

public class DataSet {
	
	private String fieldName = "";
	private int headsPerPartAcre = 0;
	private List<Double> areas = new ArrayList<Double>();
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
	
	//Set the name of the field
	public void setFieldName(String name){
		this.fieldName = name;
	}
	
	//Return the name of the field
	public String getFieldName(){
		return fieldName;
	}
	
	
	
	

}
