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
package com.sencha.gxt.widget.core.client.selection;

import java.util.List;

import com.sencha.gxt.core.client.Style.SelectionMode;
import com.sencha.gxt.data.shared.Store;

/**
 * Defines the interface for store based selection models.
 * 
 * <p>
 * The selection model supports 3 different selection modes:
 * <ul>
 * <li>SINGLE - Only single selections allowed
 * <li>SIMPLE - Multiple selections without having to use the control and shift
 * keys
 * <li>MULTI - Multiple selections
 * </ul>
 * 
 * @param <M> the model type
 */
public interface StoreSelectionModel<M> {

  /**
   * Binds the store to the selection model.
   * 
   * @param store the bound store
   */
  public void bind(Store<M> store);

  /**
   * Deselects the item at the given index.
   * 
   * @param index the index of the item to be deselected
   */
  public void deselect(int index);

  /**
   * Deselects the range.
   * 
   * @param start the start index
   * @param end the end index
   */
  public void deselect(int start, int end);

  /**
   * Deselects the items.
   * 
   * @param items the item
   */
  public void deselect(List<M> items);

  /**
   * Deselects the items.
   * 
   * @param items the items to deselect
   */
  public void deselect(M... items);

  /**
   * Deselects the item.
   * 
   * @param item the item to be deselected
   */
  public void deselect(M item);

  /**
   * Deselects all selections.
   */
  public void deselectAll();

  /**
   * Returns the selected item.
   */
  public M getSelectedItem();

  /**
   * Returns the selected items.
   */
  public List<M> getSelectedItems();

  /**
   * Returns the selection mode.
   * 
   * @return the selection mode
   */
  public SelectionMode getSelectionMode();

  /**
   * Returns true if the item is selected.
   * 
   * @param item the item
   * @return true if selected
   */
  public boolean isSelected(M item);

  /**
   * Refreshes the current selections.
   */
  public void refresh();

  /**
   * Selects the item at the given index.
   * 
   * @param index the index of the item to be selected
   * @param keepExisting true to keep existing selected
   */
  public void select(int index, boolean keepExisting);

  /**
   * Selects the range.
   * 
   * @param start the start index
   * @param end the end index
   * @param keepExisting true to keep existing selected
   */
  public void select(int start, int end, boolean keepExisting);

  /**
   * Selects the items. Selects the first item for single-select.
   * 
   * @param items the items to select
   * @param keepExisting true to keep existing selected
   */
  public void select(List<M> items, boolean keepExisting);

  /**
   * Selects the items. Selects the first item for single-select.
   * 
   * @param keepExisting true to keep existing selected
   * @param items the items
   */
  public void select(boolean keepExisting, M... items);

  /**
   * Selects the item.
   * 
   * @param item the item
   * @param keepExisting true to keep existing selected
   */
  public void select(M item, boolean keepExisting);

  /**
   * Selects all items.
   */
  public void selectAll();

  /**
   * Sets the selection mode.
   * 
   * <p>
   * The selection model supports 3 different selection modes:
   * <ul>
   * <li>SINGLE - Only single selections allowed
   * <li>SIMPLE - Multiple selections without having to use the control and
   * shift keys
   * <li>MULTI - Multiple selections
   * </ul>
   * 
   * @param selectionMode the selection mode
   */
  public void setSelectionMode(SelectionMode selectionMode);
}
