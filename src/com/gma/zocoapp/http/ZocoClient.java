package com.gma.zocoapp.http;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.gma.zocoapp.models.Token;

public class ZocoClient {

	private RequestQueue rqstQueue;
	private ZocoAPI zocoAPI;

	// TODO: IMPLEMENT SOMETHING BETTER {{{

	public static final String AccessTokenEndpoint = "http://10.1.1.104:3000/oauth/token/";
	public static final String AuthorizationUrl = "http://10.1.1.104:3000/oauth/authorize/";
	public static final String APPLICATION_ID = "338347215a15233231f3ec5c96a9f36d3b7a4d543cf687cbb960745d03875e56";
	public static final String SECRET = "1562eee67dd63a726644b782fd8bd447552b47151ff5be0a780a69987016c8ba";

	public static Token tkn = new Token();

	// }}}

	public ZocoClient(Context cntx) {
		new Volley();
		rqstQueue = Volley.newRequestQueue(cntx);
		zocoAPI = new ZocoAPI(rqstQueue);
	}

	public ZocoAPI getAPI() {
		return this.zocoAPI;
	}

}
