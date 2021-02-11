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
package com.sencha.gxt.widget.core.client.treegrid;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.sencha.gxt.data.shared.Store;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.GridSelectionModel;

public class TreeGridSelectionModel<M> extends GridSelectionModel<M> {

  protected TreeGrid<M> tree;
  protected TreeStore<M> treeStore;

  @Override
  public void bind(Store<M> store) {
    super.bind(store);
    if (store instanceof TreeStore<?>) {
      treeStore = (TreeStore<M>) store;
    } else {
      treeStore = null;
    }
  }

  @Override
  public void bindGrid(Grid<M> grid) {
    super.bindGrid(grid);
    if (grid instanceof TreeGrid<?>) {
      tree = (TreeGrid<M>) grid;
      treeStore = tree.getTreeStore();
    } else {
      tree = null;
    }
  }

  /**
   * Returns the currently bound tree grid.
   * 
   * @return the tree grid
   */
  public TreeGrid<M> getTreeGrid() {
    return tree;
  }

  @Override
  protected void onKeyLeft(NativeEvent ce) {
    if (Element.is(ce.getEventTarget()) && !grid.getView().isSelectableTarget(Element.as(ce.getEventTarget()))) {
      return;
    }
    super.onKeyLeft(ce);
    ce.preventDefault();

    // EXTGWT-3009
    if (getLastFocused() == null) {
      return;
    }

    boolean leaf = tree.isLeaf(getLastFocused());
    if (!leaf && tree.isExpanded(getLastFocused())) {
      tree.setExpanded(getLastFocused(), false);
    } else if (!leaf) {
      M parent = treeStore.getParent(getLastFocused());
      if (parent != null) {
        select(parent, false);
      }
    } else if (leaf) {
      M parent = treeStore.getParent(getLastFocused());
      if (parent != null) {
        select(parent, false);
      }
    }
  }

  @Override
  protected void onKeyRight(NativeEvent ce) {
    if (Element.is(ce.getEventTarget()) && !grid.getView().isSelectableTarget(Element.as(ce.getEventTarget()))) {
      return;
    }
    super.onKeyRight(ce);
    ce.preventDefault();
    if (!tree.isLeaf(getLastFocused()) && !tree.isExpanded(getLastFocused())) {
      tree.setExpanded(getLastFocused(), true);
    }
  }
}
