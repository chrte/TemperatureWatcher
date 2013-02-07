package com.google.gwt.sample.stockwatcher.client;


import com.allen_sauer.gwt.dnd.client.DragContext;
import com.allen_sauer.gwt.dnd.client.drop.AbstractPositioningDropController;
import com.allen_sauer.gwt.dnd.client.util.CoordinateLocation;
import com.allen_sauer.gwt.dnd.client.util.DOMUtil;
import com.allen_sauer.gwt.dnd.client.util.Location;
import com.allen_sauer.gwt.dnd.client.util.LocationWidgetComparator;
import com.allen_sauer.gwt.dnd.client.util.WidgetLocation;
import com.google.gwt.user.client.ui.InsertPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;


/**
 * A class for handling the dropcontroller, inspired by https://gwt-dnd.appspot.com
 * @author chrte707, hento581
 *
 */
public class FlexTableDropController extends AbstractPositioningDropController  {
	@SuppressWarnings("unused")
	private TemperatureWatcher parent;

	private static final String CSS_DEMO_TABLE_POSITIONER = "demo-table-positioner";
	private DnDFlexTable flexTable;
	private InsertPanel flexTableRowsAsIndexPanel = new InsertPanel() {

		@Override
		public void add(Widget w) {
			throw new UnsupportedOperationException();
		}

		@Override
		public Widget getWidget(int index) {
			return flexTable.getWidget(index, 0);
		}

		@Override
		public int getWidgetCount() {
			return flexTable.getRowCount();
		}

		@Override
		public int getWidgetIndex(Widget child) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void insert(Widget w, int beforeIndex) {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean remove(int index) {
			throw new UnsupportedOperationException();
		}
	};

	private Widget positioner = null;
	private int targetRow;
	
	public FlexTableDropController(DnDFlexTable flexTable, TemperatureWatcher parent) {
		super(flexTable);
		this.flexTable = flexTable;
		this.parent=parent;
	}

	@Override
	public void onDrop(DragContext context) {
		FlexTableDragController trDragController = (FlexTableDragController) context.dragController;
		if(targetRow==-1){
			targetRow=0;
		}
		//Moves the city to the correct temperatureList
		String country =  trDragController.getDraggableTable().getText(trDragController.getDragRow(), 0);
		String area =  trDragController.getDraggableTable().getText(trDragController.getDragRow(), 1);
		String city =  trDragController.getDraggableTable().getText(trDragController.getDragRow(), 2);
		Temperature oldTemperature = new Temperature(country, area, city,this.flexTable.getId(),trDragController.getDragRow());
//		Temperature temperature = new Temperature(country, area, city,this.flexTable.getId(),targetRow); //TODO, test target row
//		this.flexTable.addTemperature(temperature);		
//		int index = trDragController.getDraggableTable().findIndexOfCity(city);
//		trDragController.getDraggableTable().removeTemperature(index);
//		
//		
//		//Moves the city to another Flextable
//		FlexTableUtil.moveRow(trDragController.getDraggableTable(), flexTable,
//				trDragController.getDragRow(), targetRow + 1);
		
		//Adding the styles
		this.flexTable.getCellFormatter().addStyleName(targetRow+1, 3, "watchListNumericColumn");
		this.flexTable.getCellFormatter().addStyleName(targetRow+1, 4, "watchListNumericColumn");
		this.flexTable.getCellFormatter().addStyleName(targetRow+1, 5, "watchListRemoveColumn");
		
		this.parent.updateRowInDb(city, targetRow+1, this.flexTable.getListOfTemperatures().get(targetRow).getCity(), oldTemperature.getRow());
		super.onDrop(context);
	}

	@Override
	public void onEnter(DragContext context) {
		super.onEnter(context);
		positioner = newPositioner(context);
	}

	@Override
	public void onLeave(DragContext context) {
		positioner.removeFromParent();
		positioner = null;
		super.onLeave(context);
	}

	@Override
	/**
	 * Functions for handling the onMove actions
	 */
	public void onMove(DragContext context) {
		super.onMove(context);
		targetRow = DOMUtil.findIntersect(flexTableRowsAsIndexPanel, new CoordinateLocation(
				context.mouseX, context.mouseY), LocationWidgetComparator.BOTTOM_HALF_COMPARATOR) - 1;

		if (flexTable.getRowCount() > 0) {
			Widget w = flexTable.getWidget(targetRow == -1 ? 0 : targetRow, 0);
			Location widgetLocation = new WidgetLocation(w, context.boundaryPanel);
			Location tableLocation = new WidgetLocation(flexTable, context.boundaryPanel);
			context.boundaryPanel.add(positioner, tableLocation.getLeft(), widgetLocation.getTop()
					+ (targetRow == -1 ? 0 : w.getOffsetHeight()));
		}
	}

	Widget newPositioner(DragContext context) {
		Widget p = new SimplePanel();
		p.addStyleName(CSS_DEMO_TABLE_POSITIONER);
		p.setPixelSize(flexTable.getOffsetWidth(), 1);
		return p;
	}



}
