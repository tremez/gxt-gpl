/**
 * Sencha GXT 4.0.0 - Sencha for GWT
 * Copyright (c) 2006-2015, Sencha Inc.
 *
 * licensing@sencha.com
 * http://www.sencha.com/products/gxt/license/
 *
 * ================================================================================
 * Open Source License
 * ================================================================================
 * This version of Sencha GXT is licensed under the terms of the Open Source GPL v3
 * license. You may use this license only if you are prepared to distribute and
 * share the source code of your application under the GPL v3 license:
 * http://www.gnu.org/licenses/gpl.html
 *
 * If you are NOT prepared to distribute and share the source code of your
 * application under the GPL v3 license, other commercial and oem licenses
 * are available for an alternate download of Sencha GXT.
 *
 * Please see the Sencha GXT Licensing page at:
 * http://www.sencha.com/products/gxt/license/
 *
 * For clarification or additional options, please contact:
 * licensing@sencha.com
 * ================================================================================
 *
 *
 * ================================================================================
 * Disclaimer
 * ================================================================================
 * THIS SOFTWARE IS DISTRIBUTED "AS-IS" WITHOUT ANY WARRANTIES, CONDITIONS AND
 * REPRESENTATIONS WHETHER EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION THE
 * IMPLIED WARRANTIES AND CONDITIONS OF MERCHANTABILITY, MERCHANTABLE QUALITY,
 * FITNESS FOR A PARTICULAR PURPOSE, DURABILITY, NON-INFRINGEMENT, PERFORMANCE AND
 * THOSE ARISING BY STATUTE OR FROM CUSTOM OR USAGE OF TRADE OR COURSE OF DEALING.
 * ================================================================================
 */
package com.sencha.gxt.widget.core.client.grid;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Stack;
import java.util.logging.Logger;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.CssResource.ImportedWithPrefix;
import com.google.gwt.safecss.shared.SafeStyles;
import com.google.gwt.safecss.shared.SafeStylesBuilder;
import com.google.gwt.safecss.shared.SafeStylesUtils;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.HasHorizontalAlignment.HorizontalAlignmentConstant;
import com.google.gwt.user.client.ui.HasVerticalAlignment.VerticalAlignmentConstant;
import com.sencha.gxt.core.client.GXT;
import com.sencha.gxt.core.client.GXTLogConfiguration;
import com.sencha.gxt.core.client.Style;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.core.client.dom.DomHelper;
import com.sencha.gxt.core.client.dom.XDOM;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.core.client.gestures.ScrollGestureRecognizer;
import com.sencha.gxt.core.client.gestures.ScrollGestureRecognizer.ScrollDirection;
import com.sencha.gxt.core.client.resources.CommonStyles;
import com.sencha.gxt.core.client.util.DelayedTask;
import com.sencha.gxt.core.client.util.Point;
import com.sencha.gxt.core.client.util.Size;
import com.sencha.gxt.core.client.util.Util;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.SortDir;
import com.sencha.gxt.data.shared.SortInfo;
import com.sencha.gxt.data.shared.SortInfoBean;
import com.sencha.gxt.data.shared.Store.Record;
import com.sencha.gxt.data.shared.Store.StoreSortInfo;
import com.sencha.gxt.data.shared.event.StoreAddEvent;
import com.sencha.gxt.data.shared.event.StoreClearEvent;
import com.sencha.gxt.data.shared.event.StoreDataChangeEvent;
import com.sencha.gxt.data.shared.event.StoreFilterEvent;
import com.sencha.gxt.data.shared.event.StoreHandlers;
import com.sencha.gxt.data.shared.event.StoreRecordChangeEvent;
import com.sencha.gxt.data.shared.event.StoreRemoveEvent;
import com.sencha.gxt.data.shared.event.StoreSortEvent;
import com.sencha.gxt.data.shared.event.StoreUpdateEvent;
import com.sencha.gxt.messages.client.DefaultMessages;
import com.sencha.gxt.widget.core.client.ComponentHelper;
import com.sencha.gxt.widget.core.client.event.BodyScrollEvent;
import com.sencha.gxt.widget.core.client.event.CheckChangeEvent;
import com.sencha.gxt.widget.core.client.event.CheckChangeEvent.CheckChangeHandler;
import com.sencha.gxt.widget.core.client.event.ColumnMoveEvent;
import com.sencha.gxt.widget.core.client.event.ColumnWidthChangeEvent;
import com.sencha.gxt.widget.core.client.event.HeaderClickEvent;
import com.sencha.gxt.widget.core.client.event.HeaderClickEvent.HeaderClickHandler;
import com.sencha.gxt.widget.core.client.event.RefreshEvent;
import com.sencha.gxt.widget.core.client.event.SortChangeEvent;
import com.sencha.gxt.widget.core.client.grid.ColumnHeader.HeaderContextMenuFactory;
import com.sencha.gxt.widget.core.client.menu.CheckMenuItem;
import com.sencha.gxt.widget.core.client.menu.Item;
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.menu.MenuItem;

/**
 * This class encapsulates the user interface of an {@link Grid}. Methods of this class may be used to access user
 * interface elements to enable special display effects. Do not change the DOM structure of the user interface. </p>
 * <p />
 * This class does not provide ways to manipulate the underlying data. The data model of a Grid is held in a
 * {@link ListStore}.
 */
public class  GridView<M> {

  /**
   * Define the appearance of a Grid.
   */
  public interface GridAppearance {

    Element findCell(Element elem);

    Element findRow(Element elem);

    Element getRowBody(Element row);

    NodeList<Element> getRows(XElement parent);

    void onCellSelect(Element cell, boolean select);

    void onRowHighlight(Element row, boolean highlight);

    void onRowOver(Element row, boolean over);

    void onRowSelect(Element row, boolean select);

    /**
     * Renders the HTML markup for the widget.
     *
     * @param sb the safe html builder
     */
    void render(SafeHtmlBuilder sb);

    SafeHtml renderEmptyContent(String emptyText);

    GridStyles styles();

  }

  /**
   * Marker classes, used only to indicate state and landmark details in the dom. Not to be used directly to style
   * anything.
   * <p/>
   * Attached to a empty file as no real style should be set. This does not need to be injected, and should not be
   * extended.
   */
  @ImportedWithPrefix("grid")
  public interface GridStateStyles extends CssResource {

    /**
     * Marker class to indicate that the row is selected
     *
     * @see com.sencha.gxt.theme.base.client.grid.CheckBoxColumnDefaultAppearance
     * @see com.sencha.gxt.theme.base.client.grid.GroupingViewDefaultAppearance
     * @see com.sencha.gxt.theme.base.client.grid.RowExpanderDefaultAppearance
     */
    String rowSelected();

    /**
     * Marker class to indicate that the cell is selected
     *
     * @see CellSelectionModel
     */
    String cellSelected();

    /**
     * Marker class for the TD that wraps each cell in the grid.
     */
    String cell();

    /**
     * Marker class for the DIV that each cell is rendered into
     */
    String cellInner();

    /**
     * Marker class for each row in the grid
     */
    String row();

    /**
     * Optional marker class for the DIV that surrounds each row's own individual TABLE.
     *
     * @see GridView#setEnableRowBody(boolean)
     */
    String rowWrap();

    /**
     * Optional marker class for the TR that holds the extra row body.
     *
     * @see GridView#setEnableRowBody(boolean)
     */
    String rowBodyRow();

    /**
     * Optional marker class for the DIV that holds the extra row body.
     *
     * @see GridView#setEnableRowBody(boolean)
     */
    String rowBody();

  }
  interface StateBundle extends ClientBundle {
    @Source("GridStateStyles.gss")
    GridStateStyles styles();
  }

  public interface GridStyles extends CssResource {

    String cell();

    String cellDirty();

    String cellInner();

    String noPadding();

    String columnLines();

    String dataTable();

    String headerRow();

    String row();

    String rowAlt();

    String rowBody();

    String rowDirty();

    String rowHighlight();

    String rowOver();

    String rowWrap();

    String empty();

    String footer();

    String grid();
  }

  public interface GridTemplates extends SafeHtmlTemplates {
    @Template("<table cellpadding=\"0\" cellspacing=\"0\" class=\"{0}\" style=\"{1};table-layout: fixed\"><tbody>{3}</tbody><tbody>{2}</tbody></table>")
    SafeHtml table(String classes, SafeStyles tableStyles, SafeHtml contents, SafeHtml sizingHeads);

    @Template("<td cellindex=\"{0}\" class=\"{1}\" style=\"{2}\" tabindex=\"-1\"><div class=\"{3}\" style=\"{4}\">{5}</div></td>")
    SafeHtml td(int cellIndex, String cellClasses, SafeStyles cellStyles, String textClasses, SafeStyles textStyles,
        SafeHtml contents);

    @Template("<td cellindex=\"{0}\" class=\"{1}\" style=\"{2}\" rowspan=\"{3}\"><div class=\"{4}\">{5}</div></td>")
    SafeHtml tdRowSpan(int cellIndex, String classes, SafeStyles styles, int rowSpan, String cellInnerClasses,
        SafeHtml contents);

    @Template("<td cellindex=\"{0}\" class=\"{1}\" style=\"{2}\" tabindex=\"-1\"><div class=\"{3}\" style=\"{4}\" unselectable=\"on\">{5}</div></td>")
    SafeHtml tdUnselectable(int cellIndex, String cellClasses, SafeStyles cellStyles, String textClasses,
        SafeStyles textStyles, SafeHtml contents);

    @Template("<td colspan=\"{0}\" class=\"{1}\"><div class=\"{2}\">{3}</div></td>")
    SafeHtml tdWrap(int colspan, String cellClasses, String textClasses, SafeHtml content);

    @Template("<td colspan=\"{0}\" class=\"{1}\"><div class=\"{2}\" unselectable=\"on\">{3}</div></td>")
    SafeHtml tdWrapUnselectable(int colspan, String cellClasses, String textClasses, SafeHtml content);

    @Template("<th class=\"{0}\" style=\"{1}\"></th>")
    SafeHtml th(String classes, SafeStyles cellStyles);

    @Template("<tr class=\"{0}\">{1}</tr>")
    SafeHtml tr(String classes, SafeHtml contents);
  }

  private static Logger logger = Logger.getLogger(GridView.class.getName());

  /**
   * Set to true to auto expand the columns to fit the grid when the grid is created.
   */
  protected boolean autoFill;

  /**
   * The content area inside the scroller.
   */
  protected XElement body;

  /**
   * A border width calculation to be applied for browsers that do not use the old IE box model.
   */
  protected int borderWidth = 2;

