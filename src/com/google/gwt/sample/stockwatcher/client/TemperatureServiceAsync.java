package com.google.gwt.sample.stockwatcher.client;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface TemperatureServiceAsync {
	void getTemperatures(ArrayList<Temperature> listOfTemperatures, AsyncCallback<ArrayList<Temperature>> callback);
	void getAllData(AsyncCallback<ArrayList<Temperature>> callback);
	void deleteEntryFromDb(Temperature temperature, AsyncCallback<Temperature> callback);

}
