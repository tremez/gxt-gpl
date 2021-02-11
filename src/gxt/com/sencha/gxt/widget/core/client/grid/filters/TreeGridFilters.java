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
package com.sencha.gxt.widget.core.client.grid.filters;


import com.sencha.gxt.data.shared.Store;
import com.sencha.gxt.data.shared.loader.FilterPagingLoadConfig;
import com.sencha.gxt.data.shared.loader.Loader;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.treegrid.TreeGrid;

/**
 * Applies filters to the rows in a grid. For more information, see
 * {@link AbstractGridFilters}.
 * <p/>
 *
 * @param <M> the model type
 */
public class TreeGridFilters<M> extends AbstractGridFilters<M> {

  /**
   * Creates grid filters that are applied locally. See
   * {@link AbstractGridFilters#AbstractGridFilters()} for more information.
   */
  public TreeGridFilters() {
    super();
  }

  /**
   * Creates grid filters to be applied remotely. See
   * {@link AbstractGridFilters#AbstractGridFilters(Loader)} for more
   * information.
   *
   * @param loader the remote loader
   */
  public TreeGridFilters(Loader<? extends FilterPagingLoadConfig, ?> loader) {
    super(loader);
  }

  /**
   * Creates grid filters that are applied locally. See
   * {@link AbstractGridFilters#AbstractGridFilters()} for more information.
   *
   * @param appearance the filters appearance
   */
  public TreeGridFilters(GridFiltersAppearance appearance) {
    super(appearance);
  }

  /**
   * Creates grid filters to be applied remotely. See
   * {@link AbstractGridFilters#AbstractGridFilters(Loader)} for more
   * information.
   *
   * @param loader the remote loader
   * @param appearance the filters appearance
   */
  public TreeGridFilters(Loader<? extends FilterPagingLoadConfig, ?> loader, GridFiltersAppearance appearance) {
    super(loader, appearance);
  }

  /**
   * Initializes the plugin on the provided Grid instance.
   * Asserts that provided grid is an instance of TreeGrid
   *
   * @param grid - TreeGrid instance to initialize to
   */
  public void initPlugin(Grid<M> grid) {
    assert grid instanceof TreeGrid;
    super.initPlugin(grid);
  }

  @Override
  public boolean isLocal() {
    return super.isLocal();
  }

  @Override
  public void setLocal(boolean local) {
    super.setLocal(local);
  }

  /**
   * Returns the TreeStore used by the TreeGrid filters.
   *
   * @return the store used by the grid filters
   */
  @Override
  protected Store<M> getStore() {
    return ((TreeGrid) grid).getTreeStore();
  }
}
