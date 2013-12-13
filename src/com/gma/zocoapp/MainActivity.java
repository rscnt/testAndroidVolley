package com.gma.zocoapp;

import com.android.volley.RequestQueue;
import com.android.volley.Response.Listener;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.gma.zocoapp.http.ZocoClient;
import com.gma.zocoapp.models.User;
import com.gma.zocoapp.models.UserList;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.plus.PlusClient;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

// TODO: This should be a ListActivity
// TODO: Work on abstraction
public class MainActivity extends Activity implements Response.ErrorListener,
		Listener<UserList>, ConnectionCallbacks, OnConnectionFailedListener {

	ListView lstVw;
	Button btnToProductsListActivity;
	Button btnToUserNewActivity;
	Intent intentProductLstVwActivity;
	Intent intentUserDetailVwActivity;
	ArrayAdapter<User> userListAdapter;
	ZocoClient zocoClnt;
	RequestQueue rqstQueue;
	MainActivity that = this;

	// GPlus Client Provided by google play services.
	private PlusClient plsClnt;
	private ConnectionResult cnnctnRslt;
	private SignInButton btnGooglePlusLogin;
	private static final int DIALOG_GET_GOOGLE_PLAY_SERVICES = 1;
	private static final int REQUEST_CODE_SIGN_IN = 1;
	private static final int REQUEST_CODE_GET_GOOGLE_PLAY_SERVICES = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		zocoClnt = new ZocoClient(this); // TODO: Replace to just one ZocoClient
											// in the application.
		zocoClnt.getAPI().setOAuthToken();

		initUI();

		/**
		 * @see <a
		 *      href="http://developer.android.com/reference/com/google/android/gms/plus/PlusClient.Builder.html#setVisibleActivities(java.lang.String...)"></a>
		 */
		plsClnt = new PlusClient.Builder(this, this, this).setActions(
				"http://schemas.google.com/AddActivity",
				"http://schemas.google.com/BuyActivity").build();
	}

	protected void onStart() {
		super.onStart();
	}

	protected void onResume() {
		super.onResume();
	}

	private void initUI() {

		lstVw = (ListView) findViewById(R.id.listViewUsers);

		intentProductLstVwActivity = new Intent(this,
				ProductListViewActivity.class);
		intentUserDetailVwActivity = new Intent(this, UserDetailActivity.class);

		btnToProductsListActivity = (Button) findViewById(R.id.buttonViewProducts);
		btnToUserNewActivity = (Button) findViewById(R.id.buttonPOSTUser);
		btnGooglePlusLogin = (SignInButton) findViewById(R.id.google_plus_sign_in);

		userListAdapter = new ArrayAdapter<User>(this,
				android.R.layout.simple_list_item_1);
		lstVw.setAdapter(userListAdapter);

		btnGooglePlusLogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					cnnctnRslt.startResolutionForResult(that,
							REQUEST_CODE_SIGN_IN);
				} catch (IntentSender.SendIntentException e) {
					plsClnt.connect();
				}
			}
		});

		btnToProductsListActivity.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					// TODO: Implement cache.
					Toast.makeText(
							that,
							ZocoClient.tkn.getAccess_token()
									+ "ZocoClient Token", Toast.LENGTH_SHORT)
							.show();
					fillList();
				} catch (Exception excep) {
					excep.printStackTrace();
				}
				// startActivity(intentProductLstVwActivity);
			}
		});

		btnToUserNewActivity.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(that,
						ZocoClient.tkn.getAccess_token() + "zocoClnt Token",
						Toast.LENGTH_SHORT).show();
				intentUserDetailVwActivity.putExtra("userJSON",
						new User().toJSON());
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
	}

	private void fillList() {
		userListAdapter.clear();
		zocoClnt.getAPI().getUserList(this, this);
	}

	// Methods for Google Plus SignIn {{{

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		Log.d(MainActivity.class.getCanonicalName(), "Connection Failed");
	}

	@Override
	public void onConnected(Bundle connectionHint) {
		Toast.makeText(this, "User is connected!", Toast.LENGTH_LONG).show();
	}

	@Override
	public void onDisconnected() {

	}

	// }}}

}
