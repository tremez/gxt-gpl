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
package com.sencha.gxt.widget.core.client.form;

import com.sencha.gxt.core.client.Style.SelectionMode;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.ListView;

/**
 * Adapts a {@link ListView} for use as a field which can have a single value selected. For multi-select,
 * consider {@link DualListField}.
 * 
 * @param <M> the model type
 * @param <T> the field's data type
 */
public class ListField<M, T> extends AdapterField<M> {

  protected ListView<M, T> listView;

  /**
   * Creates a list field that wraps a {@link ListView} for use as a field.
   * 
   * @param view the list view to wrap
   */
  public ListField(ListView<M, T> view) {
    super(view);
    this.listView = view;
    this.listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
  }

  /**
   * Returns the store used by the list view.
   * 
   * @return the store used by the list view
   */
  public ListStore<M> getStore() {
    return listView.getStore();
  }

  @Override
  public M getValue() {
    return listView.getSelectionModel().getSelectedItem();
  }

  /**
   * Sets the list field's list store.
   * 
   * @param store the store
   */
  public void setStore(ListStore<M> store) {
    listView.setStore(store);
  }

  @Override
  public void setValue(M value) {
    if (getStore().indexOf(value) == -1) {
      listView.getSelectionModel().deselectAll();
    } else {
      listView.getSelectionModel().select(value, false);
    }
  }

  @Override
  protected void onDisable() {
    super.onDisable();
    listView.disable();
  }

  @Override
  protected void onEnable() {
    super.onEnable();
    listView.enable();
  }
}
