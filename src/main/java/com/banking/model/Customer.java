package com.banking.model;

/**
 * Represents users of the banking service.
 * 
 * @author Malcolm
 */
public class Customer {
	private int id;
	private String name;
	private String email;
	private String phone;
	
	// TBD based on database design; me thinks it's going to be internal 
	// private static customerIdGen = 0;
	
	
	// Default constructor
	public Customer() {}
	
	/**
	 * @param id Customer ID
	 * @param name Full name of the customer
	 * @param email Email address
	 * @param phone Phone number
	 */
	public Customer(int id, String name, String email, String phone) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.phone = phone;
	}

	/**
	 * @param name Full name of the customer
	 * @param email Email address
	 * @param phone Phone number
	 */
	public Customer(String name, String email, String phone) {
		this.name = name;
		this.email = email;
		this.phone = phone;
	}

	
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
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
}
