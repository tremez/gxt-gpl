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

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.BrowserEvents;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.Event;
import com.sencha.gxt.core.client.IdentityValueProvider;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.core.client.gestures.PointerEvents;
import com.sencha.gxt.core.client.gestures.PointerEventsSupport;
import com.sencha.gxt.core.shared.event.GroupingHandlerRegistration;
import com.sencha.gxt.data.shared.event.StoreClearEvent;
import com.sencha.gxt.widget.core.client.event.HeaderClickEvent;
import com.sencha.gxt.widget.core.client.event.HeaderClickEvent.HeaderClickHandler;
import com.sencha.gxt.widget.core.client.event.RefreshEvent;
import com.sencha.gxt.widget.core.client.event.RefreshEvent.RefreshHandler;
import com.sencha.gxt.widget.core.client.event.RowClickEvent;
import com.sencha.gxt.widget.core.client.event.RowMouseDownEvent;

/**
 * A grid selection model. To use, add the column config to the column model using {@link #getColumn()} and assign as
 * the grid's selection model via {@link Grid#setSelectionModel(GridSelectionModel)}.
 * 
 * <p>
 * This selection mode defaults to SelectionMode.MULTI and also supports SelectionMode.SIMPLE. With SIMPLE, the control
 * and shift keys do not need to be pressed for multiple selections.
 * 
 * @param <M> the model data type
 */
public class CheckBoxSelectionModel<M> extends GridSelectionModel<M> {

  public interface CheckBoxColumnAppearance {

    String getCellClassName();

    String getCellInnerClassName();

    boolean isHeaderChecked(XElement header);

    void onHeaderChecked(XElement header, boolean checked);

    SafeHtml renderHeadCheckBox();

    void renderCellCheckBox(Context context, Object value, SafeHtmlBuilder sb);

    boolean isRowChecker(XElement target);

  }

  protected ColumnConfig<M, M> config;

  private final CheckBoxColumnAppearance appearance;

  private GroupingHandlerRegistration handlerRegistration = new GroupingHandlerRegistration();
  private boolean showSelectAll = true;

  /**
   * Creates a CheckBoxSelectionModel that will operate on the row itself. To customize the row it is acting on, use a
   * constructor that lets you specify a ValueProvider, to customize how each row is drawn, use a constructor that lets
   * you specify an appearance instance.
   */
  public CheckBoxSelectionModel() {
    this(new IdentityValueProvider<M>(), GWT.<CheckBoxColumnAppearance> create(CheckBoxColumnAppearance.class));
  }

  /**
   * Creates a CheckBoxSelectionModel with a custom ValueProvider instance.
   * 
   * @param valueProvider the ValueProvider to use when constructing a ColumnConfig
   */
  public CheckBoxSelectionModel(ValueProvider<M, M> valueProvider) {
    this(valueProvider, GWT.<CheckBoxColumnAppearance> create(CheckBoxColumnAppearance.class));
  }

  /**
   * Creates a CheckBoxSelectionModel with a custom appearance instance.
   * 
   * @param appearance the appearance that should be used to render and update the checkbox
   */
  public CheckBoxSelectionModel(CheckBoxColumnAppearance appearance) {
    this(new IdentityValueProvider<M>(), appearance);
  }

  /**
   * Creates a CheckBoxSelectionModel with a custom ValueProvider and appearance.
   * 
   * @param valueProvider the ValueProvider to use when constructing a ColumnConfig
   * @param appearance the appearance that should be used to render and update the checkbox
   */
  public CheckBoxSelectionModel(ValueProvider<M, M> valueProvider, final CheckBoxColumnAppearance appearance) {
    this.appearance = appearance;
    config = newColumnConfig(valueProvider);
    config.setCellPadding(false);
    config.setWidth(20);
    config.setSortable(false);
    config.setHideable(false);
    config.setResizable(false);
    config.setFixed(true);
    config.setMenuDisabled(true);
    config.setCellClassName(appearance.getCellClassName());
    config.setColumnHeaderClassName(appearance.getCellInnerClassName());

    config.setHeader(appearance.renderHeadCheckBox());
    // allow touch events in
    Set<String> consumedEvents = new HashSet<String>(Arrays.asList(BrowserEvents.TOUCHSTART, BrowserEvents.TOUCHMOVE,
            BrowserEvents.TOUCHCANCEL, BrowserEvents.TOUCHEND));
    if (PointerEventsSupport.impl.isSupported()) {
      consumedEvents.add(PointerEvents.POINTERDOWN.getEventName());
    }
    config.setCell(new AbstractCell<M>(consumedEvents) {
      @Override
      public void onBrowserEvent(Context context, Element parent, M value, NativeEvent event, ValueUpdater<M> valueUpdater) {
        super.onBrowserEvent(context, parent, value, event, valueUpdater);

        // If the incoming event is touch, then selection is already taken care of
        // Don't let mouse events to pass through
        String eventType = event.getType();
        if (BrowserEvents.TOUCHSTART.equals(eventType) ||
            BrowserEvents.TOUCHMOVE.equals(eventType) ||
            BrowserEvents.TOUCHCANCEL.equals(eventType) ||
            BrowserEvents.TOUCHEND.equals(eventType) ||
            PointerEventsSupport.impl.isPointerEvent(event)
            ) {
          event.preventDefault();
        }
      }

      @Override
      public void render(Context context, M value, SafeHtmlBuilder sb) {
        CheckBoxSelectionModel.this.render(context, value, sb);
      }
    });

    deselectOnSimpleClick = false;
  }

