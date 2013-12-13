package com.gma.zocoapp.models;

import java.util.ArrayList;

import com.gma.zocoapp.utils.Res;
import com.google.gson.Gson;
import com.gma.zocoapp.R;

public class ProductList extends ArrayList<Product> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4211185590356760556L;

	public String toJSON() {
		Gson gson = new Gson();
		return gson.toJson(this);
	}

	public final String getUrl() {
		return Res.getUrlProducts();
	}
}
