package com.example.edu.ksu.crop;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Scalar;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

import android.util.Log;

public class ColorDetector {
	String currentFile;
	public Mat dilatedMask;
	public ColorDetector() {
		OpenCVLoader.initDebug();
		dilatedMask = new Mat();
	}

	
	/** 
	 * Color detection code. Declares two sets of colors to search between, and 
	 * find all pixels in that range. To edit color range, look below to the comments.
	 * Everything is done in HSV format.
	 */
	public double AreaDetection(String cF) {
		if( !OpenCVLoader.initDebug()) {
			return 0.0;
		} else {
			
			Mat hierarchy = new Mat();
			Mat HSV = new Mat();
			Mat Masked = new Mat();
			Mat squarePull = new Mat();

			Mat originalImage = Highgui.imread(cF);
		   
			Imgproc.cvtColor(originalImage, HSV, Imgproc.COLOR_BGR2HSV, 3);
	        
			/* 
			 * Give the range of colors in HSV for the sorghum head where
			 *  HSV is converted from normal 0-360 / 0-100 / 0-100 to
			 *  another HSV format: 0-180 / 0-255 / 0-255. 
			 */
	        Core.inRange(HSV, new Scalar(0, 50, 40), new Scalar(25, 175, 175), Masked);
			/* 
			 * Give the range of colors in HSV for the 1" square where
			 *  HSV is converted from normal 0-360 / 0-100 / 0-100 to
			 *  another HSV format: 0-180 / 0-255 / 0-255. 
			 */
	        Core.inRange(HSV, new Scalar(75, 80, 80), new Scalar(115, 255, 255), squarePull);
	        
	        
			List<MatOfPoint> sqContours = new ArrayList<MatOfPoint>();
	        Imgproc.findContours(squarePull, sqContours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);   
	        
	        double squareArea = 1;
		    Iterator<MatOfPoint> sqEach = sqContours.iterator();
		    while (sqEach.hasNext()) {
		        MatOfPoint wrapper = sqEach.next();
		        double area = Imgproc.contourArea(wrapper);
		        squareArea += area;
		    }

	       Log.i("CROPYIELD", "Square: " + String.valueOf(squareArea));
	        Imgproc.dilate(Masked, dilatedMask, new Mat());
	       
			List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
	        Imgproc.findContours(Masked, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);   

	        double totalArea = 0;
		    Iterator<MatOfPoint> each = contours.iterator();
		    while (each.hasNext()) {
		        MatOfPoint wrapper = each.next();
		        double area = Imgproc.contourArea(wrapper);
		        totalArea += area;
		    }
		    Log.i("CROPYIELD", "Sorghum: " + String.valueOf(totalArea));

		    return (totalArea/squareArea);
			//return(String.format("%.3f", totalArea / squareArea) + " inches squared");
		}
	}

}
