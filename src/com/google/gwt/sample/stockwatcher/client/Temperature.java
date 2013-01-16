package com.google.gwt.sample.stockwatcher.client;

import java.io.Serializable;
import java.util.Date;

public class Temperature implements Serializable  {
	private String country;
	private String area;
	private String city;
	private double temperature;
	private double change;
	private Date lastUpdate=null; //to know if we haven't get any updates
	private Date nextUpdate=null; //to know if we haven't get any updates

	public Temperature() {
	}

	public Temperature(String city, double temp, double change) {
		this.setCity(city);
//		this.setCountry(country);
//		this.setArea(area);
		this.temperature = temp;
		this.change = change;
	}



	public double getTemperature() {
		return this.temperature;
	}

	public double getChange() {
		return this.change;
	}

	public double getChangePercent() {
		return 100.0 * this.change / this.temperature;
	}
	public void setTemperature(double temp) {
		this.temperature = temp;
	}

	public void setChange(double change) {
		this.change = change;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Date getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public Date getNextUpdate() {
		return nextUpdate;
	}

	public void setNextUpdate(Date nextUpdate) {
		this.nextUpdate = nextUpdate;
	}

}
