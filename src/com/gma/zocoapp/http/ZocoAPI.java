package com.gma.zocoapp.http;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.gma.zocoapp.models.Product;
import com.gma.zocoapp.models.ProductList;
import com.gma.zocoapp.models.User;
import com.gma.zocoapp.models.UserList;

//TODO: Implement a service that connect the api with activities
public class ZocoAPI {

	private final RequestQueue rqstQueue;

	public ZocoAPI(RequestQueue requestQueue) {
		this.rqstQueue = requestQueue;
	}

	public RequestQueue getRequestQueue() {
		return rqstQueue;
	}

	@SuppressWarnings("unchecked")
	public Request<ProductList> getProductList(
			Listener<ProductList> productListener, ErrorListener errorListener) {
		Request<ProductList> rqstPrdct = new ProductListRequest(
				productListener, errorListener);
		return rqstQueue.add(rqstPrdct);
	}

	@SuppressWarnings("unchecked")
	public Request<UserList> getUserList(Listener<UserList> productListener,
			ErrorListener errorListener) {
		Request<UserList> rqstPrdct = new UserListRequest(productListener,
				errorListener);
		return rqstQueue.add(rqstPrdct);
	}

	@SuppressWarnings("unchecked")
	public Request<Product> getProduct(Product product,
			Listener<Product> productListener, ErrorListener errorListener) {
		Request<Product> rqstPrdct = new ProductRequest(product, false,
				productListener, errorListener);
		return rqstQueue.add(rqstPrdct);
	}

	@SuppressWarnings("unchecked")
	public Request<Product> deleteProduct(Product product,
			Listener<Product> productListener, ErrorListener errorListener) {
		Request<Product> rqstPrdct = new ProductRequest(product, true,
				productListener, errorListener);
		return rqstQueue.add(rqstPrdct);
	}

	// TODO: Implement validations
	@SuppressWarnings("unchecked")
	public Request<Product> postProduct(Product product,
			Listener<Product> productListener, ErrorListener errorListener) {
		if (product.getId() != null) {
			throw new IllegalAccessError(
					"Product Exists : Use PUT Method from the API if you want to update.");
		}
		Request<Product> rqstPrdct = new ProductRequest(product,
				productListener, errorListener);
		return rqstQueue.add(rqstPrdct);
	}

	// TODO: Implement validations
	@SuppressWarnings("unchecked")
	public Request<Product> putProduct(Product product,
			Listener<Product> productListener, ErrorListener errorListener) {
		if (product.getId() == null) {
			throw new IllegalAccessError(
					"Product Not Exists : Use POST Method from the API if you want to create.");
		}
		Request<Product> rqstPrdct = new ProductRequest(product,
				productListener, errorListener);
		return rqstQueue.add(rqstPrdct);
	}

	@SuppressWarnings("unchecked")
	public Request<User> getUser(User product, Listener<User> productListener,
			ErrorListener errorListener) {
		Request<User> rqstPrdct = new UserRequest(product, false,
				productListener, errorListener);
		return rqstQueue.add(rqstPrdct);
	}

	@SuppressWarnings("unchecked")
	public Request<User> deleteUser(User product,
			Listener<User> productListener, ErrorListener errorListener) {
		Request<User> rqstPrdct = new UserRequest(product, true,
				productListener, errorListener);
		return rqstQueue.add(rqstPrdct);
	}

	// TODO: Implement validations
	@SuppressWarnings("unchecked")
	public Request<User> postUser(User product, Listener<User> productListener,
			ErrorListener errorListener) {
		if (product.getId() != null) {
			throw new IllegalAccessError(
					"User Exists : Use PUT Method from the API if you want to update.");
		}
		Request<User> rqstPrdct = new UserRequest(product, productListener,
				errorListener);
		return rqstQueue.add(rqstPrdct);
	}

	// TODO: Implement validations
	@SuppressWarnings("unchecked")
	public Request<User> putUser(User product, Listener<User> productListener,
			ErrorListener errorListener) {
		if (product.getId() == null) {
			throw new IllegalAccessError(
					"User Not Exists : Use POST Method from the API if you want to create.");
		}
		Request<User> rqstPrdct = new UserRequest(product, productListener,
				errorListener);
		return rqstQueue.add(rqstPrdct);
	}
}
