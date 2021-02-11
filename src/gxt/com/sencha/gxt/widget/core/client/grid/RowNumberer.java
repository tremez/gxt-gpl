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
import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.sencha.gxt.core.client.IdentityValueProvider;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.core.client.util.DelayedTask;
import com.sencha.gxt.core.shared.event.GroupingHandlerRegistration;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.event.StoreAddEvent;
import com.sencha.gxt.data.shared.event.StoreAddEvent.StoreAddHandler;
import com.sencha.gxt.data.shared.event.StoreFilterEvent;
import com.sencha.gxt.data.shared.event.StoreFilterEvent.StoreFilterHandler;
import com.sencha.gxt.data.shared.event.StoreRemoveEvent;
import com.sencha.gxt.data.shared.event.StoreRemoveEvent.StoreRemoveHandler;
import com.sencha.gxt.widget.core.client.ComponentPlugin;

/**
 * A {@link ColumnConfig} that provides an automatic row numbering column.
 * <p/>
 * Code Snippet:
 * <p/>
 * 
 * <pre>{@code
    List&lt;ColumnConfig&lt;Data, ?>> ccs = new ArrayList&lt;ColumnConfig&lt;Data, ?>>();
    RowNumberer&lt;Data> rn = new RowNumberer&lt;Test.Data>();
    ccs.add(rn);
    ... add more column configs ...
    ColumnModel&lt;Data> cm = new ColumnModel&lt;Test.Data>(ccs);
    Grid&lt;Data> g = new Grid&lt;Data>(s, cm);
    rn.initPlugin(g);
 * }</pre>
 * 
 * @param <M>
 */
public class RowNumberer<M> extends ColumnConfig<M, M> implements ComponentPlugin<Grid<M>> {

  public interface RowNumbererAppearance {
    /**
     * Renders the row numberer cell within each row of the grid
     * @param rowNumber the current 1-indexed row number
     * @param sb the SafeHtmlBuilder to append the new html content to
     */
    void renderCell(int rowNumber, SafeHtmlBuilder sb);

    /**
     * A CSS class to append to every cell in the grid.
     * @see com.sencha.gxt.widget.core.client.grid.ColumnConfig#getCellClassName()
     */
    String getCellClassName();

    /**
     * The html content to display in the header of the grid
     */
    SafeHtml renderHeader();
  }

  private class Handler implements StoreAddHandler<M>, StoreFilterHandler<M>, StoreRemoveHandler<M> {

    @Override
    public void onAdd(StoreAddEvent<M> event) {
      doRefresh();
    }

    @Override
    public void onFilter(StoreFilterEvent<M> event) {
      doRefresh();
    }

    @Override
    public void onRemove(StoreRemoveEvent<M> event) {
      doRefresh();
    }

  }

  protected Grid<M> grid;

  private final RowNumbererAppearance appearance;
  private final Handler handler = new Handler();
  private GroupingHandlerRegistration handlerRegistration;
  private final DelayedTask updateTask = new DelayedTask() {

    @Override
    public void onExecute() {
      doUpdate();
    }
  };

  /**
   * Creates a row numberer. To use the row numberer, add it to a column model,
   * create a grid with the column model and then invoke
   * {@link RowNumberer#initPlugin(Grid)} on the grid.
   */
  public RowNumberer() {
    this(GWT.<RowNumbererAppearance>create(RowNumbererAppearance.class));
  }

  /**
   * Creates a row numberer. To use the row numberer, add it to a column model,
   * create a grid with the column model and then invoke
   * {@link RowNumberer#initPlugin(Grid)} on the grid.
   * 
   * @param appearance an alternate appearance instance to specify exactly how to draw this column
   */
  public RowNumberer(RowNumbererAppearance appearance) {
    this(new IdentityValueProvider<M>(), appearance);
  }

  /**
   * Creates a row numberer. To use the row numberer, add it to a column model,
   * create a grid with the column model and then invoke
   * {@link RowNumberer#initPlugin(Grid)} on the grid.
   *
   * @param valueProvider access to the current instance to be rendered in each row
   */
  public RowNumberer(ValueProvider<M, M> valueProvider) {
    this(valueProvider, GWT.<RowNumbererAppearance>create(RowNumbererAppearance.class));
  }

  /**
   * Creates a row numberer. To use the row numberer, add it to a column model,
   * create a grid with the column model and then invoke
   * {@link RowNumberer#initPlugin(Grid)} on the grid.
   *
   * @param valueProvider access to the current instance to be rendered in each row
   * @param appearance an alternate appearance instance to specify exactly how to draw this column
   */
  public RowNumberer(ValueProvider<M, M> valueProvider, RowNumbererAppearance appearance) {
    super(valueProvider);
    this.appearance = appearance;
    setWidth(23);
    setCellPadding(false);
    setSortable(false);
    setResizable(false);
    setFixed(true);
    setHideable(false);
    setMenuDisabled(true);
    setHeader(appearance.renderHeader());
    setCellClassName(appearance.getCellClassName());

    setCell(new AbstractCell<M>() {
      @Override
      public void render(Context context, M value, SafeHtmlBuilder sb) {
        RowNumberer.this.render(context, value, sb);
      }
    });
  }

  protected void render(Context context, M value, SafeHtmlBuilder sb) {
    appearance.renderCell(context.getIndex() + 1, sb);
  }

  @Override
  public void initPlugin(Grid<M> component) {
    if (this.grid != null) {
      handlerRegistration.removeHandler();
      handlerRegistration = null;
    }
    this.grid = component;

    if (this.grid != null) {
      handlerRegistration = new GroupingHandlerRegistration();
      handlerRegistration.add(grid.getStore().addStoreAddHandler(handler));
      handlerRegistration.add(grid.getStore().addStoreRemoveHandler(handler));
      handlerRegistration.add(grid.getStore().addStoreFilterHandler(handler));
    }
  }

  protected void doUpdate() {
    int col = grid.getColumnModel().indexOf(this);
    ModelKeyProvider<? super M> kp = grid.getStore().getKeyProvider();
    for (int i = 0, len = grid.getStore().size(); i < len; i++) {
      Element cell = grid.getView().getCell(i, col);
      if (cell != null) {
        SafeHtmlBuilder sb = new SafeHtmlBuilder();
        M item = grid.getStore().get(i);
        getCell().render(new Context(i, col, kp.getKey(item)), item, sb);
        cell.getFirstChildElement().setInnerSafeHtml(sb.toSafeHtml());
      }
    }
  }

  private void doRefresh() {
    updateTask.delay(50);
  }
}
