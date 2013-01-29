package com.google.gwt.sample.stockwatcher.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;






/**
 * A class used for the communication with the db
 * @author Christian
 *
 */

public class DatabaseHandler {

	private static final String IP ="chrte.dyndns.org"; 
	private static final String DATABASENAME ="TDDD24";
	private static final String TABLENAME = "temperatures";
	@SuppressWarnings("unused")
	private static final String IDCOLUMN = "temperatureId";
	private static final String COUNTRYCOLUMN = "country";
	private static final String AREACOLUMN ="area";
	private static final String CITYCOLUMN = "city";
	private static final String TEMPERATURECOLUMN = "temperature";
	private static final String USERNAME="TDDD24";
	private static final String PASSWORD="TDDD24";
	private Connection connection;

	public DatabaseHandler(){
		initiateConnection();

	}
	private void initiateConnection(){
		try {
			if (connection==null || connection.isClosed()){
				try {
					Class.forName("com.mysql.jdbc.Driver").newInstance();
					connection  = DriverManager.getConnection("jdbc:mysql://"+IP+"/"+DATABASENAME,USERNAME,PASSWORD);
					connection.setAutoCommit(true);
				} catch (Exception e) {

					e.printStackTrace();
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Gets the temerature (int) given a city
	 * @param temperature The city which to get the temperature from
	 * @return The temperature in the city
	 */
	public double getTemperature(String city){
		initiateConnection();
		java.sql.Statement stmt = null;
		ResultSet rs=null;
		try {
			stmt = connection.createStatement();
			rs = stmt.executeQuery("SELECT "+TEMPERATURECOLUMN+" FROM " +DATABASENAME+"."+TABLENAME+" WHERE "+CITYCOLUMN+"='"+city+"';");
			while (rs.next()){
				return rs.getDouble(TEMPERATURECOLUMN);
			}
		} catch (SQLException e) {

			e.printStackTrace();
		}

		return -273; 
	}

	/**
	 * Gets the country (String) given a city
	 * @param  The city which to get the country from
	 * @return The country of the city
	 */
	public String getCountry(String city){
		initiateConnection();
		java.sql.Statement stmt = null;
		ResultSet rs=null;
		try {
			stmt = connection.createStatement();
			rs = stmt.executeQuery("SELECT "+COUNTRYCOLUMN+" FROM " +DATABASENAME+"."+TABLENAME+" WHERE "+CITYCOLUMN+"='"+city+"';");
			while (rs.next()){
				return rs.getString(COUNTRYCOLUMN);
			}
		} catch (SQLException e) {

			e.printStackTrace();
		}

		return ""; 
	}
	/**
	 * Gets the area (String) given a city
	 * @param  The city which to get the area from
	 * @return The area of the city
	 */
	public String getArea(String city){
		initiateConnection();
		java.sql.Statement stmt = null;
		ResultSet rs=null;
		try {
			stmt = connection.createStatement();
			rs = stmt.executeQuery("SELECT "+AREACOLUMN+" FROM " +DATABASENAME+"."+TABLENAME+" WHERE "+CITYCOLUMN+"='"+city+"';");
			while (rs.next()){
				return rs.getString(AREACOLUMN);
			}
		} catch (SQLException e) {

			e.printStackTrace();
		}

		return ""; 
	}
	/**
	 * Set the temperature in a given city
	 * @param city The city which to set the temperature
	 * @param temperature The temperature in the given city
	 */
	public void setTemperature(String city,double temperature){
		initiateConnection();
		try {
			java.sql.Statement stmt=null;
			stmt =connection.createStatement();
			if (stmt.executeUpdate("UPDATE "+DATABASENAME+"."+TABLENAME+" SET "+TEMPERATURECOLUMN+"="+temperature+ " WHERE "+CITYCOLUMN+"='"+city+"';")==0) {
				stmt.executeUpdate("INSERT INTO "+DATABASENAME+"."+TABLENAME+" (`"+CITYCOLUMN+"`, `"+TEMPERATURECOLUMN+"`) VALUES ('"+city+"', "+temperature+");");
			}
		} catch (SQLException e) {

			e.printStackTrace();
		}
		
	}

	/**
	 * Creates the row for the given combination of country, area ,and city
	 * Won't insert if the set is already in the table
	 * @param country The country to be inserted
	 * @param area The area
	 * @param city The city
	 */
	public void initiateCity(String country, String area, String city){
		initiateConnection();
		try {
			java.sql.Statement stmt=null;
			stmt =connection.createStatement();
			if(getCountry(city).equals("")){
			stmt.executeUpdate("INSERT IGNORE INTO "+DATABASENAME+"."+TABLENAME+" VALUES ('"+country+"','"+area+"','"+city+"',NULL);");
			}
		} catch (SQLException e) {

			e.printStackTrace();
		}
	}

	/**
	 * Functions for deleting a city from the db
	 * @param city The city to be  deleted
	 */
	public void deleteCity(String city){
		initiateConnection();
		try {
			java.sql.Statement stmt=null;
			
			stmt = connection.createStatement();
			System.out.println("connection is read only "+ connection.isReadOnly());
		
			String query = "DELETE FROM `"+DATABASENAME+"`.`"+TABLENAME+"` WHERE `"+TABLENAME+"`.`"+CITYCOLUMN+"`='"+city+"';";
			connection.prepareStatement(query);
			
			int statResult = stmt.executeUpdate(query);
			System.out.println(statResult+". query:" +query);
			stmt.close();
			connection.close();



		} catch (SQLException e) {

			e.printStackTrace();
		}

	}

	/**
	 * Method for change city name
	 * @param newCity
	 * @param oldCity
	 */
	public void changeCityName(String newCity, String oldCity){
		initiateConnection();
		try {
			java.sql.Statement stmt=null;
			stmt =connection.createStatement();
			stmt.executeUpdate("UPDATE "+DATABASENAME+"."+TABLENAME+" SET "+CITYCOLUMN+"='"+newCity+"' WHERE "+CITYCOLUMN+"='"+oldCity+"';");

		} catch (SQLException e) {

			e.printStackTrace();
		}
	}
	/**
	 * Method for chaning a area name
	 * @param newArea
	 * @param oldArea
	 */
	public void changeAreaName(String newArea, String oldArea){
		initiateConnection();
		try {
			java.sql.Statement stmt=null;
			stmt =connection.createStatement();
			stmt.executeUpdate("UPDATE "+DATABASENAME+"."+TABLENAME+" SET "+AREACOLUMN+"='"+newArea+"' WHERE "+AREACOLUMN+"='"+oldArea+"';");

		} catch (SQLException e) {

			e.printStackTrace();
		}
	}
	/**
	 * Method for changing a countryname
	 * @param newCountry
	 * @param oldCountry
	 */
	public void changeCountryName(String newCountry, String oldCountry){
		initiateConnection();
		try {
			java.sql.Statement stmt=null;
			stmt =connection.createStatement();
			stmt.executeUpdate("UPDATE "+DATABASENAME+"."+TABLENAME+" SET "+COUNTRYCOLUMN+"='"+newCountry+"' WHERE "+CITYCOLUMN+"='"+oldCountry+"';");

		} catch (SQLException e) {

			e.printStackTrace();
		}
	}

	/**
	 * Gets all cites in the database
	 * @return An String[] with all the cities
	 */
	public ArrayList<String> getAllCities(){
		initiateConnection();
		java.sql.Statement stmt = null;
		ResultSet rs=null;
		ArrayList<String> cities = new ArrayList<String>();
		try {
			stmt = connection.createStatement();
			rs = stmt.executeQuery("SELECT "+CITYCOLUMN+" FROM " +DATABASENAME+"."+TABLENAME+";");
			while (rs.next()){
				cities.add(rs.getString(CITYCOLUMN));


			}
		} catch (SQLException e) {

			e.printStackTrace();
		}
		return cities;
	}
}


