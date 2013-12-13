package com.gma.zocoapp.models;

import android.util.Log;

import com.google.gson.Gson;

public class Token {

	private String access_token;
	private String token_type;
	private Long expires_in;

	// 96c06bef0186c20076c2ca6853ddf25179266bfee0763120f2df9b8cebcb518a

	/**
	 * @return the access_token
	 */
	public String getAccess_token() {
		return access_token;
	}

	/**
	 * @param access_token
	 *            the access_token to set
	 */
	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}

	/**
	 * @return the token_type
	 */
	public String getToken_type() {
		return token_type;
	}

	/**
	 * @param token_type
	 *            the token_type to set
	 */
	public void setToken_type(String token_type) {
		this.token_type = token_type;
	}

	/**
	 * @return the expires_in
	 */
	public Long getExpires_in() {
		return expires_in;
	}

	/**
	 * @param expires_in
	 *            the expires_in to set
	 */
	public void setExpires_in(Long expires_in) {
		this.expires_in = expires_in;
	}

	public Token() {
		// TODO Auto-generated constructor stub
	}

	public String toJSON() {
		Gson gson = new Gson();
		return gson.toJson(this);
	}

}
