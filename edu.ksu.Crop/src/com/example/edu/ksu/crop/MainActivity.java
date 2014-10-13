package com.example.edu.ksu.crop;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.location.Location;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.json.JSONObject;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

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
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

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
		else if(position == 4){
			fragmentManager
			.beginTransaction()
			.replace(R.id.container,
					SoilFragment.newInstance(position + 1))
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
		case 5:
			mTitle = getString(R.string.title_section5);
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
	public static class PlaceholderFragment extends Fragment implements
	Button.OnClickListener {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
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
			
			yieldValue = (TextView) rootView.findViewById(R.id.textYieldVal);
			
			plantsPerAcre = (EditText) rootView.findViewById(R.id.editPPA);
			tillerPerPlant = (EditText) rootView.findViewById(R.id.editTillers);
			
			avgSeedCount = (RadioGroup) rootView.findViewById(R.id.radioSeedCount);
			
			calculateButton = (Button) rootView.findViewById(R.id.buttonCalcYield);
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
			
			if(!plantsPerAcre.getText().toString().equals("") && !tillerPerPlant.getText().toString().equals("")){
				countCrops = Integer.parseInt(plantsPerAcre.getText().toString());
				tillersAvg = Double.parseDouble(tillerPerPlant.getText().toString());
				calculateValue = true;
			}
			
			
			int val = avgSeedCount.getCheckedRadioButtonId();
			int seedsPerHeadVal = 0;
			if(val == R.id.radio_low){
				seedsPerHeadVal = 1000;
			} else if(val == R.id.radio_avg){
				seedsPerHeadVal = 2000;
			} else {
				seedsPerHeadVal = 3000;
			}
			
			if(calculateValue){
				double headCount = countCrops * tillersAvg; 
				double seedsPerAcre = headCount * seedsPerHeadVal * 1000;
				double poundsPerAcre = seedsPerAcre / 17723.0;
				double yield = poundsPerAcre / 56.0;
				String finalYield = String.format("%.2f", yield) + " bu/acre";
				yieldValue.setText(finalYield);
				
			}
			else {
				Toast.makeText(getActivity(), "Please input all items.", 3000).show();
			}
			
			
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
		private TextView m_Day1, m_Date1, m_Low1, m_High1, m_Text1;
		private TextView m_Day2, m_Date2, m_Low2, m_High2, m_Text2;
		private TextView m_Day3, m_Date3, m_Low3, m_High3, m_Text3;
		private TextView m_Day4, m_Date4, m_Low4, m_High4, m_Text4;
		private TextView m_Day5, m_Date5, m_Low5, m_High5, m_Text5;
	    private final String ZIPCODE = "66502";
	    private ArrayList<String> listOfInputData = new ArrayList<String>();
	    private HashMap<Integer,ArrayList<String>> fiveDayForecast = new HashMap<Integer,ArrayList<String>>();
		
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
	
			m_Day1 = (TextView)rootView.findViewById(R.id.day1);
	        m_Date1 = (TextView)rootView.findViewById(R.id.date1);
	        m_Low1 = (TextView)rootView.findViewById(R.id.low1);
	        m_High1 = (TextView)rootView.findViewById(R.id.high1);
	        m_Text1 = (TextView)rootView.findViewById(R.id.text1);
	        m_Day2 = (TextView)rootView.findViewById(R.id.day2);
	        m_Date2 = (TextView)rootView.findViewById(R.id.date2);
	        m_Low2 = (TextView)rootView.findViewById(R.id.low2);
	        m_High2 = (TextView)rootView.findViewById(R.id.high2);
	        m_Text2 = (TextView)rootView.findViewById(R.id.text2);
	        m_Day3 = (TextView)rootView.findViewById(R.id.day3);
	        m_Date3 = (TextView)rootView.findViewById(R.id.date3);
	        m_Low3 = (TextView)rootView.findViewById(R.id.low3);
	        m_High3 = (TextView)rootView.findViewById(R.id.high3);
	        m_Text3 = (TextView)rootView.findViewById(R.id.text3);
	        m_Day4 = (TextView)rootView.findViewById(R.id.day4);
	        m_Date4 = (TextView)rootView.findViewById(R.id.date4);
	        m_Low4 = (TextView)rootView.findViewById(R.id.low4);
	        m_High4 = (TextView)rootView.findViewById(R.id.high4);
	        m_Text4 = (TextView)rootView.findViewById(R.id.text4);
	        m_Day5 = (TextView)rootView.findViewById(R.id.day5);
	        m_Date5 = (TextView)rootView.findViewById(R.id.date5);
	        m_Low5 = (TextView)rootView.findViewById(R.id.low5);
	        m_High5 = (TextView)rootView.findViewById(R.id.high5);
	        m_Text5 = (TextView)rootView.findViewById(R.id.text5);
	        new retrieve_weatherTask().execute();
			return rootView;
		}

		protected class retrieve_weatherTask extends AsyncTask<Void, String, HashMap<Integer,ArrayList<String>>>{
		    @Override
		    public HashMap<Integer,ArrayList<String>> doInBackground(Void... arg0) {
		    	try{
		    		InputStream inputXml;
		    		inputXml  = new URL("http://weather.yahooapis.com/forecastrss?p="+ZIPCODE+"&u=f").openConnection().getInputStream();
		    		DocumentBuilderFactory factory = DocumentBuilderFactory.
		    	                                        newInstance();
		    	    DocumentBuilder builder = factory.newDocumentBuilder();
		    	    org.w3c.dom.Document doc = builder.parse(inputXml);
		            NodeList nodi = doc.getElementsByTagName("yweather:forecast");

		    	    if (nodi.getLength() > 0)
		    	    {
		    	    	for (int i = 0; i < 5; i++){
		    	        	nodi = doc.getElementsByTagName("yweather:forecast");
		    	        	Element nodo = (Element)nodi.item(i);
		             		listOfInputData.add(nodo.getAttribute("day"));
		                 	listOfInputData.add(nodo.getAttribute("date"));
		                 	listOfInputData.add(nodo.getAttribute("low"));
		                 	listOfInputData.add(nodo.getAttribute("high"));
		                 	listOfInputData.add(nodo.getAttribute("text"));
		                 	fiveDayForecast.put(i,listOfInputData);
		                 	listOfInputData = new ArrayList<String>();
		            }	
		    	        }
		    	       if (inputXml != null)
		    	           inputXml.close();
		    		
		    	       return fiveDayForecast;
		    	} catch (Exception e) {
		    		e.printStackTrace();
		    		return fiveDayForecast; 
		    		}
		    }
		    protected void onPostExecute(HashMap<Integer,ArrayList<String>> forecast){
		    	try{
		    		ArrayList<String> dayForecast = new ArrayList<String>();
		    		dayForecast = forecast.get(0);
		    		m_Day1.setText(dayForecast.get(0));
		    		m_Date1.setText(dayForecast.get(1));
		    		m_Low1.setText(dayForecast.get(2));
		    		m_High1.setText(dayForecast.get(3));
		    		m_Text1.setText(dayForecast.get(4));
		    		dayForecast = forecast.get(1);
		    		m_Day2.setText(dayForecast.get(0));
		    		m_Date2.setText(dayForecast.get(1));
		    		m_Low2.setText(dayForecast.get(2));
		    		m_High2.setText(dayForecast.get(3));
		    		m_Text2.setText(dayForecast.get(4));
		    		dayForecast = forecast.get(2);
		    		m_Day3.setText(dayForecast.get(0));
		    		m_Date3.setText(dayForecast.get(1));
		    		m_Low3.setText(dayForecast.get(2));
		    		m_High3.setText(dayForecast.get(3));
		    		m_Text3.setText(dayForecast.get(4));
		    		dayForecast = forecast.get(3);
		    		m_Day4.setText(dayForecast.get(0));
		    		m_Date4.setText(dayForecast.get(1));
		    		m_Low4.setText(dayForecast.get(2));
		    		m_High4.setText(dayForecast.get(3));
		    		m_Text4.setText(dayForecast.get(4));
		    		dayForecast = forecast.get(4);
		    		m_Day5.setText(dayForecast.get(0));
		    		m_Date5.setText(dayForecast.get(1));
		    		m_Low5.setText(dayForecast.get(2));
		    		m_High5.setText(dayForecast.get(3));
		    		m_Text5.setText(dayForecast.get(4));
		    	}catch(Exception e){
		    		e.printStackTrace();
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
	
	
	public static class PictureFragment extends Fragment {

		ImageView imageView;
		LinkedList<String> currentPhotoPath = new LinkedList<String>();
		static final int REQUEST_IMAGE_CAPTURE = 1;
		static final int REQUEST_IMAGE_SELECT = 2;
		Button takePicture;
		ImageButton nextPicture;
		ImageButton previousPicture;
		ImageButton deletePicture;
		ImageButton gpsLocation;
		int currentPhotoDisplayed = 0;
		int photoCount = 0;
		LinkedList<Bitmap> currentPictures = new LinkedList<Bitmap>();
		
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

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_picture,
					container, false);
			
			imageView        = (ImageView)  rootView.findViewById(R.id.imageView1);
			takePicture      = (Button)     rootView.findViewById(R.id.button_camera);			
			nextPicture      = (ImageButton)rootView.findViewById(R.id.nextImageButton);
			previousPicture  = (ImageButton)rootView.findViewById(R.id.previousImageButton);
			deletePicture    = (ImageButton)rootView.findViewById(R.id.deleteImageButton);
			gpsLocation      = (ImageButton)rootView.findViewById(R.id.gpsImageButton);
			
			setPreviousNextButtonEnabledStatus();
			
			takePicture.setOnClickListener(new View.OnClickListener() {
	             public void onClick(View v) {
	                 cameraOrGallery(v);
	             }
	         });
			
			nextPicture.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					nextPicture(v);
				}
			});
			
			previousPicture.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					previousPicture(v);
				}
			});	
			
			deletePicture.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					deletePictureSelected(v);
				}
			});
			
			gpsLocation.setOnClickListener(new View.OnClickListener() {
	             public void onClick(View v) {
	                 selectLocationOrRetrieveCurrent(v);
	             }
	         });
			
			imageView.setImageDrawable(null);

			return rootView;
		}
	
		private void cameraOrGallery(View v) {
			CharSequence options[] = new CharSequence[] { "Take A Photo", "Choose From Gallery"};
			
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setTitle("Choose Photo Selection Method");
			builder.setItems(options,  new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int picked) {
					switch (picked) {
					case 0:
						TakeNewPhoto();
						break;
					case 1:
						SelectGalleryPhoto();
						break;
					}
				}
			});
			builder.show();
		}
		
		private void selectLocationOrRetrieveCurrent(View v) {
			CharSequence options[] = new CharSequence[] { "Retrieve Current Location", "Select A Location", "Use GPS Location Off Current Photo"};
			
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setTitle("Choose GPS Location Method");
			builder.setItems(options,  new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int picked) {
					switch (picked) {
					case 0:
						obtainLocation(true);
						break;
					case 1:
						selectLocation();
						break;
					case 2: retrieveExifData();
					break;
					
					}
				}
			});
			builder.show();		
		}
		
		private void deletePictureSelected(View v) {
			CharSequence options[] = new CharSequence[] { "Yes", "No"};
			
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setTitle("Remove Current Picture?");
			builder.setItems(options,  new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int picked) {
					switch (picked) {
					case 0:
						removePicFromLinkedList();
						sendToast("Picture Removed", Toast.LENGTH_SHORT);
						break;
					case 1:
						sendToast("Picture Not Removed", Toast.LENGTH_SHORT);
						break;
					
					}
				}
			});
			builder.show();
		}
		
		
		private Location obtainLocation(Boolean showToast) {
	        getActivity();
			LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
	        Location location= locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
	        if(showToast) sendToast( "Location is " + location.getLatitude() + ", " + location.getLongitude() + ".", Toast.LENGTH_LONG);
	        return location;
		}
		
		private void selectLocation() {
			Location location = obtainLocation(false);
			String uri = String.format(Locale.ENGLISH, "geo:%f,%f", location.getLatitude(), location.getLongitude());
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
			getActivity().startActivity(intent);
		}
		
		private void previousPicture(View v) {
			currentPhotoDisplayed++;
			imageView.setImageBitmap(currentPictures.get(currentPhotoDisplayed));
			setPreviousNextButtonEnabledStatus();
		}

		private void nextPicture(View v) {
			currentPhotoDisplayed--;
			imageView.setImageBitmap(currentPictures.get(currentPhotoDisplayed));
			setPreviousNextButtonEnabledStatus();
		}
		
		private void SelectGalleryPhoto() {
			Intent pickPhoto = new Intent(Intent.ACTION_PICK,
			           android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			startActivityForResult(pickPhoto , REQUEST_IMAGE_SELECT);
		}
		
		private void setPreviousNextButtonEnabledStatus() {
			if( currentPictures.size() > 1 && ( currentPhotoDisplayed < ( currentPictures.size() - 1 ) ) ) {
				previousPicture.setEnabled(true);
			} else {
				previousPicture.setEnabled(false);
			}
			if(currentPhotoDisplayed > 0) {
				nextPicture.setEnabled(true);
			} else {
				nextPicture.setEnabled(false);
			}
			if( currentPictures.size() > 4 ) {
				takePicture.setEnabled(false);
			} else {
				takePicture.setEnabled(true);
			}
			if( currentPictures.size() > 0 ) {
				deletePicture.setEnabled(true);
			} else {
				deletePicture.setEnabled(false);
			}
		}
		
		private void TakeNewPhoto() {			
			Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		    if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
		    	File photoFile = null;
		    	try {
		    		photoFile = createImageFile();
				    currentPhotoPath.push(photoFile.getAbsolutePath());

		    	} catch (IOException EX) {
		    		// Error
					sendToast("Error Creating Image File", Toast.LENGTH_SHORT);
		    		// Anything better to do with this error?
		    	}
		    	if(photoFile != null) {
		    		takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
		    				Uri.fromFile(photoFile));
		    		startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);

		    	}
		    }	
		}
		// Currently working on the below code to ensure ease for scaling
		
		@Override
		public void onActivityResult(int requestCode, int resultCode, Intent intent) {
			if( requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK ) {
	    		addPictureToLinkedList();
				currentPhotoDisplayed = 0;
				imageView.setImageBitmap(currentPictures.get(0));
	    		galleryAddPic();
	    		setPreviousNextButtonEnabledStatus();
			}
			else if( requestCode == REQUEST_IMAGE_SELECT && resultCode == RESULT_OK ) {
				Uri selectedImage = intent.getData();
	            String[] filePathColumn = {MediaStore.Images.Media.DATA};

	            Cursor cursor = getActivity().getContentResolver().query(
	                               selectedImage, filePathColumn, null, null, null);
	            cursor.moveToFirst();

	            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
	            currentPhotoPath.push(cursor.getString(columnIndex));

	            cursor.close();				
	            addPictureToLinkedList();
				currentPhotoDisplayed = 0;
				imageView.setImageBitmap(currentPictures.get(0));


				setPreviousNextButtonEnabledStatus();
			}
		}
	
		
		private void retrieveExifData() {
			try {
				ExifInterface exif = new ExifInterface(currentPhotoPath.peek());
				if( exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE) == null || exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE) == null ) {
					sendToast("Unable To Retrieve GPS Data, Please Try Other Option", Toast.LENGTH_LONG);
				}
