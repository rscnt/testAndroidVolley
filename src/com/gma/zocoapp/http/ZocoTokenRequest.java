package com.gma.zocoapp.http;

import java.util.HashMap;
import java.util.Map;

import android.util.Base64;

import com.android.volley.AuthFailureError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;

public class ZocoTokenRequest extends StringRequest {

	private final Listener<String> zocoTknRqstLstnr;

	public ZocoTokenRequest(int method, String url, Listener<String> listener,
			ErrorListener errorListener) {
		super(method, url, listener, errorListener);
		zocoTknRqstLstnr = listener;
	}

	@Override
	protected void deliverResponse(String token) {
		zocoTknRqstLstnr.onResponse(token);
	}

	@Override
	protected Map<String, String> getParams() throws AuthFailureError {
		Map<String, String> params = new HashMap<String, String>();
		params.put("grant_type", "client_credentials");
		return params;
	}

	@Override
	public Map<String, String> getHeaders() throws AuthFailureError {
		Map<String, String> headers = new HashMap<String, String>();
		String auth = "Basic "
				+ Base64.encodeToString(
						(ZocoClient.APPLICATION_ID + ":" + ZocoClient.SECRET)
								.getBytes(), Base64.NO_WRAP);
		headers.put("Authorization", auth);
		return headers;
	}

}
