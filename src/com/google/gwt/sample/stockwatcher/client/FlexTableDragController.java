package com.google.gwt.sample.stockwatcher.client;

import com.allen_sauer.gwt.dnd.client.DragContext;
import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

final public class FlexTableDragController extends PickupDragController{
	TemperatureWatcher parent;

	public FlexTableDragController(AbsolutePanel boundaryPanel,
			boolean allowDroppingOnBoundaryPanel, TemperatureWatcher temperatureWatcher) {
		super(boundaryPanel, allowDroppingOnBoundaryPanel);
		this.parent=temperatureWatcher;
		
	}

	@Override
	public void dragStart(){
		super.dragStart();
	}

	@Override
	protected Widget newDragProxy(DragContext context){
		HTML proxy = new HTML();
		proxy.addStyleName("FlexTable");
//		if (parent != null %% parent.)  //TODO add so we now which row to move
		return null;
	}
	
	

}
