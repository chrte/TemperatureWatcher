package com.google.gwt.sample.stockwatcher.client;

import com.allen_sauer.gwt.dnd.client.DragContext;
import com.allen_sauer.gwt.dnd.client.drop.AbstractDropController;
import com.google.gwt.user.client.ui.FlexTable;

public class FlexTableDropController extends AbstractDropController {
	TemperatureWatcher parent;

	public FlexTableDropController(FlexTable dropTarget, TemperatureWatcher temperatureWatcher) {
		super(dropTarget);
		this.parent=temperatureWatcher;
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void onDrop(DragContext context){
		super.onDrop(context);
		FlexTable target = (FlexTable) getDropTarget();
		int row = target.getRowCount();
		
		target.setText(row,0,parent.getCurrentPlace().getCountry());
		target.setText(row, 1, parent.getCurrentPlace().getArea());
		target.setText(row, 2, parent.getCurrentPlace().getCity());
		
		target.getCellFormatter().addStyleName(row, 0, "FlexTable");
		target.getCellFormatter().addStyleName(row, 1, "FlexTable");
		target.getCellFormatter().addStyleName(row, 2, "FlexTable");
		
		parent.setCurrentPlaceNull();
		
	}

}
