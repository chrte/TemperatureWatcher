package com.google.gwt.sample.stockwatcher.client;

import java.util.ArrayList;

import com.google.gwt.user.client.ui.FlexTable;

public class DnDFlexTable extends FlexTable {
	
	private ArrayList<Temperature> listOfTemperatures;
	private int id;
	
	public DnDFlexTable(int id){		
		setListOfTemperatures(new ArrayList<Temperature>());	
		this.setId(id);
	}

	
	public void addTemperature(Temperature temperature){
		this.listOfTemperatures.add(temperature);
	}
	
	public void removeTemperature(int index){
		listOfTemperatures.remove(index);
	}
	
	public void removeAllTemperature(){
		listOfTemperatures = new ArrayList<Temperature>();
	}
	
	public ArrayList<Temperature> getListOfTemperatures() {
		return listOfTemperatures;
	}

	public void setListOfTemperatures(ArrayList<Temperature> listOfTemperatures) {
		this.listOfTemperatures = listOfTemperatures;
	}
	
	public int findIndexOfCity(String city){
		int index = -1;
		boolean boo = false;
		
		for(int i = 0; i<this.listOfTemperatures.size() && !boo ; i++){
			if(this.listOfTemperatures.get(i).getCity().equals(city)){
				index = i;
				boo = true;
			}
		}
		
		return index;
		
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}
	

}
