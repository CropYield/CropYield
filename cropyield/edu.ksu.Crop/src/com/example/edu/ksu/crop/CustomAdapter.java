package com.example.edu.ksu.crop;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class CustomAdapter extends BaseAdapter{
	private static int LENGTH_OF_FORECAST = 7;
	private int i,j,k = 0;
	int[] imageId;
	Context context;
	ArrayList<String> weatherInfo;
	ArrayList<Integer> dailyWeatherIcons;
	private static LayoutInflater layoutInflater = null;
	
	public CustomAdapter(Context cxt, int[] images, ArrayList<Integer> daily, ArrayList<String> weather){
		context = cxt;
		imageId = images;
		weatherInfo = weather;
		dailyWeatherIcons = daily;
		layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	@Override
    public int getCount() {
        // TODO Auto-generated method stub
        return LENGTH_OF_FORECAST;
    }
 
    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }
 
    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }
 
    public class Holder
    {
        TextView tv_date, tv_low, tv_high, tv_pop, tv_lowExample, tv_highExample, tv_popExample;
        ImageView iv_icon, iv_low, iv_high, iv_pop, iv_lowExample, iv_highExample, iv_popExample;
        
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
    	View rowView; 
    	try{
    		j+=1;
    		
    		Holder holder=new Holder();
               
        	rowView 	   = layoutInflater.inflate(R.layout.weather_custom_rows, null);
        	holder.iv_icon =(ImageView) rowView.findViewById(R.id.iv_icon);
        	holder.iv_low  =(ImageView) rowView.findViewById(R.id.iv_low);
        	holder.iv_high =(ImageView) rowView.findViewById(R.id.iv_high);
        	holder.iv_pop  =(ImageView) rowView.findViewById(R.id.iv_pop);
        	holder.tv_date =(TextView) rowView.findViewById(R.id.tv_date);
        	holder.tv_low  =(TextView) rowView.findViewById(R.id.tv_low);
        	holder.tv_high =(TextView) rowView.findViewById(R.id.tv_high); 
        	holder.tv_pop  =(TextView) rowView.findViewById(R.id.tv_pop);
        	
        	holder.iv_icon.setImageResource(dailyWeatherIcons.get(k));
        	holder.iv_low.setImageResource(imageId[1]);
        	holder.iv_high.setImageResource(imageId[2]);
        	holder.iv_pop.setImageResource(imageId[3]);
        	if(i < 28 && j >= 8){
        		holder.iv_icon.setImageResource(dailyWeatherIcons.get(k));
        		holder.tv_date.setText(weatherInfo.get(i));
        		holder.tv_low.setText(weatherInfo.get(i+1));
        		holder.tv_high.setText(weatherInfo.get(i+2));
        		holder.tv_pop.setText(weatherInfo.get(i+3));
        		i += 4;
        		k += 1;
        	}
        	
         rowView.setOnClickListener(new OnClickListener() {         
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Toast.makeText(context, "You Clicked "+weatherInfo.indexOf(position), Toast.LENGTH_LONG).show();
            }
        });
         
        return rowView;
    	}catch(Exception e){
        	e.printStackTrace();
        	return rowView = layoutInflater.inflate(R.layout.weather_custom_rows, null);
        }
    }
}

