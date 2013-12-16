package com.gma.zocoapp;

import com.facebook.android.Facebook;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class FacebookObjectActivity extends Activity {

	private Facebook facebook;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_facebook_object);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.facebook_object, menu);
		return true;
	}

}
