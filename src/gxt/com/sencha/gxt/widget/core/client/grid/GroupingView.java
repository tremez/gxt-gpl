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
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safecss.shared.SafeStyles;
import com.google.gwt.safecss.shared.SafeStylesUtils;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.GXT;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.core.client.util.Point;
import com.sencha.gxt.core.client.util.Util;
import com.sencha.gxt.data.shared.SortDir;
import com.sencha.gxt.data.shared.SortInfo;
import com.sencha.gxt.data.shared.SortInfoBean;
import com.sencha.gxt.data.shared.Store.StoreSortInfo;
import com.sencha.gxt.messages.client.DefaultMessages;
import com.sencha.gxt.widget.core.client.event.CheckChangeEvent;
import com.sencha.gxt.widget.core.client.event.CheckChangeEvent.CheckChangeHandler;
import com.sencha.gxt.widget.core.client.event.CollapseItemEvent;
import com.sencha.gxt.widget.core.client.event.CollapseItemEvent.CollapseItemHandler;
import com.sencha.gxt.widget.core.client.event.CollapseItemEvent.HasCollapseItemHandlers;
import com.sencha.gxt.widget.core.client.event.ExpandItemEvent;
import com.sencha.gxt.widget.core.client.event.ExpandItemEvent.ExpandItemHandler;
import com.sencha.gxt.widget.core.client.event.ExpandItemEvent.HasExpandItemHandlers;
import com.sencha.gxt.widget.core.client.menu.CheckMenuItem;
import com.sencha.gxt.widget.core.client.menu.Item;
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.menu.MenuItem;
import com.sencha.gxt.widget.core.client.menu.SeparatorMenuItem;

/**
 * <code>GridView</code> that groups data based on a given grouping column.
 * 
 * @param <M> the model type
 */
