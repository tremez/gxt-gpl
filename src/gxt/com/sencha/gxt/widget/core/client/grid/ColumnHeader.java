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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.AnchorElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.TableLayout;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.TableSectionElement;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safecss.shared.SafeStylesBuilder;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlexTable.FlexCellFormatter;
import com.google.gwt.user.client.ui.HTMLTable.RowFormatter;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasHorizontalAlignment.HorizontalAlignmentConstant;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.GXT;
import com.sencha.gxt.core.client.Style.Anchor;
import com.sencha.gxt.core.client.Style.AnchorAlignment;
import com.sencha.gxt.core.client.Style.Side;
import com.sencha.gxt.core.client.dom.DomHelper;
import com.sencha.gxt.core.client.dom.DomQuery;
import com.sencha.gxt.core.client.dom.XDOM;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.core.client.gestures.LongPressOrTapGestureRecognizer;
import com.sencha.gxt.core.client.gestures.TouchData;
import com.sencha.gxt.core.client.util.Point;
import com.sencha.gxt.core.client.util.Region;
import com.sencha.gxt.data.shared.SortDir;
import com.sencha.gxt.data.shared.SortInfo;
import com.sencha.gxt.data.shared.Store.StoreSortInfo;
import com.sencha.gxt.dnd.core.client.StatusProxy;
import com.sencha.gxt.fx.client.DragCancelEvent;
import com.sencha.gxt.fx.client.DragEndEvent;
import com.sencha.gxt.fx.client.DragHandler;
import com.sencha.gxt.fx.client.DragMoveEvent;
import com.sencha.gxt.fx.client.DragStartEvent;
import com.sencha.gxt.fx.client.Draggable;
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.ComponentHelper;
import com.sencha.gxt.widget.core.client.event.HeaderClickEvent;
import com.sencha.gxt.widget.core.client.event.HeaderContextMenuEvent;
import com.sencha.gxt.widget.core.client.event.HeaderDoubleClickEvent;
import com.sencha.gxt.widget.core.client.event.HeaderMouseDownEvent;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.sencha.gxt.widget.core.client.event.HideEvent.HideHandler;
import com.sencha.gxt.widget.core.client.event.XEvent;
import com.sencha.gxt.widget.core.client.grid.GridView.GridTemplates;
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.tips.QuickTip;

/**
 * A column header component.
 */
public class ColumnHeader<M> extends Component {

  /**
   * Delegate for external code to define what menu any given column should use
   */
  public interface HeaderContextMenuFactory {
    /**
     * Returns the context menu to be used for the given column index
     *
     * @param columnIndex the index of the column to make a menu for
     * @return the menu to use for the given column
     * @see ColumnModel#getColumn(int)
     */
    Menu getMenuForColumn(int columnIndex);
  }

  public interface ColumnHeaderAppearance {
    /**
     * Returns the icon to use for the "Columns" (column selection) header menu item.
     *
     * @return the columns menu icon
     */
    ImageResource columnsIcon();

    String columnsWrapSelector();

    void render(SafeHtmlBuilder sb);

    /**
     * Returns the icon to use for the "Sort Ascending" header menu item.
     *
     * @return the sort ascending menu icon
     */
    ImageResource sortAscendingIcon();

    /**
     * Returns the icon to use for the "Sort Descending" header menu item.
     *
     * @return the sort descending menu icon
     */
    ImageResource sortDescendingIcon();

    ColumnHeaderStyles styles();

    int getColumnMenuWidth();
  }

  public interface ColumnHeaderStyles extends CssResource {

    String columnMoveBottom();

    String columnMoveTop();

    String head();

    String headButton();

    String header();

    String headerInner();

    String headInner();

    String headMenuOpen();

    String headOver();

    String headRow();

    String sortAsc();

    String sortDesc();

    String sortIcon();
  }

  public class GridSplitBar extends Component {

    protected int colIndex;
    protected Draggable d;
    protected boolean dragging;
    protected DragHandler listener = new DragHandler() {

      @Override
      public void onDragCancel(DragCancelEvent event) {
        GridSplitBar.this.onDragCancel(event);
      }

      @Override
      public void onDragEnd(DragEndEvent event) {
        GridSplitBar.this.onDragEnd(event);
      }

      @Override
      public void onDragMove(DragMoveEvent event) {
      }

      @Override
      public void onDragStart(DragStartEvent event) {
        GridSplitBar.this.onDragStart(event);
      }

    };

    protected int startX;

    public GridSplitBar() {
      setElement(Document.get().createDivElement());
      getElement().getStyle().setProperty("cursor", "col-resize");
      getElement().makePositionable(true);
      setWidth(5);

      getElement().setVisibility(false);
      getElement().getStyle().setProperty("backgroundColor", "white");
      getElement().setOpacity(0);

      d = new Draggable(this);
      d.setUseProxy(false);
      d.setConstrainVertical(true);
      d.setStartDragDistance(0);
      d.addDragHandler(listener);
    }

    protected void drag(boolean enabled, String borderLeftStyle, int opacity, int splitterWidth) {
      dragging = enabled;
      headerDisabled = enabled;
      getElement().getStyle().setProperty("borderLeft", borderLeftStyle);
      getElement().setOpacity(opacity);
      getElement().setWidth(splitterWidth);
      bar.getElement().setVisibility(enabled);
    }

