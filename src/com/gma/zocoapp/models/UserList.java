/**
 * 
 */
package com.gma.zocoapp.models;

import java.util.ArrayList;

import com.gma.zocoapp.utils.Res;
import com.google.gson.Gson;

/**
 * @author raul
 * 
 */
public class UserList extends ArrayList<User> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2283902536925820997L;

	/**
	 * 
	 */
	public UserList() {
	}

	public String toJSON() {
		Gson gson = new Gson();
		return gson.toJson(this);
	}

	public String getUrl() {
		return Res.getUrlUsers();
	}
}
