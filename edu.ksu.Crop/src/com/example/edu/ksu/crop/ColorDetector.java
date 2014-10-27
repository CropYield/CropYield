package com.example.edu.ksu.crop;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Scalar;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

import android.graphics.Color;
import android.widget.ImageView;

public class ColorDetector {
	String currentFile;

	public ColorDetector() {
	
	}
	
	public double AreaDetection(String cF) {
		if( !OpenCVLoader.initDebug()) {
			// FUCK
		} else {
			currentFile = cF;
			Mat originalImage = Highgui.imread(currentFile);
	        Mat dilatedMask = cvtColorsAndFindInBound(originalImage);
	        List<MatOfPoint> contours = findContours(dilatedMask);
		    double maxArea = 0;
		    Iterator<MatOfPoint> each = contours.iterator();
		    while (each.hasNext()) {
		        MatOfPoint wrapper = each.next();
		        double area = Imgproc.contourArea(wrapper);
		        if (area > maxArea)
		            maxArea = area;
		    }
			return maxArea;
		}
	    return 666.6;
	}
	
	private Mat cvtColorsAndFindInBound(Mat m) {
	    Scalar LowerBound = new Scalar(0);
	    Scalar UpperBound = new Scalar(0);
	    
	    float HSVUpper[] = new float[3];
	    float HSVLower[] = new float[3];

	    Color.RGBToHSV(120, 60, 15, HSVUpper);
	    Color.RGBToHSV(60, 35, 0, HSVLower);
	    
	    for(int i = 0; i < 3; i++) {
	    	LowerBound.val[ i ] = (double)HSVLower[ i ];
	    	UpperBound.val[ i ] = (double)HSVUpper[ i ];
	    }
	    
		Mat HSV = new Mat();
		Mat Masked = new Mat();
		Mat DialtedMask = new Mat();
		
		Imgproc.cvtColor(m, HSV, Imgproc.COLOR_RGB2HSV_FULL);

        Core.inRange(HSV, LowerBound, UpperBound, Masked);
        Imgproc.dilate(Masked, DialtedMask, new Mat());
		
		return DialtedMask;
	}
	
	private List<MatOfPoint> findContours(Mat dMask) {
		Mat hierarchy = new Mat();
		List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
        Imgproc.findContours(dMask, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);        
		return contours;
	}

}
