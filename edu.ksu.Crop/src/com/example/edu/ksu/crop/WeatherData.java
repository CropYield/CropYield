package com.example.edu.ksu.crop;

public class WeatherData {
	
	private long id;
	private String date;
	private String high;
	private String low;
	private String pop;
	private String primary;
	
	public long getId(){
		return id;
	}
	
	public void setId(long id){
		this.id = id;
	}
	
	public String getDate(){
		return date;
	}
	
	public void setDate(String date){
		this.date = date;
	}
	
	public String getHigh(){
		return high;
	}

	public void setHigh(String high){
		this.high = high;
	}
	
	public String getLow(){
		return low;
	}
	
	public void setLow(String low){
		this.low = low;
	}
	
	public String getPOP(){
		return pop;
	}
	
	public void setPOP(String pop){
		this.pop = pop;
	}
	
	public String getPrimary(){
		return primary;
	}
	
	public void setPrimary(String primary){
		this.primary = primary;
	}
}
