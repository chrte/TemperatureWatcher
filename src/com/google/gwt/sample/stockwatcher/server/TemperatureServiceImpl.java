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

public class TemperatureServiceImpl extends RemoteServiceServlet implements
TemperatureService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Override

	//TODO: Change so we fetch some nice data instead
	public ArrayList<Temperature> getTemperatures(ArrayList<Temperature> listOfTemperatures) throws DelistedException {
		DatabaseHandler dbHandler = new DatabaseHandler();
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
			dbHandler.setTemperature(listOfTemperatures.get(i).getCity(), temperature);
		}

		return listOfTemperatures;
	}
	/**
	 * Sort of working, chnage to input a xml file instead
	 * @param city
	 * @return
	 */
	private double getTempFromXML(Temperature temperature) {
				String xmlURL = "http://www.yr.no/place/"+temperature.getCountry()+"/"+temperature.getArea()+"/"+temperature.getCity()+"/forecast.xml";
				System.out.println("in the getTempFromXML "+xmlURL);
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
				
				System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
				NodeList nList = doc.getElementsByTagName("location");
				Node nNode = nList.item(0);
				Element eElement = (Element) nNode;
				String cityFromXML = getTagValue("name", eElement);
				String countryFromXML = getTagValue("country", eElement);
				System.out.println("-----------------------");
				System.out.println("country is " +countryFromXML +" and the city is " +cityFromXML);
				
				NodeList n2List = doc.getElementsByTagName("temperature");
				System.out.println(n2List.getLength());
				
		//		nList = doc.getElementsByTagName("tabular");
				Node nNode2 = n2List.item(0);  //takes the first element of time
				
				
		//		nNode2 = nNode2.getFirstChild();
				
				eElement = (Element) nNode2;
				NodeList tempList = eElement.getChildNodes();
				System.out.println("length of tempList " +tempList.getLength());
				Node nNode3 = tempList.item(0);
			
				
				System.out.println(eElement.getAttribute("unit"));
				System.out.println(eElement.getAttributeNode("unit"));
				System.out.println(eElement.getAttribute("value"));
				Double returnTemp = Double.parseDouble(eElement.getAttribute("value"));
				System.out.println("returnTemp is "+ returnTemp);
				System.out.println(eElement.getAttributeNode("value"));
				
				//System.out.println("valie form nNode "+ getTagValue("temperature", eElement));
				
				
				
				return returnTemp; 


	}



	private static String getTagValue(String sTag, Element eElement) {
		NodeList nlList = eElement.getElementsByTagName(sTag).item(0)
	            .getChildNodes();


	    Node nValue = (Node) nlList.item(0);
	            System.out.println(nValue.hasAttributes());

	    if (sTag.startsWith("test")) {
	        return eElement.getAttribute("w");

	    } else {
	        return nValue.getNodeValue();
	    }
	}

}
