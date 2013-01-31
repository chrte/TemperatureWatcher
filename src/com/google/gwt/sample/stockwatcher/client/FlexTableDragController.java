package com.google.gwt.sample.stockwatcher.client;

import com.allen_sauer.gwt.dnd.client.DragContext;
import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.drop.BoundaryDropController;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Widget;

/**
 * Class for handling the DragController, inspired by https://gwt-dnd.appspot.com
 * @author chrte707, hento581
 *
 */
public final class FlexTableDragController extends PickupDragController{
	private static final String CSS_DEMO_FLEX_TABLE_ROW_EXAMPLE_TABLE_PROXY = "demo-FlexTableRowExample-table-proxy";

	@Override
	public void dragStart(){

		super.dragStart();  
	}

	private DnDFlexTable draggableTable;
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
		DnDFlexTable proxy = new DnDFlexTable();
		proxy.addStyleName(CSS_DEMO_FLEX_TABLE_ROW_EXAMPLE_TABLE_PROXY);
		draggableTable = (DnDFlexTable) context.draggable.getParent();
		dragRow = getWidgetRow(context.draggable, draggableTable);
		FlexTableUtil.copyRow(draggableTable, proxy, dragRow, 0);
		return proxy;
	}

	DnDFlexTable getDraggableTable() {
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
