package com.gma.zocoapp;

import com.android.volley.RequestQueue;
import com.android.volley.Response.Listener;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.facebook.UiLifecycleHelper;
import com.gma.zocoapp.FacebookFragment.OnFragmentInteractionListener;
import com.gma.zocoapp.http.ZocoClient;
import com.gma.zocoapp.models.User;
import com.gma.zocoapp.models.UserList;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.plus.PlusClient;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.content.IntentSender.SendIntentException;
import android.support.v4.app.FragmentActivity;
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
public class MainActivity extends FragmentActivity implements OnClickListener,
		Response.ErrorListener, Listener<UserList>,
		PlusClient.ConnectionCallbacks, PlusClient.OnConnectionFailedListener,
		PlusClient.OnAccessRevokedListener, OnFragmentInteractionListener {

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
	// private PlusClient mPlusClient; {{{

	private PlusClient mPlusClient;
	private ConnectionResult mConnectionResult;
	private TextView mSignInStatus;
	private SignInButton mSignInButton;
	private View mSignOutButton;
	private View mRevokeAccessButton;
	private static final int REQUEST_CODE_RESOLVE_FAILURE = 9000;
	private static final int DIALOG_GET_GOOGLE_PLAY_SERVICES = 1;
	private static final int REQUEST_CODE_SIGN_IN = 1;
	private static final int REQUEST_CODE_GET_GOOGLE_PLAY_SERVICES = 2;

	// }}}

	// Facebook {{{

	private FacebookFragment fbFragment;

	// }}}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Facebook {{{

		if (savedInstanceState == null) {
			fbFragment = new FacebookFragment();
			getSupportFragmentManager().beginTransaction()
					.add(android.R.id.content, fbFragment).commit();
		} else {
			fbFragment = (FacebookFragment) getSupportFragmentManager()
					.findFragmentById(android.R.id.content);
		}

		// }}}

		zocoClnt = new ZocoClient(this); // TODO: Replace to just one ZocoClient
											// in the application.
		zocoClnt.getAPI().setOAuthToken();

		/**
		 * @see <a
		 *      href="http://developer.android.com/reference/com/google/android/gms/plus/PlusClient.Builder.html#setVisibleActivities(java.lang.String...)"></a>
		 */
		mPlusClient = new PlusClient.Builder(this, this, this).setActions(
				"http://schemas.google.com/AddActivity",
				"http://schemas.google.com/BuyActivity").build();

		initUI();

	}

	protected void onStart() {
		super.onStart();
		mPlusClient.connect();
	}

	protected void onResume() {
		super.onResume();
	}

	protected void onStop() {
		mPlusClient.disconnect();
		super.onStop();
	}

	private void initUI() {

		lstVw = (ListView) findViewById(R.id.listViewUsers);
		intentProductLstVwActivity = new Intent(this,
				ProductListViewActivity.class);
		intentUserDetailVwActivity = new Intent(this, UserDetailActivity.class);
		findViewById(R.id.buttonViewProducts).setOnClickListener(this);
		findViewById(R.id.buttonPOSTUser).setOnClickListener(this);

		mSignInStatus = (TextView) findViewById(R.id.sign_in_status);
		mSignInButton = (SignInButton) findViewById(R.id.google_plus_sign_in);
		mSignInButton.setOnClickListener(this);
		mSignOutButton = findViewById(R.id.sign_out_button);
		mSignOutButton.setOnClickListener(this);
		mRevokeAccessButton = findViewById(R.id.revoke_access_button);
		mRevokeAccessButton.setOnClickListener(this);

		userListAdapter = new ArrayAdapter<User>(this,
				android.R.layout.simple_list_item_1);
		lstVw.setAdapter(userListAdapter);

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

	// Methods for Google Plus SignIn {{{

	@Override
	protected Dialog onCreateDialog(int id) {
		if (id != DIALOG_GET_GOOGLE_PLAY_SERVICES) {
			return super.onCreateDialog(id);
		}

		int available = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(this);
		if (available == ConnectionResult.SUCCESS) {
			return null;
		}
		if (GooglePlayServicesUtil.isUserRecoverableError(available)) {
			return GooglePlayServicesUtil.getErrorDialog(available, this,
					REQUEST_CODE_GET_GOOGLE_PLAY_SERVICES);
		}
		return new AlertDialog.Builder(this).setMessage("CREANDO")
				.setCancelable(true).create();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_CODE_SIGN_IN
				|| requestCode == REQUEST_CODE_GET_GOOGLE_PLAY_SERVICES) {
			if (resultCode == RESULT_OK && !mPlusClient.isConnected()
					&& !mPlusClient.isConnecting()) {
				// This time, connect should succeed.
				mPlusClient.connect();
			}
		}
	}

	@Override
	public void onAccessRevoked(ConnectionResult status) {
		if (status.isSuccess()) {
			mSignInStatus.setText("Acceso NO REVOCADO");
		} else {
			mSignInStatus.setText("ACCESSO Revocado");
			mPlusClient.disconnect();
		}
		mPlusClient.connect();
	}

	@Override
	public void onConnected(Bundle connectionHint) {
		String currentPersonName = mPlusClient.getCurrentPerson() != null ? mPlusClient
				.getCurrentPerson().getDisplayName() : "Desconectado";
		mPlusClient.getCurrentPerson().getCurrentLocation();
		mSignInStatus.setText("Logueado: " + currentPersonName);
		updateButtons(true /* isSignedIn */);
	}

	@Override
	public void onDisconnected() {
		mSignInStatus.setText("Cargando");
		mPlusClient.connect();
		updateButtons(false /* isSignedIn */);
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		mConnectionResult = result;
		updateButtons(false /* isSignedIn */);
	}

	private void updateButtons(boolean isSignedIn) {
		if (isSignedIn) {
			mSignInButton.setVisibility(View.INVISIBLE);
			mSignOutButton.setEnabled(true);
			mRevokeAccessButton.setEnabled(true);
		} else {
			if (mConnectionResult == null) {
				// Disable the sign-in button until onConnectionFailed is called
				// with result.
				mSignInButton.setVisibility(View.INVISIBLE);
				mSignInStatus.setText("Cargando");
			} else {
				// Enable the sign-in button since a connection result is
				// available.
				mSignInButton.setVisibility(View.VISIBLE);
				mSignInStatus.setText("Desconectado");
			}

			mSignOutButton.setEnabled(false);
			mRevokeAccessButton.setEnabled(false);
		}
	}

	// END G+ }}}

	// onClick event {{{
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.google_plus_sign_in:

			int available = GooglePlayServicesUtil
					.isGooglePlayServicesAvailable(this);
			if (available != ConnectionResult.SUCCESS) {
				showDialog(DIALOG_GET_GOOGLE_PLAY_SERVICES);
				return;
			}

			try {
				mSignInStatus.setText("Sin Conectar");
				mConnectionResult.startResolutionForResult(this,
						REQUEST_CODE_SIGN_IN);
			} catch (IntentSender.SendIntentException e) {
				// Fetch a new result to start.
				mPlusClient.connect();
			}

			break;

		case R.id.sign_out_button:
			if (mPlusClient.isConnected()) {
				mPlusClient.clearDefaultAccount();
				mPlusClient.disconnect();
				mPlusClient.connect();
			}
			break;

		case R.id.revoke_access_button:
			if (mPlusClient.isConnected()) {
				mPlusClient.revokeAccessAndDisconnect(this);
				updateButtons(false /* isSignedIn */);
			}
			break;

		case R.id.buttonPOSTUser:
			Toast.makeText(that,
					ZocoClient.tkn.getAccess_token() + "zocoClnt Token",
					Toast.LENGTH_SHORT).show();
			intentUserDetailVwActivity
					.putExtra("userJSON", new User().toJSON());
			startActivity(intentUserDetailVwActivity);
			break;

		case R.id.buttonViewProducts:
			try {
				// TODO: Implement cache.
				Toast.makeText(that,
						ZocoClient.tkn.getAccess_token() + "ZocoClient Token",
						Toast.LENGTH_SHORT).show();
				callGETUserList();
			} catch (Exception excep) {
				excep.printStackTrace();
			}
			// startActivity(intentProductLstVwActivity);
			break;

		default:
			break;

		}
	}

	// }}}

	// Volley - Internal Request {{{

	private void callGETUserList() {
		userListAdapter.clear();
		zocoClnt.getAPI().getUserList(this, this);
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

	@Override
	public void onFragmentInteraction(Uri uri) {
		Log.d(MainActivity.class.getCanonicalName(), uri.toString());
	}

	// }}}

}