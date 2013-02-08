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
	void initiateCity(Temperature temperautre, AsyncCallback<Temperature> callback);
	void initiateCity(ArrayList<Temperature> temperatures, AsyncCallback<ArrayList<Temperature>> callback);
	void setRowsInDb(String city1, int row1, int table1, String city2, int row2, int table2, AsyncCallback<String> callback);

}