  /**
   * The grid's column model.
   */
  protected ColumnModel<M> cm;

  /**
   * The handler for column events such as move, width change and hide.
   */
  protected ColumnModelHandlers columnListener;

  /**
   * The main data table and body.
   */
  protected XElement dataTable, dataTableBody, dataTableSizingHead;

  protected int deferUpdateDelay = 500;

  protected boolean deferUpdates = false;

  /**
   * The list store that provides data for this grid view.
   */
  protected ListStore<M> ds;

  /**
   * The value to display when the grid is empty (defaults to empty string).
   */
  protected String emptyText = "";

  /**
   * True to enable a column spanning row body, as used by {@link RowExpander} (defaults to false).
   */
  protected boolean enableRowBody = false;

  protected ColumnFooter<M> footer;
  protected boolean forceFit;
  protected Grid<M> grid;
  protected ColumnHeader<M> header;
  protected int headerColumnIndex;
  protected boolean headerDisabled;
  /**
   * The inner head element.
   */
  protected XElement headerElem;
  protected int lastViewWidth;
  protected StoreHandlers<M> listener;
  protected Element overRow;
  protected boolean preventScrollToTopOnRefresh;
  /**
   * The scrollable area that contains the main body.
   */
  protected XElement scroller;
  protected int scrollOffset = XDOM.getScrollBarWidth();
  protected boolean selectable = false;

  protected SortInfo sortState;
  protected int splitterWidth = 5;
  protected final GridStateStyles states = GWT.<StateBundle> create(StateBundle.class).styles();
  protected StoreSortInfo<M> storeSortInfo;
  protected GridStyles styles;
  protected GridTemplates tpls = GWT.create(GridTemplates.class);
  protected String unselectable = CommonStyles.get().unselectableSingle();

  // we first render grid with a vbar, and remove as needed
  protected boolean vbar = true;
  protected GridViewConfig<M> viewConfig;
  private DelayedTask addTask = new DelayedTask() {
    @Override
    public void onExecute() {
      calculateVBar(false);
      refreshFooterData();
    }
  };
  private boolean adjustForHScroll = true;
  private GridAppearance appearance;
  private ColumnConfig<M, ?> autoExpandColumn;
  private int autoExpandMax = 500;

  private int autoExpandMin = 25;
  private HandlerRegistration cmHandlerRegistration;
  /**
   * True to show vertical column lines between cells.
   */
  private boolean columnLines;

  private Element focusedCell;

  private DelayedTask removeTask = new DelayedTask() {

    @Override
    public void onExecute() {
      calculateVBar(false);
      applyEmptyText();
      refreshFooterData();
      processRows(0, false);
    }

  };

  /**
   * The numbers of rows the first column should span if row bodies are enabled (defaults to 1).
   */
  private int rowBodyRowSpan = 1;

  private boolean showDirtyCells = true;
  private HandlerRegistration storeHandlerRegistration;
  private boolean stripeRows;

  private boolean trackMouseOver = true;
  private SimpleEventBus eventBus;
  private boolean removeInProgress = false;

  /**
   * Creates a new grid view.
   */
  public GridView() {
    this(GWT.<GridAppearance> create(GridAppearance.class));
  }

  /**
   * Creates a new grid view.
   *
   * @param appearance the grid appearance
   */
  public GridView(GridAppearance appearance) {
    this.appearance = appearance;

    this.styles = appearance.styles();
  }

  /**
   * Adds a handler to receive specific events from this object. Only subclasses typically should need to call this
   * directly.
   *
   * @param type the type of event to listen for
   * @param handler the handler to call when the event occurs
   * @param <H> the handler type
   * @return the handler registration, enabling the handler to be removed when not needed
   */
  public <H extends EventHandler> HandlerRegistration addHandler(GwtEvent.Type<H> type, H handler) {
    return ensureHandlers().addHandlerToSource(type, this, handler);
  }

  /**
   * Ensures the given model (row) is visible.
   *
   * @param model the target model
   * @return the calculated point
   */
  public Point ensureVisible(M model) {
    Element row = getRow(model);
    if (row == null) {
      return null;
    }
    int rowIndex = findRowIndex(row);
    return ensureVisible(rowIndex, 0, false);
  }

  /**
   * Ensured the current row and column is visible.
   * This can be used to scroll to a row.
   *
   * @param row the row index
   * @param col the column index
   * @param hscroll true to scroll horizontally if needed
   * @return the calculated point
   */
  public Point ensureVisible(int row, int col, boolean hscroll) {
    if (grid == null || !grid.isViewReady() || row < 0 || row > ds.size()) {
      return null;
    }

    if (col == -1) {
      col = 0;
    }
    focusCell(row, col, hscroll);
    return null;
  }

  /**
   * Returns the cell.
   *
   * @param elem the cell element or a child element
   * @return the cell element
   */
  public Element findCell(Element elem) {
    if (elem == null) {
      return null;
    }
    return appearance.findCell(elem);
  }

  /**
   * Returns the cell index.
   *
   * @param elem the cell or child element
   * @param requiredStyle an optional required style name
   * @return the cell index or -1 if not found
   */
  public int findCellIndex(Element elem, String requiredStyle) {
    Element cell = findCell(elem);
    if (cell != null && (requiredStyle == null || elem.hasClassName(requiredStyle))) {
      String index = cell.getAttribute("cellindex");
      return index.equals("") ? -1 : Integer.parseInt(index);
    }
    return -1;
  }

  /**
   * Returns the row element.
   *
   * @param elem the row element or any child element
   * @return the matching row element
   */
  public Element findRow(Element elem) {
    if (elem == null) {
      return null;
    }
    return appearance.findRow(elem);
  }

  /**
   * Returns the row index.
   *
   * @param elem the row or child of the row element
   * @return the index
   */
  public int findRowIndex(Element elem) {
    Element r = findRow(elem);
    if (r != null) {
      return r.getPropertyInt("rowindex");
    }
    return -1;
  }

  /**
   * Fires the given event from this object as its source.
   *
   * @param event the event to fire
   */
  public void fireEvent(GwtEvent<?> event) {
    if (eventBus != null) {
      eventBus.fireEventFromSource(event, this);
    }
  }

  /**
   * Focuses the grid.
   */
  public void focus() {
    if (focusedCell != null) {
      grid.getElement().removeAttribute("tabindex");
      focusedCell.addClassName("x-grid-item-focused");
      focusedCell.setTabIndex(0);
      if (XDOM.getActiveElement() != null && !(focusedCell.isOrHasChild(XDOM.getActiveElement()))) {
        // child already has focus, don't focus cell
        focusedCell.focus();
      }
    } else {
      grid.getElement().setTabIndex(0);
    }
  }

  /**
   * Focus the cell and scrolls into view.
   *
   * @param rowIndex the row index
   * @param colIndex the column index
   * @param hscroll true to scroll horizontally
   */
  public void focusCell(int rowIndex, int colIndex, boolean hscroll) {
    grid.getElement().removeAttribute("tabindex");

    if (focusedCell != null) {
      focusedCell.removeClassName("x-grid-item-focused");
      focusedCell.setTabIndex(-1);
      focusedCell = null;
    }

    Element cell = getCell(rowIndex, colIndex);
    if (cell != null) {
      cell.addClassName("x-grid-item-focused");
      focusedCell = cell;
      focusedCell.setTabIndex(0);
      if (XDOM.getActiveElement() != null && !(focusedCell.isOrHasChild(XDOM.getActiveElement()))) {
        // child already has focus, don't focus cell
        focusedCell.focus();
      }
    }  else {
      grid.getElement().setTabIndex(0);
    }
  }

  /**
   * Focus the row and scrolls into view.
   *
   * @param rowIndex the row index
   */
  public void focusRow(int rowIndex) {
    focusCell(rowIndex, 0, false);
  }

  /**
   * Returns the grid appearance.
   *
   * @return the grid appearance
   */
  public GridAppearance getAppearance() {
    return appearance;
  }

  /**
   * Returns the auto expand column id.
   *
   * @return the auto expand column id
   */
  public ColumnConfig<M, ?> getAutoExpandColumn() {
    return autoExpandColumn;
  }

  /**
   * Returns the auto expand maximum width.
   *
   * @return the max width in pixels
   */
  public int getAutoExpandMax() {
    return autoExpandMax;
  }

  /**
   * Returns the auto expand minimum width.
   *
   * @return the minimum width in pixels
   */
  public int getAutoExpandMin() {
    return autoExpandMin;
  }

  /**
   * Returns the body element.
   *
   * @return the body element
   */
  public XElement getBody() {
    return body;
  }

  /**
   * Returns the grid's &lt;TD> HtmlElement at the specified coordinates.
   *
   * @param row the row index in which to find the cell
   * @param col the column index of the cell
   * @return the &lt;TD> at the specified coordinates
   */
  public Element getCell(int row, int col) {
    if (removeInProgress) {
      // event is occurring on next row
      // due to possible blur event during row removal
      row++;
    }
    Element rowEl = getRow(row);
    if (rowEl == null || !rowEl.hasChildNodes() || col < 0) {
      return null;
    } else if (!enableRowBody) {
      return (Element) rowEl.getChildNodes().getItem(col);
    } else {
      return (Element) rowEl.getFirstChildElement().getFirstChildElement().getFirstChildElement().getFirstChildElement().getNextSiblingElement().getFirstChildElement().getChild(
          col);
    }
  }

  /**
   * Returns the editor parent element.
   *
   * @return the editor element
   */
  public Element getEditorParent() {
    return scroller;
  }

  /**
   * Returns the empty text.
   *
   * @return the empty text
   */
  public String getEmptyText() {
    return emptyText;
  }

  /**
   * Returns the grid's column header.
   *
   * @return the header
   */
  public ColumnHeader<M> getHeader() {
    if (header == null) {
      initHeader();
    }
    return header;
  }

  /**
   * Return the &lt;TR> HtmlElement which represents a Grid row for the specified index.
   *
   * @param row the row index
   * @return the &lt;TR> element
   */
  public Element getRow(int row) {
    if (row < 0) {
      return null;
    }
    return getRows().getItem(row);
  }

  /**
   * Return the &lt;TR> HtmlElement which represents a Grid row for the specified model.
   *
   * @param m the model
   * @return the &lt;TR> element
   */
  public Element getRow(M m) {
    return getRow(ds.indexOf(m));
  }

  public Element getRowBody(Element row) {
    return appearance.getRowBody(row);
  }

  /**
   * Returns the number of rows the first column should span when row bodies have been enabled.
   *
   * @return the rowspan
   */
  public int getRowBodyRowSpan() {
    return rowBodyRowSpan;
  }

