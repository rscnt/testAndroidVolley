package com.gma.zocoapp;

import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.gma.zocoapp.http.ZocoClient;
import com.gma.zocoapp.models.Product;
import com.gma.zocoapp.models.ProductList;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.os.Build;

public class ProductListViewActivity extends Activity implements
		Response.ErrorListener, Listener<ProductList> {

	ListView lstVw;
	ArrayAdapter<Product> productListAdapter;
	ZocoClient zocoClnt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_list_view);
		// Show the Up button in the action bar.
		setupActionBar();
		zocoClnt = new ZocoClient(this);
		initUI();
	}

	protected void onStart() {
		super.onStart();
		zocoClnt.getAPI().getProductList(this, this);
	}

	protected void onResume() {
		super.onResume();
		productListAdapter.clear();
		zocoClnt.getAPI().getProductList(this, this);
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.user_list_view, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void initUI() {
		lstVw = (ListView) findViewById(R.id.listViewProducts);
		productListAdapter = new ArrayAdapter<Product>(this,
				android.R.layout.simple_list_item_1);
		lstVw.setAdapter(productListAdapter);
	}

	@Override
	public void onResponse(ProductList productList) {
		for (Product prdct : productList) {
			productListAdapter.add(prdct);
		}
	}

	@Override
	public void onErrorResponse(VolleyError volleyError) {
		volleyError.printStackTrace();
	}

}
