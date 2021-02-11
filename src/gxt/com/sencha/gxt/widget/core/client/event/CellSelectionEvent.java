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
package com.sencha.gxt.widget.core.client.event;

import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.shared.HandlerManager;

/**
 * Fires after a selection occurs.
 * 
 * @param <T> the type about to be selected
 */
public class CellSelectionEvent<T> extends SelectionEvent<T> {

  public static <T> CellSelectionEvent<T> fire(HandlerManager manager, Context context, T item) {
    if (manager.isEventHandled(SelectionEvent.getType())) {
      CellSelectionEvent<T> event = new CellSelectionEvent<T>(context, item);
      manager.fireEvent(event);
      return event;
    }
    return null;
  }
  
  public static <T> CellSelectionEvent<T> fire(HasSelectionHandlers<T> handler, Context context, T item) {
    CellSelectionEvent<T> event = new CellSelectionEvent<T>(context, item);
    handler.fireEvent(event);
    return event;
  }

  private Context context;

  /**
   * Creates a new selection event.
   * 
   * @param context the cell context
   * @param item the selected item
   */
  protected CellSelectionEvent(Context context, T item) {
    super(item);
    this.context = context;
  }

  /**
   * Returns the cell context.
   * 
   * @return the cell context
   */
  public Context getContext() {
    return context;
  }

}
