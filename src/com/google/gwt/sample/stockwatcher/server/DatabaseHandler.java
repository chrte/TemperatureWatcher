package com.google.gwt.sample.stockwatcher.server;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;



/**
 * A class used for the communication with the db
 * @author Christian
 *
 */

public class DatabaseHandler {

	private static final String IP ="192.168.1.100"; //may change to 192.168.1.100 if working local from @christians home, else chrte.dyndns.org
	private static final String DATABASENAME ="TDDD24";
	private static final String TABLENAME = "temperatures";
	private static final String CITYCOLUMN = "city";
	private static final String TEMPERATURECOLUMN = "temperature";
	private static final String USERNAME="TDDD24";
	private static final String PASSWORD="TDDD24";
	private java.sql.Connection connection;

	public DatabaseHandler(){
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			connection  = DriverManager.getConnection("jdbc:mysql://"+IP+"/"+DATABASENAME,USERNAME,PASSWORD);
			connection.setAutoCommit(true);
			} catch (Exception e) {
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
		java.sql.Statement stmt = null;
		ResultSet rs=null;
		try {
			stmt = connection.createStatement();
			rs = stmt.executeQuery("SELECT "+TEMPERATURECOLUMN+" FROM " +DATABASENAME+"."+TABLENAME+" WHERE "+CITYCOLUMN+"='"+city+"';");
			while (rs.next()){
			return rs.getDouble(TEMPERATURECOLUMN);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return -273; //TODO, fix better error handling
		
	}

	/**
	 * Set the temperature in a given city
	 * @param city The city which to set the temperature
	 * @param temperature The temperature in the given city
	 */
	public void setTemperature(String city,double temperature){

		try {

			java.sql.Statement stmt=null;
			stmt =connection.createStatement();
			if (stmt.executeUpdate("UPDATE "+DATABASENAME+"."+TABLENAME+" SET "+TEMPERATURECOLUMN+"="+temperature+ " WHERE "+CITYCOLUMN+"='"+city+"';")==0) {
			stmt.executeUpdate("INSERT INTO "+DATABASENAME+"."+TABLENAME+" (`"+CITYCOLUMN+"`, `"+TEMPERATURECOLUMN+"`) VALUES ('"+city+"', "+temperature+");");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}