    protected void onDragCancel(DragCancelEvent event) {
      drag(false, "none", 0, splitterWidth);
    }

    protected void onDragEnd(DragEndEvent e) {
      drag(false, "none", 0, splitterWidth);

      int endX = e.getX();
      int diff = endX - startX;

      int width = Math.max(getMinColumnWidth(), cm.getColumnWidth(colIndex) + diff);
      cm.setUserResized(true);
      cm.setColumnWidth(colIndex, width);
    }

    protected void onDragStart(DragStartEvent e) {
      drag(true, "1px solid black", 1, 1);

      getElement().getStyle().setCursor(Cursor.DEFAULT);

      startX = e.getX();

      int cols = cm.getColumnCount();
      for (int i = 0, len = cols; i < len; i++) {
        if (cm.isHidden(i) || !cm.isResizable(i)) continue;
        Element hd = getHead(i).getElement();
        if (hd != null) {
          Region rr = XElement.as(hd).getRegion();
          if (startX > rr.getRight() - 5 && startX < rr.getRight() + 5) {
            colIndex = heads.indexOf(getHead(i));
            if (colIndex != -1) break;
          }
        }
      }
      if (colIndex > -1) {
        Element c = getHead(colIndex).getElement();
        int x = startX;
        int minx = x - XElement.as(c).getX() - minColumnWidth;
        int maxx = (XElement.as(container.getElement()).getX() + XElement.as(container.getElement()).getWidth(false))
            - e.getNativeEvent().<XEvent>cast().getXY().getX();
        d.setXConstraint(minx, maxx);
      }
    }

    protected void onMouseMove(Head header, Event event) {
      int activeHdIndex = heads.indexOf(header);

      if (dragging || !header.config.isResizable()) {
        return;
      }

      // find the previous column which is not hidden
      int before = -1;
      for (int i = activeHdIndex - 1; i >= 0; i--) {
        if (!cm.isHidden(i)) {
          before = i;
          break;
        }
      }
      int x = event.<XEvent>cast().getXY().getX();
      Region r = header.getElement().getRegion();
      int hw = splitterWidth;

      getElement().setY(XElement.as(container.getElement()).getY());
      getElement().setHeight(container.getOffsetHeight());

      Style ss = getElement().getStyle();

      if (x - r.getLeft() <= hw && before != -1 && cm.isResizable(before) && !cm.isFixed(before)) {
        bar.getElement().setVisibility(true);
        getElement().setX(r.getLeft() - (hw / 2));
        ss.setProperty("cursor", GXT.isSafari() ? "e-resize" : "col-resize");
      } else if (r.getRight() - x <= hw && cm.isResizable(activeHdIndex) && !cm.isFixed(activeHdIndex)) {
        bar.getElement().setVisibility(true);
        getElement().setX(r.getRight() - (hw / 2));
        ss.setProperty("cursor", GXT.isSafari() ? "w-resize" : "col-resize");
      } else {
        bar.getElement().setVisibility(false);
        ss.setProperty("cursor", "");
      }
    }
  }

  public class Group extends Component {

    private HeaderGroupConfig config;

    public Group(HeaderGroupConfig config) {
      this.config = config;
      groups.add(this);

      setElement(Document.get().createDivElement());
      setStyleName(styles.headInner());

      if (config.getWidget() != null) {
        getElement().appendChild(config.getWidget().getElement());
      } else {
        getElement().setInnerSafeHtml(config.getHtml());
      }
    }

    public void setHtml(SafeHtml html) {
      getElement().setInnerSafeHtml(html);
    }

    @Override
    protected void doAttachChildren() {
      ComponentHelper.doAttach(config.getWidget());
    }

    @Override
    protected void doDetachChildren() {
      ComponentHelper.doDetach(config.getWidget());
    }
  }

  public class Head extends Component {

    protected int column;
    protected ColumnConfig<M, ?> config;

    private AnchorElement btn;
    private ImageElement img;
    private InlineHTML text;
    private Widget widget;
    private int row;

    @SuppressWarnings({"rawtypes", "unchecked"})
    public Head(ColumnConfig column) {
      this.config = column;
      this.column = cm.indexOf(column);

      setElement(Document.get().createDivElement());

      btn = Document.get().createAnchorElement();
      btn.setHref("#");
      btn.setClassName(styles.headButton());

      img = Document.get().createImageElement();
      img.setSrc(GXT.getBlankImageUrl());
      img.setClassName(styles.sortIcon());

      getElement().appendChild(btn);

      if (config.getWidget() != null) {
        Element span = Document.get().createSpanElement().cast();
        widget = config.getWidget();
        span.appendChild(widget.getElement());
        getElement().appendChild(span);
      } else {
        text = new InlineHTML(config.getHeader());
        getElement().appendChild(text.getElement());
      }

      getElement().appendChild(img);

      SafeHtml tip = config.getToolTip();
      if (tip != null) {
        getElement().setAttribute("qtip", tip.asString());
      }

      sinkEvents(Event.ONCLICK | Event.MOUSEEVENTS | Event.FOCUSEVENTS | Event.ONKEYPRESS);

      addStyleName(styles.headInner());
      if (column.getColumnHeaderClassName() != null) {
        addStyleName(column.getColumnHeaderClassName());
      }
      heads.add(this);

      addGestureRecognizer(new LongPressOrTapGestureRecognizer() {
        @Override
        public boolean handleStart(NativeEvent startEvent) {
          // TODO- this preventDefault here is to deal with synthesized mouse events
          // causing the column header menu to close immediately.  This does, however,
          // cause issues with scrolling in surrounding containers, so touch scroll must
          // be addressed.
          startEvent.preventDefault();
          return super.handleStart(startEvent);
        }

        @Override
        protected void onLongPress(TouchData touchData) {
          onDropDownClick(touchData.getLastNativeEvent().<Event>cast(), Head.this.column);
          super.onLongPress(touchData);
        }

        @Override
        protected void onEnd(List<TouchData> touches) {
          Event endEvent = touches.get(0).getLastNativeEvent().cast();
          onHeaderClick(endEvent, Head.this.column);
          super.onEnd(touches);
        }
      });
    }

