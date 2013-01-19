package com.google.gwt.sample.stockwatcher.client;

import com.allen_sauer.gwt.dnd.client.DragContext;
import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.drop.BoundaryDropController;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.FlexTable;




public final class FlexTableDragController extends PickupDragController{
	
	 private static final String CSS_DEMO_FLEX_TABLE_ROW_EXAMPLE_TABLE_PROXY = "demo-FlexTableRowExample-table-proxy";

	@Override
	public void dragStart(){
		
		super.dragStart();  
	}

	  private FlexTable draggableTable;

	  private int dragRow;

	  public FlexTableDragController(AbsolutePanel boundaryPanel) {
	    super(boundaryPanel, false);
	    setBehaviorDragProxy(true);
	    setBehaviorMultipleSelection(false);
	  }

	  @Override
	  public void dragEnd() {
	    super.dragEnd();

	    // cleanup
	    draggableTable = null;
	  }

	  @Override
	  public void setBehaviorDragProxy(boolean dragProxyEnabled) {
	    if (!dragProxyEnabled) {
	      // TODO implement drag proxy behavior
	      throw new IllegalArgumentException();
	    }
	    super.setBehaviorDragProxy(dragProxyEnabled);
	  }

	  @Override
	  protected BoundaryDropController newBoundaryDropController(AbsolutePanel boundaryPanel,
	      boolean allowDroppingOnBoundaryPanel) {
	    if (allowDroppingOnBoundaryPanel) {
	      throw new IllegalArgumentException();
	    }
	    return super.newBoundaryDropController(boundaryPanel, allowDroppingOnBoundaryPanel);
	  }

	  @Override
	  protected Widget newDragProxy(DragContext context) {
	    FlexTable proxy;
	    proxy = new FlexTable();
	    proxy.addStyleName(CSS_DEMO_FLEX_TABLE_ROW_EXAMPLE_TABLE_PROXY);
	    draggableTable = (FlexTable) context.draggable.getParent();
	    dragRow = getWidgetRow(context.draggable, draggableTable);
	    FlexTableUtil.copyRow(draggableTable, proxy, dragRow, 0);
	    return proxy;
	  }

	  FlexTable getDraggableTable() {
	    return draggableTable;
	  }

	  int getDragRow() {
	    return dragRow;
	  }

	  private int getWidgetRow(Widget widget, FlexTable table) {
	    for (int row = 0; row < table.getRowCount(); row++) {
	      for (int col = 0; col < table.getCellCount(row); col++) {
	        Widget w = table.getWidget(row, col);
	        if (w == widget) {
	          return row;
	        }
	      }
	    }
	    throw new RuntimeException("Unable to determine widget row");
	  }

	
	

	
	


}
