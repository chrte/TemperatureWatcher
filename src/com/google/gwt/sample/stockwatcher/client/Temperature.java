package com.google.gwt.sample.stockwatcher.client;

import java.io.Serializable;
import java.util.Date;

/**
 * A class for storing the temperature data
 * @author hento581, chrte707
 *
 */

public class Temperature implements Serializable  {
	private static final long serialVersionUID = 1L;
	private String country;
	private String area;
	private String city;
	private double temperature;
	private double change;
	private int table;
	private Date lastUpdate=null; //to know if we haven't get any updates
	private Date nextUpdate=null; //to know if we haven't get any updates

	/**
	 * Default empty constructor for the temperature class
	 */
	public Temperature(){
	}
	
	
	public Temperature(String country, String area, String city, int table){
		this.city = city;
		this.area = area;
		this.country = country;
		this.table=table;
		
	}
	/**
	 * Constructor for the temperature class
	 * @param city The city
	 * @param temp The temperature to set
	 * @param change The change from last temperature
	 */
	public Temperature(String city, double temp, double change) {
		this.setCity(city);
		this.temperature = temp;
		this.change = change;
	}

	/**
	 * Getter for the temperature
	 * @return the temperature
	 */
	public double getTemperature() {
		return this.temperature;
	}

	/**
	 * Getter for the change
	 * @return The change
	 */
	public double getChange() {
		return this.change;
	}

	/**
	 * A getter for the change percent
	 * @return the change percent
	 */
	public double getChangePercent() {
		return 100.0 * this.change / this.temperature;
	}
	
	/**
	 * Setter for the temperature
	 * @param temp The temperature to set
	 */
	public void setTemperature(double temp) {
		this.temperature = temp;
	}

	/**
	 * Setter for the change
	 * @param change The change to set
	 */
	public void setChange(double change) {
		this.change = change;
	}

	/**
	 * Getter for the country
	 * @return The country
	 */
	public String getCountry() {
		return country;
	}

	/**
	 * Setter for the country
	 * @param country
	 */
	public void setCountry(String country) {
		this.country = country;
	}

	/**
	 * Getter for the area
	 * @return the area
	 */
	public String getArea() {
		return area;
	}

	/**
	 * Setter for the area
	 * @param area
	 */
	public void setArea(String area) {
		this.area = area;
	}

	/**
	 * Getter for the city
	 * @return The city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * Setter for the city
	 * @param city
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * getter for the last update
	 * @return the last update
	 */
	public Date getLastUpdate() {
		return lastUpdate;
	}

	/**
	 * Setter for the last update
	 * @param lastUpdate
	 */
	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	/**
	 * Getter for the next update
	 * @return The date for next update
	 */
	public Date getNextUpdate() {
		return nextUpdate;
	}

	/**
	 * Setter for the next update
	 * @param nextUpdate
	 */
	public void setNextUpdate(Date nextUpdate) {
		this.nextUpdate = nextUpdate;
	}


	public void setTable(int table){
		this.table=table;
	}
	public int getTable() {
	
		return this.table;
	}

}
