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

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.Event;
import com.sencha.gxt.core.client.IdentityValueProvider;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.widget.core.client.ComponentPlugin;
import com.sencha.gxt.widget.core.client.event.BeforeCollapseItemEvent;
import com.sencha.gxt.widget.core.client.event.BeforeCollapseItemEvent.BeforeCollapseItemHandler;
import com.sencha.gxt.widget.core.client.event.BeforeCollapseItemEvent.HasBeforeCollapseItemHandlers;
import com.sencha.gxt.widget.core.client.event.BeforeExpandItemEvent;
import com.sencha.gxt.widget.core.client.event.BeforeExpandItemEvent.BeforeExpandItemHandler;
import com.sencha.gxt.widget.core.client.event.BeforeExpandItemEvent.HasBeforeExpandItemHandlers;
import com.sencha.gxt.widget.core.client.event.CollapseItemEvent;
import com.sencha.gxt.widget.core.client.event.CollapseItemEvent.CollapseItemHandler;
import com.sencha.gxt.widget.core.client.event.CollapseItemEvent.HasCollapseItemHandlers;
import com.sencha.gxt.widget.core.client.event.ExpandItemEvent;
import com.sencha.gxt.widget.core.client.event.ExpandItemEvent.ExpandItemHandler;
import com.sencha.gxt.widget.core.client.event.ExpandItemEvent.HasExpandItemHandlers;
import com.sencha.gxt.widget.core.client.event.RowClickEvent;
import com.sencha.gxt.widget.core.client.event.RowClickEvent.RowClickHandler;

/**
 * A {@link ColumnConfig} subclass and a {@link ComponentPlugin} that adds the ability for each row to be expanded,
 * showing custom content that spans all the rows columns.
 * 
 * <p />
 * As with all component plugins, {@link #initPlugin(Grid)} must be called to initialize the row expander.
 * 
 * @param <M> the model type
 */