  /**
   * Returns the check box column appearance.
   * 
   * @return appearance
   */
  public CheckBoxColumnAppearance getAppearance() {
    return appearance;
  }

  @Override
  public void bindGrid(Grid<M> grid) {
    if (this.grid != null) {
      handlerRegistration.removeHandler();
    }
    super.bindGrid(grid);

    if (grid != null) {
      handlerRegistration.add(grid.addHeaderClickHandler(new HeaderClickHandler() {
        @Override
        public void onHeaderClick(HeaderClickEvent event) {
          handleHeaderClick(event);
        }
      }));

      handlerRegistration.add(grid.addRefreshHandler(new RefreshHandler() {
        @Override
        public void onRefresh(RefreshEvent event) {
          CheckBoxSelectionModel.this.onRefresh(event);
        }
      }));
    }

  }

  /**
   * Returns the column config.
   * 
   * @return the column config
   */
  public ColumnConfig<M, M> getColumn() {
    return config;
  }

  /**
   * Returns true if the header checkbox is rendered and selected.
   * 
   * @return true if selected
   */
  public boolean isSelectAllChecked() {
    if (isShowSelectAll() && grid != null && grid.isViewReady()) {
      XElement hd = grid.getView().getHeader().getHead(grid.getColumnModel().getColumns().indexOf(getColumn())).getElement();
      return appearance.isHeaderChecked(hd);
    }
    return false;
  }

  /**
   * Returns true if this column header contains a checkbox that the user can interact with.
   *
   * @return true if the column header should contain a checkbox to select all items
   */
  public boolean isShowSelectAll() {
    return showSelectAll;
  }

  /**
   * Sets the select all checkbox in the grid header and selects / deselects all rows. If the header checkbox is not
   * visible ({@link #setShowSelectAll(boolean)}), this will only update the visible row checkboxes.
   * 
   * @param select true to select all
   */
  public void setSelectAllChecked(boolean select) {
    assert grid.isViewReady() : "cannot call this method before grid has been rendered";
    if (!select) {
      setChecked(false);
      deselectAll();
    } else {
      setChecked(true);
      selectAll();
    }
  }

  /**
   * True to show the select all checkbox in the grid header, false to hide it and prevent select all behavior. Defaults
   * to true.
   * <p/>
   * Must either be called before the grid header is rendered, or calling code must force the header to be rerendered
   * (for example, via {@link GridView#refresh(boolean)}, passing {@code true} to get the header to refresh).
   *
   * @param showSelectAll true to show a header checkbox, false to hide it
   */
  public void setShowSelectAll(boolean showSelectAll) {
    this.showSelectAll = showSelectAll;
    config.setHeader(showSelectAll ? appearance.renderHeadCheckBox() : SafeHtmlUtils.EMPTY_SAFE_HTML);
  }

  protected void handleHeaderClick(HeaderClickEvent event) {
    if (!isShowSelectAll()) {
      return;
    }
    ColumnConfig<M, ?> c = grid.getColumnModel().getColumn(event.getColumnIndex());
    if (c == config) {
      XElement hd = grid.getView().getHeader().getHead(grid.getColumnModel().getColumns().indexOf(getColumn())).getElement();
      boolean isChecked = appearance.isHeaderChecked(hd);
      if (isChecked) {
        setChecked(false);
        deselectAll();
      } else {
        setChecked(true);
        selectAll();
      }
    }
  }

  @Override
  protected void onRowClick(RowClickEvent event) {
    if (event.getColumnIndex() == grid.getColumnModel().getColumns().indexOf(getColumn())) {
      return;
    }
    super.onRowClick(event);
  }

  @Override
  protected void onRowMouseDown(RowMouseDownEvent event) {
    boolean left = event.getEvent().getButton() == Event.BUTTON_LEFT;

    if (left && event.getColumnIndex() == grid.getColumnModel().getColumns().indexOf(getColumn())) {
      M model = listStore.get(event.getRowIndex());
      if (model != null) {
        if (isSelected(model)) {
          deselect(model);
        } else {
          select(model, true);
        }
      }
    } else {
      super.onRowMouseDown(event);
    }
  }

  protected ColumnConfig<M, M> newColumnConfig(ValueProvider<M, M> valueProvider) {
    return new ColumnConfig<M, M>(valueProvider);
  }

  @Override
  protected void onAdd(List<? extends M> models) {
    super.onAdd(models);
    updateHeaderCheckBox();
  }

  @Override
  protected void onClear(StoreClearEvent<M> event) {
    super.onClear(event);
    updateHeaderCheckBox();
  }

  protected void onRefresh(RefreshEvent event) {
    updateHeaderCheckBox();
  }

  @Override
  protected void onRemove(M model) {
    super.onRemove(model);
    updateHeaderCheckBox();
  }

  @Override
  protected void onSelectChange(M model, boolean select) {
    super.onSelectChange(model, select);
    updateHeaderCheckBox();
  }

  protected void render(Context context, M value, SafeHtmlBuilder sb) {
    appearance.renderCellCheckBox(context, value, sb);
  }

  protected void setChecked(boolean checked) {
    if (isShowSelectAll() && grid.isViewReady()) {
      int idx = grid.getColumnModel().getColumns().indexOf(getColumn());
      if (idx == -1) {
        return;
      }
      XElement hd = grid.getView().getHeader().getHead(idx).getElement();
      if (hd != null) {
        appearance.onHeaderChecked(hd.getParentElement().<XElement> cast(), checked);
      }
    }
  }

  protected void updateHeaderCheckBox() {
    setChecked(getSelection().size() == listStore.size());
  }

}