    public void activateTrigger(boolean activate) {
      XElement e = getElement().findParent("td", 3);
      if (e != null) {
        if (activate) {
          e.addClassName(styles.headMenuOpen());
        } else {
          e.removeClassName(styles.headMenuOpen());
        }
      }
    }

    public Element getTrigger() {
      return (Element) btn.cast();
    }

    @Override
    public void onBrowserEvent(Event ce) {
      super.onBrowserEvent(ce);

      int type = ce.getTypeInt();
      switch (type) {
        case Event.ONMOUSEOVER:
          onMouseOver(ce);
          break;
        case Event.ONMOUSEOUT:
          onMouseOut(ce);
          break;
        case Event.ONMOUSEMOVE:
          onMouseMove(ce);
          break;
        case Event.ONMOUSEDOWN:
          onHeaderMouseDown(ce, column);
          break;
        case Event.ONCLICK:
          onClick(ce);
          break;
        case Event.ONDBLCLICK:
          onDoubleClick(ce);
          break;
      }
    }

    public void setHeader(SafeHtml header) {
      if (text != null) text.setHTML(header);
    }

    public void updateWidth(int width) {
      if (!config.isHidden()) {
        XElement td = getElement().findParent("td", 3);

        int adj = td.getFrameWidth(Side.LEFT, Side.RIGHT);
        int newWidth = width - adj;

        // EXTGWT-3511 The setWidth call is not working as the framing is greater than the specified column width in
        // some cases causing the overall width to be < 0
        if (!getElement().isBorderBox()) {
          newWidth -= getElement().getFrameWidth(Side.LEFT, Side.RIGHT);
          newWidth = Math.max(1, newWidth);
        }

        getElement().setWidth(newWidth, false);

        Element th = getTableHeader(column);
        th.getStyle().setWidth(width, Unit.PX);

        String tdHeight = td.getStyle().getHeight();

        if (tdHeight.equals("")) {
          int h = overrideHeaderHeight != -1 ? overrideHeaderHeight : td.getHeight(true);
          h -= ColumnHeader.this.getElement().<XElement>cast().getBorders(Side.TOP, Side.BOTTOM);
          td.setHeight(h);

          getElement().setHeight(h, true);

          if (btn != null) {
            XElement.as(btn).setHeight(h, true);
          }
        }
      }
    }

    protected void activate() {
      if (!cm.isMenuDisabled(indexOf(this))) {
        XElement td = getElement().findParent("td", 3);
        td.addClassName(styles.headOver());
      }
    }

    protected void deactivate() {
      getElement().findParent("td", 3).removeClassName(styles.headOver());
    }

    @Override
    protected void doAttachChildren() {
      super.doAttachChildren();
      ComponentHelper.doAttach(widget);
    }

    @Override
    protected void doDetachChildren() {
      super.doDetachChildren();
      ComponentHelper.doDetach(widget);
    }

    private void onClick(Event ce) {
      ce.preventDefault();
      if (ce.getEventTarget().<Element>cast() == (Element) btn.cast()) {
        onDropDownClick(ce, column);
      } else {
        onHeaderClick(ce, column);
      }
    }

    private void onDoubleClick(Event ce) {
      onHeaderDoubleClick(ce, column);
    }

    private void onMouseMove(Event ce) {
      if (bar != null) bar.onMouseMove(this, ce);
    }

    private void onMouseOut(Event ce) {
      deactivate();
    }

    private void onMouseOver(Event ce) {
      if (headerDisabled) {
        return;
      }
      activate();
    }
  }

  public class HiddenHeaderGroupConfig extends HeaderGroupConfig {

    public HiddenHeaderGroupConfig(int row, int col) {
      super("", row, col);

    }

  }

  protected class ReorderDragHandler implements DragHandler {
    protected Head active;
    protected int newIndex = -1;
    protected Head start;
    protected XElement statusIndicatorBottom;
    protected XElement statusIndicatorTop;
    protected StatusProxy statusProxy = StatusProxy.get();

    @Override
    public void onDragCancel(DragCancelEvent event) {
      afterDragEnd();
    }

    @Override
    public void onDragEnd(DragEndEvent event) {
      if (statusProxy.getStatus()) {
        cm.moveColumn(start.column, newIndex);
      }
      afterDragEnd();
    }

