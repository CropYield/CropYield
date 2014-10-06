package com.example.edu.ksu.crop;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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
import android.widget.ImageView;
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
			return rootView;
		}

		@Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);
			((MainActivity) activity).onSectionAttached(getArguments().getInt(
					ARG_SECTION_NUMBER));
		}
	}
	
	
	public static class PictureFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		ImageView imageView;
		TextView latitude;
		TextView longitude;
		TextView focalLength;
		String currentPhotoPath[] = new String[5];
		static final int REQUEST_IMAGE_CAPTURE = 1;
		static final int REQUEST_IMAGE_SELECT = 2;
		Button button;
		int nextPhoto = 0;

		
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
			
			imageView   = (ImageView) rootView.findViewById(R.id.imageView1);
			button      = (Button)    rootView.findViewById(R.id.button_camera);
			longitude   = (TextView)  rootView.findViewById(R.id.exifLongitudeTextView);
			latitude    = (TextView)  rootView.findViewById(R.id.exifLatitudeTextView);
			focalLength = (TextView)  rootView.findViewById(R.id.exifFocalLengthTextView);
			
			
	         button.setOnClickListener(new View.OnClickListener() {
	             public void onClick(View v) {
	                 cameraOrGallery(v);
	             }
	         });
			
			return rootView;
		}
	
		private void cameraOrGallery(View v) {
			CharSequence options[] = new CharSequence[] { "Take a Photo", "Choose from Gallery"};
			
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
		private void SelectGalleryPhoto() {
			Intent pickPhoto = new Intent(Intent.ACTION_PICK,
			           android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			startActivityForResult(pickPhoto , REQUEST_IMAGE_SELECT);
		}
		
		private void TakeNewPhoto() {			
			Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		    if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
		    	File photoFile = null;
		    	try {
		    		photoFile = createImageFile();
				    currentPhotoPath[nextPhoto] = photoFile.getAbsolutePath();

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
	    		galleryAddPic();
	    		addPreviewPic();
	    		retrieveExifData();
	    		nextPhoto++; 
			}
			else if( requestCode == REQUEST_IMAGE_SELECT && resultCode == RESULT_OK ) {
				Uri selectedImage = intent.getData();
	            String[] filePathColumn = {MediaStore.Images.Media.DATA};

	            Cursor cursor = getActivity().getContentResolver().query(
	                               selectedImage, filePathColumn, null, null, null);
	            cursor.moveToFirst();

	            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
	            currentPhotoPath[nextPhoto] = cursor.getString(columnIndex);
	            cursor.close();				
	            addPreviewPic();
				retrieveExifData();
				nextPhoto++;
			}
		}
		
		private void retrieveExifData() {
			try {
				ExifInterface exif = new ExifInterface(currentPhotoPath[nextPhoto]);
				String tempString = exif.getAttribute(ExifInterface.TAG_FOCAL_LENGTH);
				if( exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE) == null || exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE) == null ) {
					sendToast("Cannot access latitude and longitude data, please input location on Weather screen.", Toast.LENGTH_LONG);
				}
				focalLength.setText((CharSequence) tempString);
			} catch(Exception EX) {
	    		// Error
				sendToast("Error Retrieving Data From Picture", Toast.LENGTH_SHORT);
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
			File f = new File(currentPhotoPath[nextPhoto]);
			Uri contentUri = Uri.fromFile(f);
			mediaScanIntent.setData(contentUri);
			getActivity().sendBroadcast(mediaScanIntent);
		}
		
		private void addPreviewPic() {			
		    // Get the dimensions of the View
		    int targetW = imageView.getWidth();
		    int targetH = imageView.getHeight();

		    // Get the dimensions of the bitmap
		    BitmapFactory.Options bmOptions = new BitmapFactory.Options();
		    bmOptions.inJustDecodeBounds = true;
		    BitmapFactory.decodeFile(currentPhotoPath[nextPhoto], bmOptions);
		    int photoW = bmOptions.outWidth;
		    int photoH = bmOptions.outHeight;

		    // Determine how much to scale down the image
		    int scaleFactor = Math.min(photoW/targetH, photoH/targetW);

		    // Decode the image file into a Bitmap sized to fill the View
		    bmOptions.inJustDecodeBounds = false;
		    bmOptions.inSampleSize = scaleFactor;
		    bmOptions.inPurgeable = true;

		    Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath[nextPhoto], bmOptions);
		    Matrix matrix = new Matrix();
		    matrix.postRotate(90);
		    Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bmOptions.outWidth, bmOptions.outHeight, matrix, true);
		    imageView.setImageBitmap(rotatedBitmap);
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

	
	
}