  /**
   * Returns the scroll element.
   *
   * @return the scroll element
   */
  public XElement getScroller() {
    return scroller;
  }

  /**
   * Returns the current scroll state.
   *
   * @return the scroll state
   */
  public Point getScrollState() {
    return new Point(scroller.getScrollLeft(), scroller.getScrollTop());
  }

  /**
   * Returns the grid's sort information.
   *
   * @return the grid's sort information (or null if the grid is not sorted).
   */
  public StoreSortInfo<M> getSortState() {
    if (ds.getSortInfo().size() > 0) {
      return ds.getSortInfo().get(0);
    }
    return null;
  }

  /**
   * @return The CssResource instance used to denote structural or stateful details about the grid.
   */
  public GridStateStyles getStateStyles() {
    return states;
  }

  /**
   * Returns the view config.
   *
   * @return the view config
   */
  public GridViewConfig<M> getViewConfig() {
    return viewConfig;
  }

  /**
   * Returns true if the grid width will be adjusted based on visibility of horizontal scroll bar.
   *
   * @return true if adjusting
   */
  public boolean isAdjustForHScroll() {
    return adjustForHScroll;
  }

  /**
   * Returns true if auto fill is enabled.
   *
   * @return true for auto fill
   */
  public boolean isAutoFill() {
    return autoFill;
  }

  /**
   * Returns true if column lines are enabled.
   *
   * @return true if column lines are enabled
   */
  public boolean isColumnLines() {
    return columnLines;
  }

  /**
   * Returns true if rows are updated deferred on updates.
   *
   * @return true if updates deferred
   */
  public boolean isDeferUpdates() {
    return deferUpdates;
  }

  /**
   * Returns true if row bodies are enabled.
   *
   * @return true for row bodies
   */
  public boolean isEnableRowBody() {
    return enableRowBody;
  }

  /**
   * Returns true if force fit is enabled.
   *
   * @return true for force fit
   */
  public boolean isForceFit() {
    return forceFit;
  }

  /**
   * Returns true if the given element is selectable.
   *
   * @param target the element to check
   * @return true if the given element is selectable
   */
  public boolean isSelectableTarget(Element target) {
    if (target == null) {
      return false;
    }

    String tag = target.getTagName();
    if ("input".equalsIgnoreCase(tag) || "textarea".equalsIgnoreCase(tag)) {
      return false;
    }

    int colIndex = findCellIndex(target, null);
    Element cellParent = getCell(findRowIndex(target), colIndex);

    com.google.gwt.cell.client.Cell<?> cell = grid.getColumnModel().getCell(colIndex);
    if (cell != null && cellParent != null && cellParent.isOrHasChild(target) && cell.handlesSelection()) {
      return false;
    }
    return true;
  }

  /**
   * Returns true if dirty cell markers are enabled.
   *
   * @return true of dirty cell markers
   */
  public boolean isShowDirtyCells() {
    return showDirtyCells;
  }

  /**
   * Returns true if sorting is enabled.
   *
   * @return true for sorting
   */
  public boolean isSortingEnabled() {
    return !headerDisabled;
  }

  /**
   * Returns true if row striping is enabled.
   *
   * @return the strip row state
   */
  public boolean isStripeRows() {
    return stripeRows;
  }

  /**
   * Returns true if rows are highlighted on mouse over.
   *
   * @return the track mouse state
   */
  public boolean isTrackMouseOver() {
    return trackMouseOver;
  }

  /**
   * Lays out the grid view, adjusting the header and footer width and accounting for force fit and auto fill settings.
   */
  public void layout() {
    layout(false);
  }

  /**
   * Rebuilds the grid using its current configuration and data.
   *
   * @param headerToo true to refresh the header
   */
  public void refresh(boolean headerToo) {
    if (grid != null && grid.isViewReady()) {

      if (!preventScrollToTopOnRefresh) {
        scrollToTop(headerToo);
      }

      if (GXT.isIE()) {
        dataTableBody.removeChildren();
        dataTableSizingHead.removeChildren();
      } else {
        dataTableBody.setInnerSafeHtml(SafeHtmlUtils.EMPTY_SAFE_HTML);
        dataTableSizingHead.setInnerSafeHtml(SafeHtmlUtils.EMPTY_SAFE_HTML);
      }

      DomHelper.insertHtml("afterBegin", dataTableSizingHead, renderHiddenHeaders(getColumnWidths()));
      DomHelper.insertHtml("afterBegin", dataTableBody, renderRows(0, -1));

      dataTable.getStyle().setWidth(getTotalWidth(), Unit.PX);

      if (headerToo) {
        sortState = null;

        header.refresh();

        header.setEnableColumnResizing(grid.isColumnResize());
        header.setEnableColumnReorder(grid.isColumnReordering());
      }

      processRows(0, true);

      if (footer != null) {
        ComponentHelper.doDetach(footer);
        footer.getElement().removeFromParent();
      }
      if (cm.getAggregationRows().size() > 0) {
        footer = new ColumnFooter<M>(grid, cm);
        renderFooter();
        if (grid.isAttached()) {
          ComponentHelper.doAttach(footer);
        }
      }

      calculateVBar(true);

      updateHeaderSortState();

      applyEmptyText();

      grid.getElement().repaint();

      grid.fireEvent(new RefreshEvent());
    }
  }

  /**
   * Invoked after the view has been rendered, may be overridden to perform any
   * activities that require a rendered view.
   */
  protected void onAfterRenderView() {
  }

  /**
   * Scrolls the grid to the top.
   */
  public void scrollToTop() {
    scrollToTop(true);
  }

  /**
   * Scrolls the grid the top.
   *
   * @param resetHorizontal true to reset horizontal
   */
  public void scrollToTop(boolean resetHorizontal) {
    scroller.setScrollTop(0);
    if (resetHorizontal) {
      scroller.setScrollLeft(0);
    }
  }

  /**
   * True to adjust the grid width when the horizontal scrollbar is hidden and visible (defaults to true).
   *
   * @param adjustForHScroll true to adjust for horizontal scroll bar
   */
  public void setAdjustForHScroll(boolean adjustForHScroll) {
    this.adjustForHScroll = adjustForHScroll;
  }

  /**
   * The id of a column in this grid that should expand to fill unused space (pre-render). This id can not be 0.
   *
   * @param autoExpandColumn the auto expand column
   */
  public void setAutoExpandColumn(ColumnConfig<M, ?> autoExpandColumn) {
    this.autoExpandColumn = autoExpandColumn;
  }

  /**
   * The maximum width the autoExpandColumn can have (if enabled) (defaults to 500, pre-render).
   *
   * @param autoExpandMax the auto expand max
   */
  public void setAutoExpandMax(int autoExpandMax) {
    this.autoExpandMax = autoExpandMax;
  }

  /**
   * The minimum width the autoExpandColumn can have (if enabled)(pre-render).
   *
   * @param autoExpandMin the auto expand min width
   */
  public void setAutoExpandMin(int autoExpandMin) {
    this.autoExpandMin = autoExpandMin;
  }

  /**
   * True to auto expand the columns to fit the grid <b>when the grid is created</b>.
   *
   * @param autoFill true to expand
   */
  public void setAutoFill(boolean autoFill) {
    this.autoFill = autoFill;
  }

  /**
   * True to enable column separation lines (defaults to false).
   *
   * @param columnLines true to enable column separation lines
   */
  public void setColumnLines(boolean columnLines) {
    this.columnLines = columnLines;
  }

  /**
   * True to update rows deferred (defaults to false).
   *
   * @param deferUpdates true to update deferred
   */
  public void setDeferUpdates(boolean deferUpdates) {
    this.deferUpdates = deferUpdates;
  }

  /**
   * Default text to display in the grid body when no rows are available (defaults to '').
   *
   * @param emptyText the empty text
   */
  public void setEmptyText(String emptyText) {
    this.emptyText = emptyText;
  }

  /**
   * True to enable a column spanning row body, as used by {@link RowExpander} (defaults to false).
   *
   * @param enableRowBody true to enable row bodies
   */
  public void setEnableRowBody(boolean enableRowBody) {
    this.enableRowBody = enableRowBody;
  }

  /**
   * True to auto expand/contract the size of the columns to fit the grid width and prevent horizontal scrolling
   * (defaults to false).
   *
   * @param forceFit true to force fit
   */
  public void setForceFit(boolean forceFit) {
    this.forceFit = forceFit;
    if (forceFit) {
      lastViewWidth = -1;
    }
  }

  /**
   * Sets the rowspan the first column should span when row bodies have been enabled (defaults to 1).
   *
   * @param rowBodyRowSpan the rowspan
   */
  public void setRowBodyRowSpan(int rowBodyRowSpan) {
    this.rowBodyRowSpan = rowBodyRowSpan;
  }

  /**
   * True to display a red triangle in the upper left corner of any cells which are "dirty" as defined by any existing
   * records in the data store (defaults to true).
   *
   * @param showDirtyCells true to display the dirty flag
   */
  public void setShowDirtyCells(boolean showDirtyCells) {
    this.showDirtyCells = showDirtyCells;
  }

  /**
   * True to allow column sorting when the user clicks a column (defaults to true).
   *
   * @param sortable true for sortable columns
   */
  public void setSortingEnabled(boolean sortable) {
    this.headerDisabled = !sortable;
  }

  /**
   * True to stripe the rows (defaults to false).
   *
   * @param stripeRows true to strip rows
   */
  public void setStripeRows(boolean stripeRows) {
    this.stripeRows = stripeRows;
  }

  /**
   * True to highlight rows when the mouse is over (defaults to true).
   *
   * @param trackMouseOver true to highlight rows on mouse over
   */
  public void setTrackMouseOver(boolean trackMouseOver) {
    this.trackMouseOver = trackMouseOver;
  }

  /**
   * Sets the view config.
   *
   * @param viewConfig the view config
   */
  public void setViewConfig(GridViewConfig<M> viewConfig) {
    this.viewConfig = viewConfig;
  }

  protected void adjustColumnWidths(int[] columnWidths) {
    int clen = cm.getColumnCount();

    NodeList<Element> tables = scroller.select("." + appearance.styles().dataTable());

    for (int t = 0, len = tables.getLength(); t < len; t++) {
      XElement table = tables.getItem(t).cast();

      table.getStyle().setWidth(getTotalWidth(), Unit.PX);

      NodeList<Element> ths = getTableHeads(table);
      if (ths == null) {
        continue;
      }

      for (int i = 0; i < ths.getLength(); i++) {
        ths.getItem(i).getStyle().setPropertyPx("width", cm.isHidden(i) ? 0 : columnWidths[i]);
      }
    }

    for (int i = 0; i < clen; i++) {
      header.updateColumnWidth(i, columnWidths[i]);
      if (footer != null) {
        footer.updateColumnWidth(i, columnWidths[i]);
      }
    }

    header.adjustColumnWidths(columnWidths);

    // safari cell widths incorrect
    if (GXT.isSafari()) {
      repaintGrid();
    }
  }