    @Override
    public void onDragMove(DragMoveEvent event) {
      Point eventXY = event.getNativeEvent().<XEvent>cast().getXY();
      event.setX(eventXY.getX() + 12 + XDOM.getBodyScrollLeft());
      event.setY(eventXY.getY() + 12 + XDOM.getBodyScrollTop());

      Element target = event.getNativeEvent().getEventTarget().cast();
      Head h = null;

      if (GXT.isTouch()) {
        // for touch events, getEventTarget will always return the element you started the gesture on
        for (Head head : heads) {
          if (head.getElement().getBounds().contains(eventXY.getX(), eventXY.getY())) {
            h = head;
            break;
          }
        }
      } else {
        h = getHeadFromElement(adjustTargetElement(target));
      }

      if (h != null && !h.equals(start)) {
        HeaderGroupConfig g = cm.getGroup(h.row - 1, h.column);
        HeaderGroupConfig s = cm.getGroup(start.row - 1, start.column);
        if ((g == null && s == null) || (g != null && g.equals(s))) {
          active = h;
          boolean before = eventXY.getX() < active.getAbsoluteLeft() + active.getOffsetWidth() / 2;
          showStatusIndicator(true);

          if (before) {
            statusIndicatorTop.alignTo(active.getElement(), new AnchorAlignment(Anchor.BOTTOM, Anchor.TOP_LEFT), -1, 0);
            statusIndicatorBottom.alignTo(active.getElement(), new AnchorAlignment(Anchor.TOP, Anchor.BOTTOM_LEFT), -1,
                0);
          } else {
            statusIndicatorTop.alignTo(active.getElement(), new AnchorAlignment(Anchor.BOTTOM, Anchor.TOP_RIGHT), 1, 0);
            statusIndicatorBottom.alignTo(active.getElement(), new AnchorAlignment(Anchor.TOP, Anchor.BOTTOM_RIGHT), 1,
                0);
          }

          int i = active.column;
          if (!before) {
            i++;
          }

          int aIndex = i;

          if (start.column < active.column) {
            aIndex--;
          }
          newIndex = i;
          if (aIndex != start.column) {
            statusProxy.setStatus(true);
          } else {
            showStatusIndicator(false);
            statusProxy.setStatus(false);
          }
        } else {
          active = null;
          showStatusIndicator(false);
          statusProxy.setStatus(false);
        }

      } else {
        active = null;
        showStatusIndicator(false);
        statusProxy.setStatus(false);
      }
    }

    @Override
    public void onDragStart(DragStartEvent event) {
      Element target = event.getNativeEvent().getEventTarget().cast();

      Head h = getHeadFromElement(target);
      if (h != null && !h.config.isFixed()) {
        headerDisabled = true;
        quickTip.disable();
        if (bar != null) {
          bar.hide();
        }

        if (statusIndicatorBottom == null) {
          statusIndicatorBottom = XElement.createElement("div");
          statusIndicatorBottom.addClassName(styles.columnMoveBottom());
          statusIndicatorTop = XElement.createElement("div");
          statusIndicatorTop.addClassName(styles.columnMoveTop());
        }

        Document.get().getBody().appendChild(statusIndicatorTop);
        Document.get().getBody().appendChild(statusIndicatorBottom);

        start = h;
        statusProxy.setStatus(false);
        statusProxy.update(start.config.getHeader());
      } else {
        event.setCancelled(true);
      }
    }

    protected Element adjustTargetElement(Element target) {
      return (Element) (target.getFirstChildElement() != null ? target.getFirstChildElement() : target);
    }

    protected void afterDragEnd() {
      start = null;
      active = null;
      newIndex = -1;
      removeStatusIndicator();

      headerDisabled = false;

      if (bar != null) {
        bar.show();
      }

      quickTip.enable();
    }

    @SuppressWarnings("unchecked")
    protected Head getHeadFromElement(Element element) {
      Widget head = ComponentHelper.getWidgetWithElement(element);
      Head h = null;
      if (head instanceof ColumnHeader.Head && heads.contains(head)) {
        h = (Head) head;
      }
      return h;
    }

    protected void removeStatusIndicator() {
      if (statusIndicatorBottom != null) {
        statusIndicatorBottom.removeFromParent();
        statusIndicatorTop.removeFromParent();
      }
    }

    protected void showStatusIndicator(boolean show) {
      if (statusIndicatorBottom != null) {
        statusIndicatorBottom.setVisibility(show);
        statusIndicatorTop.setVisibility(show);
      }
    }
  }

  protected GridSplitBar bar;
  protected ColumnModel<M> cm;
  protected Grid<M> container;
  protected List<Group> groups = new ArrayList<Group>();
  protected boolean headerDisabled;

  /**
   * The height of the header is based on the content height in each header. Change this field to override the default
   * behavior and specify an exact header height.
   */
  protected int overrideHeaderHeight = -1;

  /**
   * The list off all Head instances. There will be a Head instance for all columns, including hidden ones. The table TH
   * and TD rows DO NOT contain elements for hidden columns. As such, there is not a direct mapping between column and
   * DOM.
   */
  protected List<Head> heads = new ArrayList<Head>();