//				focalLength.setText((CharSequence) tempString);
			} catch(Exception EX) {
	    		// Error
				sendToast("No Picture Available", Toast.LENGTH_SHORT);
	    		// Anything better to do with this error?
	    	}
		}
		private void sendToast(String message, int length) {
    		
    		Activity context = getActivity();
    		CharSequence text = (CharSequence) message;
    		int duration = length;
    		
    		Toast toast = Toast.makeText(context, text, duration);
    		toast.show();
		}
		
		private void galleryAddPic() {
			Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
			File f = new File(currentPhotoPath.peek());
			Uri contentUri = Uri.fromFile(f);
			mediaScanIntent.setData(contentUri);
			getActivity().sendBroadcast(mediaScanIntent);
		}
		
		private void addPictureToLinkedList() {			
		    // Get the dimensions of the View
		    int targetW = imageView.getWidth();
		    int targetH = imageView.getHeight();

		    // Get the dimensions of the bitmap
		    BitmapFactory.Options bmOptions = new BitmapFactory.Options();
		    bmOptions.inJustDecodeBounds = true;
		    BitmapFactory.decodeFile(currentPhotoPath.peek(), bmOptions);
		    int photoW = bmOptions.outWidth;
		    int photoH = bmOptions.outHeight;

		    // Determine how much to scale down the image
		    int scaleFactor = Math.min(photoW/targetH, photoH/targetW);

		    // Decode the image file into a Bitmap sized to fill the View
		    bmOptions.inJustDecodeBounds = false;
		    bmOptions.inSampleSize = scaleFactor;
		    bmOptions.inPurgeable = true;
		    Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath.peek(), bmOptions);
		    Matrix matrix = new Matrix();
		    matrix.postRotate(90);
		    Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bmOptions.outWidth, bmOptions.outHeight, matrix, true);
		    currentPictures.push(rotatedBitmap);
		    photoCount++;
		    
