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
		
		super.dragStart();  //TODO: Not working
	}

	@Override
	protected Widget newDragProxy(DragContext context){
		HTML proxy = new HTML();
		proxy.addStyleName("FlexTable");
		if (parent != null && parent.getCurrentPlace() != null){  //TODO add so we now which row to move
		proxy.setHTML("<table border='1' ><tr><td>"+parent.getCurrentPlace().getCountry() + "</td><td>"+
		parent.getCurrentPlace().getArea()+"</td><td>"+ parent.getCurrentPlace().getCity()+"</td><tr></table>");
		}
		return proxy;
	}

}