public class RowExpander<M> extends ColumnConfig<M, M> implements ComponentPlugin<Grid<M>>,
    HasBeforeExpandItemHandlers<M>, HasExpandItemHandlers<M>, HasBeforeCollapseItemHandlers<M>,
    HasCollapseItemHandlers<M> {

  public interface RowExpanderAppearance<M> {
    String getRowStyles(M model, int rowIndex);

    String getCellClassName();

    boolean isExpanded(XElement row);

    boolean isExpandElement(XElement target);

    void onExpand(XElement row, boolean expand);

    void renderExpander(Context context, M value, SafeHtmlBuilder sb);

    void finishInit(XElement gridParent);
  }

  protected Grid<M> grid;
  protected Cell<M> contentCell;

  private SimpleEventBus eventBus;
  private final RowExpanderAppearance<M> appearance;

  /**
   * Creates a new row expander.
   *
   * @param contentCell the content cell
   */
  public RowExpander(Cell<M> contentCell) {
    this(new IdentityValueProvider<M>(), contentCell, GWT.<RowExpanderAppearance<M>> create(RowExpanderAppearance.class));
  }

  /**
   * Creates a new row expander.
   *
   * @param valueProvider the value provider
   * @param contentCell the content cell
   */
  public RowExpander(IdentityValueProvider<M> valueProvider, Cell<M> contentCell) {
    this(valueProvider, contentCell, GWT.<RowExpanderAppearance<M>> create(RowExpanderAppearance.class));
  }

  /**
   * Creates a new row expander.
   * 
   * @param valueProvider the value provider
   * @param contentCell the content cell
   * @param appearance the appearance
   */
  public RowExpander(IdentityValueProvider<M> valueProvider, Cell<M> contentCell,
      final RowExpanderAppearance<M> appearance) {
    super(valueProvider);

    this.contentCell = contentCell;
    this.appearance = appearance;

    setHeader("");
    setWidth(20);
    setSortable(false);
    setResizable(false);
    setFixed(true);
    setMenuDisabled(true);
    setCellPadding(false);
    setCellClassName(appearance.getCellClassName());

    setCell(new AbstractCell<M>() {
      @Override
      public void render(Context context, M value, SafeHtmlBuilder sb) {
        assert grid != null : "RowExpander grid null, must call initPlugin";
        appearance.renderExpander(context, value, sb);
      }

      @Override
      public boolean handlesSelection() {
        return true;
      }
    });
  }

  @Override
  public HandlerRegistration addBeforeCollapseHandler(BeforeCollapseItemHandler<M> handler) {
    return ensureHandlers().addHandler(BeforeCollapseItemEvent.getType(), handler);
  }

  @Override
  public HandlerRegistration addBeforeExpandHandler(BeforeExpandItemHandler<M> handler) {
    return ensureHandlers().addHandler(BeforeExpandItemEvent.getType(), handler);
  }

  @Override
  public HandlerRegistration addCollapseHandler(CollapseItemHandler<M> handler) {
    return ensureHandlers().addHandler(CollapseItemEvent.getType(), handler);
  }

  @Override
  public HandlerRegistration addExpandHandler(ExpandItemHandler<M> handler) {
    return ensureHandlers().addHandler(ExpandItemEvent.getType(), handler);
  }

  /**
   * Collapses the given row.
   * 
   * @param rowIndex the rowIndex
   */
  public void collapseRow(int rowIndex) {
    XElement row = XElement.as(grid.getView().getRow(rowIndex));
    if (row != null && isExpanded(row)) {
      collapseRow(row);
    }
  }

  /**
   * Expands the given row.
   * 
   * @param rowIndex the rowIndex
   */
  public void expandRow(int rowIndex) {
    XElement row = XElement.as(grid.getView().getRow(rowIndex));
    if (row != null && !isExpanded(row)) {
      expandRow(row);
    }
  }

  public RowExpanderAppearance<M> getAppearance() {
    return appearance;
  }

  /**
   * Returns the content cell.
   * 
   * @return the content cell
   */
  public Cell<M> getContentCell() {
    return contentCell;
  }

  @Override
  public void initPlugin(Grid<M> component) {
    grid = component;
    grid.getView().setEnableRowBody(true);
    grid.getView().setRowBodyRowSpan(2);

    appearance.finishInit(grid.getElement());

    GridView<M> view = grid.getView();

    final GridViewConfig<M> config = view.getViewConfig();
    view.viewConfig = new GridViewConfig<M>() {

      @Override
      public String getColStyle(M model, ValueProvider<? super M, ?> valueProvider, int rowIndex, int colIndex) {
        return "";
      }

      @Override
      public String getRowStyle(M model, int rowIndex) {
        String s = appearance.getRowStyles(model, rowIndex);
        if (config != null) {
          return s + " " + config.getRowStyle(model, rowIndex);
        } else {
          return s;
        }
      }
    };

    grid.addRowClickHandler(new RowClickHandler() {

      @Override
      public void onRowClick(RowClickEvent event) {
        onMouseDown(event);
      }
    });
  }

  /**
   * Returns true if the row is expanded.
   * 
   * @param rowIndex the row index
   * @return true if expanded
   */
  public boolean isExpanded(int rowIndex) {
    XElement row = grid.getView().getRow(rowIndex).<XElement>cast();
    return row == null ? false : isExpanded(grid.getView().getRow(rowIndex).<XElement>cast());
  }

  /**
   * Sets the content cell.
   * 
   * @param contentCell the content cell
   */
  public void setContentCell(Cell<M> contentCell) {
    this.contentCell = contentCell;
  }

  protected boolean beforeExpand(M model, Element body, XElement row, int rowIndex) {
    BeforeExpandItemEvent<M> e = new BeforeExpandItemEvent<M>(model);
    ensureHandlers().fireEvent(e);

    if (!e.isCancelled()) {
      body.setInnerSafeHtml(getBodyContent(model, rowIndex));
      return true;
    }
    return false;
  }

  protected void collapseRow(XElement row) {
    int idx = row.getPropertyInt("rowindex");
    M model = grid.getStore().get(idx);

    BeforeCollapseItemEvent<M> e = new BeforeCollapseItemEvent<M>(model);
    ensureHandlers().fireEvent(e);

    if (!e.isCancelled()) {
      appearance.onExpand(row, false);
      ensureHandlers().fireEvent(new CollapseItemEvent<M>(model));
    }

  }

  protected void expandRow(XElement row) {
    int idx = row.getPropertyInt("rowindex");
    M model = grid.getStore().get(idx);
    Element rowBody = grid.getView().getRowBody(row);
    if (beforeExpand(model, rowBody, row, idx)) {
      appearance.onExpand(row, true);
      ensureHandlers().fireEvent(new ExpandItemEvent<M>(model));
    }
  }

  protected SafeHtml getBodyContent(M model, int rowIndex) {
    SafeHtmlBuilder sb = new SafeHtmlBuilder();
    contentCell.render(new Context(rowIndex, 0, grid.getStore().getKeyProvider().getKey(model)), model, sb);
    return sb.toSafeHtml();
  }

  protected boolean isExpanded(XElement row) {
    return appearance.isExpanded(row);
  }

  protected void onMouseDown(RowClickEvent event) {
    Event e = event.getEvent();
    XElement target = e.getEventTarget().cast();
    if (appearance.isExpandElement(target)) {
      e.stopPropagation();
      e.preventDefault();
      XElement row = grid.getView().findRow(target).cast();
      toggleRow(row);
    }
  }

  protected void toggleRow(XElement row) {
    if (!appearance.isExpanded(row)) {
      expandRow(row);
    } else {
      collapseRow(row);
    }
    grid.getView().calculateVBar(false);
  }

  SimpleEventBus ensureHandlers() {
    return eventBus == null ? eventBus = new SimpleEventBus() : eventBus;
  }

}
