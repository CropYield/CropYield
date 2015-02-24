package com.example.edu.ksu.crop;

public class Trip {
	private String calculatedYield;
	private String fieldName;
	private String headsPerAcre;
	private String location;
	private String photosAnalyzed;
	private String rowSize;
	private String sizeOfField;
	private String date;
	public Trip(
			String fieldName,
			String calculatedYield,
			String headsPerAcre,
			String location,
			String photosAnalyzed,
			String rowSize,
			String sizeOfField,
			String date ) 
	{
		this.fieldName = fieldName;
		this.calculatedYield = calculatedYield;
		this.headsPerAcre = headsPerAcre;
		this.location = location;
		this.photosAnalyzed = photosAnalyzed;
		this.rowSize = rowSize;
		this.sizeOfField = sizeOfField;
		this.date = date;
	}
	public String getDate() {
		return date;
	}
	public String getFieldName() {
		return fieldName;
	}
	
	public String getCalculatedYield() {
		return calculatedYield;
	}
	
	public String getHeadsPerAcre() {
		return headsPerAcre;
	}
	
	public String getLocation() {
		return location;
	}
	
	public String getPhotosAnalyzed() {
		return photosAnalyzed;
	}
	
	public String getRowSize() {
		return rowSize;
	}
	
	public String getSizeOfField() {
		return sizeOfField;
	}
}	

