package com.google.gwt.sample.stockwatcher.client;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

/**
 * A class for manipulating the flextable
 * @author This class is copied from https://gwt-dnd.appspot.com
 *
 */
public class FlexTableUtil {

  /**
   * Copy an entire FlexTable from one FlexTable to another. Each element is copied by creating a
   * new {@link HTML} widget by calling {@link FlexTable#getHTML(int, int)} on the source table.
   *
   * @param sourceTable the FlexTable to copy a row from
   * @param targetTable the FlexTable to copy a row to
   * @param sourceRow the index of the source row
   * @param targetRow the index before which to insert the copied row
   */
  public static void copyRow(DnDFlexTable sourceTable, DnDFlexTable targetTable, int sourceRow,
      int targetRow) {
    targetTable.insertRow(targetRow);
    for (int col = 0; col < sourceTable.getCellCount(sourceRow); col++) {
      HTML html = new HTML(sourceTable.getHTML(sourceRow, col));
      targetTable.setWidget(targetRow, col, html);
    }
    copyRowStyle(sourceTable, targetTable, sourceRow, targetRow);
  }

  /**
   * Move an entire FlexTable from one FlexTable to another. Elements are moved by attempting to
   * call {@link FlexTable#getWidget(int, int)} on the source table. If no widget is found (because
   * <code>null</code> is returned), a new {@link HTML} is created instead by calling
   * {@link FlexTable#getHTML(int, int)} on the source table.
   *
   * @param sourceTable the FlexTable to move a row from
   * @param targetTable the FlexTable to move a row to
   * @param sourceRow the index of the source row
   * @param targetRow the index before which to insert the moved row
   */
  public static void moveRow(DnDFlexTable sourceTable, DnDFlexTable targetTable, int sourceRow,
      int targetRow) {
    if (sourceTable == targetTable && sourceRow >= targetRow) {
      sourceRow++;
    }
    targetTable.insertRow(targetRow);
    for (int col = 0; col < sourceTable.getCellCount(sourceRow); col++) {
      Widget w = sourceTable.getWidget(sourceRow, col);
      if (w != null) {
        targetTable.setWidget(targetRow, col, w);
      } else {
        HTML html = new HTML(sourceTable.getHTML(sourceRow, col));
        targetTable.setWidget(targetRow, col, html);
      }
    }
    copyRowStyle(sourceTable, targetTable, sourceRow, targetRow);
    sourceTable.removeRow(sourceRow);
  }

  /**
   * Copies the CSS style of a source row to a target row.
   *
   * @param sourceTable
   * @param targetTable
   * @param sourceRow
   * @param targetRow
   */
  private static void copyRowStyle(DnDFlexTable sourceTable, DnDFlexTable targetTable, int sourceRow, int targetRow) {
    String rowStyle = sourceTable.getRowFormatter().getStyleName(sourceRow);
    targetTable.getRowFormatter().setStyleName(targetRow, rowStyle);
  }

}