  /**
   * Invoked after the view element is first attached, performs steps that require that the view element is attached.
   */
  protected void afterRender() {
    DomHelper.insertHtml("afterBegin", dataTableBody, renderRows(0, -1));

    dataTable.getStyle().setWidth(getTotalWidth(), Unit.PX);

    processRows(0, true);

    // overflow: hidden not working on render
    // alignment issues with some browsers
    if (GXT.isSafari() || GXT.isChrome() || GXT.isIE()) {
      repaintGrid();
    }

    if (footer != null && grid.getLazyRowRender() > 0) {
      footer.refresh();
    }

    int sh = scroller.getComputedHeight();
    int dh = body.getComputedHeight();
    boolean vbar = dh < sh;
    if (vbar) {
      this.vbar = !vbar;
      lastViewWidth = -1;
      layout();
    }

    applyEmptyText();
  }

  /**
   * Applies the empty text, then displaying it if the grid is empty.
   */
  protected void applyEmptyText() {
    if (!hasRows()) {
      if (GXT.isIE()) {
        dataTableBody.removeChildren();
      } else {
        dataTableBody.setInnerSafeHtml(SafeHtmlUtils.EMPTY_SAFE_HTML);
      }

      SafeHtml con = appearance.renderEmptyContent(emptyText);
      con = tpls.tr("", tpls.tdWrap(cm.getColumnCount(), "", styles.empty(), con));
      DomHelper.append(dataTableBody, con);
    }
  }

  /**
   * Expands the column that was specified (via {@link #setAutoExpandColumn}) as the column in this grid that should
   * expand to fill unused space.
   *
   * @param preventUpdate true to update the column model width without updating the displayed width.
   */
  protected void autoExpand(boolean preventUpdate) {
    if (!cm.isUserResized() && getAutoExpandColumn() != null) {
      int tw = cm.getTotalWidth(false);
      int aw = grid.getOffsetWidth(true) - getScrollAdjust();
      if (tw != aw) {
        int ci = cm.indexOf(getAutoExpandColumn());
        assert ci != Style.DEFAULT : "auto expand column not found";
        if (cm.isHidden(ci)) {
          return;
        }
        int currentWidth = cm.getColumnWidth(ci);
        int cw = Math.min(Math.max(((aw - tw) + currentWidth), getAutoExpandMin()), getAutoExpandMax());
        if (cw != currentWidth) {
          cm.setColumnWidth(ci, cw, true);

          if (!preventUpdate) {
            updateColumnWidth(ci, cw);
          }
        }
      }
    }
  }

  /**
   * Determines whether the need for a vertical scroll bar has changed and if so updates the display.
   *
   * @param force true to force the display to update regardless of whether a change has occurred.
   */
  protected void calculateVBar(boolean force) {
    if (force) {
      resize();
    }
    int sh = scroller.getComputedHeight();
    int dh = body.getComputedHeight();
    boolean vbar = dh > sh;
    if (force || this.vbar != vbar) {
      this.vbar = vbar;
      lastViewWidth = -1;
      layout(true);
    }
  }

  /**
   * Creates a context menu for the given column, including sort menu items and column visibility sub-menu.
   *
   * @param colIndex the column index
   * @return the context menu for the given column
   */
  protected Menu createContextMenu(final int colIndex) {
    final Menu menu = new Menu();

    if (cm.isSortable(colIndex)) {
      MenuItem item = new MenuItem();
      item.setText(DefaultMessages.getMessages().gridView_sortAscText());
      item.setIcon(header.getAppearance().sortAscendingIcon());
      item.addSelectionHandler(new SelectionHandler<Item>() {
        @Override
        public void onSelection(SelectionEvent<Item> event) {
          doSort(colIndex, SortDir.ASC);
        }
      });
      menu.add(item);

      item = new MenuItem();
      item.setText(DefaultMessages.getMessages().gridView_sortDescText());
      item.setIcon(header.getAppearance().sortDescendingIcon());
      item.addSelectionHandler(new SelectionHandler<Item>() {
        @Override
        public void onSelection(SelectionEvent<Item> event) {
          doSort(colIndex, SortDir.DESC);
        }
      });
      menu.add(item);
    }

    MenuItem columns = new MenuItem();
    columns.setText(DefaultMessages.getMessages().gridView_columnsText());
    columns.setIcon(header.getAppearance().columnsIcon());
    columns.setData("gxt-columns", "true");

    final Menu columnMenu = new Menu();

    int cols = cm.getColumnCount();
    for (int i = 0; i < cols; i++) {
      ColumnConfig<M, ?> config = cm.getColumn(i);
      // ignore columns that can't be hidden
      if (!config.isHideable()) {
        continue;
      }
      final int fcol = i;
      final CheckMenuItem check = new CheckMenuItem();
      check.setHideOnClick(false);
      check.setHTML(cm.getColumnHeader(i));
      check.setChecked(!cm.isHidden(i));
      check.setData("gxt-column-index", i);
      check.addCheckChangeHandler(new CheckChangeHandler<CheckMenuItem>() {
        @Override
        public void onCheckChange(CheckChangeEvent<CheckMenuItem> event) {
          cm.setHidden(fcol, !cm.isHidden(fcol));
          restrictMenu(cm, columnMenu);
        }
      });
      columnMenu.add(check);
    }

    restrictMenu(cm, columnMenu);
    columns.setEnabled(columnMenu.getWidgetCount() > 0);
    columns.setSubMenu(columnMenu);
    menu.add(columns);
    return menu;
  }

  /**
   * Helper method that creates a StoreSortInfo from the given ColumnConfig and sort direction. This will use the
   * provided {@link Comparator}, if any, otherwise will fall back to assuming that the data in the column is
   * {@link Comparable}.
   *
   * @param column the column config
   * @param sortDir the sort direction
   * @return the new store sort info instance
   */
  protected <V> StoreSortInfo<M> createStoreSortInfo(ColumnConfig<M, V> column, SortDir sortDir) {
    if (column.getComparator() == null) {
      // These casts can fail, but in dev mode the exception will be caught by
      // the try/catch in doSort, unless there are no items in the Store
      @SuppressWarnings({"unchecked", "rawtypes"})
      ValueProvider<M, Comparable> vp = (ValueProvider) column.getValueProvider();
      @SuppressWarnings("unchecked")
      StoreSortInfo<M> s = new StoreSortInfo<M>(ds.wrapRecordValueProvider(vp), sortDir);
      return s;
    } else {
      return new StoreSortInfo<M>(ds.wrapRecordValueProvider(column.getValueProvider()), column.getComparator(),
          sortDir);
    }
  }

  /**
   * Attaches ancillary widgets such as the header and footer to the grid.
   */
  protected void doAttach() {
    ComponentHelper.doAttach(header);
    ComponentHelper.doAttach(footer);
  }

  /**
   * Detaches ancillary widgets such as the header and footer from the grid.
   */
  protected void doDetach() {
    ComponentHelper.doDetach(header);
    ComponentHelper.doDetach(footer);
  }

  /**
   * Renders the grid view into safe HTML.
   *
   * @param cs the column attributes required for rendering
   * @param rows the data models for the rows to be rendered
   * @param startRow the index of the first row in <code>rows</code>
   */
  protected SafeHtml doRender(List<ColumnData> cs, List<M> rows, int startRow) {
    final int colCount = cm.getColumnCount();
    final int last = colCount - 1;

    int[] columnWidths = getColumnWidths();

    // root builder
    SafeHtmlBuilder buf = new SafeHtmlBuilder();

    final SafeStyles rowStyles = SafeStylesUtils.fromTrustedString("width: " + getTotalWidth() + "px;");

    final String unselectableClass = unselectable;
    final String rowAltClass = styles.rowAlt();
    final String rowDirtyClass = styles.rowDirty();

    final String cellClass = styles.cell() + " " + states.cell();
    final String cellInnerClass = styles.cellInner() + " " + states.cellInner();
    final String cellFirstClass = "x-grid-cell-first";
    final String cellLastClass = "x-grid-cell-last";
    final String cellDirty = styles.cellDirty();

    final String rowWrap = styles.rowWrap() + " " + states.rowWrap();
    final String rowBody = styles.rowBody() + " " + states.rowBody();
    final String rowBodyRow = states.rowBodyRow();

    // loop over all rows
    for (int j = 0; j < rows.size(); j++) {
      M model = rows.get(j);

      ListStore<M>.Record r = ds.hasRecord(model) ? ds.getRecord(model) : null;

      int rowBodyColSpanCount = colCount;
      if (enableRowBody) {
        for (ColumnConfig<M, ?> c : cm.getColumns()) {
          if (c instanceof RowExpander) {
            rowBodyColSpanCount--;
          }
        }
      }

      int rowIndex = (j + startRow);

      String rowClasses = styles.row() + " " + states.row();

      if (!selectable) {
        rowClasses += " " + unselectableClass;
      }
      if (isStripeRows() && ((rowIndex + 1) % 2 == 0)) {
        rowClasses += " " + rowAltClass;
      }

      if (showDirtyCells && r != null && r.isDirty()) {
        rowClasses += " " + rowDirtyClass;
      }

      if (viewConfig != null) {
        rowClasses += " " + viewConfig.getRowStyle(model, rowIndex);
      }

      SafeHtmlBuilder trBuilder = new SafeHtmlBuilder();

      // loop each cell per row
      for (int i = 0; i < colCount; i++) {
        SafeHtml rv = getRenderedValue(rowIndex, i, model, r);
        ColumnConfig<M, ?> columnConfig = cm.getColumn(i);
        ColumnData columnData = cs.get(i);

        String cellClasses = cellClass;
        if (i == 0) {
          cellClasses += " " + cellFirstClass;
        } else if (i == last) {
          cellClasses += " " + cellLastClass;
        }

        String cellInnerClasses = cellInnerClass;
        if (columnConfig.getColumnTextClassName() != null) {
          cellInnerClasses += " " + columnConfig.getColumnTextClassName();
        }
        if (!columnConfig.isCellPadding()) {
          cellInnerClasses += " " + styles.noPadding();
        }

        if (columnData.getClassNames() != null) {
          cellClasses += " " + columnData.getClassNames();
        }

        if (columnConfig.getCellClassName() != null) {
          cellClasses += " " + columnConfig.getCellClassName();
        }

        if (showDirtyCells && r != null && r.getChange(columnConfig.getValueProvider()) != null) {
          cellClasses += " " + cellDirty;
        }

        if (viewConfig != null) {
          cellClasses += " " + viewConfig.getColStyle(model, cm.getValueProvider(i), rowIndex, i);
        }

        final SafeStyles cellStyles = columnData.getStyles();

        final SafeHtml tdContent;
        if (enableRowBody && i == 0) {
          tdContent = tpls.tdRowSpan(i, cellClasses, cellStyles, rowBodyRowSpan, cellInnerClasses, rv);
        } else {
          if (!selectable && GXT.isIE()) {
            tdContent = tpls.tdUnselectable(i, cellClasses, cellStyles, cellInnerClasses,
                columnConfig.getColumnTextStyle(), rv);
          } else {
            tdContent = tpls.td(i, cellClasses, cellStyles, cellInnerClasses, columnConfig.getColumnTextStyle(), rv);
          }

        }
        trBuilder.append(tdContent);
      }

      if (enableRowBody) {
        String cls = styles.dataTable() + " x-grid-resizer";

        SafeHtmlBuilder sb = new SafeHtmlBuilder();
        sb.append(tpls.tr("", trBuilder.toSafeHtml()));
        sb.appendHtmlConstant("<tr class='" + rowBodyRow + "'><td colspan=" + rowBodyColSpanCount + "><div class='"
            + rowBody + "'></div></td></tr>");

        SafeHtml tdWrap = null;
        if (!selectable && GXT.isIE()) {
          tdWrap = tpls.tdWrapUnselectable(colCount, "", rowWrap,
              tpls.table(cls, rowStyles, sb.toSafeHtml(), renderHiddenHeaders(columnWidths)));
        } else {
          tdWrap = tpls.tdWrap(colCount, "", rowWrap,
              tpls.table(cls, rowStyles, sb.toSafeHtml(), renderHiddenHeaders(columnWidths)));
        }
        buf.append(tpls.tr(rowClasses, tdWrap));

      } else {
        buf.append(tpls.tr(rowClasses, trBuilder.toSafeHtml()));
      }

    }
    // end row loop
    return buf.toSafeHtml();

  }

