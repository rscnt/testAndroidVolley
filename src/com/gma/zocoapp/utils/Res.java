package com.gma.zocoapp.utils;

public final class Res {

	private static String urlBase = "http://10.1.1.104:3000/api/";

	public Res() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the urlBase
	 */
	public static String getUrlBase() {
		return urlBase;
	}

	/**
	 * @param urlBase
	 *            the urlBase to set
	 */
	public static void setUrlBase(String urlBase) {
		Res.urlBase = urlBase;
	}
	
	public static String getUrlProducts() {
		return urlBase + "products/";
	}

	public static String getUrlUsers() {
		return urlBase + "users/";
	}
}
