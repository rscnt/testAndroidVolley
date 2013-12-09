package com.gma.zocoapp.models;

import com.google.gson.Gson;

public class UserShippingInfo {

	private Long id;
	private Long user_id;
	private Long country_id;
	private Long region_id;
	private String first_name;
	private String last_name;
	private String address;
	private String address2;
	private String postalcode;
	private String phone;
	
	public UserShippingInfo() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the user_id
	 */
	public Long getUser_id() {
		return user_id;
	}

	/**
	 * @param user_id the user_id to set
	 */
	public void setUser_id(Long user_id) {
		this.user_id = user_id;
	}

	/**
	 * @return the country_id
	 */
	public Long getCountry_id() {
		return country_id;
	}

	/**
	 * @param country_id the country_id to set
	 */
	public void setCountry_id(Long country_id) {
		this.country_id = country_id;
	}

	/**
	 * @return the region_id
	 */
	public Long getRegion_id() {
		return region_id;
	}

	/**
	 * @param region_id the region_id to set
	 */
	public void setRegion_id(Long region_id) {
		this.region_id = region_id;
	}

	/**
	 * @return the first_name
	 */
	public String getFirst_name() {
		return first_name;
	}

	/**
	 * @param first_name the first_name to set
	 */
	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	/**
	 * @return the last_name
	 */
	public String getLast_name() {
		return last_name;
	}

	/**
	 * @param last_name the last_name to set
	 */
	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}

	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * @return the address2
	 */
	public String getAddress2() {
		return address2;
	}

	/**
	 * @param address2 the address2 to set
	 */
	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	/**
	 * @return the postalcode
	 */
	public String getPostalcode() {
		return postalcode;
	}

	/**
	 * @param postalcode the postalcode to set
	 */
	public void setPostalcode(String postalcode) {
		this.postalcode = postalcode;
	}

	/**
	 * @return the phone
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * @param phone the phone to set
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}
	

	public String toJSON() {
		Gson gson = new Gson();
		System.out.println("\n"+ gson.toJson(this) +"\n");
		return gson.toJson(this);
	}

}
