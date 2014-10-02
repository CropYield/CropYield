package com.example.edu.ksu.crop;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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
		if (position == 0) {
			fragmentManager
					.beginTransaction()
					.replace(R.id.container,
							PlaceholderFragment.newInstance(position + 1))
					.commit();
		}
		else if(position == 1){
			fragmentManager
			.beginTransaction()
			.replace(R.id.container,
					WeatherFragment.newInstance(position + 1))
			.commit();
		}
		else if(position == 3){
			fragmentManager
			.beginTransaction()
			.replace(R.id.container,
					PictureFragment.newInstance(position + 1))
			.commit();
		}
		else {
			fragmentManager
			.beginTransaction()
			.replace(R.id.container,
					PlaceholderFragment.newInstance(position + 1))
			.commit();
		}
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

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		private static final String ARG_SECTION_NUMBER = "section_number";

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

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
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
		
		private static final String ZIPCODE = "66502";
		private String location, date, temperature = "";
		private List<String> listOfInputData = new ArrayList<String>();
		private TextView tv_WeatherText, tv_DateText, tv_Temperature;
		
		public static WeatherFragment newInstance(int sectionNumber) {
			WeatherFragment fragment = new WeatherFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			
			return fragment;
		}
		
		protected class retrieve_weatherTask extends AsyncTask<Void, String, List<String>>{
			@Override
			public List<String> doInBackground(Void... arg0) {
				
				String qResult = "";
				HttpClient httpClient = new DefaultHttpClient();
				HttpContext localContext = new BasicHttpContext();
				HttpGet httpGet = new HttpGet("http://weather.yahooapis.com/forecastrss?p="+ZIPCODE+"&u=f");
				try {
					HttpResponse response = httpClient.execute(httpGet,localContext);
					HttpEntity entity = response.getEntity();
	   
					DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
	   
					if (localContext != null) {
						InputStream inputStream = entity.getContent();
						Reader in = new InputStreamReader(inputStream);
						BufferedReader bufferedreader = new BufferedReader(in);
						StringBuilder stringBuilder = new StringBuilder();
						String stringReadLine = null;
						while ((stringReadLine = bufferedreader.readLine()) != null) {
							stringBuilder.append(stringReadLine + "\n");
						}
						qResult = stringBuilder.toString();
						Pattern pat1 = Pattern.compile("<title>(.*?)</title>");
						Matcher mat1 = pat1.matcher(qResult);
						Pattern pat2 = Pattern.compile("<title>(.*?)</title>");
						Matcher mat2 = pat1.matcher(qResult);
						Pattern pat3 = Pattern.compile("temp=\"(.*?)\"");
						Matcher mat3 = pat3.matcher(qResult);
						if(mat1.find()){
							location = mat1.group(1);
							listOfInputData.add(location);
						}
						if(mat2.find()){
							mat2.find();
							mat2.find();
							date = mat2.group(1);
							listOfInputData.add(date);
						}
						if(mat3.find()){
							temperature = mat3.group();
							temperature = temperature.replaceAll("\\D+","");
							listOfInputData.add(temperature);
						}
					}
	   
	   
	    /*Document dest = null;
	    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder parser;
	    try{
	    parser = dbFactory.newDocumentBuilder();
	    dest = parser.parse(new ByteArrayInputStream(qResult.getBytes("UTF8")));*/
	    /*DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder db = dbFactory.newDocumentBuilder();
	    org.w3c.dom.Document doc = db.parse(new InputSource(getUrl.openStream()));
	    doc.getDocumentElement().normalize();
	   
	    NodeList nodeList = doc.getElementsByTagName("rss");
	*/
					return listOfInputData;
				} catch (Exception e) {
					Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
					e.printStackTrace();
					return listOfInputData; 
				}
			}
			protected void onPostExecute(List<String> listUpdateTextBox){
				try{
					tv_WeatherText.setText(listUpdateTextBox.get(0));
					tv_DateText.setText(listUpdateTextBox.get(1));
					tv_Temperature.setText(listUpdateTextBox.get(2));
					
				}catch (Exception e){
					Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
					e.printStackTrace();
				}
			}
		}

		public WeatherFragment() {
			
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_weather,
					container, false);
			
			tv_WeatherText = (TextView)rootView.findViewById(R.id.weather_title);
			tv_DateText = (TextView)rootView.findViewById(R.id.dateText);
			tv_Temperature = (TextView)rootView.findViewById(R.id.temperature);
			new retrieve_weatherTask().execute();
			return rootView;
		}

		@Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);
			((MainActivity) activity).onSectionAttached(getArguments().getInt(
					ARG_SECTION_NUMBER));
		}
	}
	
	
	public static class PictureFragment extends Fragment implements Button.OnClickListener {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		Button button;
		
		
		private static final String ARG_SECTION_NUMBER = "section_number";

		/**
		 * Returns a new instance of this fragment for the given section number.
		 */
		public static PictureFragment newInstance(int sectionNumber) {
			PictureFragment fragment = new PictureFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}

		public PictureFragment() {

		}
		static final int REQUEST_IMAGE_CAPTURE = 1;


		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_picture,
					container, false);
			
			button = (Button) rootView.findViewById(R.id.button_camera);
			
			button.setOnClickListener(this);
					
			return rootView;
		}
		
		public void onClick(View v) {
			Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		    if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
		        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
		    }				
		}

		@Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);
			((MainActivity) activity).onSectionAttached(getArguments().getInt(
					ARG_SECTION_NUMBER));
		}
	}
	
	
	

	
	
}
