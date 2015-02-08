package com.example.edu.ksu.crop;

import com.parse.Parse;

import android.app.Application;

public class CropYield extends Application {
	public void onCreate() {
		super.onCreate();
		
		// Enable Local Datastore.
		Parse.enableLocalDatastore(this);
		//This will initialize Parse for the application and allow us to add stuff to a DB
		Parse.initialize(this, "gD6b5jCam6YiHvknBsJr2Vl34oFvThlSdOUZBXkq", "DnsiavpkN6mQPpuh7yZEo8R9o4zpSVNvnSPCRYQc");
		
	}
}