//		    imageView.setImageBitmap(rotatedBitmap);
		}
		
		private void removePicFromLinkedList() {
			
			try{
				imageView.setImageDrawable(null);
				currentPictures.remove(currentPhotoDisplayed);
				currentPhotoPath.remove(currentPhotoDisplayed);
				if(currentPictures.size() == 0 ) {
					imageView.setImageDrawable(null);
				}
				else if(currentPhotoDisplayed != 0) {
					imageView.setImageBitmap(currentPictures.get(--currentPhotoDisplayed));
				}
				else {
					imageView.setImageBitmap(currentPictures.get(0));
				}
				photoCount--;
				setPreviousNextButtonEnabledStatus();
			} catch(Exception EX) {
				sendToast("Error removing picture", Toast.LENGTH_LONG);
			}
		}
		
		
		@SuppressLint("SimpleDateFormat") // This removes the warning thrown about creating the date format.
		                                  // Normally, you should use a different call but it provides a date
									      // in a format not suitable for saving as a file name, so I used this.
		private File createImageFile() throws IOException {
		    // Create an image file name
		    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		    String imageFileName = "JPEG_" + timeStamp + "_";
		    File storageDir = Environment.getExternalStoragePublicDirectory(
		            Environment.DIRECTORY_DCIM + "/Camera"); //Add the pictures to the default DCIM/Camera folder. 
		    File image = File.createTempFile(
		        imageFileName,  /* prefix */
		        ".jpg",         /* suffix */
		        storageDir      /* directory */
		    );

		    // Save a file: path for use with ACTION_VIEW intents
//		    currentPhotoPath[nextPhoto] = "file:" + image.getAbsolutePath();
		    return image;
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
		private TextView m_Soil1, m_Soil2, m_Soil3, m_Soil4, m_Soil5;
		private ArrayList<String> listOfData = new ArrayList<String>();
	    private final String LATITUDE = "44.50";//some random spot in spain
	    private final String LONGITUDE = "5.29"; 
		
		public static SoilFragment newInstance(int sectionNumber) {
			SoilFragment fragment = new SoilFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}

		public SoilFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_soil,
					container, false);
			m_Soil1 = (TextView)rootView.findViewById(R.id.textView1);
			m_Soil2 = (TextView)rootView.findViewById(R.id.textView2);
			m_Soil3 = (TextView)rootView.findViewById(R.id.textView3);
			m_Soil4 = (TextView)rootView.findViewById(R.id.textView4);
			m_Soil5 = (TextView)rootView.findViewById(R.id.textView5);
	        new retrieve_soilTask().execute();
			return rootView;
		}

		protected class retrieve_soilTask extends AsyncTask<Void,String, ArrayList<String>>{
		    @Override
		    public ArrayList<String> doInBackground(Void... arg0) {
		    	try{
		    			
		    			String strUrl = "http://rest.soilgrids.org/query?lon="+ LATITUDE + "&lat=" + LONGITUDE;
		    			URL url = new URL(strUrl);
		    			Scanner scan = new Scanner(url.openStream());
		    		    String str = new String();
		    		    while (scan.hasNext())
		    		        str += scan.nextLine();
		    		    scan.close();
		    		    
		    		    JSONObject obj = new JSONObject(str);
		    		    
		    		    //listOfData.add(obj.getJSONObject("crs").getString("type"));
		    		    listOfData.add(obj.getJSONObject("properties").getString("publication_date"));
		    		    listOfData.add(obj.getJSONObject("geometry").getString("type"));
		    		    listOfData.add(obj.getJSONObject("properties").getString("soilmask"));
		    		    listOfData.add(obj.getJSONObject("properties").getJSONObject("BLD").getJSONObject("U").getString("sd2"));
		    		    listOfData.add(obj.getJSONObject("properties").getJSONObject("TAXOUSDA").getString("Aquolls"));
		    			return listOfData;
		    			
		    	} catch (Exception e) {
		    		e.printStackTrace();
		    		return listOfData; 
		    		}
		    }
		    protected void onPostExecute(ArrayList<String> data){
		    	try{
		    		m_Soil1.setText(data.get(0));
		    		m_Soil2.setText(data.get(1));
		    		m_Soil3.setText(data.get(2));
		    		m_Soil4.setText(data.get(3));
		    		m_Soil5.setText(data.get(4));
		    	}catch(Exception e){
		    		e.printStackTrace();
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
}
