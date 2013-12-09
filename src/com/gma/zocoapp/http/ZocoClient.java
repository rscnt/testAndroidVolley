package com.gma.zocoapp.http;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class ZocoClient {

	private RequestQueue rqstQueue;
	private ZocoAPI zocoAPI;
	
	public ZocoClient(Context cntx) {
		new Volley();
		rqstQueue = Volley.newRequestQueue(cntx);
		zocoAPI = new ZocoAPI(rqstQueue);
	}
	
	public ZocoAPI getAPI(){
		return this.zocoAPI;
	}
}
