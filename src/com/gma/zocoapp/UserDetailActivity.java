package com.gma.zocoapp;

import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.gma.zocoapp.http.ZocoClient;
import com.gma.zocoapp.models.User;
import com.google.gson.Gson;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.os.Build;

public class UserDetailActivity extends Activity implements
		Response.ErrorListener, Listener<User> {

	User usr;
	Gson gson = new Gson();
	ZocoClient zocoClnt;
	UserDetailActivity that = this;
	EditText editTxtUserName;
	EditText editTxtFirstName;
	EditText editTxtLastName;
	EditText editTxtEmail;
	EditText editTxtPassword;
	Button btnSENDRequest;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_detail);
		// Show the Up button in the action bar.
		setupActionBar();
		zocoClnt = new ZocoClient(this);
		if (null == savedInstanceState) {
			Bundle extras = getIntent().getExtras();
			if (extras != null) {
				usr = gson.fromJson(extras.getString("userJSON"), User.class);
			} else {
				usr = new User();
			}
		} else {
			usr = gson.fromJson(savedInstanceState.getString("userJSON"),
					User.class);
		}
		initUI();
	}

	protected void onStart() {
		super.onStart();
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			usr = gson.fromJson(extras.getString("userJSON"), User.class);
		} else {
			usr = new User();
		}
	}

	private void initUI() {

		editTxtUserName = (EditText) findViewById(R.id.editTextUserName);
		editTxtFirstName = (EditText) findViewById(R.id.EditTextFirstName);
		editTxtLastName = (EditText) findViewById(R.id.EditTextLastName);
		editTxtEmail = (EditText) findViewById(R.id.EditTextEmail);
		editTxtPassword = (EditText) findViewById(R.id.EditTextPassword);
		btnSENDRequest = (Button) findViewById(R.id.buttonSENDRequest);

		editTxtUserName.setText(usr.getUsername());
		editTxtFirstName.setText(usr.getFirst_name());
		editTxtLastName.setText(usr.getLast_name());
		editTxtEmail.setText(usr.getEmail());

		btnSENDRequest.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				usr.setUsername(editTxtUserName.getText().toString());
				usr.setFirst_name(editTxtFirstName.getText().toString());
				usr.setLast_name(editTxtLastName.getText().toString());
				usr.setPassword(editTxtPassword.getText().toString());
				usr.setEmail(editTxtEmail.getText().toString());
				usr.setCountry_id(1L);

				if (usr.isNew()) {
					zocoClnt.getAPI().postUser(usr, that, that);
				} else {
					zocoClnt.getAPI().putUser(usr, that, that);
				}
			}
		});

		if (!usr.isNew()) {
			Button btnDeleteUser = new Button(this);
			btnDeleteUser.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					zocoClnt.getAPI().deleteUser(usr, that, that);
				}
			});
		}
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
		getMenuInflater().inflate(R.menu.user_detail, menu);
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

	@Override
	public void onResponse(User user) {
		Log.d("RESPONSE : ", user.toJSON());
	}

	@Override
	public void onErrorResponse(VolleyError volleyError) {
		volleyError.printStackTrace();
	}

}
