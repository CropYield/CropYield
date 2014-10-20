package com.example.edu.ksu.crop;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Scalar;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.location.Location;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class PictureFragment extends Fragment {

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
		CharSequence options[] = new CharSequence[] { "Retrieve Current Location", "Select A Location", "Use GPS Location Off Current Photo", "Calculate Max Area"};
		
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
				case 2: 
					retrieveExifData();
					break;
				case 3:
					calculateArea();
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
//	private void calculateArea() {
//		Double currentArea;
//		ColorDetector colorDetector = new ColorDetector();
//		currentArea = colorDetector.AreaDetection(currentPhotoPath.get(currentPhotoDisplayed));
//		sendToast(currentArea.toString(), Toast.LENGTH_LONG);
//	}
	
	private void calculateArea() {
		if( !OpenCVLoader.initDebug()) {
			sendToast("poop", Toast.LENGTH_LONG);
		} else {
	        Mat dilatedMask = new Mat();
			Mat hierarchy = new Mat();
			Mat HSV = new Mat();
			Mat Masked = new Mat();
			Mat DialtedMask = new Mat();
			
	        Scalar LowerBound = new Scalar(0);
		    Scalar UpperBound = new Scalar(0);
		    
		    float HSVUpper[] = new float[3];
		    float HSVLower[] = new float[3];

			Mat originalImage = Highgui.imread(currentPhotoPath.get(currentPhotoDisplayed));
				setImageViewTest(originalImage, "1");
			
		    Color.RGBToHSV(15, 60, 120, HSVUpper);
		    Color.RGBToHSV(0, 35, 60, HSVLower);
		    
		    for(int i = 0; i < 3; i++) {
		    	LowerBound.val[ i ] = (double)HSVLower[ i ];
		    	UpperBound.val[ i ] = (double)HSVUpper[ i ];
		    }
		   
			Imgproc.cvtColor(originalImage, HSV, Imgproc.COLOR_BGR2HSV, 3);
				setImageViewTest(HSV, "2");

	        Core.inRange(HSV, new Scalar(0, 100, 30), new Scalar(15, 255, 255), Masked);
	        	sendToast("" + Masked.cols(), Toast.LENGTH_LONG);
	        	setImageViewTest2(Masked, "3");

	        Imgproc.dilate(Masked, dilatedMask, new Mat());
	        setImageViewTest2(dilatedMask, "4");
	
			List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
	        Imgproc.findContours(Masked, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);   
			sendToast("Countour Count: " + contours.size() , Toast.LENGTH_LONG);

	        double maxArea = 0;
		    Iterator<MatOfPoint> each = contours.iterator();
		    while (each.hasNext()) {
		        MatOfPoint wrapper = each.next();
		        double area = Imgproc.contourArea(wrapper);
		        if (area > maxArea)
		            maxArea = area;
		    }
			sendToast( ( "The max area is: " + maxArea ), Toast.LENGTH_LONG);
		}
	}
	
	private void setImageViewTest(Mat mat, String toastNum) {
		try{
//			sendToast("" + mat.height(), Toast.LENGTH_LONG);
			Bitmap bmp = Bitmap.createBitmap(mat.cols(), mat.rows(), Bitmap.Config.ARGB_8888);
			Mat tmp = new Mat(mat.rows(), mat.cols(), CvType.CV_8U, new Scalar(4));
		    Imgproc.cvtColor(mat, tmp, Imgproc.COLOR_HSV2RGB, 4);
			Utils.matToBitmap(tmp, bmp);
			imageView.setImageBitmap(bmp);
		} catch (Exception EX) {
			sendToast( "Bitmap " + toastNum + " is null", Toast.LENGTH_SHORT );
		}
		
	}
	
	
	private void setImageViewTest2(Mat mat, String toastNum) {
		try{
//			sendToast("" + mat.height(), Toast.LENGTH_LONG);
			Bitmap bmp = Bitmap.createBitmap(mat.cols(), mat.rows(), Bitmap.Config.ARGB_8888);
			Mat tmp = new Mat(mat.rows(), mat.cols(), CvType.CV_8U, new Scalar(4));
		    Imgproc.cvtColor(mat, tmp, Imgproc.COLOR_GRAY2BGR, 4);
			Utils.matToBitmap(tmp, bmp);
			imageView.setImageBitmap(bmp);
		} catch (Exception EX) {
			sendToast( "Bitmap " + toastNum + " is null", Toast.LENGTH_SHORT );
		}
		
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
		if( requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK ) {
    		addPictureToLinkedList();
			currentPhotoDisplayed = 0;
			imageView.setImageBitmap(currentPictures.get(0));
    		galleryAddPic();
    		setPreviousNextButtonEnabledStatus();
		}
		else if( requestCode == REQUEST_IMAGE_SELECT && resultCode == Activity.RESULT_OK ) {
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
//			focalLength.setText((CharSequence) tempString);
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
	    
//	    imageView.setImageBitmap(rotatedBitmap);
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
//	    currentPhotoPath[nextPhoto] = "file:" + image.getAbsolutePath();
	    return image;
	}
	
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		((MainActivity) activity).onSectionAttached(getArguments().getInt(
				ARG_SECTION_NUMBER));
	}
}