  /**
   * Defaults to assume one sort at a time.
   *
   * @param colIndex the column to sort
   * @param sortDir the sort direction
   */
  protected void doSort(int colIndex, SortDir sortDir) {
    ColumnConfig<M, ?> column = cm.getColumn(colIndex);
    if (!isRemoteSort()) {
      ds.clearSortInfo();

      StoreSortInfo<M> s = createStoreSortInfo(column, sortDir);

      if (sortDir == null && storeSortInfo != null
          && storeSortInfo.getValueProvider().getPath().equals(column.getValueProvider().getPath())) {
        s.setDirection(storeSortInfo.getDirection() == SortDir.ASC ? SortDir.DESC : SortDir.ASC);
      } else if (sortDir == null) {
        s.setDirection(SortDir.ASC);
      }

      if (GWT.isProdMode()) {
        ds.addSortInfo(s);
      } else {
        try {
          // addSortInfo will apply its sort when called, which might trigger an
          // exception if the column passed in's data isn't Comparable
          ds.addSortInfo(s);
        } catch (ClassCastException ex) {
          GWT.log("Column can't be sorted " + column.getValueProvider().getPath()
              + " is not Comparable, and no Comparator was set for that column. ", ex);
          throw ex;
        }
      }

    } else {
      ValueProvider<? super M, ?> vp = column.getValueProvider();

      SortInfoBean bean = new SortInfoBean(vp, sortDir);

      if (sortDir == null && sortState != null && vp.getPath().equals(sortState.getSortField())) {
        bean.setSortDir(sortState.getSortDir() == SortDir.ASC ? SortDir.DESC : SortDir.ASC);

      } else if (sortDir == null) {
        bean.setSortDir(SortDir.ASC);
      }

      grid.getLoader().clearSortInfo();
      grid.getLoader().addSortInfo(bean);
      grid.getLoader().load();
    }
  }

  /**
   * Distribute the width of the columns amongst the available grid width as required by {@link #setAutoFill(boolean)}
   * and {@link #setForceFit(boolean)} .
   *
   * @param preventRefresh true to perform calculations and update column models but do not update display
   * @param onlyExpand unused in <code>GridView</code> implementation
   * @param omitColumn index of column to exclude from operation
   */
  // TODO: Consider removing unused parameter onlyExpand or adding support for
  // it
  protected void fitColumns(boolean preventRefresh, boolean onlyExpand, int omitColumn) {
    int tw = getTotalWidth();
    int aw = grid.getElement().getWidth(true) - getScrollAdjust();
    if (aw <= 0) {
      aw = grid.getElement().getComputedWidth();
    }

    if (aw < 20) { // not initialized, so don't screw up the
      // default widths
      return;
    }

    int extra = (int) aw - tw;

    if (extra == 0) {
      return;
    }

    int colCount = cm.getColumnCount();
    Stack<Integer> cols = new Stack<Integer>();
    int width = 0;
    int w;

    for (int i = 0; i < colCount; i++) {
      w = cm.getColumnWidth(i);
      if (!cm.isHidden(i) && !cm.isFixed(i) && i != omitColumn) {
        cols.push(i);
        cols.push(w);
        width += w;
      }
    }

    double frac = ((double) (extra)) / width;
    while (cols.size() > 0) {
      w = cols.pop();
      int i = cols.pop();
      int ww = Math.max(header.getMinColumnWidth(), (int) Math.floor(w + w * frac));
      cm.setColumnWidth(i, ww, true);
    }

    tw = getTotalWidth();
    if (tw > aw) {
      width = 0;
      for (int i = 0; i < colCount; i++) {
        w = cm.getColumnWidth(i);
        if (!cm.isHidden(i) && !cm.isFixed(i) && w > header.getMinColumnWidth()) {
          cols.push(i);
          cols.push(w);
          width += w;
        }
      }
      frac = ((double) (aw - tw)) / width;
      while (cols.size() > 0) {
        w = cols.pop();
        int i = cols.pop();
        int ww = Math.max(header.getMinColumnWidth(), (int) Math.floor(w + w * frac));
        cm.setColumnWidth(i, ww, true);
      }
    }

    if (!preventRefresh) {
      updateAllColumnWidths();
    }
  }

  /**
   * Gets the properties required for rendering the columns.
   *
   * @return a list of the grid's column properties
   */
  protected List<ColumnData> getColumnData() {
    int colCount = cm.getColumnCount();
    List<ColumnData> cs = new ArrayList<ColumnData>();
    for (int i = 0; i < colCount; i++) {
      ColumnData data = new ColumnData();
      data.setStyles(getColumnStyle(i, false));
      cs.add(data);
    }
    return cs;
  }

  /**
   * Returns the CSS styles for the given column.
   *
   * @param colIndex the column index
   * @param isHeader true to include the column header styles
   * @return the styles
   */
  protected SafeStyles getColumnStyle(int colIndex, boolean isHeader) {
    SafeStylesBuilder builder = new SafeStylesBuilder();
    if (!isHeader) {
      SafeStyles columnStyles = cm.getColumnStyles(colIndex);
      if (columnStyles != null) {
        builder.append(columnStyles);
      }
    }

    HorizontalAlignmentConstant alignHorz = cm.getColumnHorizontalAlignment(colIndex);
    if (alignHorz != null) {
      builder.append(SafeStylesUtils.fromTrustedString("text-align:" + alignHorz.getTextAlignString() + ";"));
    }

    VerticalAlignmentConstant alignVert = cm.getColumnVerticalAlignment(colIndex);
    if (alignVert != null) {
      builder.append(SafeStylesUtils.fromTrustedString("vertical-align:" + alignVert.getVerticalAlignString() + ";"));
    }

    return builder.toSafeStyles();
  }

  /**
   * Returns the width of the given column
   *
   * @param col the column index
   * @return the column width
   */
  protected int getColumnWidth(int col) {
    return cm.getColumnWidth(col);
  }

  protected int[] getColumnWidths() {
    int colCount = cm.getColumnCount();
    int[] columnWidths = new int[colCount];
    for (int i = 0; i < colCount; i++) {
      columnWidths[i] = getColumnWidth(i);
    }
    return columnWidths;
  }

  /**
   * Returns the offset width of the grid including the total visible column width and the amount required or reserved
   * for the vertical scroll bar.
   *
   * @return the grid's offset width
   */
  protected int getOffsetWidth() {
    return (getTotalWidth() + getScrollAdjust());
  }

  /**
   * Renders the value of a cell into safe HTML.
   *
   * @param rowIndex the row index
   * @param colIndex the column index
   * @param m the data model
   * @param record the optional {@link Record} for this row (may be null)
   * @return the safe HTML representing the cell
   */
  protected <N> SafeHtml getRenderedValue(int rowIndex, int colIndex, M m, ListStore<M>.Record record) {
    ValueProvider<? super M, N> valueProvider = cm.getValueProvider(colIndex);
    N val = null;
    if (record != null) {
      val = record.getValue(valueProvider);
    } else {
      val = valueProvider.getValue(m);
    }
    Cell<N> r = cm.getCell(colIndex);
    if (r != null) {
      SafeHtmlBuilder sb = new SafeHtmlBuilder();
      r.render(new Context(rowIndex, colIndex, ds.getKeyProvider().getKey(m)), val, sb);
      return sb.toSafeHtml();
    }

    String text = null;
    if (val != null) {
      text = val.toString();
    }
    return Util.isEmptyString(text) ? Util.NBSP_SAFE_HTML : SafeHtmlUtils.fromString(text);
  }

  /**
   * Returns the HTML elements representing the body of the table.
   *
   * @return the HTML elements representing the rows in the table (empty if the table has no rows)
   */
  protected NodeList<Element> getRows() {
    if (!hasRows()) {
      return JavaScriptObject.createArray().cast();
    }
    return appearance.getRows(body);
  }

  /**
   * Returns the number of pixels required or reserved for the vertical scroll bar.
   *
   * @return the nominal width of the vertical scroll bar
   */
  protected int getScrollAdjust() {
    return adjustForHScroll ? (scroller != null ? (vbar ? scrollOffset + 1 : 2) : scrollOffset) : scrollOffset;
  }

  /**
   * The total width of the visible columns in the grid (for the width including the vertical scroll bar, see
   * {@link #getOffsetWidth()}.
   *
   * @return the total width of the columns in the grid.
   */
  protected int getTotalWidth() {
    return cm.getTotalWidth();
  }

