package com.gma.zocoapp.http;

import java.util.HashMap;
import java.util.Map;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.JsonRequest;
import com.gma.zocoapp.models.User;
import com.gma.zocoapp.utils.Res;
import com.google.gson.Gson;

public class UserRequest extends JsonRequest<User> {

	private final Listener<User> usrListener;
	Gson gson = new Gson();

	// GET - DELETE : Request
	public UserRequest(User user, Boolean delete, Listener<User> userListener,
			ErrorListener errorListener) {
		super((delete) ? Method.DELETE : Method.GET, user.getUrl(), null,
				userListener, errorListener);
		this.usrListener = userListener;
	}

	// POST - PUT : Request
	public UserRequest(User user, Listener<User> userListener,
			ErrorListener errorListener) {
		super((user.getId() != null) ? Method.PUT : Method.POST,
				(user.getId() != null) ? user.getUrl() : Res.getUrlUsers(),
				user.toJSON(), userListener, errorListener);
		this.usrListener = userListener;
	}

	@Override
	protected Response<User> parseNetworkResponse(NetworkResponse netResponse) {
		String jsonUser = new String(netResponse.data);
		if (jsonUser == "") {
			return Response.success(new User(), getCacheEntry());
		} else {
			User usr = gson.fromJson(jsonUser, User.class);
			return Response.success(usr, getCacheEntry());
		}
	}

	@Override
	protected void deliverResponse(User user) {
		usrListener.onResponse(user);
	}

	@Override
	public void deliverError(VolleyError volleyError) {
		Log.d(UserRequest.class.getCanonicalName(), volleyError.toString());
	}

	@Override
	public Map<String, String> getHeaders() throws AuthFailureError {
		Map<String, String> headers = new HashMap<String, String>();
		Log.d(UserListRequest.class.getCanonicalName(),
				ZocoClient.tkn.getAccess_token());
		String auth = "Bearer " + ZocoClient.tkn.getAccess_token();
		headers.put("Authorization", auth);
		return headers;
	}
}
