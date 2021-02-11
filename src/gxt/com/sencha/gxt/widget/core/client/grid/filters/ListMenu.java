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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.event.CheckChangeEvent;
import com.sencha.gxt.widget.core.client.event.CheckChangeEvent.CheckChangeHandler;
import com.sencha.gxt.widget.core.client.menu.CheckMenuItem;
import com.sencha.gxt.widget.core.client.menu.Menu;

/**
 * A menu of list items for use with a {@link ListFilter}.
 * 
 * @param <M> the model type
 * @param <V> the {@link ListStore} type
 */
public class ListMenu<M, V> extends Menu {

  private ListFilter<M, V> filter;
  private List<V> selected;
  private ListStore<V> store;
  private CheckChangeHandler<CheckMenuItem> handler = new CheckChangeHandler<CheckMenuItem>() {

    @Override
    public void onCheckChange(CheckChangeEvent<CheckMenuItem> event) {
      ListMenu.this.onCheckChange(event);
    }
  };

  private Map<CheckMenuItem, V> map = new HashMap<CheckMenuItem, V>();

  /**
   * Creates a list menu for use with the specified filter and store.
   * 
   * @param filter the filter that uses this list menu
   * @param store contains the filter items to appear in the menu
   */
  public ListMenu(ListFilter<M, V> filter, ListStore<V> store) {
    this.filter = filter;
    this.store = store;

    selected = new ArrayList<V>();
  }

  /**
   * Returns the list filter associated with this list menu.
   * 
   * @return the list filter for this menu
   */
  public ListFilter<M, V> getFilter() {
    return filter;
  }

  /**
   * Returns the currently selected filter items from the list menu.
   * 
   * @return the selected filter items
   */
  public List<V> getSelected() {
    return getValue();
  }

  /**
   * Returns the currently selected filter items from the list menu.
   * 
   * @return the selected filter items
   */
  public List<V> getValue() {
    return new ArrayList<V>(selected);
  }

  /**
   * Sets the selected filter items for the list menu.
   * 
   * @param selected the selected filter items
   */
  public void setSelected(List<V> selected) {
    this.selected = new ArrayList<V>(selected);
  }

  protected void onCheckChange(CheckChangeEvent<CheckMenuItem> event) {
    CheckMenuItem item = event.getItem();
    V m = map.get(item);
    if (item.isChecked()) {
      if (!selected.contains(m)) {
        selected.add(m);
      }
    } else {
      selected.remove(m);
    }
    filter.onCheckChange(event);

  }

  @Override
  protected void onShow() {
    super.onShow();

    clear();
    List<Object> values = new ArrayList<Object>();
    map.clear();
    for (int i = 0; i < store.size(); i++) {
      V m = store.get(i);

      if (!values.contains(m)) {
        values.add(m);
        CheckMenuItem item = new CheckMenuItem();
        item.setText(m == null ? "" : m.toString());
        item.setChecked(selected.contains(m));
        item.setHideOnClick(false);
        item.addCheckChangeHandler(handler);
        add(item);

        map.put(item, m);

      }

    }

  }
}