public class GroupingView<M> extends GridView<M> implements HasCollapseItemHandlers<List<M>>,
    HasExpandItemHandlers<List<M>> {

  /**
   * Wrapper describing a given group, with the items in the group, and the value they hold in common. These are not
   * presently persisted, but only analyzed when changes occur to the grid's data.
   *
   * @param <M> the type of row in the grid
   */
  public static class GroupingData<M> {
    private final Object value;
    private final int startRow;
    private final List<M> items = new ArrayList<M>();
    private boolean collapsed;

    public GroupingData(Object value, int startRow) {
      this.value = value;
      this.startRow = startRow;
    }

    @Override
    public boolean equals(Object other) {
      if (other instanceof GroupingData) {
        return Util.equalWithNull(((GroupingData<?>) other).value, value);
      }
      return false;
    }

    public List<M> getItems() {
      return items;
    }

    public int getStartRow() {
      return startRow;
    }

    public Object getValue() {
      return value;
    }

    @Override
    public int hashCode() {
      return value == null ? 0 : value.hashCode();
    }

    public boolean isCollapsed() {
      return collapsed;
    }

    public void setCollapsed(boolean collapsed) {
      this.collapsed = collapsed;
    }

  }

  public interface GroupingViewAppearance {

    XElement findHead(XElement element);

    XElement getGroup(XElement head);

    ImageResource groupByIcon();

    boolean isCollapsed(XElement group);

    void onGroupExpand(XElement group, boolean expanded);

    SafeHtml renderGroupHeader(GroupingData<?> groupInfo);

    GroupingViewStyle style();
  }

  public interface GroupingViewStyle extends CssResource {

    String bodyCollapsed();

    String group();

    String groupCollapsed();

    String groupHead();
  }

  public interface GroupSummaryTemplate<M> {
    SafeHtml renderGroupSummary(GroupingData<M> groupInfo);
  }

  protected ColumnConfig<M, ?> lastGroupField;
  protected ColumnConfig<M, ?> groupingColumn;

  protected boolean enableGrouping;

  @SuppressWarnings("unused")
  private GroupSummaryTemplate<M> groupSummaryTemplate;
  private final GroupingViewAppearance groupAppearance;

  private boolean enableGroupingMenu = true;
  private boolean enableNoGroups = true;
  private boolean showGroupedColumn = true;
  private boolean startCollapsed = false;
  protected final Map<Object, Boolean> state = new HashMap<Object, Boolean>();

  private boolean isUpdating = false;
  private StoreSortInfo<M> lastStoreSort;
  private SortInfo lastSort;
  /**
   * Creates a new grouping view instance.
   */
  public GroupingView() {
    this(GWT.<GridAppearance> create(GridAppearance.class),
        GWT.<GroupingViewAppearance> create(GroupingViewAppearance.class));
  }

  /**
   * Creates a new grouping view instance.
   *
   * @param appearance the grid appearance
   * @param groupingAppearance the grouping appearance
   */
  public GroupingView(GridAppearance appearance, GroupingViewAppearance groupingAppearance) {
    super(appearance);
    this.groupAppearance = groupingAppearance;
  }

  /**
   * Creates a new grouping view instance.
   *
   * @param groupAppearance the group appearance
   */
  public GroupingView(GroupingViewAppearance groupAppearance) {
    this(GWT.<GridAppearance> create(GridAppearance.class), groupAppearance);
  }

  @Override
  public HandlerRegistration addCollapseHandler(CollapseItemHandler<List<M>> handler) {
    return addHandler(CollapseItemEvent.getType(), handler);
  }

  @Override
  public HandlerRegistration addExpandHandler(ExpandItemHandler<List<M>> handler) {
    return addHandler(ExpandItemEvent.getType(), handler);
  }

  /**
   * Collapses all groups.
   */
  public void collapseAllGroups() {
    toggleAllGroups(false);
  }

  /**
   * Expands all groups.
   */
  public void expandAllGroups() {
    toggleAllGroups(true);
  }

  /**
   * Returns the grouping view appearance.
   *
   * @return the grouping appearance
   */
  public GroupingViewAppearance getGroupingAppearance() {
    return groupAppearance;
  }

  @Override
  public StoreSortInfo<M> getSortState() {
    if (groupingColumn != null) {
      if (ds.getSortInfo().size() > 1) {
        return ds.getSortInfo().get(1);
      }
    }
    return super.getSortState();
  }

  public void groupBy(ColumnConfig<M, ?> column) {
    if (grid == null) {
      // if still being configured, save the grouping column for later
      groupingColumn = column;
    }

    if (column != groupingColumn) {
      // remove the existing group, if any
      if (groupingColumn != null) {
        if (grid.getLoader() == null || !grid.getLoader().isRemoteSort()) {
          assert lastStoreSort != null && ds.getSortInfo().contains(lastStoreSort);
          // remove the lastStoreSort from the listStore
          ds.getSortInfo().remove(lastStoreSort);
        } else {
          assert lastSort != null;
          grid.getLoader().removeSortInfo(lastSort);
        }
      } else {// groupingColumn == null;
        assert lastStoreSort == null && lastSort == null;
      }

      // set the new one
      groupingColumn = column;
      if (column != null) {
        if (grid.getLoader() == null || !grid.getLoader().isRemoteSort()) {
          lastStoreSort = createStoreSortInfo(column, SortDir.ASC);
          ds.addSortInfo(0, lastStoreSort);// this triggers the sort
        } else {
          lastSort = new SortInfoBean(column.getValueProvider(), SortDir.ASC);
          grid.getLoader().addSortInfo(0, lastSort);
          grid.getLoader().load();
        }
      } else {// new column == null
        lastStoreSort = null;
        lastSort = null;
        // full redraw without groups
        refresh(false);
      }
    }

    if (column == null) {
      doLastSort();
    }
  }

  /**
   * Returns true if the grouping menu is enabled.
   *
   * @return the enable grouping state
   */
  public boolean isEnableGroupingMenu() {
    return enableGroupingMenu;
  }

  /**
   * True to enable the the grouping menu items in the header context menu (defaults to true).
   *
   * @param enableGroupingMenu true to enable the grouping menu items
   */
  public void setEnableGroupingMenu(boolean enableGroupingMenu) {
    this.enableGroupingMenu = enableGroupingMenu;
  }

  /**
   * Returns true if the user can turn off grouping.
   *
   * @return the enable no groups state
   */
  public boolean isEnabledNoGroups() {
    return enableNoGroups;
  }

  /**
   * Returns true if the group is expanded.
   *
   * @param group the group
   * @return true if expanded
   */
  public boolean isExpanded(Element group) {
    return !groupAppearance.isCollapsed(group.<XElement> cast());
  }

  /**
   * Returns true if the grouped column is visible.
   *
   * @return the show grouped column
   */
  public boolean isShowGroupedColumn() {
    return showGroupedColumn;
  }

  /**
   * Sets whether the grouped column is visible (defaults to true).
   *
   * @param showGroupedColumn true to show the grouped column
   */
  public void setShowGroupedColumn(boolean showGroupedColumn) {
    this.showGroupedColumn = showGroupedColumn;
  }

  /**
   * Returns true if start collapsed is enabled.
   *
   * @return the start collapsed state
   */
  public boolean isStartCollapsed() {
    return startCollapsed;
  }

  /**
   * Sets whether the groups should start collapsed (defaults to false).
   *
   * @param startCollapsed true to start collapsed
   */
  public void setStartCollapsed(boolean startCollapsed) {
    this.startCollapsed = startCollapsed;
  }

  /**
   * True to enable the no groups menu item in the header context menu (defaults to true).
   *
   * @param enableNoGroups true to enable no groups menu item
   */
  public void setEnableNoGroups(boolean enableNoGroups) {
    this.enableNoGroups = enableNoGroups;
  }

  /**
   * Toggles all groups.
   *
   * @param expanded true to expand
   */
  public void toggleAllGroups(boolean expanded) {
    NodeList<Element> groups = getGroups();
    List<GroupingData<M>> groupData = createGroupingData();
    for (int i = 0; i < groups.getLength(); i++) {
      toggleGroup(groups.getItem(i), expanded);
      GroupingData<M> groupingData = groupData.get(i);
      if (expanded) {
        fireEvent(new ExpandItemEvent<List<M>>(groupingData.getItems()));
      } else {
        fireEvent(new CollapseItemEvent<List<M>>(groupingData.getItems()));
      }
    }
  }

  @Override
  protected void afterRender() {
    ColumnConfig<M, ?> column = groupingColumn;

    // set groupingColumn to null to force regrouping only if grouping
    // hasn't already occurred
    if (lastStoreSort == null && lastSort == null && column != null) {
      groupingColumn = null;
    }
    groupBy(column);
    super.afterRender();
  }

  @Override
  protected Menu createContextMenu(final int colIndex) {
    Menu menu = super.createContextMenu(colIndex);

    if (menu != null && enableGroupingMenu) {
      if (cm.isGroupable(colIndex)) {
        MenuItem groupBy = new MenuItem(DefaultMessages.getMessages().groupingView_groupByText());
        groupBy.setIcon(groupAppearance.groupByIcon());
        groupBy.addSelectionHandler(new SelectionHandler<Item>() {
          @Override
          public void onSelection(SelectionEvent<Item> event) {
            groupBy(cm.getColumn(colIndex));
          }
        });
        menu.add(new SeparatorMenuItem());
        menu.add(groupBy);

        groupBy.setEnabled(cm.getColumnCount(true) > 1);

        initMenuColumnShowHideHandling(menu, groupBy);
      }
      if (enableGrouping && enableNoGroups) {
        final CheckMenuItem showInGroups = new CheckMenuItem(
            DefaultMessages.getMessages().groupingView_showGroupsText());
        showInGroups.setChecked(true);
        showInGroups.addSelectionHandler(new SelectionHandler<Item>() {
          @Override
          public void onSelection(SelectionEvent<Item> event) {
            if (showInGroups.isChecked()) {
              groupBy(cm.getColumn(colIndex));
            } else {
              groupBy(null);
            }
          }
        });
        menu.add(showInGroups);
      }
    }

    return menu;
  }

  /**
   * @see #createGroupingData(java.util.List, int)
   * @return the group data for the current store
   */
  protected List<GroupingData<M>> createGroupingData() {
    List<GroupingData<M>> groups = new ArrayList<GroupingData<M>>();

    GroupingData<M> curGroup = null;
    for (int i = 0, len = ds.size(); i < len; i++) {
      M m = ds.get(i);
      final Object gvalue;
      if (ds.hasRecord(m)) {
        gvalue = ds.getRecord(m).getValue(groupingColumn.getValueProvider());
      } else {
        gvalue = groupingColumn.getValueProvider().getValue(m);
      }
      if (curGroup == null || !valueBelongsInGroup(curGroup, gvalue)) {
        curGroup = makeGroupForRow(i, m, gvalue);
        verifyNewGroup(groups, curGroup);
        groups.add(curGroup);
      } else {
        curGroup.getItems().add(m);
      }
    }

    return groups;
  }

  /**
   * @see #createGroupingData()
   * @param rows set of rows in the store to build into groups
   * @param startGroupIndex the index to use for the first group in the returned list
   * @return a list of groups
   */
  protected List<GroupingData<M>> createGroupingData(List<M> rows, int startGroupIndex) {
    List<GroupingData<M>> groups = new ArrayList<GroupingData<M>>();

    // iterate through each item, creating a new group as needed. Assumes the
    // list is sorted
    GroupingData<M> curGroup = null;
    for (int j = 0; j < rows.size(); j++) {
      M model = rows.get(j);

      int rowIndex = (j + startGroupIndex);

      // the value for the group field
      final Object gvalue;
      if (ds.hasRecord(model)) {
        gvalue = ds.getRecord(model).getValue(groupingColumn.getValueProvider());
      } else {
        gvalue = groupingColumn.getValueProvider().getValue(model);
      }

      if (curGroup == null || !valueBelongsInGroup(curGroup, gvalue)) {
        curGroup = makeGroupForRow(rowIndex, model, gvalue);
        verifyNewGroup(groups, curGroup);
        groups.add(curGroup);

      } else {
        curGroup.getItems().add(model);
      }
    }
    return groups;
  }

  protected void doLastSort() {
    StoreSortInfo<M> info = getSortState();
    if (info == null) {
      return;
    }

    ValueProvider<? super M, ?> vp = info.getValueProvider();
    if (vp == null) {
      return;
    }

    String p = vp.getPath();
    if (p == null) {
      return;
    }

    ColumnConfig<M, ?> config = cm.findColumnConfig(p);
    if (config == null) {
      return;
    }

    int index = cm.indexOf(config);
    doSort(index, info.getDirection());
  }

  @Override
  protected SafeHtml doRender(List<ColumnData> cs, List<M> rows, int startRow) {
    enableGrouping = groupingColumn != null;

    if (!enableGrouping || isUpdating) {
      return super.doRender(cs, rows, startRow);
    }


    List<GroupingData<M>> groups = createGroupingData(rows, startRow);

    SafeHtmlBuilder buf = new SafeHtmlBuilder();

    String styles = "width:" + getTotalWidth() + "px;";
    SafeStyles tableStyles = SafeStylesUtils.fromTrustedString(styles);

    for (int i = 0, len = groups.size(); i < len; i++) {
      GroupingData<M> g = groups.get(i);
      SafeHtml renderedRows = tpls.table(getAppearance().styles().dataTable(), tableStyles,
          super.doRender(cs, g.getItems(), g.getStartRow()), renderHiddenHeaders(getColumnWidths()));
      renderGroup(buf, g, renderedRows);
    }

    return buf.toSafeHtml();
  }

  @Override
  protected void doSort(int colIndex, SortDir sortDir) {
    ColumnConfig<M, ?> column = cm.getColumn(colIndex);
    if (groupingColumn != null) {
      if (grid.getLoader() == null || !grid.getLoader().isRemoteSort()) {
        // first sort is lastStoreSort
        assert lastStoreSort != null;
        ds.getSortInfo().clear();

        StoreSortInfo<M> sortInfo = createStoreSortInfo(column, sortDir);

        if (sortDir == null && storeSortInfo != null
            && storeSortInfo.getValueProvider().getPath().equals(column.getValueProvider().getPath())) {
          sortInfo.setDirection(storeSortInfo.getDirection() == SortDir.ASC ? SortDir.DESC : SortDir.ASC);
        } else if (sortDir == null) {
          sortInfo.setDirection(SortDir.ASC);
        }

        ds.getSortInfo().add(0, lastStoreSort);
        ds.getSortInfo().add(1, sortInfo);

        if (GWT.isProdMode()) {
          ds.applySort(false);
        } else {
          try {
            // applySort will apply its sort when called, which might trigger an
            // exception if the column passed in's data isn't Comparable
            ds.applySort(false);
          } catch (ClassCastException ex) {
            GWT.log("Column can't be sorted " + column.getValueProvider().getPath() + " is not Comparable. ", ex);
            throw ex;
          }
        }
      } else {
        assert lastSort != null;
        ValueProvider<? super M, ?> vp = column.getValueProvider();
        grid.getLoader().clearSortInfo();
        grid.getLoader().addSortInfo(0, lastSort);
        grid.getLoader().addSortInfo(1, new SortInfoBean(vp, sortDir));
        grid.getLoader().load();
      }

    } else {
      super.doSort(colIndex, sortDir);
    }
  }

  protected int getGroupIndex(XElement group) {
    return group.getParentElement().<XElement> cast().indexOf(group) / 2;
  }

  protected NodeList<Element> getGroups() {
    if (!enableGrouping) {
      return JsArray.createArray().cast();
    }
    return dataTable.<XElement> cast().select("." + groupAppearance.style().group());
  }

  @Override
  protected NodeList<Element> getRows() {
    if (!enableGrouping || !hasRows()) {
      return super.getRows();
    }
    return dataTable.<XElement> cast().select("." + styles.row());
  }

  /**
   * @param groupIndex the index of this group
   * @param firstModel the first model to add to the group
   * @param value the value that this group is based on
   * @return the newly created group
   */
  protected GroupingData<M> makeGroupForRow(int groupIndex, M firstModel, Object value) {
    GroupingData<M> curGroup;
    curGroup = new GroupingData<M>(value, groupIndex);
    curGroup.setCollapsed(state.containsKey(value) ? state.get(value) : isStartCollapsed());
    curGroup.getItems().add(firstModel);
    return curGroup;
  }

  @Override
  protected void onAdd(List<M> models, int index) {
    if (enableGrouping) {
      Point ss = getScrollState();
      refresh(false);
      restoreScroll(ss);
    } else {
      super.onAdd(models, index);
    }
  }

  @Override
  protected void onMouseDown(Event ge) {
    super.onMouseDown(ge);
    XElement head = ge.getEventTarget().cast();
    head = groupAppearance.findHead(head);
    if (head != null) {
      ge.stopPropagation();
      XElement group = groupAppearance.getGroup(head);
      int index = getGroupIndex(group);
      toggleGroup(index, groupAppearance.isCollapsed(group));
    }
  }

  @Override
  protected void onRemove(M m, int index, boolean isUpdate) {
    Element parentToRemove = null;
    if (enableGrouping) {
      Element row = getRow(index).cast();

      // TODO appearance this
      Element groupContainer = row.getParentElement().cast();
      if (groupContainer.getChildCount() == 1) {
        parentToRemove = groupContainer.getParentElement().cast();
      }
    }
    super.onRemove(m, index, isUpdate);
    if (parentToRemove != null) {
      parentToRemove.removeFromParent();
    }
  }

  @Override
  protected void refreshRow(int row) {
    isUpdating = true;
    super.refreshRow(row);
    isUpdating = false;
  }

  protected void renderGroup(SafeHtmlBuilder buf, GroupingData<M> g, SafeHtml renderedRows) {
    String groupClass = groupAppearance.style().group();
    String bodyClass = "";
    if (g.isCollapsed()) {
      groupClass += " " + groupAppearance.style().groupCollapsed();
      bodyClass = groupAppearance.style().bodyCollapsed();
    }
    String headClass = groupAppearance.style().groupHead();
    final SafeHtml groupHtml;
    String cellClasses = headClass + " " + styles.cell() + " " + states.cell();
    if (selectable) {
      groupHtml = (tpls.tr(groupClass,
          tpls.tdWrap(cm.getColumnCount(), cellClasses, styles.cellInner() + " " + states.cellInner(), renderGroupHeader(g))));
    } else {
      String innerCellClasses = styles.cellInner() + " " + states.cellInner() + " " + styles.noPadding();
      if (GXT.isIE()) {
        groupHtml = (tpls.tr(groupClass, tpls.tdWrapUnselectable(cm.getColumnCount(), cellClasses,
                innerCellClasses, renderGroupHeader(g))));
      } else {
        groupHtml = (tpls.tr(groupClass,
            tpls.tdWrap(cm.getColumnCount(), cellClasses, innerCellClasses, renderGroupHeader(g))));
      }

    }
    buf.append(groupHtml);

    buf.append(tpls.tr(bodyClass, tpls.tdWrap(cm.getColumnCount(), "", "", renderedRows)));
  }

  protected SafeHtml renderGroupHeader(GroupingData<M> groupInfo) {
    return groupAppearance.renderGroupHeader(groupInfo);
  }

  @Override
  protected SafeHtml renderRows(int startRow, int endRow) {
    boolean eg = groupingColumn != null;
    if (!showGroupedColumn) {
      int colIndex = cm.indexOf(groupingColumn);
      if (!eg && lastGroupField != null) {
        dataTableBody.removeChildren();
        cm.setHidden(cm.indexOf(lastGroupField), false);
        cm.getColumn(cm.indexOf(lastGroupField)).setHideable(true);
        lastGroupField = groupingColumn;
      } else if (eg && (lastGroupField == null || lastGroupField == groupingColumn)) {
        lastGroupField = groupingColumn;
        cm.setHidden(colIndex, true);
        cm.getColumn(colIndex).setHideable(false);
      } else if (eg && lastGroupField != null && !groupingColumn.equals(lastGroupField)) {
        dataTableBody.removeChildren();
        int oldIndex = cm.indexOf(lastGroupField);
        cm.setHidden(oldIndex, false);
        cm.getColumn(oldIndex).setHideable(true);
        lastGroupField = groupingColumn;
        cm.setHidden(colIndex, true);
        cm.getColumn(colIndex).setHideable(false);
      }
    }
    return super.renderRows(startRow, endRow);
  }

  /**
   * Toggles the given group index, dealing with all logical and view details.
   */
  protected void toggleGroup(int i, boolean expanded) {
    GroupingData<M> groupingData = createGroupingData().get(i);
    Object key = groupingData.getValue();
    state.put(key, !expanded);
    toggleGroup(getGroups().getItem(i), expanded);
    if (expanded) {
      fireEvent(new ExpandItemEvent<List<M>>(groupingData.getItems()));
    } else {
      fireEvent(new CollapseItemEvent<List<M>>(groupingData.getItems()));
    }
  }

  /**
   * Toggles the visibility of the group elements, but does not handle the internal state details or events.
   */
  protected void toggleGroup(Element group, boolean expanded) {
    assert group != null;
    groupAppearance.onGroupExpand(group.<XElement> cast(), expanded);
    calculateVBar(false);
  }

  /**
   * @param group a group that the value might match
   * @param value an object which may match the data in group.getValue()
   * @return true if a row with the given value belongs in the group
   */
  protected boolean valueBelongsInGroup(GroupingData<M> group, Object value) {
    return Util.equalWithNull(group.getValue(), value);
  }

  /**
   * Ensures that items which belong in the same group are not fragmented across multiple groups. It is technically
   * possible to override this to do nothing, but doing so is unsupported.
   * This method should be an assertion - use {@code assert} or {@link Class#desiredAssertionStatus()} to ensure that
   * this won't be in the compiled app if assertions are disabled.
   *
   * @param groups all groups created before the current one
   * @param nextGroup the next group to add to the list, assuming it shouldn't already be listed there
   */
  protected void verifyNewGroup(List<GroupingData<M>> groups, GroupingData<M> nextGroup) {
    assert !groups.contains(nextGroup) : "List didn't appear to be correctly sorted, group exists in more than one place in the list";
  }

  private void initMenuColumnShowHideHandling(Menu menu, final MenuItem groupBy) {
    // Loop through the menu and find the columns
    for (int i = 0; i < menu.getWidgetCount(); i++) {
      Widget subMenuWidget = menu.getWidget(i);
      // Only work with the columns MenuItem instances
      if (subMenuWidget instanceof MenuItem) {
        MenuItem columnsMenu = (MenuItem) subMenuWidget;
        String hasColumns = columnsMenu.getData("gxt-columns");
        // Find the columns and add handlers onto the CheckMenuItems columns
        if (hasColumns != null && hasColumns.equals("true")) {
          Menu colMenu = columnsMenu.getSubMenu();
          for (int b = 0; b < colMenu.getWidgetCount(); b++) {
            CheckMenuItem colItem = (CheckMenuItem) colMenu.getWidget(b);
            // Observe column events 'showing' and 'hiding' events
            colItem.addCheckChangeHandler(new CheckChangeHandler<CheckMenuItem>() {
              @Override
              public void onCheckChange(CheckChangeEvent<CheckMenuItem> event) {
                Scheduler.get().scheduleDeferred(new ScheduledCommand() {
                  @Override
                  public void execute() {
                    // Disable the group by option when only *one* column is displayed
                    groupBy.setEnabled(cm.getColumnCount(true) > 1);
                  }
                });
              }
            });
          }
        }
      }
    }
  }

}