  /**
   * Maps actual column indexes to the TABLE TH and TD index.
   */
  protected int[] columnToHead;
  protected boolean disableSortIcon;
  protected HeaderContextMenuFactory menu;
  protected int minColumnWidth = 25;
  protected Draggable reorderer;
  protected int rows;
  protected int splitterWidth = 5;
  protected FlexTable table = new FlexTable();
  protected GridTemplates tpls = GWT.create(GridTemplates.class);

  /**
   * The amount of padding in pixels for right aligned columns (defaults to 16).
   */
  private int rightAlignOffset;

  private QuickTip quickTip;
  private boolean enableColumnReorder;
  private final ColumnHeaderAppearance appearance;
  private ColumnHeaderStyles styles;
  private TableSectionElement tbody = Document.get().createTBodyElement();
  private int oldWidth;
  private int oldHeight;

  /**
   * Creates a new column header.
   *
   * @param container the containing widget
   * @param cm the column model
   */
  public ColumnHeader(Grid<M> container, ColumnModel<M> cm) {
    this(container, cm, GWT.<ColumnHeaderAppearance>create(ColumnHeaderAppearance.class));
  }

  /**
   * Creates a new column header.
   *
   * @param container the containing widget
   * @param cm the column model
   * @param appearance the column header appearance
   */
  public ColumnHeader(Grid<M> container, ColumnModel<M> cm, ColumnHeaderAppearance appearance) {
    this.container = container;
    this.cm = cm;
    this.appearance = appearance;
    rightAlignOffset = 2 + getAppearance().getColumnMenuWidth();
    setAllowTextSelection(false);

    styles = appearance.styles();

    SafeHtmlBuilder builder = new SafeHtmlBuilder();
    this.appearance.render(builder);

    setElement((Element) XDOM.create(builder.toSafeHtml()));

    table.setCellPadding(0);
    table.setCellSpacing(0);

    table.getElement().getStyle().setTableLayout(TableLayout.FIXED);

    getElement().selectNode(appearance.columnsWrapSelector()).appendChild(table.getElement());

    quickTip = new QuickTip(this);
  }

  /**
   * Returns the column header appearance.
   *
   * @return the column header appearance
   */
  public ColumnHeaderAppearance getAppearance() {
    return appearance;
  }

  /**
   * Returns the header's container widget.
   *
   * @return the container widget
   */
  public Widget getContainer() {
    return container;
  }

  /**
   * Returns the head at the current index.
   *
   * @param column the column index
   * @return the column or null if no match
   */
  public Head getHead(int column) {
    return (column > -1 && column < heads.size()) ? heads.get(column) : null;
  }

  /**
   * Returns the minimum column width.
   *
   * @return the column width in pixels
   */
  public int getMinColumnWidth() {
    return minColumnWidth;
  }

  /**
   * Returns the amount of padding in pixels for right aligned columns (defaults to 16).
   *
   * @return the right align offset
   */
  public int getRightAlignOffset() {
    return rightAlignOffset;
  }

  /**
   * Returns the splitter width.
   *
   * @return the splitter width in pixels.
   */
  public int getSplitterWidth() {
    return splitterWidth;
  }

  /**
   * Returns the index of the given column head.
   *
   * @param head the column head
   * @return the index
   */
  public int indexOf(Head head) {
    return head.column;
  }

  /**
   * Returns the state of the sort icon.
   */
  public boolean isDisableSortIcon() {
    return disableSortIcon;
  }

  /**
   * Returns true if column reordering is enabled.
   *
   * @return the column reorder state
   */
  public boolean isEnableColumnReorder() {
    return enableColumnReorder;
  }

