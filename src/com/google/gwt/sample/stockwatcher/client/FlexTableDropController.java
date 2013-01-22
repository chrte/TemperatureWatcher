package com.google.gwt.sample.stockwatcher.client;


import com.allen_sauer.gwt.dnd.client.DragContext;
import com.allen_sauer.gwt.dnd.client.drop.AbstractPositioningDropController;
import com.allen_sauer.gwt.dnd.client.util.CoordinateLocation;
import com.allen_sauer.gwt.dnd.client.util.DOMUtil;
import com.allen_sauer.gwt.dnd.client.util.Location;
import com.allen_sauer.gwt.dnd.client.util.LocationWidgetComparator;
import com.allen_sauer.gwt.dnd.client.util.WidgetLocation;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.InsertPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;


public class FlexTableDropController extends AbstractPositioningDropController  {
	TemperatureWatcher parent;


	private static final String CSS_DEMO_TABLE_POSITIONER = "demo-table-positioner";


	  private FlexTable flexTable;

	
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

		  public FlexTableDropController(FlexTable flexTable) {
		    super(flexTable);
		    this.flexTable = flexTable;
		  }

		  @Override
		  public void onDrop(DragContext context) {
		    FlexTableDragController trDragController = (FlexTableDragController) context.dragController;
		    if(targetRow==-1){
		    	targetRow=0;
		    }
		    FlexTableUtil.moveRow(trDragController.getDraggableTable(), flexTable,
		        trDragController.getDragRow(), targetRow + 1);
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
		  public void onMove(DragContext context) {
		    super.onMove(context);
		    targetRow = DOMUtil.findIntersect(flexTableRowsAsIndexPanel, new CoordinateLocation(
		        context.mouseX, context.mouseY), LocationWidgetComparator.BOTTOM_HALF_COMPARATOR) - 1;

		    if (flexTable.getRowCount() > 0) {
		      Widget w = flexTable.getWidget(targetRow == -1 ? 0 : targetRow, 0);  //TODO, cause of the nullpointer, w can be null
		      Location widgetLocation = new WidgetLocation(w, context.boundaryPanel);
		      Location tableLocation = new WidgetLocation(flexTable, context.boundaryPanel);
		      context.boundaryPanel.add(positioner, tableLocation.getLeft(), widgetLocation.getTop()
		          + (targetRow == -1 ? 0 : w.getOffsetHeight()));  //TODO: fix nullpointer exception here
		    }
		  }

		  Widget newPositioner(DragContext context) {
		    Widget p = new SimplePanel();
		    p.addStyleName(CSS_DEMO_TABLE_POSITIONER);
		    p.setPixelSize(flexTable.getOffsetWidth(), 1);
		    return p;
		  }

	
	
}
