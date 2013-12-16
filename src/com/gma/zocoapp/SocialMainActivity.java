package com.gma.zocoapp;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.IntentSender;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
// --------FB-------------------

import com.facebook.LoggingBehavior;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.Settings;
import com.facebook.android.Facebook;

// -----------------------------

// --------G+-------------------

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.plus.PlusClient;
import com.google.android.gms.common.GooglePlayServicesUtil;

// -----------------------------

public class SocialMainActivity extends Activity implements
		PlusClient.ConnectionCallbacks, PlusClient.OnConnectionFailedListener,
		PlusClient.OnAccessRevokedListener {

	private String TAG = SocialMainActivity.class.getCanonicalName();
	private static final String URL_PREFIX_FRIENDS = "https://graph.facebook.com/me/friends?access_token=";

	private SignInButton gpSignIn;
	private Button btnFbSignIn;

	private SocialMainActivity that = this;
	private TextView txtFbStatus;
	private TextView txtGpStatus;

	// FB {{{

	private Session.StatusCallback statusCallback = new SessionStatusCallback();
	private Facebook facebook;

	// }}}

	// G+ {{{

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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_social_main);

		initUI();

		// Facebook {{{

		Settings.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);

		Session session = Session.getActiveSession();

		if (session == null) {
			if (savedInstanceState != null) {
				session = Session.restoreSession(this, null, statusCallback,
						savedInstanceState);
			}
			if (session == null) {
				session = new Session(this);
			}
			Session.setActiveSession(session);
			if (session.getState().equals(SessionState.CREATED_TOKEN_LOADED)) {
				session.openForRead(new Session.OpenRequest(this)
						.setCallback(statusCallback));
			}
		}

		Log.d(FacebookActivity.class.getCanonicalName(), "FB Activity");

		fbUpdateView();

		// }}}

		/**
		 * @see <a
		 *      href="http://developer.android.com/reference/com/google/android/gms/plus/PlusClient.Builder.html#setVisibleActivities(java.lang.String...)"></a>
		 */
		mPlusClient = new PlusClient.Builder(this, this, this).setActions(
				"http://schemas.google.com/AddActivity",
				"http://schemas.google.com/BuyActivity").build();
	}

	@Override
	public void onStart() {
		super.onStart();
		Session.getActiveSession().addCallback(statusCallback); // Facebook
		mPlusClient.connect(); // G+
	}

	@Override
	public void onStop() {
		super.onStop();
		Session.getActiveSession().removeCallback(statusCallback); // Facebook
		mPlusClient.disconnect(); // G+
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// Facebook {{{
		Session.getActiveSession().onActivityResult(this, requestCode,
				resultCode, data);
		// }}}
		// G+ {{{
		if (requestCode == REQUEST_CODE_SIGN_IN
				|| requestCode == REQUEST_CODE_GET_GOOGLE_PLAY_SERVICES) {
			if (resultCode == RESULT_OK && !mPlusClient.isConnected()
					&& !mPlusClient.isConnecting()) {
				// This time, connect should succeed.
				mPlusClient.connect();
			}
		}
		// }}}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		Session session = Session.getActiveSession();
		Session.saveSession(session, outState);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.social_main, menu);
		return true;
	}

	// Facebook {{{

	private void fbUpdateView() {
		Session session = Session.getActiveSession();
		Toast.makeText(this, "fbUpdateView", Toast.LENGTH_SHORT).show();

		Log.d("SocialMainActivity", session.getState().name());

		if (session.isOpened()) {
			Toast.makeText(this, "isOpened", Toast.LENGTH_SHORT).show();
			txtFbStatus.setText("Logout");
			btnFbSignIn.setOnClickListener(new OnClickListener() {
				public void onClick(View view) {
					fbOnClickLogout();
				}
			});
		} else {
			Toast.makeText(this, "ToLogin", Toast.LENGTH_SHORT).show();
			btnFbSignIn.setText("Login");
			btnFbSignIn.setOnClickListener(new OnClickListener() {
				public void onClick(View view) {
					Toast.makeText(that, "ClickLogin", Toast.LENGTH_SHORT)
							.show();
					fbOnClickLogin();
				}
			});
		}
	}

	private void fbOnClickLogin() {
		Log.d(FacebookActivity.class.getCanonicalName(), "fbOnClickLogin");
		Session session = Session.getActiveSession();
		txtFbStatus.setText(URL_PREFIX_FRIENDS + session.getAccessToken());
		Log.d(FacebookActivity.class.getCanonicalName(), session
				.getAccessToken().toString());
		if (!session.isOpened() && !session.isClosed()) {
			session.openForRead(new Session.OpenRequest(this)
					.setCallback(statusCallback));
		} else {
			Session.openActiveSession(this, true, statusCallback);
		}
	}

	private void fbOnClickLogout() {
		Session session = Session.getActiveSession();
		if (!session.isClosed()) {
			session.closeAndClearTokenInformation();
		}
	}

	private class SessionStatusCallback implements Session.StatusCallback {
		@Override
		public void call(Session session, SessionState state,
				Exception exception) {
			fbUpdateView();
		}
	}

	// }}}

	// G+ {{{

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
	public void onAccessRevoked(ConnectionResult status) {
		if (status.isSuccess()) {
			mSignInStatus.setText("El Acceso se ha denegado");
		} else {
			mSignInStatus.setText("El Acceso se ha garantizado");
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
		gpUpdateButtons(true /* isSignedIn */);
	}

	@Override
	public void onDisconnected() {
		mSignInStatus.setText("Cargando");
		mPlusClient.connect();
		gpUpdateButtons(false /* isSignedIn */);
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		mConnectionResult = result;
		gpUpdateButtons(false /* isSignedIn */);
	}

	private void gpUpdateButtons(boolean isSignedIn) {
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

	// }}} G+

	private void initUI() {

		// G+ Widgets {{{
		gpSignIn = (SignInButton) findViewById(R.id.btn_gplus_auth_2);
		txtGpStatus = (TextView) findViewById(R.id.txtGpStatus);

		mSignInStatus = (TextView) findViewById(R.id.txtGpStatus);
		mSignInButton = (SignInButton) findViewById(R.id.btn_gplus_auth_2);
		mSignInButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int available = GooglePlayServicesUtil
						.isGooglePlayServicesAvailable(that);
				if (available != ConnectionResult.SUCCESS) {
					showDialog(DIALOG_GET_GOOGLE_PLAY_SERVICES);
					return;
				}

				try {
					mSignInStatus.setText("Sin Conectar");
					mConnectionResult.startResolutionForResult(that,
							REQUEST_CODE_SIGN_IN);
				} catch (IntentSender.SendIntentException e) {
					// Fetch a new result to start.
					mPlusClient.connect();
				}
			}
		});
		mSignOutButton = findViewById(R.id.btn_log_out_gplus);
		mSignOutButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mPlusClient.isConnected()) {
					mPlusClient.clearDefaultAccount();
					mPlusClient.disconnect();
					mPlusClient.connect();
				}
			}
		});
		mRevokeAccessButton = findViewById(R.id.btn_rvk_access_gplus);
		mRevokeAccessButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mPlusClient.isConnected()) {
					mPlusClient.revokeAccessAndDisconnect(that);
					gpUpdateButtons(false /* isSignedIn */);
				}
			}
		});

		// }}} G+ Widgets

		// Fb Widgets {{{

		btnFbSignIn = (Button) findViewById(R.id.btn_face_auth);
		txtFbStatus = (TextView) findViewById(R.id.txtFbStatus);

		// }}} Fb Widgets
	}

}
