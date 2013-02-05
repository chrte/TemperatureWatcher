package com.google.gwt.sample.stockwatcher.client;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("temperatures")
public interface TemperatureService extends RemoteService {


	ArrayList<Temperature> getTemperatures(ArrayList<Temperature> temperatures) throws DelistedException;
	ArrayList<Temperature> getAllData();
	Temperature deleteEntryFromDb(Temperature temperature);
	String setCountry(String newValue, String oldValue, String oldCity);
	String setArea(String newValue, String oldValue, String oldCity);
	String setCity(String newValue, String oldValue);
	Temperature initiateCity(Temperature temperature);
	ArrayList<Temperature> initiateCity(ArrayList<Temperature> temperatures);
	
	
	
}