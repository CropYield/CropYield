package com.example.edu.ksu.crop;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseObject;
import com.parse.ParseUser;

//import com.example.edu.ksu.crop.MainActivity.WeatherFragment.PictureFragment;

public class MainActivity extends ActionBarActivity implements
		NavigationDrawerFragment.NavigationDrawerCallbacks {

	/**
	 * Fragment managing the behaviors, interactions and presentation of the
	 * navigation drawer.
	 */
	private NavigationDrawerFragment mNavigationDrawerFragment;

	/**
	 * Used to store the last screen title. For use in
	 * {@link #restoreActionBar()}.
	 */
	private CharSequence mTitle;
	private String mId = "gD6b5jCam6YiHvknBsJr2Vl34oFvThlSdOUZBXkq";
	private String mKey = "DnsiavpkN6mQPpuh7yZEo8R9o4zpSVNvnSPCRYQc";
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Parse.initialize(this, mId, mKey);
		ParseAnalytics.trackAppOpened(getIntent());
		
		setContentView(R.layout.activity_main);

		mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager()
				.findFragmentById(R.id.navigation_drawer);
		mTitle = getTitle();

		// Set up the drawer.
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout));
		
	}

	@Override
	public void onNavigationDrawerItemSelected(int position) {
		// update the main content by replacing fragments
		FragmentManager fragmentManager = getSupportFragmentManager();
		Fragment tempFragment = PlaceholderFragment.newInstance(position + 1);
		if (position == 0) {
			tempFragment = PlaceholderFragment.newInstance(position + 1);
		} else if (position == 1) {
			tempFragment = HistoryFragment.newInstance(position + 1);
		} else if (position == 2) {
			tempFragment = WeatherFragment.newInstance(position + 1);
		} else if (position == 3) {
			tempFragment = SoilFragment.newInstance(position + 1);
		} else if (position == 4) {
			tempFragment = CalculateFragment.newInstance(position + 1);
		} else if (position == 5) {
			tempFragment = ResearchFragment.newInstance(position + 1);
		} else {
			tempFragment = PlaceholderFragment.newInstance(position + 1);
		}
		fragmentManager
		.beginTransaction()
		.replace(R.id.container, tempFragment).commit();
		
	}

	public void onSectionAttached(int number) {
		switch (number) {
		case 1:
			mTitle = getString(R.string.title_section1);
			break;
		case 2:
			mTitle = getString(R.string.title_section2);
			break;
		case 3:
			mTitle = getString(R.string.title_section3);
			break;
		case 4:
			mTitle = getString(R.string.title_section4);
			break;
		case 5:
			mTitle = getString(R.string.title_section5);
			break;
		case 6:
			mTitle = getString(R.string.title_section6);
			break;
		}
	}

	public void restoreActionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(mTitle);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (!mNavigationDrawerFragment.isDrawerOpen()) {
			// Only show items in the action bar relevant to this screen
			// if the drawer is not showing. Otherwise, let the drawer
			// decide what to show in the action bar.
			getMenuInflater().inflate(R.menu.main, menu);
			restoreActionBar();
			return true;
		}
		return super.onCreateOptionsMenu(menu);
	}

	private void logoutCurrentUser() {
		ParseUser currentUser = ParseUser.getCurrentUser();
		Intent intent = new Intent(this, LoginActivity.class);
		if (currentUser != null) {
			currentUser.logOut();
			ParseObject.unpinAllInBackground();
			Toast.makeText(this,  "User " + currentUser.getUsername() + " logged out", Toast.LENGTH_LONG).show();
		}
		startActivity(intent);
		finish();
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		} else if (id == R.id.action_logout) {
			logoutCurrentUser();
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * The Placeholder Fragment is actually the view for the home page. This is
	 * the page that contains the two buttons that you would normally click to
	 * navigate to either the Yield Calculator or the Plan Trip area.
	 */
	public static class PlaceholderFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		private static final String ARG_SECTION_NUMBER = "section_number";
		Button yieldCalculator;
		Button planTrip;
		Button logOut;

		ImageView twitterLink;
		
		Fragment newFragment;
		FragmentTransaction transaction;

		/**
		 * Returns a new instance of this fragment for the given section number.
		 */
		public static PlaceholderFragment newInstance(int sectionNumber) {
			PlaceholderFragment fragment = new PlaceholderFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}

		public PlaceholderFragment() {
		}

		// This sets the functionality of the two buttons and where they
		// navigate
		// when they are clicked.
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
		
			twitterLink = (ImageView) rootView.findViewById(R.id.imageView_twitter);

			twitterLink.setOnClickListener(new View.OnClickListener() { 
			    @Override 
			    public void onClick(View v) {
			    	Intent intent = null;
			    	try { 
			    	    // get the Twitter app if possible 
			    	    getActivity().getPackageManager().getPackageInfo("com.twitter.android", 0);
			    	    intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?user_id=176287044"));
			    	    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			    	} catch (Exception e) {
			    	    // no Twitter app, revert to browser 
			    	    intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/KSUCROPS"));
			    	} 
			    	getActivity().startActivity(intent);

			    } 
			}); 
			
			yieldCalculator = (Button) rootView
					.findViewById(R.id.YieldCalculator);
			planTrip = (Button) rootView.findViewById(R.id.PlanTrip);

			planTrip.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					// Create new fragment and transaction
					newFragment = PlanTripFragment.newInstance(10);// Instance
																	// has
																	// something
																	// to do
																	// with
																	// title,
																	// will work
																	// on this
																	// later
																	// during
																	// clearn up
					transaction = getFragmentManager().beginTransaction();
					transaction.replace(R.id.container, newFragment);
					transaction.addToBackStack(null);
					transaction.commit();
				}
			});

			// Sets the button to navigate to the start of the Yield Calculator
			yieldCalculator.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					newFragment = InputFragment.newInstance(7);
					transaction = getFragmentManager().beginTransaction();
					transaction.replace(R.id.container, newFragment);
					transaction.addToBackStack(null);
					transaction.commit();
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
	}

	public static class WeatherFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		private static final String ARG_SECTION_NUMBER = "section_number";

		/**
		 * Returns a new instance of this fragment for the given section number.
		 */

		private static final int LENGTH_OF_FORECAST = 7;
		private ImageView iv_lowExample, iv_highExample, iv_popExample;
		private TextView tv_lowExample, tv_highExample, tv_popExample;
		private ListView listView;
		private static int[] weatherImages = { R.drawable.sun48,
				R.drawable.down48, R.drawable.up48, R.drawable.littlerain48 }; // 7
																				// images,
																				// 1
																				// set
																				// for
																				// each
																				// listview
		private ArrayList<WeatherData> listOfWeatherData;
		@SuppressLint("UseSparseArrays")
		private HashMap<Integer, ArrayList<String>> dictFiveDayForecast = new HashMap<Integer, ArrayList<String>>();
		WeatherData weatherData;
		private double latitude, longitude = 0.0;

		public static WeatherFragment newInstance(int sectionNumber) {

			WeatherFragment fragment = new WeatherFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}

		public WeatherFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_weather,
					container, false);

			iv_lowExample = (ImageView) rootView
					.findViewById(R.id.iv_lowExample);
			iv_highExample = (ImageView) rootView
					.findViewById(R.id.iv_highExample);
			iv_popExample = (ImageView) rootView
					.findViewById(R.id.iv_popExample);
			tv_lowExample = (TextView) rootView
					.findViewById(R.id.tv_lowExample);
			tv_highExample = (TextView) rootView
					.findViewById(R.id.tv_highExample);
			tv_popExample = (TextView) rootView
					.findViewById(R.id.tv_popExample);
			listView = (ListView) rootView.findViewById(R.id.listView);
			Location location = obtainLocation(false);
			try {
			latitude = location.getLatitude();
			longitude = location.getLongitude();
			} catch (NullPointerException np) {
				latitude = 39.1917; 
				longitude = -96.5917;
				sendToast("Unable to obtain location, defaulting to Manhattan, KS"
						, Toast.LENGTH_LONG);
			}

			new retrieve_weatherTask().execute();
			Toast.makeText(getActivity(), "Retrieving Weather",
					Toast.LENGTH_SHORT).show();
			return rootView;
		}

		private Location obtainLocation(Boolean showToast) {
			getActivity();
			LocationManager locationManager = (LocationManager) getActivity()
					.getSystemService(Context.LOCATION_SERVICE);
			Location location = locationManager
					.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			if (showToast)
				sendToast("Location is " + location.getLatitude() + ", "
						+ location.getLongitude() + ".", Toast.LENGTH_LONG);
			return location;
		}

		private void sendToast(String message, int length) {

			Activity context = getActivity();
			CharSequence text = (CharSequence) message;
			int duration = length;
			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
		}

		protected class retrieve_weatherTask extends
				AsyncTask<Void, String, ArrayList<WeatherData>> {
			@Override
			public ArrayList<WeatherData> doInBackground(
					Void... arg0) {
				JSONObject jsonObject;
				JSONArray jsonArray;
				
				try {

					jsonArray = getJsonArray();
					listOfWeatherData = new ArrayList<WeatherData>();
					for (int i = 0; i < LENGTH_OF_FORECAST; i++) {
						
						weatherData = new WeatherData();
						jsonObject = jsonArray.getJSONObject(i);
						weatherData.setId(i);
						weatherData.setDate((jsonObject.getString("validTime")
								.substring(0, 10)));
						weatherData.setLow(jsonObject.getString("minTempF"));
						weatherData.setHigh(jsonObject.getString("maxTempF"));
						weatherData.setPOP(jsonObject.getString("pop") + "%");
						weatherData.setPrimary(getPrimaryWeather(jsonObject
								.getString("weatherPrimaryCoded")));
						
						listOfWeatherData.add(weatherData);

					}
					return listOfWeatherData;
				} catch (Exception e) {
					e.printStackTrace();
					return listOfWeatherData;
				}
			}

			protected void onPostExecute(
					ArrayList<WeatherData> listOfWeatherData) {
				try {
					WeatherData wd = new WeatherData();
					ArrayList<String> weatherInfo = new ArrayList<String>();
					ArrayList<String> dailyWeatherIcon = new ArrayList<String>();
					ArrayList<Integer> dailyWeatherDrawables = new ArrayList<Integer>();
					for(int i = 0; i < LENGTH_OF_FORECAST; i++){
						
						wd = listOfWeatherData.get(i);
						dailyWeatherIcon.add(wd.getPrimary());
						weatherInfo.add(wd.getDate());
						weatherInfo.add(wd.getLow());
						weatherInfo.add(wd.getHigh());
						weatherInfo.add(wd.getPOP());
					}

					for (int i = 0; i < dailyWeatherIcon.size(); i++) {
						if (dailyWeatherIcon.get(i).equals("1"))// sunny
							dailyWeatherDrawables.add(R.drawable.sun48);
						else if (dailyWeatherIcon.get(i).equals("2"))// overcast
							dailyWeatherDrawables.add(R.drawable.clouds48);
						else if (dailyWeatherIcon.get(i).equals("3"))// rain
							dailyWeatherDrawables.add(R.drawable.littlerain48);
						else if (dailyWeatherIcon.get(i).equals("4"))// thunderstorm
							dailyWeatherDrawables.add(R.drawable.storm48);
						else if (dailyWeatherIcon.get(i).equals("5"))// snow
							dailyWeatherDrawables.add(R.drawable.snow48);
						else if (dailyWeatherIcon.get(i).equals("6"))// windy
							dailyWeatherDrawables.add(R.drawable.windy48);
						else// partly cloudy
							dailyWeatherDrawables
									.add(R.drawable.partlycloudyday48);
					}

					tv_lowExample.setText("Low");
					tv_highExample.setText("High");
					tv_popExample.setText("Rain");
					iv_lowExample.setImageResource(R.drawable.down48);
					iv_highExample.setImageResource(R.drawable.up48);
					iv_popExample.setImageResource(R.drawable.littlerain48);
					listView.setAdapter(new CustomAdapter(getActivity(),
							weatherImages, dailyWeatherDrawables, weatherInfo));

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
					URL url = new URL(
							"https://api.aerisapi.com/forecasts/closest?p="
									+ latitude
									+ ",%20"
									+ longitude
									+ "&limit="
									+ LENGTH_OF_FORECAST
									+ "&client_id=3697JMRSKj6ncjcGrRIZt&client_secret=hyOUsu3HU4TG4kXAGEmwsL50ZKKRhoaYMFM8pti5");
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

		@Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);
			((MainActivity) activity).onSectionAttached(getArguments().getInt(
					ARG_SECTION_NUMBER));
		}
	}

	public static class SoilFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		private static final String ARG_SECTION_NUMBER = "section_number";

		/**
		 * Returns a new instance of this fragment for the given section number.
		 */

		private double latitude = 0.00;
		private double longitude = 0.00;
		private WebView webView;
		private View rootView;
		private Location location;
		
		public static SoilFragment newInstance(int sectionNumber) {
			SoilFragment fragment = new SoilFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}

		public SoilFragment() {
		}
		
		private Location obtainLocation() {
			getActivity();
			LocationManager locationManager = (LocationManager) getActivity()
					.getSystemService(Context.LOCATION_SERVICE);
			Location location = locationManager
					.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			return location;
		}

		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {

			rootView = inflater.inflate(R.layout.fragment_soil, container,
					false);
			
			ParseAnalytics.trackEventInBackground("Test Soil Opened");
			
			ParseUser currentUser = ParseUser.getCurrentUser();
			ParseObject soilObject = new ParseObject("Soil");
			if (currentUser != null){
				soilObject.put("User", currentUser.getUsername());		
			}else 
				soilObject.put("User", "Guest");
			location = obtainLocation();
			try {
				latitude = location.getLatitude();
				longitude = location.getLongitude();
			} catch (Exception e){
				latitude = 0.00;
				longitude = 0.00;
				e.printStackTrace();
			}
			soilObject.put("Location", latitude + ", " + longitude);
			soilObject.saveInBackground();

			return rootView;
			// new retrieve_soilTask().execute();
		}

		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);
			try {
				WebView webView = (WebView) (rootView
						.findViewById(R.id.wv_Soil));
				WebSettings webSettings = webView.getSettings();

				webSettings.setJavaScriptEnabled(true);

				webView.setWebChromeClient(new WebChromeClient() {
					public void onGeolocationPermissionsShowPrompt(
							String origin,
							GeolocationPermissions.Callback callback) {
						// callback.invoke(String origin, boolean allow, boolean
						// remember);
						callback.invoke(origin, true, false);
					}
				});

				webView.loadUrl("http://casoilresource.lawr.ucdavis.edu/gmap/");
			} catch (Exception e) {
				e.printStackTrace();

			}
		}

		/*
		 * protected class retrieve_soilTask extends AsyncTask<Void,String,
		 * ArrayList<String>>{
		 * 
		 * @Override public ArrayList<String> doInBackground(Void... arg0) {
		 * try{
		 * 
		 * } catch (Exception e) { e.printStackTrace(); } } protected void
		 * onPostExecute(ArrayList<String> data){ try{
		 * 
		 * }catch(Exception e){ e.printStackTrace(); } } }
		 */

		@Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);
			((MainActivity) activity).onSectionAttached(getArguments().getInt(
					ARG_SECTION_NUMBER));
		}
	}

	/*
	 * This is for the that is available in the side-menu, it can be used to
	 * calculate output based off of a few different things.
	 */
	public static class CalculateFragment extends Fragment implements
			Button.OnClickListener {
		private static final String ARG_SECTION_NUMBER = "section_number";
		Button calculateButton;
		Button resetButton;

		EditText plantsPerAcre;
		EditText tillerPerPlant;

		TextView yieldValue;

		RadioGroup avgSeedCount;

		/**
		 * Returns a new instance of this fragment for the given section number.
		 */
		public static CalculateFragment newInstance(int sectionNumber) {
			CalculateFragment fragment = new CalculateFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}

		public CalculateFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_calculate,
					container, false);

			yieldValue = (TextView) rootView.findViewById(R.id.textYieldVal);

			plantsPerAcre = (EditText) rootView.findViewById(R.id.editPPA);
			tillerPerPlant = (EditText) rootView.findViewById(R.id.editTillers);

			avgSeedCount = (RadioGroup) rootView
					.findViewById(R.id.radioSeedCount);

			calculateButton = (Button) rootView
					.findViewById(R.id.buttonCalcYield);
			calculateButton.setOnClickListener(this);

			resetButton = (Button) rootView.findViewById(R.id.buttonResetYield);

			resetButton.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					plantsPerAcre.setText("");
					tillerPerPlant.setText("");
					avgSeedCount.clearCheck();
					avgSeedCount.check(R.id.radio_avg);
					yieldValue.setText("");
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

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub

			boolean calculateValue = false;
			int countCrops = 0;
			double tillersAvg = 0;

			if (!plantsPerAcre.getText().toString().equals("")
					&& !tillerPerPlant.getText().toString().equals("")) {
				countCrops = Integer.parseInt(plantsPerAcre.getText()
						.toString());
				tillersAvg = Double.parseDouble(tillerPerPlant.getText()
						.toString());
				calculateValue = true;
			}

			int val = avgSeedCount.getCheckedRadioButtonId();
			int seedsPerHeadVal = 0;
			if (val == R.id.radio_low) {
				seedsPerHeadVal = 1000;
			} else if (val == R.id.radio_avg) {
				seedsPerHeadVal = 2000;
			} else {
				seedsPerHeadVal = 3000;
			}

			if (calculateValue) {
				double headCount = countCrops * tillersAvg;
				double seedsPerAcre = headCount * seedsPerHeadVal * 1000;
				double poundsPerAcre = seedsPerAcre / 17723.0;
				double yield = poundsPerAcre / 56.0;
				String finalYield = String.format("%.2f", yield) + " bu/acre";
				yieldValue.setText(finalYield);

			} else {
				Toast.makeText(getActivity(), "Please input all items.",
						Toast.LENGTH_LONG).show();
			}

		}

	}
}
