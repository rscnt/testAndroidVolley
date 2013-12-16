package com.gma.zocoapp;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.support.v4.app.FragmentActivity;

public class DummyMainActivity extends FragmentActivity {

	private DummyMainFragment dummyMainFragment;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (savedInstanceState == null) {
			// Add the fragment on initial activity setup
			dummyMainFragment = new DummyMainFragment();

			getSupportFragmentManager().beginTransaction()
					.add(android.R.id.content, dummyMainFragment).commit();
		} else {
			// Or set the fragment from restored state info
			dummyMainFragment = (DummyMainFragment) getSupportFragmentManager()
					.findFragmentById(android.R.id.content);
		}
	}
}
