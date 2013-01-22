package com.google.gwt.sample.stockwatcher.server;


import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.google.gwt.sample.stockwatcher.client.DelistedException;
import com.google.gwt.sample.stockwatcher.client.Temperature;
import com.google.gwt.sample.stockwatcher.client.TemperatureService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;


/**
 * The class that implements the TemperatureService
 * @author hento581, chrte707
 *
 */

public class TemperatureServiceImpl extends RemoteServiceServlet implements TemperatureService {


	private static final long serialVersionUID = 1L;
	DatabaseHandler dbHandler = new DatabaseHandler();
	@Override

	/**
	 * Functions for getting the temperature data from the database, given object with the listOfTemperatures (cites etc..)
	 */
	public ArrayList<Temperature> getTemperatures(ArrayList<Temperature> listOfTemperatures) throws DelistedException {

		for (int i=0; i<listOfTemperatures.size(); i++) {
			Calendar cal = Calendar.getInstance(); // creates calendar
			cal.setTime(new Date()); // sets calendar time/date
			Double temperature;
			if(listOfTemperatures.get(i).getLastUpdate()==null || listOfTemperatures.get(i).getNextUpdate().before(cal.getTime())){
				temperature = getTempFromXML(listOfTemperatures.get(i)); //only do this if we can expect a new value
			}
			else  temperature=listOfTemperatures.get(i).getTemperature();
			double change = 0;
			if(listOfTemperatures.get(i).getLastUpdate()!=null){
				change = temperature-listOfTemperatures.get(i).getTemperature();
			}
			listOfTemperatures.get(i).setTemperature(temperature+change);
			listOfTemperatures.get(i).setChange(change);
			listOfTemperatures.get(i).setLastUpdate(cal.getTime());
			cal.add(Calendar.HOUR_OF_DAY, +1);
			listOfTemperatures.get(i).setNextUpdate(cal.getTime());

			System.out.println("change is" + change);
			dbHandler.initiateCity(listOfTemperatures.get(i).getCountry(), listOfTemperatures.get(i).getArea(), listOfTemperatures.get(i).getCity());
			dbHandler.setTemperature(listOfTemperatures.get(i).getCity(), temperature);	
			
		}
		return listOfTemperatures;
	}
	
	
	public double getTemperatureInCity(String city){
		return dbHandler.getTemperature(city);
	}
	public ArrayList<Temperature> getAllData(){
		ArrayList<String> cities = dbHandler.getAllCities();
		ArrayList<Temperature> citiesInArrayList = new ArrayList<Temperature>();
		for (int i=0; i<cities.size();i++){
			Temperature tempTemperature = new Temperature(dbHandler.getCountry(cities.get(i)), dbHandler.getArea(cities.get(i)), cities.get(i));
			tempTemperature.setTemperature(dbHandler.getTemperature(cities.get(i)));
			citiesInArrayList.add(tempTemperature);
		}
		return citiesInArrayList;
	}
	public void changArea(Temperature temperature, String oldArea){
		dbHandler.changeAreaName(temperature.getCity(), oldArea);
	}
	
	public void changeCountry(Temperature temperature, String oldCountry){
		dbHandler.changeCountryName(temperature.getCountry(), oldCountry);
	}
	public void changeCity(Temperature temperature, String oldCity){
		dbHandler.changeCityName(temperature.getCity(), oldCity);
	}
	
	/**
	 * Private function for fetching weather data from yr.no, 
	 * @param temperature, the temperature object (including city etc)
	 * @return The temperature for the given temperatureclass
	 */
	private double getTempFromXML(Temperature temperature) {
		String xmlURL = "http://www.yr.no/place/"+temperature.getCountry()+"/"+temperature.getArea()+"/"+temperature.getCity()+"/forecast.xml";
		URL url = null;
		URLConnection conn = null;
		try {
			url = new URL(xmlURL);
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			conn = url.openConnection();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = null;
		try {
			dBuilder = dbFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Document doc = null;
		try {
			doc = dBuilder.parse(conn.getInputStream());
			//					doc = dBuilder.parse(fXmlFile);
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		doc.getDocumentElement().normalize();

		NodeList nList = doc.getElementsByTagName("location");
		Node nNode = nList.item(0);
		Element eElement = (Element) nNode;
		//				String cityFromXML = getTagValue("name", eElement);
		//				String countryFromXML = getTagValue("country", eElement);				
		NodeList n2List = doc.getElementsByTagName("temperature");
		Node nNode2 = n2List.item(0);  //takes the first element of time
		eElement = (Element) nNode2;
		NodeList tempList = eElement.getChildNodes();
		System.out.println("length of tempList " +tempList.getLength());
		return Double.parseDouble(eElement.getAttribute("value"));
	}


	@Override
	public Temperature deleteEntryFromDb(Temperature temperature) {
		dbHandler.deleteCity(temperature.getCity());
		return null;
	}
}
