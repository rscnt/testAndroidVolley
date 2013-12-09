package com.gma.zocoapp.http;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.JsonRequest;
import com.gma.zocoapp.models.UserList;
import com.gma.zocoapp.utils.Res;
import com.google.gson.Gson;

public class UserListRequest extends JsonRequest<UserList> {

	private final Listener<UserList> usrLstListener;
	private Gson gson = new Gson();
	
	public UserListRequest(Listener<UserList> userListListener,
			ErrorListener errorListener) {
		super(Method.GET, Res.getUrlUsers(), null, userListListener,
				errorListener);
		this.usrLstListener = userListListener;
	}

	@Override
	protected Response<UserList> parseNetworkResponse(NetworkResponse response) {
		String responseJson = new String(response.data);
		UserList usrLst = gson.fromJson(responseJson, UserList.class);
		return Response.success(usrLst, getCacheEntry());
	}

	@Override
	protected void deliverResponse(UserList prdctLst) {
		usrLstListener.onResponse(prdctLst);
	}
}