  /**
   * Handles browser events of interest to the grid view. The default implementation for {@link GridView} includes
   * support for mouse-over tracking (see {@link GridView#setTrackMouseOver(boolean)} and scroll bar synchronization.
   *
   * @param event the browser event
   */
  protected void handleComponentEvent(Event event) {
    Element row = Element.is(event.getEventTarget()) ? findRow((Element) event.getEventTarget().cast()) : null;

    switch (event.getTypeInt()) {
      case Event.ONMOUSEMOVE:

        if (overRow != null && row == null) {
          onRowOut(overRow);
        } else if (row != null && overRow != row) {
          if (overRow != null) {
            onRowOut(overRow);
          }
          onRowOver(row);
        }

        break;

      case Event.ONMOUSEOVER:
        EventTarget from = event.getRelatedEventTarget();
        if (from == null || (Element.is(from) && !grid.getElement().isOrHasChild(Element.as(from)))) {
          Element r = null;
          if (Element.is(event.getEventTarget())) {
            r = findRow(Element.as(event.getEventTarget()));
          }
          if (r != null) {
            onRowOver(r);
          }
        }
        break;
      case Event.ONMOUSEOUT:
        EventTarget to = event.getRelatedEventTarget();
        if (to == null || (Element.is(to) && !grid.getElement().isOrHasChild(Element.as(to)))) {
          if (overRow != null) {
            onRowOut(overRow);
          }
        }
        break;
      case Event.ONMOUSEDOWN:
        onMouseDown(event);
        break;
      case Event.ONSCROLL:
        if (scroller.isOrHasChild(Element.as(event.getEventTarget()))) {
          syncScroll();
        }
        break;
    }
    if (!trackMouseOver && overRow != null) {
      trackMouseOver = true;
      onRowOut(overRow);
      trackMouseOver = false;
    }

    if (event.getTypeInt() == Event.ONSCROLL) {
      if (scroller.isOrHasChild(Element.as(event.getEventTarget()))) {
        syncScroll();
      }
    }
  }

  /**
   * Returns true if the grid has rows.
   *
   * @return true if the grid has rows.
   */
  protected boolean hasRows() {
    if (dataTable == null || dataTableBody == null || dataTableBody.getChildCount() == 0) {
      return false;
    }

    Element emptyRowElement = dataTableBody.getFirstChildElement();
    if (emptyRowElement == null) {
      return false;
    }
    emptyRowElement = emptyRowElement.getFirstChildElement();
    if (emptyRowElement == null) {
      return false;
    }
    emptyRowElement = emptyRowElement.getFirstChildElement();
    if (emptyRowElement == null) {
      return false;
    }
    return !emptyRowElement.getClassName().equals(styles.empty());
  }

  /**
   * Initializes the view.
   *
   * @param grid the grid
   */
  protected void init(final Grid<M> grid) {
    this.grid = grid;
    this.cm = grid.getColumnModel();
    selectable = grid.isAllowTextSelection();

    initListeners();

    grid.getElement().addClassName(appearance.styles().grid());
    grid.getElement().setClassName(styles.columnLines(), columnLines);

    initData(grid.getStore(), cm);
    initUI(grid);
    initHeader();

    grid.addHeaderClickHandler(new HeaderClickHandler() {
      @Override
      public void onHeaderClick(HeaderClickEvent event) {
        GridView.this.onHeaderClick(event);
      }
    });

    header.addResizeHandler(new ResizeHandler() {
      @Override
      public void onResize(ResizeEvent event) {
        resize(); // updates scroller
      }
    });

    if (cm.getAggregationRows().size() > 0) {
      footer = new ColumnFooter<M>(grid, cm);
    }

    renderUI();
    grid.sinkEvents(Event.ONCLICK | Event.ONDBLCLICK | Event.MOUSEEVENTS | Event.FOCUSEVENTS);
  }

  public void setColumnHeader(ColumnHeader<M> columnHeader) {
    // check if we've been assigned a grid instance yet via init, if so, we've already wired up and
    // attached our own header instance
    if (grid != null && grid.isRendered()) {
      throw new IllegalStateException("Can't set a new ColumnHeader after the grid has been rendered");
    }
    this.header = columnHeader;
  }

  /**
   * Initializes the column header and saves reference for future use, creating one if it hasn't yet been set
   */
  protected void initHeader() {
    if (header == null) {
      header = new ColumnHeader<M>(grid, cm);
    }
    header.setMenuFactory(new HeaderContextMenuFactory() {
      @Override
      public Menu getMenuForColumn(int columnIndex) {
        return createContextMenu(columnIndex);
      }
    });
    header.setSplitterWidth(splitterWidth);
  }

  /**
   * Initializes the data.
   *
   * @param ds the data store
   * @param cm the column model
   */
  protected void initData(ListStore<M> ds, ColumnModel<M> cm) {
    if (storeHandlerRegistration != null) {
      storeHandlerRegistration.removeHandler();
      storeHandlerRegistration = null;
    }
    if (ds != null) {
      storeHandlerRegistration = ds.addStoreHandlers(listener);
    }
    this.ds = ds;

    if (cmHandlerRegistration != null) {
      cmHandlerRegistration.removeHandler();
      cmHandlerRegistration = null;
    }
    if (cm != null) {
      cmHandlerRegistration = cm.addColumnModelHandlers(columnListener);
    }
    this.cm = cm;

    if (this.header != null) {
      this.header.setColumnModel(cm);
    }
  }

  /**
   * Collects references to the HTML elements of the grid view and saves them in instance variables for future
   * reference.
   */
  protected void initElements() {
    NodeList<Node> cs = grid.getElement().getChildNodes();

    // headerWrap = (XElement) cs.getItem(0);
    // headerInner = headerWrap.getFirstChildElement().cast();

    scroller = (XElement) cs.getItem(1);
    scroller.addEventsSunk(Event.ONSCROLL);

    grid.addGestureRecognizer(new ScrollGestureRecognizer(scroller, ScrollDirection.BOTH));

    if (forceFit) {
      scroller.getStyle().setOverflowX(Overflow.HIDDEN);
    }

    body = scroller.getFirstChildElement().cast();

    dataTable = body.getFirstChildElement().cast();
    dataTableSizingHead = dataTable.getFirstChildElement().cast();
    dataTableBody = dataTableSizingHead.getNextSiblingElement().cast();
  }

  /**
   * Creates the grid view listeners, including {@link StoreHandlers} and {@link ColumnModelHandlers}, and saves
   * references for future use.
   */
  protected void initListeners() {
    listener = new StoreHandlers<M>() {

      @Override
      public void onAdd(StoreAddEvent<M> event) {
        GridView.this.onAdd(event.getItems(), event.getIndex());

      }

      @Override
      public void onClear(StoreClearEvent<M> event) {
        GridView.this.onClear(event);

      }

      @Override
      public void onDataChange(StoreDataChangeEvent<M> event) {
        GridView.this.onDataChanged(event);

      }

      @Override
      public void onFilter(StoreFilterEvent<M> event) {
        GridView.this.onDataChanged(null);
      }

      @Override
      public void onRecordChange(StoreRecordChangeEvent<M> event) {
        GridView.this.onUpdate(ds, Collections.singletonList(event.getRecord().getModel()));
      }

      @Override
      public void onRemove(StoreRemoveEvent<M> event) {
        GridView.this.onRemove(event.getItem(), event.getIndex(), false);

      }

      @Override
      public void onSort(StoreSortEvent<M> event) {
        GridView.this.onDataChanged(null);

      }

      @Override
      public void onUpdate(StoreUpdateEvent<M> event) {
        GridView.this.onUpdate(ds, event.getItems());
      }

    };

    columnListener = new ColumnModelHandlers() {
      @Override
      public void onColumnHeaderChange(ColumnHeaderChangeEvent event) {
        GridView.this.onHeaderChange(event.getIndex(), cm.getColumnHeader(event.getIndex()));

      }

      @Override
      public void onColumnHiddenChange(ColumnHiddenChangeEvent event) {
        GridView.this.onHiddenChange(cm, event.getIndex(), cm.isHidden(event.getIndex()));

      }

      @Override
      public void onColumnMove(ColumnMoveEvent event) {
        GridView.this.onColumnMove(event.getIndex());

      }

      @Override
      public void onColumnWidthChange(ColumnWidthChangeEvent event) {
        GridView.this.onColumnWidthChange(event.getIndex(), cm.getColumnWidth(event.getIndex()));

      }
    };
  }

  /**
   * Invoked to perform additional initialization of the grid view's user interface, after the data has been
   * initialized, the default implementation for {@link GridView} does nothing.
   *
   * @param grid the grid for this grid view
   */
  protected void initUI(final Grid<M> grid) {

  }

  /**
   * Inserts the given rows (already present in the grid's list store) into the grid view.
   *
   * @param firstRow the first row index
   * @param lastRow the last row index
   * @param isUpdate true if update to existing rows
   */
  protected void insertRows(int firstRow, int lastRow, boolean isUpdate) {
    if (lastRow < firstRow) {
      return;
    }

    if (!hasRows()) {
      if (GXT.isIE()) {
        dataTableBody.removeChildren();
      } else {
        dataTableBody.setInnerSafeHtml(SafeHtmlUtils.EMPTY_SAFE_HTML);
      }
    }

    SafeHtml html = renderRows(firstRow, lastRow);
    XElement before = getRow(firstRow).cast();

    if (before != null) {
      DomHelper.insertBefore(before, html);
    } else {
      DomHelper.insertHtml("beforeEnd", dataTableBody, html);
    }
    if (!isUpdate) {
      processRows(firstRow, false);
    }
  }

  /**
   * Return true if configured for remote sorting.
   *
   * @return if configured for remote sorting.
   */
  protected boolean isRemoteSort() {
    return grid.getLoader() != null && grid.getLoader().isRemoteSort();
  }

  /**
   * Lays out the grid view, adjusting the header and footer width and accounting for force fit and auto fill settings.
   *
   * @param skipResize true to skip resizing of the grid view
   */
  protected void layout(boolean skipResize) {
    if (body == null) {
      return;
    }

    XElement c = grid.getElement();
    Size csize = c.getStyleSize();

    if (GXTLogConfiguration.loggingIsEnabled()) {
      logger.finest("layout() " + csize);
    }

    int vw = csize.getWidth();
    if (vw < 10 || (csize.getHeight() < 20 && !grid.isHideHeaders())) {
      return;
    }

    if (!skipResize) {
      resize();
    }

    if (forceFit || autoFill) {
      if (lastViewWidth != vw) {
        fitColumns(false, false, -1);
        header.updateTotalWidth(getOffsetWidth(), getTotalWidth());
        if (footer != null) {
          footer.updateTotalWidth(getOffsetWidth(), getTotalWidth());
        }
        lastViewWidth = vw;
      }
    } else {
      autoExpand(false);
      header.updateTotalWidth(getOffsetWidth(), getTotalWidth());
      if (footer != null) {
        footer.updateTotalWidth(getOffsetWidth(), getTotalWidth());
      }
      syncHeaderScroll();
    }

  }

