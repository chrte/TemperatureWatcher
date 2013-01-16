package com.google.gwt.sample.stockwatcher.client;

import com.allen_sauer.gwt.dnd.client.drop.AbstractDropController;
import com.google.gwt.user.client.ui.FlexTable;

public class FlexTableDropController extends AbstractDropController {
	TemperatureWatcher parent;

	public FlexTableDropController(FlexTable dropTarget, TemperatureWatcher temperatureWatcher) {
		super(dropTarget);
		this.parent=temperatureWatcher;
		// TODO Auto-generated constructor stub
	}

}
