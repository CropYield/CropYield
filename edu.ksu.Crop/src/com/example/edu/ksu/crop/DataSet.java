package com.example.edu.ksu.crop;

import java.util.ArrayList;
import java.util.List;

import android.location.Location;

public class DataSet {
	
	private String fieldName = "";
	private int headsPerPartAcre = 0;
	private List<Double> areas = new ArrayList<Double>();
	private int imageAreaCounter = 0;
	private Location loction;
	private Double sizeOfField;
	private int photosAnalyzed;
	private Double rowSize;
	public DataSet(){
		
	}
	
	public void SetHeadsPerAcre(int value){
		headsPerPartAcre = value;
	}
	
	//Returns the value for heads in the current acre set
	public int getHeadsPerAcre(){
		return headsPerPartAcre;
	}
	
	public void SetRowSize(Double d) {
		rowSize = d;
	}
	
	public Double getRowSize() {
		return rowSize;
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
		return 113.6 * AverageArea() - 255.8;
	}
	
	//Set the name of the field
	public void setFieldName(String name){
		this.fieldName = name;
	}
	
	//Return the name of the field
	public String getFieldName(){
		return fieldName;
	}
	
	public void setLocation(Location location){
		this.loction = location;
	}
	
	public Location getLocation(){
		return this.loction;
	}
	
	public void setFieldSize(double fieldSize){
		this.sizeOfField = fieldSize;
	}
	
	public Double getFieldSize(){
		return this.sizeOfField;
	}
	
	public void setPhotosAnalyzed(int count){
		this.photosAnalyzed = count;
	}
	
	public int getPhotoAnalyzed(){
		return this.photosAnalyzed;
	}
	
	
	
	

}
