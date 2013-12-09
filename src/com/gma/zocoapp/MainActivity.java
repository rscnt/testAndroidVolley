package com.gma.zocoapp;

import com.android.volley.Response.Listener;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.gma.zocoapp.http.ZocoClient;
import com.gma.zocoapp.models.User;
import com.gma.zocoapp.models.UserList;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

// TODO: This should be a ListActivity
// TODO: Work on abstraction
public class MainActivity extends Activity implements Response.ErrorListener,
		Listener<UserList> {

	ListView lstVw;
	Button btnToProductsListActivity;
	Button btnToUserNewActivity;
	Intent intentProductLstVwActivity;
	Intent intentUserDetailVwActivity;
	ArrayAdapter<User> userListAdapter;
	ZocoClient zocoClnt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		zocoClnt = new ZocoClient(this); // TODO: Replace to just one ZocoClient
											// in the application.
		initUI();
	}

	protected void onStart() {
		super.onStart();
		zocoClnt.getAPI().getUserList(this, this);
	}

	protected void onResume() {
		super.onResume();
		// TODO: Implement cache.
		userListAdapter.clear();
		zocoClnt.getAPI().getUserList(this, this);
	}

	private void initUI() {

		lstVw = (ListView) findViewById(R.id.listViewUsers);

		intentProductLstVwActivity = new Intent(this,
				ProductListViewActivity.class);
		intentUserDetailVwActivity = new Intent(this, UserDetailActivity.class);

		btnToProductsListActivity = (Button) findViewById(R.id.buttonViewProducts);
		btnToUserNewActivity = (Button) findViewById(R.id.buttonPOSTUser);

		userListAdapter = new ArrayAdapter<User>(this,
				android.R.layout.simple_list_item_1);
		lstVw.setAdapter(userListAdapter);

		btnToProductsListActivity.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				intentUserDetailVwActivity.putExtra("userJSON", new User().toJSON());
				startActivity(intentProductLstVwActivity);
			}
		});

		btnToUserNewActivity.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(intentUserDetailVwActivity);
			}
		});

		lstVw.setOnItemClickListener(new OnItemClickListener() {
			// [parentView, view, position, id]
			@Override
			public void onItemClick(AdapterView<?> parentView, View view,
					int position, long id) {
				User user = userListAdapter.getItem(position);
				intentUserDetailVwActivity.putExtra("userJSON", user.toJSON());
				startActivity(intentUserDetailVwActivity);
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onResponse(UserList userList) {
		for (User user : userList) {
			userListAdapter.add(user);
		}
	}

	@Override
	public void onErrorResponse(VolleyError volleyError) {
		volleyError.printStackTrace();
	}

}
