package com.gma.zocoapp.http;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.JsonRequest;
import com.gma.zocoapp.models.ProductList;
import com.gma.zocoapp.utils.Res;
import com.google.gson.Gson;

public class ProductListRequest extends JsonRequest<ProductList> {

	private final Listener<ProductList> prdctLstListener;
	Gson gson = new Gson();

	public ProductListRequest(Listener<ProductList> productListListener,
			ErrorListener errorListener) {
		super(Method.GET, Res.getUrlProducts(), null, productListListener, errorListener);
		this.prdctLstListener = productListListener;
		// TODO Auto-generated constructor stub
	}

	@Override
	protected Response<ProductList> parseNetworkResponse(NetworkResponse response) {
		String responseJson = new String(response.data);
		ProductList prdctLst = gson.fromJson(responseJson, ProductList.class);
		return Response.success(prdctLst, getCacheEntry());
	}

	@Override
	protected void deliverResponse(ProductList prdctLst) {
		prdctLstListener.onResponse(prdctLst);
	}
	
}