  /**
   * Refreshes the columns.
   */
  public void refresh() {
    groups.clear();
    heads.clear();

    columnToHead = new int[cm.getColumnCount()];
    for (int i = 0, mark = 0; i < columnToHead.length; i++) {
      columnToHead[i] = cm.isHidden(i) ? -1 : mark++;
    }

    int cnt = table.getRowCount();
    for (int i = 0; i < cnt; i++) {
      table.removeRow(0);
    }

    table.setWidth(cm.getTotalWidth() + "px");
    // Defer header size check until heads are created

    Element body = table.getElement().<XElement>cast().selectNode("tbody");

    table.getElement().insertBefore(tbody, body);
    tbody.<XElement>cast().removeChildren();
    DomHelper.insertHtml("afterBegin", tbody, renderHiddenHeaders(getColumnWidths()));

    List<HeaderGroupConfig> configs = cm.getHeaderGroups();

    FlexCellFormatter cf = table.getFlexCellFormatter();
    RowFormatter rf = table.getRowFormatter();

    rows = 0;
    for (HeaderGroupConfig config : configs) {
      rows = Math.max(rows, config.getRow() + 1);
    }
    rows++;

    for (int i = 0; i < rows; i++) {
      rf.setStyleName(i, styles.headRow());
    }

    int cols = cm.getColumnCount();

    String cellClass = styles.header() + " " + styles.head();

    if (rows > 1) {
      Map<Integer, Integer> map = new HashMap<Integer, Integer>();
      for (int i = 0; i < rows - 1; i++) {
        for (HeaderGroupConfig config : cm.getHeaderGroups()) {
          int col = config.getColumn();
          int row = config.getRow();
          Integer start = map.get(row);

          if (start == null || col < start) {
            map.put(row, col);
          }
        }
      }
    }

    for (HeaderGroupConfig config : cm.getHeaderGroups()) {
      int col = config.getColumn();
      int row = config.getRow();
      int rs = config.getRowspan();
      int cs = config.getColspan();

      Group group = createNewGroup(config);

      boolean hide = true;
      if (rows > 1) {
        for (int i = col; i < (col + cs); i++) {
          if (!cm.isHidden(i)) {
            hide = false;
          }
        }
      }
      if (hide) {
        continue;
      }

      table.setWidget(row, col, group);

      cf.setStyleName(row, col, cellClass);

      HorizontalAlignmentConstant align = config.getHorizontalAlignment();
      cf.setHorizontalAlignment(row, col, align);

      int ncs = cs;
      if (cs > 1) {
        for (int i = col; i < (col + cs); i++) {
          if (cm.isHidden(i)) {
            ncs -= 1;
          }
        }
      }

      cf.setRowSpan(row, col, rs);
      cf.setColSpan(row, col, ncs);
    }

    for (int i = 0; i < cols; i++) {
      Head h = createNewHead(cm.getColumn(i));
      if (cm.isHidden(i)) {
        continue;
      }
      int rowspan = 1;
      if (rows > 1) {
        for (int j = rows - 2; j >= 0; j--) {
          if (!cm.hasGroup(j, i)) {
            rowspan += 1;
          }
        }
      }

      int row;
      if (rowspan > 1) {
        row = (rows - 1) - (rowspan - 1);
      } else {
        row = rows - 1;
      }

      h.row = row;

      if (rowspan > 1) {
        table.setWidget(row, i, h);
        table.getFlexCellFormatter().setRowSpan(row, i, rowspan);
      } else {
        table.setWidget(row, i, h);
      }
      ColumnConfig<M, ?> cc = cm.getColumn(i);
      String s = cc.getCellClassName() == null ? "" : " " + cc.getCellClassName();
      cf.setStyleName(row, i, cellClass + s);
      cf.getElement(row, i).setPropertyInt("gridColumnIndex", i);

      HorizontalAlignmentConstant align = cm.getColumnHorizontalAlignment(i);

      // override the header alignment
      if (cm.getColumnHorizontalHeaderAlignment(i) != null) {
        align = cm.getColumnHorizontalHeaderAlignment(i);
      }

      if (align != null) {
        table.getCellFormatter().setHorizontalAlignment(row, i, align);
        if (align == HasHorizontalAlignment.ALIGN_RIGHT) {
          table.getCellFormatter().getElement(row, i).getFirstChildElement().getStyle().setPropertyPx("paddingRight",
              getRightAlignOffset());
        }
      }
    }

    if (container instanceof Grid) {
      Grid<M> grid = (Grid<M>) container;
      if (grid.getView().isRemoteSort()) {
        List<? extends SortInfo> sortInfos = grid.getLoader().getSortInfo();
        if (sortInfos.size() > 0) {
          SortInfo sortInfo = sortInfos.get(0);
          String sortField = sortInfo.getSortField();
          if (sortField != null && !"".equals(sortField)) {
            ColumnConfig<M, ?> column = cm.findColumnConfig(sortField);
            if (column != null) {
              int index = cm.indexOf(column);
              if (index != -1) {
                updateSortIcon(index, sortInfo.getSortDir());
              }
            }
          }
        }
      } else {
        StoreSortInfo<M> sortInfo = grid.getView().getSortState();
        if (sortInfo != null && sortInfo.getValueProvider() != null) {
          ColumnConfig<M, ?> column = grid.getColumnModel().findColumnConfig(sortInfo.getPath());
          if (column != null) {
            updateSortIcon(grid.getColumnModel().indexOf(column), sortInfo.getDirection());
          }
        }
      }
    }

    cleanCells();

    adjustColumnWidths(getColumnWidths());

  }

  /**
   * Do not call.
   */
  public void release() {
    ComponentHelper.doDetach(this);
    getElement().removeFromParent();
    if (bar != null) {
      bar.getElement().removeFromParent();
    }
  }

  /**
   * Assigns a new set of columns to the header, but does not yet rebuild the headers. The {@link #refresh()} method
   * must be called to achieve that.
   *
   * @param columnModel the new set of columns to use
   */
  public void setColumnModel(ColumnModel<M> columnModel) {
    this.cm = columnModel;
  }

  /**
   * True to disable the column sort icon (defaults to false).
   */
  public void setDisableSortIcon(boolean disableSortIcon) {
    this.disableSortIcon = disableSortIcon;
  }

  /**
   * True to enable column reordering.
   *
   * @param enable true to enable
   */
  public void setEnableColumnReorder(boolean enable) {
    this.enableColumnReorder = enable;

    if (enable && reorderer == null) {
      reorderer = newColumnReorderingDraggable();
    }

    if (reorderer != null && !enable) {
      reorderer.release();
      reorderer = null;
    }
  }