  /**
   * Invoked after the grid has been hidden, the default implementation for {@link GridView} does nothing.
   */
  protected void notifyHide() {
  }

  /**
   * Invoked after the grid has been shown.
   */
  protected void notifyShow() {
    // EXTGWT-3741 - Sizing issues in header when Grid is rendered hidden (such as inactive tab or inactive
    // card in card layout)
    if (header != null) {
      header.refresh();
    }
  }

  /**
   * Handles adding new data models to the store.
   *
   * @param models the new data models
   * @param index the index of the first model
   */
  protected void onAdd(List<M> models, int index) {
    if (grid != null && grid.isViewReady()) {
      insertRows(index, index + (models.size() - 1), false);
      addTask.delay(10);
    }
  }

  /**
   * Handles the clearing of the selection for the given cell.
   *
   * @param row the row index
   * @param col the cell index
   */
  protected void onCellDeselect(int row, int col) {
    Element cell = getCell(row, col);
    if (cell != null) {
      appearance.onCellSelect(cell, false);
      cell.removeClassName(states.cellSelected());
    }
  }

  /**
   * Handles selecting the given cell.
   *
   * @param row the row index
   * @param col the cell index
   */
  protected void onCellSelect(int row, int col) {
    Element cell = getCell(row, col);
    if (cell != null) {
      appearance.onCellSelect(cell, true);
      cell.addClassName(states.cellSelected());
    }
  }

  /**
   * Handles clearing the store.
   *
   * @param se the event that cleared the store
   */
  protected void onClear(StoreClearEvent<M> se) {
    refresh(false);
  }

  /**
   * Handles the click event, the default implementation for {@link GridView} does nothing.
   *
   * @param ce the click event
   */
  protected void onClick(Event ce) {

  }

  /**
   * Handles the column move request.
   *
   * @param newIndex the destination column index
   */
  protected void onColumnMove(int newIndex) {
    boolean pScroll = preventScrollToTopOnRefresh;
    preventScrollToTopOnRefresh = true;
    refresh(true);
    preventScrollToTopOnRefresh = pScroll;
  }

  /**
   * Handles a change to the column model width (see {@link ColumnModel#setColumnWidth(int, int)});
   *
   * @param column the index of the column
   * @param width the new width
   */
  protected void onColumnWidthChange(int column, int width) {
    if (forceFit) {
      fitColumns(false, false, column);
      header.updateTotalWidth(getOffsetWidth(), getTotalWidth());
    } else {
      updateColumnWidth(column, width);
      header.updateTotalWidth(getOffsetWidth(), getTotalWidth());
      if (GXT.isIE()) {
        syncHeaderScroll();
      }
    }
  }

  /**
   * Handles a change in the data in the store, including changes to the filter or sort state.
   *
   * @param se the change (may be null)
   */
  protected void onDataChanged(StoreDataChangeEvent<M> se) {
    if (!grid.viewReady) return;
    refresh(false);
    if (grid != null && grid.isLoadMask()) {
      if (grid.isEnabled()) {
        grid.unmask();
      } else {
        grid.mask();
      }
    }
  }

  protected void onFocus(Event event) {
    EventTarget eventTarget = event.getEventTarget();
    if (Element.is(eventTarget)) {
      final Element target = eventTarget.cast();
      int rowIndex = findRowIndex(target);
      int columnIndex = findCellIndex(target, null);
      focusCell(rowIndex, columnIndex, false);
    }

    focus();
  }

  /**
   * Handles a change in the safe HTML that represents the header (see
   * {@link ColumnModel#setColumnHeader(int, SafeHtml)}).
   *
   * @param column the column index
   * @param text the new safe HTML
   */
  protected void onHeaderChange(int column, SafeHtml text) {
    header.setHeader(column, text);
  }

  /**
   * Handles a header click event.
   *
   * @param event the header click event
   */
  protected void onHeaderClick(HeaderClickEvent event) {
    headerColumnIndex = event.getColumnIndex();
    if (!headerDisabled && cm.isSortable(headerColumnIndex)) {
      doSort(headerColumnIndex, null);
    }
  }

  /**
   * Handles a change in the column model's hidden state (see {@link ColumnModel#setHidden(int, boolean)}).
   *
   * @param cm the column model
   * @param col the column index
   * @param hidden true if the column is hidden
   */
  protected void onHiddenChange(ColumnModel<M> cm, int col, boolean hidden) {
    updateColumnHidden(col, hidden);
  }

  /**
   * Handles a request to change the highlight state of a row.
   *
   * @param rowIndex the row index
   * @param highlight true to highlight the row
   */
  protected void onHighlightRow(int rowIndex, boolean highlight) {
    Element row = getRow(rowIndex);
    if (row != null) {
      appearance.onRowHighlight(row, highlight);
    }
  }

  /**
   * Invoked when a mouse down event occurs, the default implementation for {@link GridView} does nothing.
   */
  protected void onMouseDown(Event ge) {

  }

  /**
   * Called with key down is pressed while on last row.
   *
   * @param index the index of the last row
   */
  protected void onNoNext(int index) {

  }

  /**
   * Called when key up is pressed while on first row.
   */
  protected void onNoPrev() {

  }

  /**
   * Handles removing a data model from the store.
   *
   * @param m the data model
   * @param index the row index
   * @param isUpdate true to indicate an update an existing row
   */
  protected void onRemove(M m, int index, boolean isUpdate) {
    if (grid != null && grid.isViewReady()) {
      removeRow(index);
      if (!isUpdate) {
        removeTask.delay(10);
      } else {
        removeTask.delay(0);
      }
    }
  }

  /**
   * Handles clearing the selection on a row.
   *
   * @param rowIndex the row index
   */
  protected void onRowDeselect(int rowIndex) {
    Element row = getRow(rowIndex);
    if (row != null) {
      appearance.onRowSelect(row, false);
      appearance.onRowHighlight(row, false);
      row.removeClassName(states.rowSelected());
    }
  }

  /**
   * Handles moving the mouse off a row.
   *
   * @param row the HTML element for the row
   */
  protected void onRowOut(Element row) {
    if (isTrackMouseOver()) {
      if (overRow != null && overRow != row) {
        appearance.onRowOver(overRow, false);
      }
      appearance.onRowOver(row, false);
      overRow = null;
    }
  }

  /**
   * Handles moving the mouse onto a row.
   *
   * @param row the HTML element for the row
   */
  protected void onRowOver(Element row) {
    if (isTrackMouseOver()) {
      appearance.onRowOver(row, true);
      overRow = row;
    }
  }

  /**
   * Handles setting the selection on a row.
   *
   * @param rowIndex the row index
   */
  protected void onRowSelect(int rowIndex) {
    Element row = getRow(rowIndex);
    if (row != null) {
      onRowOut(row);
      appearance.onRowSelect(row, true);
      row.addClassName(states.rowSelected());
    }
  }

  /**
   * Handles an update to data in the store.
   *
   * @param store the store
   * @param models the updated data
   */
  protected void onUpdate(final ListStore<M> store, final List<M> models) {
    if (!deferUpdates) {
      for (M m : models) {
        refreshRow(store.indexOf(m));
      }
    } else {
      Timer t = new Timer() {
        @Override
        public void run() {
          for (M m : models) {
            refreshRow(store.indexOf(m));
            grid.getSelectionModel().onUpdate(m);
          }
        }
      };
      t.schedule(deferUpdateDelay);
    }
  }

  /**
   * Makes a pass through the rows in the grid to finalize the appearance, the default implementation in
   * {@link GridView} assigns the row index property and stripes the rows (if striping is enabled).
   *
   * @param startRow the row index
   * @param skipStripe true to prevent striping (striping is always prevented if {@link GridView#isStripeRows()} returns
   *          false).
   */
  protected void processRows(int startRow, boolean skipStripe) {
    if (ds.size() < 1) {
      return;
    }
    skipStripe = skipStripe || !isStripeRows();
    NodeList<Element> rows = getRows();
    String cls = styles.rowAlt();
    for (int i = startRow, len = rows.getLength(); i < len; i++) {
      Element row = rows.getItem(i);
      row.setPropertyInt("rowindex", i);
      if (!skipStripe) {
        boolean isAlt = (i + 1) % 2 == 0;
        boolean hasAlt = row.getClassName() != null && row.getClassName().indexOf(cls) != -1;
        if (isAlt == hasAlt) {
          continue;
        }
        if (isAlt) {
          row.addClassName(cls);
        } else {
          row.removeClassName(cls);
        }
      }
    }
  }

  /**
   * Refreshes the displayed content for the given row.
   *
   * @param row the row index
   */
  protected void refreshRow(int row) {
    if (grid != null && grid.isViewReady()) {
      M m = ds.get(row);
      if (m != null) {
        insertRows(row, row, true);
        getRow(row).setPropertyInt("rowindex", row);
        // sets a flag for getCell to know it's being removed and adjust index
        removeInProgress = true;
        onRemove(m, row + 1, true);
        removeInProgress = false;
      }
    }
  }

  /**
   * Removes the given row.
   *
   * @param row the row index
   */
  protected void removeRow(int row) {
    Element r = getRow(row);
    if (r != null) {
      r.removeFromParent();
    }
  }

  /**
   * Renders the footer.
   */
  protected void renderFooter() {
    footer.setAllowTextSelection(false);

    grid.getElement().appendChild(footer.getElement());
    footer.refresh();
  }

  /**
   * Renders the header.
   */
  protected void renderHeader() {
    headerElem = header.getElement();
    grid.getElement().insertFirst(headerElem);
    header.refresh();
    if (grid.isHideHeaders()) {
      headerElem.setVisible(false);
    }
  }

  /**
   * Renders the hidden TH elements that keep the column widths.
   *
   * @param columnWidths the column widths
   * @return markup representing the hidden table header elements
   */
  protected SafeHtml renderHiddenHeaders(int[] columnWidths) {
    SafeHtmlBuilder heads = new SafeHtmlBuilder();
    for (int i = 0; i < columnWidths.length; i++) {
      int w = cm.isHidden(i) ? 0 : columnWidths[i];
      SafeStylesBuilder builder = new SafeStylesBuilder();
      builder.appendTrustedString("height: 0px;");
      builder.appendTrustedString("width:" + w + "px;");
      heads.append(tpls.th("", builder.toSafeStyles()));
    }
    return tpls.tr(appearance.styles().headerRow(), heads.toSafeHtml());
  }

