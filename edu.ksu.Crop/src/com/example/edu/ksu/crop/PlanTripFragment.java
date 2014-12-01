package com.example.edu.ksu.crop;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class PlanTripFragment extends Fragment {
	private static final String ARG_SECTION_NUMBER = "section_number";

	Button saveButton;
	EditText zipcode;
	// Initial and final half for querying using the weather api
	private static final String INITAL_HALF = "https://api.aerisapi.com/forecasts/closest?p=";
	private static final String FINAL_HALF = "&limit=7&client_id=3697JMRSKj6ncjcGrRIZt&client_secret=hyOUsu3HU4TG4kXAGEmwsL50ZKKRhoaYMFM8pti5";

	String zipcodeString;

	Fragment newFragment;
	FragmentTransaction transaction;
	private ArrayList<String> listOfWeatherData = new ArrayList<String>();
	private WeatherDataSource datasource;

	public static DataSet dataPassThrough;

	public static PlanTripFragment newInstance(int sectionNumber) {
		PlanTripFragment fragment = new PlanTripFragment();
		Bundle args = new Bundle();
		args.putInt(ARG_SECTION_NUMBER, sectionNumber);
		fragment.setArguments(args);
		return fragment;
	}

	public PlanTripFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_plan_trip,
				container, false);

		saveButton = (Button) rootView.findViewById(R.id.buttonSaveInfo);
		zipcode = (EditText) rootView.findViewById(R.id.etZipcode);

		datasource = new WeatherDataSource(getActivity());

		saveButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					zipcodeString = zipcode.getText().toString();

				} catch (Exception e) {
					Toast.makeText(getActivity(), "Must be valid Zip Code",
							Toast.LENGTH_SHORT).show();
				}

				try {
					new retrieve_weatherTask2().execute();
				} catch (Exception e) {
					Toast.makeText(getActivity(), "Failed to save information",
							Toast.LENGTH_SHORT).show();
				}

				

			}
		});

		return rootView;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		((MainActivity) activity).onSectionAttached(getArguments().getInt(
				ARG_SECTION_NUMBER));
	}

	protected class retrieve_weatherTask2 extends
			AsyncTask<Void, String, HashMap<Integer, ArrayList<String>>> {

		@Override
		public HashMap<Integer, ArrayList<String>> doInBackground(Void... arg0) {
			JSONObject jsonObject;
			JSONArray jsonArray;
			ArrayList<String> dailyWeatherIcon = new ArrayList<String>();

			try {
				WeatherData weather;
				jsonArray = getJsonArray();// Gets the weather
				listOfWeatherData = new ArrayList<String>();
				datasource.open();
				for (int i = 0; i < 7; i++) {
					weather = new WeatherData();

					jsonObject = jsonArray.getJSONObject(i);

					weather.setDate(jsonObject.getString("validTime")
							.substring(0, 10));
					weather.setHigh(jsonObject.getString("maxTempF"));
					weather.setLow(jsonObject.getString("minTempF"));
					weather.setPOP(jsonObject.getString("pop") + "%");
					weather.setPrimary(getPrimaryWeather(jsonObject
							.getString("weatherPrimaryCoded")));
					datasource.createWeather(weather.getDate(),
							weather.getHigh(), weather.getLow(),
							weather.getPOP(), weather.getPrimary());
				}

			} catch (Exception e) {
				e.printStackTrace();

			}

			return null;
		}

		protected void onPostExecute(
				HashMap<Integer, ArrayList<String>> forecast) {
			try {
				ArrayList<String> weatherInfo = new ArrayList<String>();
				ArrayList<String> dailyWeatherIcon = new ArrayList<String>();
				ArrayList<Integer> dailyWeatherDrawables = new ArrayList<Integer>();

				weatherInfo.addAll(forecast.get(0));
				dailyWeatherIcon.addAll(forecast.get(1)); // here

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		private String getPrimaryWeather(String primaryWeatherCode) {
			String[] weatherCodeSplit = primaryWeatherCode.split(":");
			primaryWeatherCode = weatherCodeSplit[2];
			try {
				if (primaryWeatherCode.equalsIgnoreCase("CL")
						|| primaryWeatherCode.equalsIgnoreCase("FW"))
					primaryWeatherCode = "1";// sunny

				else if (primaryWeatherCode.equalsIgnoreCase("OV")
						|| primaryWeatherCode.equalsIgnoreCase("BK"))
					primaryWeatherCode = "2"; // cloudy

				else if (primaryWeatherCode.equalsIgnoreCase("R")
						|| primaryWeatherCode.equalsIgnoreCase("L")
						|| primaryWeatherCode.equalsIgnoreCase("RW")
						|| primaryWeatherCode.equalsIgnoreCase("RS"))
					primaryWeatherCode = "3";// rain

				else if (primaryWeatherCode.equalsIgnoreCase("T"))
					primaryWeatherCode = "4";// Thunderstorms

				else if (primaryWeatherCode.equalsIgnoreCase("BS")
						|| primaryWeatherCode.equalsIgnoreCase("RS")
						|| primaryWeatherCode.equalsIgnoreCase("SI")
						|| primaryWeatherCode.equalsIgnoreCase("S")
						|| primaryWeatherCode.equalsIgnoreCase("S"))
					primaryWeatherCode = "5";// Snow

				else if (primaryWeatherCode.equalsIgnoreCase("BD")
						|| primaryWeatherCode.equalsIgnoreCase("BN")
						|| primaryWeatherCode.equalsIgnoreCase("BY"))
					primaryWeatherCode = "6";// windy

				else
					primaryWeatherCode = "7";// Partly cloudy by default

				return primaryWeatherCode;
			} catch (Exception ex) {
				ex.printStackTrace();
				return "7";
			}
		}

		private JSONArray getJsonArray() {
			JSONObject jsonObject;
			JSONArray jsonArray, jsonArray2 = new JSONArray();
			try {
				String str = new String();
				URL url = new URL(INITAL_HALF + zipcodeString + FINAL_HALF);
				Scanner scan = new Scanner(url.openStream());
				while (scan.hasNext())
					str += scan.nextLine();
				scan.close();
				jsonObject = new JSONObject(str);
				jsonArray = jsonObject.getJSONArray("response");
				jsonObject = jsonArray.getJSONObject(0);
				jsonObject = new JSONObject(jsonObject.toString());
				jsonArray2 = jsonObject.getJSONArray("periods");
				return jsonArray2;

			} catch (Exception e) {
				e.printStackTrace();
				return jsonArray2;
			}
		}

	}

}