  /**
   * True to enable column resizing.
   *
   * @param enable true to enable, otherwise false
   */
  public void setEnableColumnResizing(boolean enable) {
    if (bar == null && enable) {
      bar = new GridSplitBar();
      container.getElement().appendChild(bar.getElement());
      if (isAttached()) {
        ComponentHelper.doAttach(bar);
      }
      bar.show();
    } else if (bar != null && !enable) {
      ComponentHelper.doDetach(bar);
      bar.getElement().removeFromParent();
      bar = null;
    }
  }

  /**
   * Sets the column's header text.
   *
   * @param column the column index
   * @param header the header text
   */
  public void setHeader(int column, SafeHtml header) {
    getHead(column).setHeader(header);
    checkHeaderSizeChange();
  }

  /**
   * Specifies which menu to use for any given column
   *
   * @param menuFactory the instance to use when requesting a menu
   */
  public void setMenuFactory(HeaderContextMenuFactory menuFactory) {
    this.menu = menuFactory;
  }

  /**
   * Sets the minimum column width (defaults to 25px).
   *
   * @param minColumnWidth the minimum column width in pixels
   */
  public void setMinColumnWidth(int minColumnWidth) {
    this.minColumnWidth = minColumnWidth;
  }

  /**
   * Sets the amount of padding in pixels for right aligned columns (defaults to 16).
   *
   * @param rightAlignOffset the right align offset
   */
  public void setRightAlignOffset(int rightAlignOffset) {
    this.rightAlignOffset = rightAlignOffset;
  }

  /**
   * Sets the splitter width.
   *
   * @param splitterWidth the splitter width
   */
  public void setSplitterWidth(int splitterWidth) {
    this.splitterWidth = splitterWidth;
  }

  /**
   * Shows the column's header context menu.
   *
   * @param column the column index
   */
  public void showColumnMenu(final int column) {
    Menu menu = getContextMenu(column);

    if (menu == null) {
      return;
    }

    HeaderContextMenuEvent e = new HeaderContextMenuEvent(column, menu);
    container.fireEvent(e);
    if (e.isCancelled()) {
      return;
    }

    final Head h = getHead(column);
    menu.setId(h.getId() + "-menu");
    h.activateTrigger(true);
    menu.addHideHandler(new HideHandler() {

      @Override
      public void onHide(HideEvent event) {
        h.activateTrigger(false);
        container.focus();
      }
    });
    menu.show(h.getTrigger(), new AnchorAlignment(Anchor.TOP_LEFT, Anchor.BOTTOM_LEFT, true));
  }

  /**
   * Updates the visibility of a column.
   *
   * @param index the column index
   * @param hidden true to hide, otherwise false
   */
  public void updateColumnHidden(int index, boolean hidden) {
    // need to refresh as colspan and rowspan could be impacted
    refresh();
  }

  /**
   * Updates the column width.
   *
   * @param column the column index
   * @param width the new width
   */
  public void updateColumnWidth(int column, int width) {
    if (groups != null && groups.size() > 0) {
      adjustColumnWidths(getColumnWidths());
      return;
    }
    Head h = getHead(column);
    if (h != null) {
      h.updateWidth(width);
    }
  }

  /**
   * Updates the sort icon of a column.
   *
   * @param colIndex the column index
   * @param dir the sort direction
   */
  public void updateSortIcon(int colIndex, SortDir dir) {
    String desc = styles.sortDesc();
    String asc = styles.sortAsc();
    for (int i = 0; i < heads.size(); i++) {
      Head h = heads.get(i);
      if (!disableSortIcon && i == colIndex) {
        h.addStyleName(dir == SortDir.DESC ? desc : asc);
        h.removeStyleName(dir != SortDir.DESC ? desc : asc);
      } else {
        h.getElement().removeClassName(asc, desc);
      }
    }
  }

  /**
   * Updates the total width of the header.
   *
   * @param offset the offset
   * @param width the new width
   */
  public void updateTotalWidth(int offset, int width) {
    if (offset != -1) table.getElement().getParentElement().getStyle().setWidth(++offset, Unit.PX);
    table.getElement().getStyle().setWidth(width, Unit.PX);
    checkHeaderSizeChange();
  }

  protected void adjustCellWidth(XElement cell, int width) {
    cell.getStyle().setPropertyPx("width", width);
    int adj = cell.getFrameWidth(Side.LEFT, Side.RIGHT);
    XElement inner = cell.getFirstChildElement().cast();

    inner.setWidth(width - adj, true);
    if (isAttached()) {
      int before = cell.getOffsetHeight();
      inner.setHeight(before, true);
      int after = cell.getOffsetHeight();
      // getting different values when some browsers are zoomed which is
      // causing the column heights to grow
      if (after != before) {
        inner.setHeight(before + (before - after), true);
      }
    }
  }