  /**
   * Renders the grid's rows.
   *
   * @param startRow the index in the store of the first row to render
   * @param endRow the index of the last row to render (may be -1 to indicate all rows)
   * @return safe HTML representing the rendered rows
   */
  protected SafeHtml renderRows(int startRow, int endRow) {
    if (ds.size() < 1) {
      return SafeHtmlUtils.EMPTY_SAFE_HTML;
    }

    List<ColumnData> cs = getColumnData();

    if (endRow == -1) {
      endRow = ds.size() - 1;
    }

    List<M> rs = ds.subList(startRow, ++endRow);
    return doRender(cs, rs, startRow);
  }

  /**
   * Responsible for rendering all aspects of the grid view.
   */
  protected void renderUI() {
    renderHeader();

    initElements();

    DomHelper.insertHtml("afterBegin", dataTableSizingHead, renderHiddenHeaders(getColumnWidths()));

    header.setEnableColumnResizing(grid.isColumnResize());
    header.setEnableColumnReorder(grid.isColumnReordering());

    if (footer != null) {
      renderFooter();
    }

    updateHeaderSortState();
  }

  protected void repaintGrid() {
    dataTable.getStyle().setProperty("display", "block");

    Scheduler.get().scheduleFinally(new ScheduledCommand() {

      @Override
      public void execute() {
        dataTable.getStyle().clearDisplay();
      }
    });
  }

  /**
   * Resizes the grid view, adjusting the scroll bars and accounting for the footer height (if any).
   */
  protected void resize() {
    if (body == null) {
      return;
    }

    Size csize = grid.getElement().getStyleSize();

    if (GXTLogConfiguration.loggingIsEnabled()) {
      logger.finest("resize() " + csize);
    }

    int vw = csize.getWidth();
    int vh = 0;
    if (vw < 10 || csize.getHeight() < 22) {
      return;
    }

    if (grid.isAutoHeight()) {
      scroller.setWidth(vw);
    }

    int hdHeight = headerElem.getHeight(false);
    vh = csize.getHeight() - hdHeight;

    if (footer != null) {
      vh -= footer.getOffsetHeight();
    }

    if (!grid.isAutoHeight()) {
      scroller.setSize(vw, vh);
    }

    if (headerElem != null) {
      headerElem.setWidth(vw);
    }
    if (footer != null) {
      footer.setWidth(vw);
    }

  }

  /**
   * Restores the scroll state.
   *
   * @param state the state as returned from a previous call to {@link #getScrollState()}.
   */
  protected void restoreScroll(Point state) {
    if (state.getY() < scroller.getWidth(false)) {
      scroller.setScrollLeft(state.getX());
    }
    if (state.getX() < scroller.getHeight(false)) {
      scroller.setScrollTop(state.getY());
    }
  }

  /**
   * Synchronizes the header position (and footer, if present) with the horizontal scroll bar.
   */
  protected void syncHeaderScroll() {
    int sl = scroller.getScrollLeft();
    headerElem.setScrollLeft(sl);
    // second time for IE (1/2 time first fails, other browsers ignore)
    headerElem.setScrollLeft(sl);

    if (footer != null) {
      footer.getElement().setScrollLeft(sl);
      footer.getElement().setScrollLeft(sl);
    }
  }

  /**
   * Synchronizes the grid scroll bars.
   */
  protected void syncScroll() {
    syncHeaderScroll();
    int scrollLeft = scroller.getScrollLeft();
    int scrollTop = scroller.getScrollTop();
    grid.fireEvent(new BodyScrollEvent(scrollLeft, scrollTop));
  }

  /**
   * Invoked after all column widths have been updated, the default implementation for {@link GridView} does nothing.
   */
  protected void templateOnAllColumnWidthsUpdated(int[] columnWidths, int tw) {

  }

  /**
   * Invoked after the hidden column status been updated, the default implementation for {@link GridView} does nothing.
   */
  protected void templateOnColumnHiddenUpdated(int col, boolean hidden, int tw) {
    // template method
  }

  /**
   * Invoked after a column width has been updated, the default implementation for {@link GridView} does nothing.
   */
  protected void templateOnColumnWidthUpdated(int col, int w, int tw) {
    // template method
  }

  /**
   * Synchronizes the displayed width of each column with the defined width of each column from its column model.
   */
  protected void updateAllColumnWidths() {
    int tw = getTotalWidth();
    int clen = cm.getColumnCount();

    int[] columnWidths = new int[clen];
    for (int i = 0; i < clen; i++) {
      columnWidths[i] = cm.isHidden(i) ? 0 : getColumnWidth(i);
    }

    adjustColumnWidths(columnWidths);

    templateOnAllColumnWidthsUpdated(columnWidths, tw);
  }

  /**
   * Updates the row width and cell display properties to hide or show the given column.
   *
   * @param index the column index
   * @param hidden true to hide the column
   */
  protected void updateColumnHidden(int index, boolean hidden) {
    int tw = getTotalWidth();

    header.updateColumnHidden(index, hidden);
    if (footer != null) {
      footer.updateTotalWidth(getOffsetWidth(), tw);
      footer.updateColumnHidden(index, hidden);
    }

    NodeList<Element> tables = scroller.select("." + appearance.styles().dataTable());

    for (int t = 0, len = tables.getLength(); t < len; t++) {
      XElement table = tables.getItem(t).cast();

      table.getStyle().setWidth(getTotalWidth(), Unit.PX);

      NodeList<Element> ths = getTableHeads(table);
      if (ths == null) {
        continue;
      }

      if (index < ths.getLength()) {
        ths.getItem(index).getStyle().setPropertyPx("width", hidden ? 0 : getColumnWidth(index));
      }
    }

    dataTable.getStyle().setWidth(tw, Unit.PX);

    // cell widths incorrect
    if (GXT.isIE() || GXT.isSafari()) {
      repaintGrid();
    }

    lastViewWidth = -1;

    if (isForceFit() && !hidden) {
      ColumnConfig<M, ?> config = cm.getColumn(index);
      boolean fixed = config.isFixed();
      config.setFixed(true);
      layout();
      config.setFixed(fixed);
    }  else {
      layout();
    }

    templateOnColumnHiddenUpdated(index, hidden, tw);
  }

  /**
   * Updates the column width to the given value, which should have previously been stored in the column model.
   *
   * @param col the column index
   * @param width the width of the column
   */
  protected void updateColumnWidth(int col, int width) {
    int tw = getTotalWidth();
    int w = getColumnWidth(col);

    header.updateTotalWidth(-1, tw);
    header.updateColumnWidth(col, width);

    if (footer != null) {
      footer.updateTotalWidth(getOffsetWidth(), tw);
      footer.updateColumnWidth(col, width);
    }

    int clen = cm.getColumnCount();
    int[] columnWidths = new int[clen];
    for (int i = 0; i < clen; i++) {
      columnWidths[i] = cm.isHidden(i) ? 0 : getColumnWidth(i);
    }
    adjustColumnWidths(columnWidths);

    templateOnColumnWidthUpdated(col, w, tw);
  }

  /**
   * Update the header to reflect any changes in the sort state.
   */
  protected void updateHeaderSortState() {
    if (!isRemoteSort()) {
      StoreSortInfo<M> info = getSortState();
      if (info != null) {
        ValueProvider<? super M, ?> vp = info.getValueProvider();
        if (vp != null) {
          String p = vp.getPath();
          if (p != null && !"".equals(p)) {
            ColumnConfig<M, ?> config = cm.findColumnConfig(p);
            if (config != null) {
              if (storeSortInfo == null || (!p.equals(storeSortInfo.getPath()))
                  || storeSortInfo.getDirection() != info.getDirection()) {
                int index = cm.indexOf(config);
                if (index != -1) {
                  updateSortIcon(index, info.getDirection());
                }
                grid.fireEvent(new SortChangeEvent(new SortInfoBean(info.getPath(), info.getDirection())));
              }

            }
          }
          storeSortInfo = info;
        }

      }
    } else {
      List<? extends SortInfo> infos = grid.getLoader().getSortInfo();
      if (infos.size() > 0) {
        SortInfo info = infos.get(0);
        String p = info.getSortField();
        if (p != null && !"".equals(p)) {
          ColumnConfig<M, ?> config = cm.findColumnConfig(p);
          if (config != null) {
            if (sortState == null || (!sortState.getSortField().equals(p))
                || sortState.getSortDir() != info.getSortDir()) {
              int index = cm.indexOf(config);
              if (index != -1) {
                updateSortIcon(index, info.getSortDir());
              }
              grid.fireEvent(new SortChangeEvent(info));
            }
          }
          sortState = info;
        }
      }
    }
  }

  /**
   * Updates the sort icon for the given column and sort direction.
   *
   * @param colIndex the column index
   * @param dir the sort direction
   */
  protected void updateSortIcon(int colIndex, SortDir dir) {
    header.updateSortIcon(colIndex, dir);
  }

  private SimpleEventBus ensureHandlers() {
    return eventBus == null ? eventBus = new SimpleEventBus() : eventBus;
  }

  private NodeList<Element> getTableHeads(Element table) {
    // tbody
    table = table.getFirstChildElement();
    if (table == null) return null;

    // tr
    table = table.getFirstChildElement();
    if (table == null) return null;

    return table.getChildNodes().cast();
  }

  private void refreshFooterData() {
    if (footer != null) {
      footer.refresh();
    }
  }

  private void restrictMenu(ColumnModel<M> cm, Menu columns) {
    int count = 0;
    for (int i = 0, len = cm.getColumnCount(); i < len; i++) {
      ColumnConfig<M, ?> cc = cm.getColumn(i);
      if (cc.isHidden() || !cc.isHideable()) {
        continue;
      }
      count++;
    }

    if (count == 1) {
      for (int i = 0, len = columns.getWidgetCount(); i < len; i++) {
        CheckMenuItem ci = (CheckMenuItem) columns.getWidget(i);
        if (ci.isChecked()) {
          ci.disable();
        }
      }
    } else {
      for (int i = 0, len = columns.getWidgetCount(); i < len; i++) {
        CheckMenuItem item = (CheckMenuItem) columns.getWidget(i);
        int col = item.getData("gxt-column-index");
        ColumnConfig<M, ?> config = cm.getColumn(col);
        if (config.isHideable()) {
          item.enable();
        }
      }
    }
  }

}
