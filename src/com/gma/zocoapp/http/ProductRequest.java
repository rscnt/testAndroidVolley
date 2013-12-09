package com.gma.zocoapp.http;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.JsonRequest;
import com.gma.zocoapp.models.Product;
import com.gma.zocoapp.utils.Res;
import com.google.gson.Gson;

public class ProductRequest extends JsonRequest<Product> {

	private final Listener<Product> prdctListener;
	Gson gson = new Gson();

	// GET - DELETE : Request
	public ProductRequest(Product product, Boolean delete,
			Listener<Product> productListener, ErrorListener errorListener) {
		super((delete) ? Method.DELETE : Method.GET, product.getUrl(), null,
				productListener, errorListener);
		this.prdctListener = productListener;
	}

	// POST - PUT : Request
	public ProductRequest(Product product, Listener<Product> productListener,
			ErrorListener errorListener) {
		super((product.getId() != null) ? Method.PUT : Method.POST, (product
				.getId() != null) ? product.getUrl() : Res.getUrlProducts(),
				product.toJSON(), productListener, errorListener);
		this.prdctListener = productListener;
	}

	@Override
	protected Response<Product> parseNetworkResponse(NetworkResponse netResponse) {
		String jsonProduct = new String(netResponse.data);
		if (jsonProduct == "") {
			return Response.success(new Product(), getCacheEntry());
		} else {
			Product prdct = gson.fromJson(jsonProduct, Product.class);
			return Response.success(prdct, getCacheEntry());
		}
	}

	@Override
	protected void deliverResponse(Product product) {
		prdctListener.onResponse(product);
	}

}