  protected void adjustColumnWidths(int[] columnWidths) {
    NodeList<Element> ths = tbody.getFirstChildElement().getChildNodes().cast();
    if (ths == null) {
      return;
    }

    for (int i = 0; i < columnWidths.length; i++) {
      if (cm.isHidden(i)) {
        continue;
      }

      ths.getItem(getDomIndexByColumn(i)).getStyle().setPropertyPx("width", columnWidths[i]);
    }

    cleanCells();

    for (int i = 0; i < heads.size(); i++) {
      Head head = heads.get(i);
      if (head != null && !head.isRendered()) continue;
      head.updateWidth(cm.getColumnWidth(head.column));
    }

    for (int i = 0; i < groups.size(); i++) {
      Group group = groups.get(i);
      if (group != null && !group.isRendered()) continue;
      XElement cell = group.getElement().getParentElement().cast();
      int colspan = 1;
      String scolspan = cell.getAttribute("colspan");
      if (scolspan != null && !scolspan.equals("")) {
        colspan = Integer.parseInt(scolspan);
      }
      int w = 0;
      int mark = group.config.getColumn();
      for (int k = mark; k < (mark + colspan); k++) {
        ColumnConfig<M, ?> c = cm.getColumn(k);
        if (c.isHidden()) {
          mark++;
          continue;
        }
        w += cm.getColumnWidth(k);
      }
      mark += colspan;

      adjustCellWidth(cell, w);
    }
  }

  protected void checkHeaderSizeChange() {
    int width = getOffsetWidth();
    int height = getOffsetHeight();
    if (width != oldWidth || height != oldHeight) {
      ResizeEvent.fire(this, width, height);
      oldWidth = width;
      oldHeight = height;
    }
  }

  protected void cleanCells() {
    NodeList<Element> tds = DomQuery.select("tr." + styles.headRow() + " > td", table.getElement());
    for (int i = 0; i < tds.getLength(); i++) {
      Element td = tds.getItem(i);
      if (!td.hasChildNodes()) {
        XElement.as(td).removeFromParent();
      }
    }
  }

  protected Group createNewGroup(HeaderGroupConfig config) {
    return new Group(config);
  }

  @SuppressWarnings("rawtypes")
  protected Head createNewHead(ColumnConfig config) {
    return new Head(config);
  }

  @Override
  protected void doAttachChildren() {
    super.doAttachChildren();
    ComponentHelper.doAttach(table);
    ComponentHelper.doAttach(bar);
  }

  @Override
  protected void doDetachChildren() {
    super.doDetachChildren();
    ComponentHelper.doDetach(table);
    ComponentHelper.doDetach(bar);
  }

  protected int getColumnIndexByDom(int domIndex) {
    assert columnToHead != null && domIndex < columnToHead.length;
    for (int i = 0; i < columnToHead.length; i++) {
      if (columnToHead[i] == domIndex) {
        return i;
      }
    }

    return -1;

  }

  /**
   * Builds an array of the sizes for each column, visible or not
   */
  protected int[] getColumnWidths() {
    int colCount = cm.getColumnCount();
    int[] columnWidths = new int[colCount];
    for (int i = 0; i < colCount; i++) {
      columnWidths[i] = cm.getColumnWidth(i);
    }
    return columnWidths;
  }

  protected Menu getContextMenu(int column) {
    return menu == null ? null : menu.getMenuForColumn(column);
  }

  protected int getDomIndexByColumn(int column) {
    assert columnToHead != null && column < columnToHead.length;
    return columnToHead[column];
  }

  protected Draggable newColumnReorderingDraggable() {
    reorderer = new Draggable(this);
    reorderer.setUseProxy(true);
    reorderer.setSizeProxyToSource(false);
    reorderer.setProxy(StatusProxy.get().getElement());
    reorderer.setMoveAfterProxyDrag(false);

    reorderer.addDragHandler(newColumnReorderingDragHandler());
    return reorderer;
  }

  protected DragHandler newColumnReorderingDragHandler() {
    return new ReorderDragHandler();
  }

  @Override
  protected void onAttach() {
    super.onAttach();
    refresh();
  }

  protected void onDropDownClick(Event ce, int column) {
    ce.stopPropagation();
    ce.preventDefault();
    showColumnMenu(column);
  }

  protected void onHeaderClick(Event event, int column) {
    container.fireEvent(new HeaderClickEvent(column, event));
  }

  protected void onHeaderDoubleClick(Event event, int column) {
    container.fireEvent(new HeaderDoubleClickEvent(column, event));
  }

  protected void onHeaderMouseDown(Event ce, int column) {
    container.fireEvent(new HeaderMouseDownEvent(column, ce));
  }

  @Override
  protected void onResize(int width, int height) {
    super.onResize(width, height);
    checkHeaderSizeChange();
  }

  protected SafeHtml renderHiddenHeaders(int[] columnWidths) {
    SafeHtmlBuilder heads = new SafeHtmlBuilder();
    for (int i = 0; i < columnWidths.length; i++) {
      // unlike GridView, we do NOT render TH's for hidden elements because of support of
      // rowspan and colspan with header configs
      if (cm.isHidden(i)) {
        continue;
      }

      SafeStylesBuilder builder = new SafeStylesBuilder();
      builder.appendTrustedString("height: 0px;");
      builder.appendTrustedString("width:" + columnWidths[i] + "px;");
      heads.append(tpls.th("", builder.toSafeStyles()));
    }

    return tpls.tr("", heads.toSafeHtml());
  }

  private Element getTableHeader(int columnIndex) {
    int domIndex = getDomIndexByColumn(columnIndex);

    NodeList<Element> ths = getTableHeads();

    if (ths.getLength() > domIndex) {
      return ths.getItem(domIndex);
    }

    return null;
  }

  private NodeList<Element> getTableHeads() {
    return tbody.getFirstChildElement().getChildNodes().cast();
  }

}
