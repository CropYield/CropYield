package com.example.edu.ksu.crop;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Locale;


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
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class PictureFragment extends Fragment {

	/*
	 * Declare constant values
	 */
	static final int REQUEST_IMAGE_CAPTURE = 1;
	static final int REQUEST_IMAGE_SELECT = 2;
	private static final String ARG_SECTION_NUMBER = "4";
	
	/*
	 * Declare user interface elements
	 */
	ImageView imageView;
	Button takePicture;
	Button choosePicture;
	Button finish;
	Button deletePicture;
	ImageButton nextPicture;
	ImageButton previousPicture;
	TextView imageCounter;
	
	/*
	 * Declare different data values used throughout the Picture/Camera logic.
	 */
	LinkedList<String> currentPhotoPath = new LinkedList<String>();
	LinkedList<Bitmap> currentPictures = new LinkedList<Bitmap>();
	int currentPhotoDisplayed = 0;
	int photoCount = 0;
	ColorDetector cd;
	double areaCalc = 0.0;
	static DataSet data = new DataSet();

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

	/**
	 * The overloaded constructor of PictureFragment offers
	 * a DataSet, in addition to the default sectionNumber.
	 */
	public static PictureFragment newInstance(int sectionNumber, DataSet d) {
		PictureFragment fragment = new PictureFragment();
		data = d;
		Bundle args = new Bundle();
		args.putInt(ARG_SECTION_NUMBER, sectionNumber);
		fragment.setArguments(args);
		return fragment;
	}

	public PictureFragment() {

	}


	/**
	 * Sets up all of the UI elements, closes the keyboard,
	 * and creates onClickListeners to watch for button presses.
	 */
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_picture, container,
				false);
		imageView =       (ImageView) rootView.findViewById(R.id.imageView1);
		takePicture =     (Button) rootView.findViewById(R.id.button_camera);
		choosePicture =   (Button) rootView.findViewById(R.id.button_select);
		finish =          (Button) rootView.findViewById(R.id.finishButton);
		nextPicture =     (ImageButton) rootView.findViewById(R.id.nextImageButton);
		previousPicture = (ImageButton) rootView.findViewById(R.id.previousImageButton);
		deletePicture =   (Button) rootView.findViewById(R.id.deleteImageButton);
		imageCounter =    (TextView) rootView.findViewById(R.id.imageCounterText);
		cd = new ColorDetector();
		
		setPreviousNextButtonEnabledStatus();

		takePicture.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				TakeNewPhoto();
			}
		});

		choosePicture.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				SelectGalleryPhoto();
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

		finish.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				finishPictureFragment();
			}
		});

		imageView.setImageDrawable(null);
		hideKeyboard();

		
		return rootView;
	}

	/**
	 * Hides the keyboard. Helpful for instances where the previous
	 * fragment has the keyboard open and utilizes the 'Done' key.
	 */
	private void hideKeyboard() {    
	    // Check if no view has focus: 
	    View view = getActivity().getCurrentFocus();
	    if (view != null) {
	        InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
	        inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	    } 
	} 
	
	
	/**
	 * Simply confirms the user's intent in deleting a picture,
	 * and will let the user know the success of the operation.
	 */
	private void deletePictureSelected(View v) {
		CharSequence options[] = new CharSequence[] { "Yes", "No" };

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("Remove Current Picture?");
		builder.setItems(options, new DialogInterface.OnClickListener() {
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

	/**
	 * The user is done with the picture/camera fragment. This
	 * transitions to the next fragment, and calculates the area
	 * in all of the pictures.
	 */
	private void finishPictureFragment() {
		String photoPath;
		for(int i = 0; i < currentPhotoPath.size(); i++ ) {
			photoPath = currentPhotoPath.pop();
			data.AddAreas(cd.AreaDetection(photoPath));
		}
		Fragment newFragment = FinalFragment.newInstance(10, data);//Instance has something to do with title, will work on this later during clearn up
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		transaction.replace(R.id.container, newFragment);
		transaction.addToBackStack(null);
		transaction.commit();//Navigates to the final fragment
	}
	
	
	/**
	 * This function sets the image preview to the analyzed
	 * and edited image. Not intending on using this code,
	 * but it's good for debugging.
	 */
//	private void setImageViewTest2(Mat mat, String toastNum) {
//		try {
//			if (mat.rows() > 1200) {
//				Mat smallerMat = new Mat();
//				Imgproc.resize(mat, smallerMat, new Size(800.0, 1200.0), 0, 0,
//						Imgproc.INTER_NEAREST);
//				Bitmap bmp = Bitmap.createBitmap(smallerMat.cols(),
//						smallerMat.rows(), Bitmap.Config.ARGB_8888);
//				Mat tmp = new Mat(smallerMat.rows(), smallerMat.cols(),
//						CvType.CV_8U, new Scalar(4));
//				Imgproc.cvtColor(smallerMat, tmp, Imgproc.COLOR_GRAY2BGR, 4);
//				Utils.matToBitmap(tmp, bmp);
//				if (bmp.getWidth() > bmp.getHeight()) {
//					Matrix matrix = new Matrix();
//					matrix.postRotate(90);
//					Bitmap rotatedBitmap = Bitmap.createBitmap(bmp, 0, 0, 800,
//							1200, matrix, true);
//					imageView.setImageBitmap(rotatedBitmap);
//				} else {
//					imageView.setImageBitmap(bmp);
//				}
//			} else {
//				Bitmap bmp = Bitmap.createBitmap(mat.cols(), mat.rows(),
//						Bitmap.Config.ARGB_8888);
//				Mat tmp = new Mat(mat.rows(), mat.cols(), CvType.CV_8U,
//						new Scalar(4));
//				Imgproc.cvtColor(mat, tmp, Imgproc.COLOR_GRAY2BGR, 4);
//				Utils.matToBitmap(tmp, bmp);
//				if (bmp.getWidth() > bmp.getHeight()) {
//					Matrix matrix = new Matrix();
//					matrix.postRotate(90);
//					Bitmap rotatedBitmap = Bitmap.createBitmap(bmp, 0, 0,
//							bmp.getWidth(), bmp.getHeight(), matrix, true);
//					imageView.setImageBitmap(rotatedBitmap);
//				} else {
//					imageView.setImageBitmap(bmp);
//				}
//			}
//		} catch (Exception EX) {
//			sendToast("Bitmap " + toastNum + " is null", Toast.LENGTH_SHORT);
//		}
//	}

	
	/**
	 * Currently not using this code here. Might be useful elsewhere,
	 * so not deleting yet. May be removed at release time if not needed.
	 */
	private Location obtainLocation(Boolean showToast) {
		getActivity();
		LocationManager locationManager = (LocationManager) getActivity()
				.getSystemService(Context.LOCATION_SERVICE);
		Location location = locationManager
				.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		if (showToast)
			sendToast(
					"Location is " + location.getLatitude() + ", "
							+ location.getLongitude() + ".", Toast.LENGTH_LONG);
		return location;
	}

	/**
	 * Currently not using this code here. Might be useful elsewhere,
	 * so not deleting yet. May be removed at release time if not needed.
	 */
	@SuppressWarnings("unused")
	private void selectLocation() {
		Location location = obtainLocation(false);
		String uri = String.format(Locale.ENGLISH, "geo:%f,%f",
				location.getLatitude(), location.getLongitude());
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
		getActivity().startActivity(intent);
	}

	
	/**
	 * Increase the current photo displayed.
	 */
	private void previousPicture(View v) {
		currentPhotoDisplayed++;
		imageView.setImageBitmap(currentPictures.get(currentPhotoDisplayed));
		setPreviousNextButtonEnabledStatus();
	}

	/**
	 * Decrease the current photo displayed.
	 */
	private void nextPicture(View v) {
		currentPhotoDisplayed--;
		imageView.setImageBitmap(currentPictures.get(currentPhotoDisplayed));
		setPreviousNextButtonEnabledStatus();
	}

	/**
	 * Choose a photo from the gallery. Uses
	 * Android's built in functionality for this.
	 */
	private void SelectGalleryPhoto() {
		Intent pickPhoto = new Intent(Intent.ACTION_PICK,
				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(pickPhoto, REQUEST_IMAGE_SELECT);
	}

	
	/**
	 * The logic behind whether or not buttons should be enabled.
	 */
	private void setPreviousNextButtonEnabledStatus() {
		if (currentPictures.size() > 1
				&& (currentPhotoDisplayed < (currentPictures.size() - 1))) {
			previousPicture.setEnabled(true);
		} else {
			previousPicture.setEnabled(false);
		}
		if (currentPhotoDisplayed > 0) {
			nextPicture.setEnabled(true);
		} else {
			nextPicture.setEnabled(false);
		}
		if (currentPictures.size() > 9) {
			takePicture.setEnabled(false);
			choosePicture.setEnabled(false);
		} else {
			takePicture.setEnabled(true);
			choosePicture.setEnabled(true);
		}
		if (currentPictures.size() > 0) {
			deletePicture.setEnabled(true);
		} else {
			deletePicture.setEnabled(false);
		}
	}

	/**
	 * The code to take a new photo, save it in the default directory,
	 * and call the functions to set it in the image preview.
	 */
	private void TakeNewPhoto() {
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		if (takePictureIntent
				.resolveActivity(getActivity().getPackageManager()) != null) {
			File photoFile = null;
			try {
				photoFile = createImageFile();
				currentPhotoPath.push(photoFile.getAbsolutePath());

			} catch (IOException EX) {
				// Error
				sendToast("Error Creating Image File", Toast.LENGTH_SHORT);
				// Anything better to do with this error?
			}
			if (photoFile != null) {
				takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
						Uri.fromFile(photoFile));
				startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);

			}
		}
	}


	/**
	 * Code called after either a camera capture or gallery select.
	 * The bitmap (image preview) will be updated accordingly. 
	 */
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (requestCode == REQUEST_IMAGE_CAPTURE
				&& resultCode == Activity.RESULT_OK) {
			addPictureToLinkedList();
			currentPhotoDisplayed = 0;
			imageView.setImageBitmap(currentPictures.get(0));
			galleryAddPic();
			setPreviousNextButtonEnabledStatus();
		} else if (requestCode == REQUEST_IMAGE_SELECT
				&& resultCode == Activity.RESULT_OK) {
			Uri selectedImage = intent.getData();
			String[] filePathColumn = { MediaStore.Images.Media.DATA };

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

	
	/**
	 * Currently not used code. Could be useful if EXIF data fetching is ever
	 * desired. Might leave code in for release, just for future purposes. 
	 */
	@SuppressWarnings("unused")
	private void retrieveExifData() {
		try {
			ExifInterface exif = new ExifInterface(currentPhotoPath.peek());
			if (exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE) == null
					|| exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE) == null) {
				sendToast(
						"Unable To Retrieve GPS Data, Please Try Other Option",
						Toast.LENGTH_LONG);
			}
			// focalLength.setText((CharSequence) tempString);
		} catch (Exception EX) {
			// Error
			sendToast("No Picture Available", Toast.LENGTH_SHORT);
			// Anything better to do with this error?
		}
	}

	/**
	 * Function to display a message to the user easily.
	 */
	private void sendToast(String message, int length) {

		Activity context = getActivity();
		CharSequence text = (CharSequence) message;
		int duration = length;

		Toast toast = Toast.makeText(context, text, duration);
		toast.show();
	}

	
	/**
	 * Adds a picture for the gallery. 
	 */
	private void galleryAddPic() {
		Intent mediaScanIntent = new Intent(
				Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
		File f = new File(currentPhotoPath.peek());
		Uri contentUri = Uri.fromFile(f);
		mediaScanIntent.setData(contentUri);
		getActivity().sendBroadcast(mediaScanIntent);
	}

	/**
	 * Add a picture to the linked list which contains file paths
	 * and bitmap image previews.
	 */
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
		int scaleFactor = Math.min(photoW / targetH, photoH / targetW);

		// Decode the image file into a Bitmap sized to fill the View
		bmOptions.inJustDecodeBounds = false;
		bmOptions.inSampleSize = scaleFactor;
		bmOptions.inPurgeable = true;
		Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath.peek(),
				bmOptions);
		if (bitmap.getWidth() > bitmap.getHeight()) {
			Matrix matrix = new Matrix();
			matrix.postRotate(90);
			Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
					bmOptions.outWidth, bmOptions.outHeight, matrix, true);
			currentPictures.push(rotatedBitmap);
		} else {
			currentPictures.push(bitmap);
		}
		photoCount++;
		imageCounter.setText("Image Counter: " + photoCount + "/10");
		// imageView.setImageBitmap(rotatedBitmap);
	}

	
	/**
	 * A user has selected to delete a picture. This is 
	 * the underlying logic for picture removal. Delete it
	 * from the LinkedList, and decrement the count-of/current photo.
	 */
	private void removePicFromLinkedList() {

		try {
			imageView.setImageDrawable(null);
			currentPictures.remove(currentPhotoDisplayed);
			currentPhotoPath.remove(currentPhotoDisplayed);
			if (currentPictures.size() == 0) {
				imageView.setImageDrawable(null);
			} else if (currentPhotoDisplayed != 0) {
				imageView.setImageBitmap(currentPictures
						.get(--currentPhotoDisplayed));
			} else {
				imageView.setImageBitmap(currentPictures.get(0));
			}
			photoCount--;
			imageCounter.setText("Image Counter: " + photoCount + "/10");
			setPreviousNextButtonEnabledStatus();
		} catch (Exception EX) {
			sendToast("Error removing picture", Toast.LENGTH_LONG);
		}
	}

	/**
	 * Google's default code for creating an image file. Format is 
	 * JPEG_YYYY/MM/DD_HH/MM/SS_.jpg to avoid name collision.
	 * @return
	 * @throws IOException
	 */
	@SuppressLint("SimpleDateFormat")
	private File createImageFile() throws IOException {
		// Create an image file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
				.format(new Date());
		String imageFileName = "JPEG_" + timeStamp + "_";
		File storageDir = Environment
				.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM
						+ "/Camera"); // Add the pictures to the default
										// DCIM/Camera folder.
		File image = File.createTempFile(imageFileName, /* prefix */
				".jpg", /* suffix */
				storageDir /* directory */
		);

		// Save a file: path for use with ACTION_VIEW intents
		// currentPhotoPath[nextPhoto] = "file:" + image.getAbsolutePath();
		return image;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		((MainActivity) activity).onSectionAttached(getArguments().getInt(
				ARG_SECTION_NUMBER));
	}
}
