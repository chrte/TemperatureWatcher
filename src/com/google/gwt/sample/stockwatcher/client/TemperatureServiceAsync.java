package com.google.gwt.sample.stockwatcher.client;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface TemperatureServiceAsync {
	void getTemperatures(ArrayList<Temperature> listOfTemperatures, AsyncCallback<ArrayList<Temperature>> callback);
	void getAllData(AsyncCallback<ArrayList<Temperature>> callback);
	void deleteEntryFromDb(Temperature temperature, AsyncCallback<Temperature> callback);
	void setCountry(String tempText, String oldValue, String oldCity, AsyncCallback<String> callback);
	void setArea(String tempText, String oldValue, String oldCity, AsyncCallback<String>  callback);
	void setCity(String tempText, String oldValue, AsyncCallback<String>  callback);

}
